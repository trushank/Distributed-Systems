import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import edu.rit.numeric.ListSeries;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;

/**
 * 
 * Node.java
 * @author Trushank
 * Date Apr 14, 2013
 * Version 1.0
 *
 * 
 */

/**
 * 
 * Node Represents a Node/Viewer of the Video
 * 
 * @author Trushank Date: Apr 30, 2013
 */
public class Node {

    static int dropped = 0;
    private boolean pushPull = false;
    private Nodeinfo peers;
    private ArrayList<Integer> has;
    private String name;

    private int startTime;
    private int length;
    private int misses = 0;
    private Simulation sim;
    private VideoBuffer buffer;
    private int lastFrame = 0;
    private Random r;
    ListSeries missesList;

    /**
     * 
     * @param name
     *            : Name of node
     * @param startTime
     *            : Start time of video
     * @param length
     *            : length of video
     * @param sim
     *            : simulation object
     * @param bufferSize
     *            : size of video buffer
     * @param t
     *            : Tracker
     * @param pushPull
     *            : push-pull or pull system
     * @param misses
     *            : ListSeries to store miss data
     * @param seed
     *            : seed for random number generator
     */
    public Node(String name, int startTime, int length, final Simulation sim,
	    int bufferSize, Tracker t, boolean pushPull, ListSeries misses,
	    int seed) {
	this.missesList = misses;
	has = new ArrayList<Integer>();
	this.pushPull = pushPull;
	r = new Random(seed);
	this.length = length;
	this.startTime = startTime - 1;
	peers = new Nodeinfo(bufferSize);
	this.sim = sim;
	t.addViewer(this);
	buffer = new VideoBuffer(bufferSize);
	this.name = name;
	ArrayList<Node> nodes = t.getPeers();
	if (nodes != null) {
	    Iterator<Node> itr = nodes.iterator();
	    while (itr.hasNext()) {
		Node n = itr.next();
		if (n.equals(this))
		    continue;
		addNewPeer(n);
		n.addNewPeer(this);
	    }
	}
    }

    /**
     * 
     * start: Starts the node Date: Apr 30, 2013
     * 
     * @author: Trushank void
     * 
     */
    public void start() {
	sim.doAt(sim.time(), new Event() {

	    @Override
	    public void perform() {
		startPulling();
	    }
	});
	sim.doAfter(startTime - sim.time(), new Event() {
	    @Override
	    public void perform() {
		play();
	    }
	});

    }

    public static int getDropped() {
	int temp = dropped;
	dropped = 0;
	return temp;
    }

    public void receiveUpdate(long seq) {

    }

    /**
     * 
     * startPulling Start pulling frames from peers Date: Apr 30, 2013
     * 
     * @author: Trushank void
     * 
     */
    private void startPulling() {
	for (int i = 0; i < buffer.maxSize; i++) {
	    final int last = lastFrame;
	    sim.doAfter(i, new Event() {

		@Override
		public void perform() {

		    int next = buffer.checkNeeded(last);
		    if (next >= 0 && next < length) {
			pull(next);

		    }

		}
	    });
	}
    }

    /**
     * 
     * pull Request for a frame to peers Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @param seq
     *            Seq no of Frame void
     * 
     */
    private void pull(int seq) {
	if (buffer.get(new Long(seq)) == null) {

	    Node n = peers.findFrame(seq);
	    if (n != null) {
		n.requestFrame(this, seq);
	    }

	}
    }

    /**
     * 
     * getHas Get list of frames held by the node Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @return ArrayList<Integer>
     * 
     */
    public ArrayList<Integer> getHas() {
	return has;
    }

    /**
     * 
     * addNewPeer Add new peer to peer list of current node Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @param n
     * @return boolean
     * 
     */
    public boolean addNewPeer(Node n) {
	return peers.addNode(n);
    }

    /**
     * 
     * requestFrame Request frame to peers Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @param n
     * @param seq
     *            void
     * 
     */
    public void requestFrame(final Node n, final int seq) {
	sim.doAfter(r.nextInt(1), new Event() {

	    @Override
	    public void perform() {
		n.receiveFrame(buffer.get(new Long(seq)));
	    }
	});

    }

    /**
     * 
     * play Play the frames according to deadline Date: Apr 30, 2013
     * 
     * @author: Trushank void
     * 
     */
    private void play() {
	for (int i = 0; i < length; i++) {
	    sim.doAfter(i, new Event() {
		@Override
		public void perform() {
		    if (lastFrame < length - 1)
			pull(lastFrame + 1);
		    if (buffer.get((long) lastFrame) == null) {
			misses++;
		    } else {
			// Play Frame
		    }
		    if (lastFrame >= length - 1) {
			missesList.add(misses);
		    }
		    lastFrame++;
		}
	    });
	}
    }

    /**
     * 
     * push Pushes the frames to peers Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @param to
     *            : peers to push the frame to
     * @param seq
     *            : Frame number ot be forwarded @ return void
     * 
     */
    private void push(ArrayList<Node> to, final int seq) {

	final Iterator<Node> itr = to.iterator();
	while (itr.hasNext()) {
	    final Node n = itr.next();
	    if (r.nextInt(7) != 0)
		continue;
	    sim.doAfter(r.nextInt(6), new Event() {
		@Override
		public void perform() {
		    // Get the frame from local buffer and forward it to the
		    // peers
		    Frame f = buffer.get(new Long(seq));
		    if (f != null)
			n.receiveFrame(f);

		}
	    });
	}
    }

    /**
     * 
     * receiveFrame: Receive Frames sent by videosource of other peers Date: Apr
     * 30, 2013
     * 
     * @author: Trushank
     * @param f
     *            : Frame that was sent
     * @return void
     * 
     */
    public void receiveFrame(final Frame f) {
	// Check if it meets deadline
	if (f.getDeadline() > sim.time()) {
	    // if don't already have it, add it to buffer
	    if (buffer.get(f.getSeq()) == null) {
		buffer.put(f);
		has.add(new Integer((int) f.getSeq()));
		updatePeers(f.getSeq()); // Inform peers about new frame
		// If hybrid system, check if anyone needs the frame
		if (pushPull)
		    sim.doAfter(r.nextInt(5), new Event() {
			@Override
			public void perform() {
			    // push to whoever needs the frame
			    push(peers.whoNeeds((int) f.getSeq() - 3),
				    (int) f.getSeq() - 3);
			}
		    });
	    }
	}
    }

    /**
     * 
     * updateInfo Updates information about possession of new frame by peer
     * 
     * @param seq
     *            : seq no of the frame
     * @param n
     *            : which node has it
     */
    public void updateInfo(long seq, Node n) {
	// Add to the buffermap
	peers.addEntry(n, (int) seq);
    }

    /**
     * 
     * getName returns name of the node
     * 
     * @return name of the node
     */
    public String getName() {
	return name;
    }

    /**
     * 
     * updatePeers Sends update to all peers about new Frame
     * 
     * @param seq
     *            : Seq no of Frame
     */
    private void updatePeers(long seq) {
	// Iterate through the peers and update everyone
	Iterator<Node> itr = peers.getPeers().iterator();
	while (itr.hasNext()) {
	    itr.next().updateInfo(seq, this);
	}
    }
}
