import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * 
 * Tracker.java
 * @author Trushank
 * Date Apr 14, 2013
 * Version 1.0
 *
 */

/**
 * 
 * Tracker Tracks the viewers of the video and assigns peers
 * 
 * @author Trushank Date: Apr 30, 2013
 */
public class Tracker {

    private ArrayList<Node> viewers; // list of viewers
    int defaultNoOfPeers; // default number of peers initially assigned
    Random r;

    /**
     * 
     * @param defaultNoOfPeers
     * @param seed
     */
    public Tracker(int defaultNoOfPeers, int seed) {
	this.defaultNoOfPeers = defaultNoOfPeers;
	viewers = new ArrayList<Node>();
	r = new Random(seed);
    }

    /**
     * 
     * addViewer Adds new viewer to the video Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @param n
     *            : new viewer void
     * 
     */
    public void addViewer(Node n) {
	viewers.add(n);
    }

    /**
     * 
     * getPeers Gets random peers for newly joining node Date: Apr 30, 2013
     * 
     * @author: Trushank
     * @return ArrayList<Node>
     * 
     */
    public ArrayList<Node> getPeers() {
	ArrayList<Node> n = new ArrayList<Node>();
	HashSet<Integer> ran = new HashSet<Integer>();
	for (int i = 0; i < viewers.size() && i < defaultNoOfPeers; i++) {
	    try {
		Integer a = new Integer(r.nextInt(viewers.size()));
		while (!ran.add(a)) {
		    a = new Integer(r.nextInt(viewers.size()));
		}
		n.add(viewers.get(a));

	    } catch (IllegalArgumentException e) {
		return null;
	    }
	}
	return n;
    }
}
