import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventGenerator;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.AlreadyBoundException;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * 
 * GPSOffice.java
 * @author Trushank
 * Date Mar 25, 2013
 * Version 1.0
 * 
 */
/**
 * @author Trushank
 * 
 */
public class GPSOffice implements GPSOfficeRef {
    private String host;
    private int port;
    private Coordinates myLocation;
    private String myname;

    private RemoteEventGenerator<GPSEvent> eventGenerator;

    private RegistryProxy registry;
    private ExecutorService exec = Executors.newCachedThreadPool();

    /**
     * Constructs a GPSOffice object
     * 
     * Command line arguments: 
     * args[0] = Registry Server's host 
     * args[1]=Registry Server's port 
     * args[2] = name of GPSOffice 
     * args[3] = X co-ordinate of GPSOffice 
     * args[4] = Y co-ordinate of GPSOffice
     * 
     * @param args
     *            Command line arguments.
     * 
     * @exception IllegalArgumentException
     *                (unchecked exception) Thrown if there was a problem with
     *                the command line arguments.
     * @exception IOException
     *                Thrown if an I/O error or a remote error occurred.
     * @exception NumberFormatException
     *                Thrown if invalid coordinates entered
     */
    public GPSOffice(String args[]) throws IOException {
	// Check number of args
	if (args.length != 5) {
	    throw new IllegalArgumentException(
		    "Usage: java Start GPSOffice <host> <port> <name> <X> <Y>");
	}
	host = args[0];
	// Check valid port
	try {
	    port = Integer.parseInt(args[1]);
	} catch (NumberFormatException e) {
	    throw new NumberFormatException("Please enter a valid port");
	}
	myname = args[2];
	// Check valid co-ordinates
	try {
	    myLocation = new Coordinates(Double.parseDouble(args[3]),
		    Double.parseDouble(args[4]));

	} catch (NumberFormatException e) {
	    throw new NumberFormatException("Please enter valid co-ordinates");

	}
	eventGenerator = new RemoteEventGenerator<GPSEvent>();
	registry = new RegistryProxy(host, port);
	UnicastRemoteObject.exportObject(this, 0);

	// Binding to the registry
	try {
	    registry.bind(myname, this);
	}
	// if already bound
	catch (AlreadyBoundException exc) {
	    try {
		// Removing object
		UnicastRemoteObject.unexportObject(this, true);
	    } catch (NoSuchObjectException exc2) {
	    }
	    // display error
	    throw new IllegalArgumentException("GPSOffice(): <name> = \""
		    + myname + "\" already exists");
	} catch (RemoteException exc) {
	    try {
		UnicastRemoteObject.unexportObject(this, true);
	    } catch (NoSuchObjectException exc2) {
	    }
	    throw new RemoteException("No Registry server on given host-port");
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see GPSOfficeRef#getDistance(int, int)
     */
    @Override
    public Coordinates getCoordiantes() throws RemoteException {
	return myLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see GPSOfficeRef#beam(java.lang.String)
     */
    @Override
    public void beam(final Package p) throws RemoteException {

	final GPSOffice self = this;
	exec.execute(new Runnable() {

	    @Override
	    public void run() {
		// if no tracking ID assigned, assign ID
		if (p.checkTrackingID())
		    assignTrackingID(p);

		GPSEvent arrive = new GPSEvent("Package number "
			+ p.getTrackingNumber() + " arrived at " + myname
			+ " office");
		// inform hQ
		eventGenerator.reportEvent(arrive);
		// customerEventGenerator.reportEvent(arrive);

		try {
		    // Inform "individual" customers of their packages progress
		    p.getListener().getUpdates(arrive.getMsg());
		} catch (RemoteException e1) {
		    // TODO Auto-generated catch block
		    // e1.printStackTrace();
		}

		// Examine package
		self.examinePackage(p);
		// Find closest to destination out of 3 neighbours and self
		GPSOfficeRef next = findNext(p.getDestination());
		// if self is closest, beam to destination
		if (next.equals(self)) {
		    GPSEvent delivery = new GPSEvent("Package number "
			    + p.getTrackingNumber() + " delivered from "
			    + myname + " office to ("
			    + p.getDestination().getX() + ","
			    + p.getDestination().getY() + ")");
		    eventGenerator.reportEvent(delivery);
		    try {
			// Inform "individual" customers of their packages
			// progress
			p.getListener().getUpdates(delivery.getMsg());
		    } catch (RemoteException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		    }
		    return;
		}
		// Try to forward packet to next Office
		try {
		    GPSEvent departed = new GPSEvent("Package number "
			    + p.getTrackingNumber() + " departed from "
			    + myname + " office");
		    next.beam(p);
		    eventGenerator.reportEvent(departed);
		 // Inform "individual" customers of their packages
		 			// progress
		    p.getListener().getUpdates(departed.getMsg());
		} catch (RemoteException e) {
		    // If forwarding unsuccessful
		    GPSEvent lost = new GPSEvent("Package number "
			    + p.getTrackingNumber() + " lost by " + myname
			    + " office");
		    eventGenerator.reportEvent(lost);
		    try {
			// Inform "individual" customers of their packages
			// progress
			p.getListener().getUpdates(lost.getMsg());
		    } catch (RemoteException e1) {
			
		    }
		}
	    }
	});
    }

    /*
     * 
     * /* (non-Javadoc)
     * 
     * @see GPSOfficeRef#addListener(edu.rit.ds.RemoteEventListener)
     */
    @Override
    public Lease addListener(RemoteEventListener<GPSEvent> listener)
	    throws RemoteException {

	return eventGenerator.addListener(listener);
    }

    /**
     * 
     * findNeighbours Looks up the registry and creates a GPSHashMap of all
     * nodes Then computes closest 3
     */

    /**
     * examinePackage Simulates examination of package by waiting for 3 seconds
     * Checks trackingID and assigns if its the first GPSOffice
     */
    private void examinePackage(Package p) {

	try {
	    // Checking for 3 seconds
	    Thread.sleep(3000);
	} catch (InterruptedException e) {

	    e.printStackTrace();
	}
    }

    /**
     * 
     * assignTrackingID Assigns Tracking ID to package
     * 
     * @param p
     *            Package to assign ID to
     */
    private void assignTrackingID(Package p) {
	p.assignTrackingID(System.currentTimeMillis());
    }

    /**
     * 
     * findNext Finds next office to beam packet to Or send directly if it is
     * closest
     * 
     * @param cord
     * @return
     * @throws RemoteException
     */
    private GPSOfficeRef findNext(Coordinates cord) {
	try {

	    // Querying registry for "GPSOffice" type objects
	    List<String> peers = registry.list("GPSOffice");
	    GPSOfficeRef n[] = new GPSOfficeRef[3];

	    // Find Neighbours
	    for (String office : peers) {
		// Skip if self
		if (office.equals(myname))
		    continue;
		// Looking up the object
		GPSOfficeRef neighbour = (GPSOfficeRef) registry.lookup(office);

		if (n[0] == null) {
		    n[0] = neighbour;
		    continue;
		} else if (n[1] == null) {
		    n[1] = neighbour;
		    continue;
		} else if (n[2] == null) {
		    n[2] = neighbour;
		    continue;
		} else {
		    int furthest = 0;
		    double d[] = new double[3];
		    for (int i = 0; i < n.length; i++) {
			try {
			    d[i] = n[i].getCoordiantes()
				    .getDistance(myLocation);
			} catch (RemoteException e) {
			    n[i] = neighbour;
			    continue;
			}
		    }

		    if (d[0] > d[1] && d[0] > d[2]) {
			furthest = 0;
		    } else if (d[1] > d[0] && d[1] > d[2]) {
			furthest = 1;
		    } else if (d[2] > d[1] && d[2] > d[0]) {
			furthest = 2;
		    }
		    try {
			//replace new Office as neighbour if closer
			if (neighbour.getCoordiantes().getDistance(myLocation) < d[furthest]) {
			    n[furthest] = neighbour;
			    continue;
			}
		    } catch (RemoteException e) {
		    }
		}
	    }
	    // Find neighbour closest to packet
	    // or self if it is closest
	    try{
	    double d1 = n[0].getCoordiantes().getDistance(cord);
	    double d2 = n[1].getCoordiantes().getDistance(cord);
	    double d3 = n[2].getCoordiantes().getDistance(cord);
	    double d4 = myLocation.getDistance(cord);
	    if (d1 < d2 && d1 < d3 && d1 < d4) {
		return n[0];
	    } else if (d2 < d1 && d2 < d3 && d2 < d4) {
		return n[1];
	    } else if (d3 < d2 && d3 < d1 && d3 < d4) {
		return n[2];
	    } else {
		return this;
	    }
	    }catch(RemoteException e1){
		return findNext(cord);
	    }
	} catch (RemoteException e) {

	} catch (NotBoundException e) {

	    e.printStackTrace();
	}
	return null;
    }

}
