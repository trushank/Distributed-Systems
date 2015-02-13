import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.List;

import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryEvent;
import edu.rit.ds.registry.RegistryEventFilter;
import edu.rit.ds.registry.RegistryEventListener;
import edu.rit.ds.registry.RegistryProxy;

/**
 * 
 * Headquarters.java
 * @author Trushank
 * Date Mar 31, 2013
 * Version 1.0
 *
 */

/**
 * Headquarters Represents GPS-Headquarters
 * 
 * @author Trushank
 * 
 */
public class Headquarters {
    private static String host;
    private static int port;
    private static RegistryProxy registry;
    private static RemoteEventListener<GPSEvent> listener;
    private static RegistryEventListener registryListener;
    private static RegistryEventFilter registryBoundFilter;

    /**
     * main Command line arguments: args[0] = Registry Server's host args[1]
     * =Registry Server's port
     * 
     * @param args
     */
    public static void main(String[] args) {
	// check number of arguments
	if (args.length != 2) {
	    System.err.println("Usage: java Headquarters <host> <port>");
	    System.exit(1);
	}
	host = args[0];
	// check valid port
	try {
	    port = Integer.parseInt(args[1]);
	} catch (NumberFormatException e) {
	    System.err.println("Please enter a valid port");
	    System.exit(1);
	}
	try {
	    registry = new RegistryProxy(host, port);
	} catch (RemoteException e) {
	    System.err.println("No registry server found at given host-port");
	    System.exit(1);
	}
	// List, look up and then listen to events from all GPSOffice objects
	try {
	    List<String> names = registry.list("GPSOffice");
	    Iterator<String> itr = names.iterator();

	    // When updates arrive, print
	    listener = new RemoteEventListener<GPSEvent>() {
		@Override
		public void report(long arg0, GPSEvent event)
			throws RemoteException {
		    System.out.println(event.getMsg());
		}
	    };
	    UnicastRemoteObject.exportObject(listener, 0);

	    // Request registry to inform about newly opened offices
	    registryListener = new RegistryEventListener() {

		@Override
		public void report(long arg0, RegistryEvent event)
			throws RemoteException {
		    try {
			// Look up and listen to new office
			GPSOfficeRef office = (GPSOfficeRef) registry
				.lookup(event.objectName());
			office.addListener(listener);
		    } catch (NotBoundException exc) {
		    } catch (RemoteException exc) {
		    }

		}
	    };
	    UnicastRemoteObject.exportObject(registryListener, 0);
	    registryBoundFilter = new RegistryEventFilter().reportType(
		    "GPSOffice").reportBound();
	    registry.addEventListener(registryListener, registryBoundFilter);

	    // Request updates from all GPSOffice objects
	    while (itr.hasNext()) {
		GPSOfficeRef temp = (GPSOfficeRef) registry.lookup(itr.next());
		temp.addListener(listener);

	    }
	} catch (NotBoundException e) {
	    e.printStackTrace();
	} catch (RemoteException e) {

	    e.printStackTrace();
	}
    }

}
