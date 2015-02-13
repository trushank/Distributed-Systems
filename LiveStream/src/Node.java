import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
 * @author Trushank
 * 
 */
public class Node {
    private static int totalMisses = 0;
    private static int dropped = 0;
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
    private Tracker t;
    private Random r;

    public Node(String name, int startTime, int length, Simulation sim,
	    int bufferSize, Tracker t, boolean pushPull) {
	has = new ArrayList<Integer>();
	this.pushPull = pushPull;
	r = new Random(1234);
	this.length = length;
	this.startTime = startTime - 1;
	peers = new Nodeinfo(bufferSize);
	this.sim = sim;
	t.addViewer(this);
	buffer = new VideoBuffer(bufferSize);
	this.t = t;
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
	sim.doAt(sim.time(), new Event() {

	    @Override
	    public void perform() {
		startPulling();

	    }
	});
	sim.doAfter(startTime - sim.time(), new Event() {

	    @Override
	    public void perform() {

		 System.out.println(getName() + " now playing");
		play();

	    }
	});

    }

    public static int getTotalMisses() {
	int temp = totalMisses;
	totalMisses = 0;
	return temp;
    }

    public static int getDropped() {
	int temp = dropped;
	dropped = 0;
	return temp;
    }

    public void receiveUpdate(long seq) {

    }

    private void startPulling() {

	int x = 0;
	for (int i = 0; i < buffer.maxSize; i++) {
	    final int last = lastFrame;
	    // System.out.println("here"+x);
	    sim.doAfter(i, new Event() {

		@Override
		public void perform() {

		    int next = buffer.checkNeeded(last);
		    // System.out.println("Next: "+next);

		    if (next >= 0 && next < length) {
			pull(next);
		    }

		}
	    });
	}
    }

    private void pull(int seq) {
	if (buffer.get(new Long(seq)) == null) {

	    Node n = peers.findFrame(seq);
	    if (n != null) {
		 System.out.println(getName() + " requesting for " + seq+
		 " to " + n.getName()+" at "+sim.time());
		n.requestFrame(this, seq);
	    }

	}
    }

    public ArrayList<Integer> getHas() {
	return has;
    }

    public boolean addNewPeer(Node n) {
	// System.out.println(getName()+" added "+n.getName());
	return peers.addNode(n);
    }

    public void requestFrame(final Node n, final int seq) {
	sim.doAfter(r.nextInt(3), new Event() {

	    @Override
	    public void perform() {
		n.receiveFrame(buffer.get(new Long(seq)));
	    }
	});

    }

    private void play() {

	for (int i = 0; i < length; i++) {

	    final long next = i;
	    sim.doAfter(i, new Event() {

		@Override
		public void perform() {
		    if (lastFrame < length - 1)
			pull(lastFrame + 1);
		    if (buffer.get((long) lastFrame) == null) {
			misses++;
			totalMisses++;

			// System.out.println(getName() + " missed "+
			// (lastFrame) + " at " + sim.time());
		    } else {

			// System.out.println(getName() + " Playing "+
			// (lastFrame) + " at " + sim.time());
		    }
		    lastFrame++;

		}
	    });
	}

    }

    private void push(ArrayList<Node> to, final int seq) {

	final Iterator<Node> itr = to.iterator();

	while (itr.hasNext()) {
	    final Node n = itr.next();

	    sim.doAfter(r.nextInt(5), new Event() {

		@Override
		public void perform() {

		    Frame f = buffer.get(new Long(seq));
		    if (f != null)
			n.receiveFrame(f);

		}
	    });

	}
    }

    public void receiveFrame(Frame f) {
	if (f.getDeadline() > sim.time()) {
	    if (buffer.get(f.getSeq()) == null) {

		buffer.put(f);
		has.add(new Integer((int) f.getSeq()));
		// System.out.println(name + " receieved frame " + f.getSeq() +
		// " at "+ sim.time() + " deadline " + f.getDeadline());
		updatePeers(f.getSeq());
		if (pushPull)
		    push(peers.whoNeeds((int) f.getSeq() - 3),
			    (int) f.getSeq() - 3);
	    }
	}

	else {
	    // System.out.println(name + " dropped frame " + f.getSeq() +
	    // " at "+ sim.time() + " deadline " + f.getDeadline());
	    dropped++;
	}
    }

    /**
     * 
     * updateInfo Updates information about possession of new frame by peer
     * 
     * @param seq
     * @param n
     */
    public void updateInfo(long seq, Node n) {
	// System.out.println(n.getName() + " has " + seq);
	peers.addEntry(n, (int) seq);
    }

    /**
     * 
     * getName returns name of the node
     * 
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * 
     * updatePeers Sends update to all peers
     * 
     * @param seq
     */
    private void updatePeers(long seq) {
	Iterator<Node> itr = peers.getPeers().iterator();
	while (itr.hasNext()) {
	    itr.next().updateInfo(seq, this);
	}
    }
}
