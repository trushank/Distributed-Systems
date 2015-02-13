import edu.rit.sim.Simulation;

/**
 * 
 * Frame.java
 * @author Trushank
 * Date Apr 4, 2013
 * Version 1.0
 *
 * 
 */

/**
 * 
 * Frame: Represents a frame
 * 
 * @author Trushank Date: Apr 30, 2013
 */
public class Frame {
    private long seq_no; // seq no of the frame
    private long deliveryDeadline; // expected deadline of the frame

    /**
     * 
     * @param sim
     *            : Simulation object
     * @param starttime
     *            : start time of the video
     * @param seq
     *            : seq no of the frame
     */
    public Frame(Simulation sim, int starttime, int seq) {

	this.seq_no = seq;
	deliveryDeadline = (long) starttime + seq_no;

    }

    /**
     * 
     * getSeq Get seq number of the frame Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @return long seq number
     * 
     */
    public long getSeq() {
	return seq_no;
    }

    /**
     * 
     * getDeadline get deadline of the frame Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @return long
     * 
     */
    public long getDeadline() {
	return deliveryDeadline;
    }
}
