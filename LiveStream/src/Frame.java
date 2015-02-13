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
 * @author Trushank
 * 
 */
public class Frame {
    private long seq_no;
    private long deliveryDeadline;
    static long cur_seq = -1;

    public Frame(Simulation sim, int starttime) {
	this.seq_no = ++cur_seq;
	deliveryDeadline = (long) starttime + seq_no;

    }

    public long getSeq() {
	return seq_no;
    }

    public long getDeadline() {
	return deliveryDeadline;
    }
}
