import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 
 * Nodeinfo.java
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
public class Nodeinfo {
    HashSet<Info> info;
    int maxSize;

    public Nodeinfo(int maxSize) {
	info = new HashSet<Info>();
	this.maxSize = maxSize;
    }

    public Node findFrame(int seq) {

	Iterator<Info> itr = info.iterator();
	while (itr.hasNext()) {
	    Info next = itr.next();
	    if (next.checkFrame(seq)) {
		return next.getNode();
	    }
	}
	return null;
    }

    public boolean addNode(Node n) {
	return info.add(new Info(n, n.getHas(), maxSize));
    }

    public boolean removeNode(Node n) {
	Iterator<Info> itr = info.iterator();
	while (itr.hasNext()) {
	    Info i = itr.next();
	    if (i.getNode().equals(n)) {
		return info.remove(i);
	    }
	}
	return false;
    }

    public boolean addEntry(Node n, int seq) {
	Iterator<Info> itr = info.iterator();
	while (itr.hasNext()) {
	    Info i = itr.next();
	    if (i.getNode().equals(n)) {
		return i.addEntry(seq);
	    }
	}
	return false;
    }

    public ArrayList<Node> getPeers() {
	ArrayList<Node> peers = new ArrayList<Node>();
	Iterator<Info> itr = info.iterator();
	while (itr.hasNext()) {
	    peers.add(itr.next().getNode());
	}
	return peers;
    }

    public ArrayList<Node> whoNeeds(int seq) {
	ArrayList<Node> peers = new ArrayList<Node>();
	if (seq < 0)
	    return peers;

	Iterator<Info> itr = info.iterator();
	while (itr.hasNext()) {

	    Info nodeInfo = itr.next();
	    if (!nodeInfo.checkFrame(seq))
		peers.add(nodeInfo.getNode());
	}

	return peers;
    }

    private class Info {
	int maxSize;
	private Node n;
	private ArrayList<Integer> has;

	private Info(Node n, ArrayList<Integer> has, int maxSize) {
	    this.n = n;
	    this.maxSize = maxSize;
	    this.has = has;
	}

	public boolean checkFrame(int seq) {
	    if (has.contains(new Integer(seq)))
		return true;
	    return false;
	}

	public Node getNode() {
	    return n;
	}

	public boolean addEntry(int seq) {
	    if (has.size() == maxSize)
		has.remove(0);
	    return has.add(new Integer(seq));
	}
    }
}
