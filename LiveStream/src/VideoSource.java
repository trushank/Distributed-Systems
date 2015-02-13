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
 * @author Trushank
 * 
 */
public class VideoSource {
    VideoBuffer buffer;
    Random r;
    ArrayList<Node> firstLevel;
    int start;
    private Simulation sim;

    public VideoSource(Simulation sim, int length, int bufferSize,
	    ArrayList<Node> firstLevel, int start) {
	this.sim = sim;
	this.firstLevel = firstLevel;
	r = new Random(1234);
	this.buffer = new VideoBuffer(bufferSize);
	startVideo(length);
	this.start = start;
    }

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

    private Frame generate(int start) {
	return new Frame(sim, start);
    }

    private void push(final Frame f) {
	final Iterator<Node> itr = firstLevel.iterator();

	while (itr.hasNext()) {
	    final Node next = itr.next();

	    sim.doAfter(r.nextInt(2), new Event() {

		@Override
		public void perform() {
		    // System.out.println("Sending to "+next.getName());
		    next.receiveFrame(f);
		}
	    });

	}
    }

}
