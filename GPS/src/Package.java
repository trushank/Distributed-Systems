import java.io.Serializable;

/**
 * 
 * Package.java
 * Represents a package in the GPS system
 * @author Trushank
 * Date Mar 27, 2013
 * Version 1.0
 *
 * 
 */
/**
 * Package
 * Represents a package
 * @author Trushank
 * 
 */
public class Package implements Serializable {

    private static final long serialVersionUID = 1L;
    private CustomerListenerRef listner;
    private long trackingID = 0;
    private Coordinates destination;

    /**
     * 
     * @param cord
     *            Destination
     * @param listner
     *            Customers Listner
     */
    public Package(Coordinates cord, CustomerListenerRef listner) {

	this.listner = listner;
	destination = cord;
    }

    /**
     * 
     * getListener Retrieve the customers listner sent along with package
     * 
     * @return listner
     */
    public CustomerListenerRef getListener() {
	return listner;
    }

    /**
     * 
     * getDestination Retrieves the destination of the package
     * 
     * @return destination
     */
    public Coordinates getDestination() {
	return destination;
    }

    /**
     * 
     * checkTrackingID Checks if tracking id is assigned
     * 
     * @return if tracking id is assigned
     */
    public boolean checkTrackingID() {
	if (trackingID == 0)
	    return true;
	return false;
    }

    /**
     * 
     * assignTrackingID assigns TrackingID to the package
     * 
     * @param trackingID
     * @return
     */
    public boolean assignTrackingID(long trackingID) {
	if (this.trackingID == 0) {
	    this.trackingID = trackingID;
	    return true;
	}
	return false;
    }

    /**
     * 
     * getTrackingNumber Retrieves the trackingID of the package
     * 
     * @return TrackingID
     */
    public long getTrackingNumber() {
	return trackingID;
    }

}
