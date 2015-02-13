import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
 * @author Trushank
 * 
 */
public class Tracker {

    private ArrayList<Node> viewers;
    int defaultNoOfPeers;

    public Tracker(int defaultNoOfPeers) {
	this.defaultNoOfPeers = defaultNoOfPeers;
	viewers = new ArrayList<Node>();
    }

    public void addViewer(Node n) {
	viewers.add(n);
    }

    public ArrayList<Node> getPeers() {
	ArrayList<Node> n = new ArrayList<Node>();
	Random r = new Random(12345);
	HashSet<Integer> ran = new HashSet<>();
	// System.out.println("Viewers size: "+viewers.size());
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
