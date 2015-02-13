import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* 
* CustomerListenerRef.java
* @author Trushank
* Date Apr 15, 2013
* Version 1.0
*
 */
 /**
 * @author Trushank
 *CustomerListenerRef
 *Interface for sending update to customer about his package
 */
public interface CustomerListenerRef extends Remote{
    /**
     * 
     * getUpdates
     * gets updates about the package
     * @param msg
     * @throws RemoteException
     */
public void getUpdates(String msg)  throws RemoteException;
}

