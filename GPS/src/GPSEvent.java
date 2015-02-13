import java.io.Serializable;

import edu.rit.ds.RemoteEvent;

/**
 * 
 * GPSEvent.java
 * @author Trushank
 * Date Mar 25, 2013
 * Version 1.0
 *
 */
/**
 * GPSEvent Represents a remote event in the GPS system
 * 
 * @author Trushank
 * 
 */
public class GPSEvent extends RemoteEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String msg; // Message containing tracking info

    /**
     * GPSEvent
     * 
     * @param msg
     *            Tracking info
     */
    public GPSEvent(String msg) {
	this.msg = msg;

    }

    /**
     * 
     * getMsg Retrieves the Message for the event
     * 
     * @return message
     */
    public String getMsg() {
	return msg;
    }
}
