import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import edu.rit.sim.Event;
import edu.rit.sim.Simulation;

/**
 * 
 * VideoSource.java
 * @author Trushank
 * Date Apr 14, 2013
 * Version 1.0
 *
 */
/**
 * 
 * VideoSource Acts as the videosource
 * 
 * @author Trushank Date: May 1, 2013
 */
public class VideoSource {
    VideoBuffer buffer;
    Random r;
    ArrayList<Node> firstLevel;
    int start;
    int last_seq_no = -1;
    private Simulation sim;

    /**
     * 
     * @param sim
     *            : Simulation object
     * @param length
     *            : Length of video
     * @param bufferSize
     *            : Size of buffer
     * @param firstLevel
     *            : List of first level nodes
     * @param start
     *            : Start time
     * @param seed
     *            : Random seed
     */
    public VideoSource(Simulation sim, int length, int bufferSize,
	    ArrayList<Node> firstLevel, int start, int seed) {
	this.sim = sim;
	this.firstLevel = firstLevel;
	r = new Random(seed);
	this.buffer = new VideoBuffer(bufferSize);
	startVideo(length);
	this.start = start;
    }

    /**
     * 
     * startVideo: Start streaming video Date: May 1, 2013
     * 
     * @author: Trushank
     * @param length
     *            : Length of video void
     * 
     */
    private void startVideo(int length) {
	for (int i = 0; i < length; i++) {
	    sim.doAfter(i + 1, new Event() {

		@Override
		public void perform() {
		    Frame f = generate(start);
		    // System.out.println("New Frame generated "+f.getSeq()+" at "+sim.time());
		    buffer.put(f);
		    push(f);

		}
	    });
	}

    }

    /**
     * 
     * generate: Generate a new frame Date: May 1, 2013
     * 
     * @author: Trushank
     * @param start
     * @return Frame
     * 
     */
    private Frame generate(int start) {
	return new Frame(sim, start, ++last_seq_no);
    }

    /**
     * 
     * push: Push frame to first level nodes Date: May 1, 2013
     * 
     * @author: Trushank
     * @param f
     *            : The frame void
     * 
     */
    private void push(final Frame f) {
	final Iterator<Node> itr = firstLevel.iterator();

	while (itr.hasNext()) {
	    final Node next = itr.next();

	    sim.doAfter(r.nextInt(2), new Event() {

		@Override
		public void perform() {
		    next.receiveFrame(f);
		}
	    });

	}
    }

}
