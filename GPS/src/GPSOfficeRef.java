/**
* GPSOfficeRef.java
* @author Trushank
* Date Mar 25, 2013
* Version 1.0
* 
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventListener;
/**
 * @author Trushank
 *
 */
public interface GPSOfficeRef extends Remote{
   /**
    * Interface GPSOfficeRef defines a GPSOffice object in a distributed enviornment
    */
    
 
    /**
     * 
     * getDistance
     * @param x X position of destination
     * @param y Y position of destination
     * @return Eucladian distance between the current node and the parameters
     * @throws RemoteException
     */
    public Coordinates getCoordiantes() throws RemoteException;
    
    /**
     * 
     * beam Beams the package to specified GPSOffice
     * @param name GPSOffice name
     * @throws RemoteException
     */
    public void beam(Package p) throws RemoteException;

    
      /**
       * Add the given remote event listener to this node. Everytime a package arrives or leaves
       * this GPSOffice, an event is sent to the listeners (GPS Main office and Client)
       * @param  listener  Remote event listener.
       */
      public Lease addListener
         (RemoteEventListener<GPSEvent> listener)
         throws RemoteException;

    
      }





