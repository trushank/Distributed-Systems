import java.io.Serializable;
import java.rmi.RemoteException;

/**
* 
* CustomerListner.java
* @author Trushank
* Date Apr 15, 2013
* Version 1.0
* 
 */

/**
 * CustomerListener
 * Remote object used to notify customer of the packages progress
 * @author Trushank
 * 
 */
class CustomerListener implements Serializable, CustomerListenerRef {

    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see CustomerListenerRef#getUpdates(java.lang.String)
     */
    @Override
    public void getUpdates(String msg) throws RemoteException {
	
		System.out.println(msg);
		if (msg.contains("delivered from") || msg.contains("lost")) {
		    System.exit(0);
		}
	    
	
    }

  
}