import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * 
 * Customer.java
 * @author Trushank
 * Date Mar 29, 2013
 * Version 1.0
 *
 */
/**
 * Customer Represents the customer
 * 
 * @author Trushank
 * 
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private static RegistryProxy registry;
    private static String originatorName;
    private static String host;
    private static int port;
    private static Coordinates destination;
    private static CustomerListenerRef customerListner;

    /**
     * main
     * 
     * Command line arguments: args[0] = Registry Server's host args[1]
     * =Registry Server's port args[2] = name of originating GPSOffice args[3] =
     * X co-ordinate of GPSOffice args[4] = Y co-ordinate of destination
     * 
     * @param args
     * 
     */
    public static void main(String[] args) {
	// check number of args
	if (args.length != 5) {
	    System.err
		    .println("Usage: java Customer <host> <port> <name> <X> <Y>");
	    System.exit(1);
	}
	host = args[0];
	// check valid port
	try {
	    port = Integer.parseInt(args[1]);
	} catch (NumberFormatException e) {
	    System.err.println("Please enter a valid port");
	    e.printStackTrace();
	    System.exit(1);
	}
	originatorName = args[2];
	// check valid co-ordinates
	try {
	    destination = new Coordinates(Double.parseDouble(args[3]),
		    Double.parseDouble(args[4]));

	} catch (NumberFormatException e) {
	    System.err.println("Please enter valid co-ordinates");
	    e.printStackTrace();
	    System.exit(1);

	}
	
	// Look for originator GPSOffice in registry and dispatch package to it
	try {
	    registry = new RegistryProxy(host, port);
	    GPSOfficeRef originator = (GPSOfficeRef) registry
		    .lookup(originatorName);
	    customerListner = new CustomerListener();
	    UnicastRemoteObject.exportObject(customerListner, 0);

	    originator.beam(new Package(destination, customerListner));

	} catch (RemoteException e) {
	    System.err.println("No registry server found at given host-port");
	    e.printStackTrace();
	    System.exit(0);
	} catch (NotBoundException e) {
	    System.err.println("No GPSOffice with " + originatorName
		    + " name found");
	    e.printStackTrace();
	    System.exit(0);
	}
    }

}
