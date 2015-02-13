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
 * 
 * Nodeinfo Stores info about what the peers are holding
 * 
 * @author Trushank Date: May 1, 2013
 */
public class Nodeinfo {
    HashSet<Info> info;
    int maxSize;

    /**
     * 
     * @param maxSize
     *            : MaxSize of the buffer
     */
    public Nodeinfo(int maxSize) {
	info = new HashSet<Info>();
	this.maxSize = maxSize;
    }

    /**
     * 
     * findFrame: Find which node has the given frame Date: May 1, 2013
     * 
     * @author: Trushank
     * @param seq
     *            : Frame seq number
     * @return Node: Node holding that frame
     * 
     */
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

    /**
     * 
     * addNode: Add node to the list Date: May 1, 2013
     * 
     * @author: Trushank
     * @param n
     *            : New Node
     * @return boolean
     * 
     */
    public boolean addNode(Node n) {
	return info.add(new Info(n, n.getHas(), maxSize));
    }

    /**
     * 
     * removeNode: Remove node from the list Date: May 1, 2013
     * 
     * @author: Trushank
     * @param n
     *            : Node to be removed
     * @return boolean
     * 
     */
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

    /**
     * 
     * addEntry: Add new entry about a new Frame Date: May 1, 2013
     * 
     * @author: Trushank
     * @param n
     *            : Which node has a new frame
     * @param seq
     *            : The new frame seq
     * @return boolean
     * 
     */
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

    /**
     * 
     * getPeers: List all the peers connected to this node Date: May 1, 2013
     * 
     * @author: Trushank
     * @return ArrayList<Node>
     * 
     */
    public ArrayList<Node> getPeers() {
	ArrayList<Node> peers = new ArrayList<Node>();
	Iterator<Info> itr = info.iterator();
	while (itr.hasNext()) {
	    peers.add(itr.next().getNode());
	}
	return peers;
    }

    /**
     * 
     * whoNeeds: Chec who needs this frame Date: May 1, 2013
     * 
     * @author: Trushank
     * @param seq
     *            : Frame to be checked
     * @return ArrayList<Node>: List of nodes who need this frame
     * 
     */
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

    /**
     * 
     * Info: Stores info about Node and the list of frames it has
     * 
     * @author Trushank Date: May 1, 2013
     */
    private class Info {
	int maxSize;
	private Node n;
	private ArrayList<Integer> has;

	/**
	 * 
	 * @param n
	 *            Node
	 * @param has
	 *            List of frames it has
	 * @param maxSize
	 *            maxsize of list
	 */
	private Info(Node n, ArrayList<Integer> has, int maxSize) {
	    this.n = n;
	    this.maxSize = maxSize;
	    this.has = has;
	}

	/**
	 * 
	 * checkFrame: Check if this node has the frame Date: May 1, 2013
	 * 
	 * @author: Trushank
	 * @param seq
	 *            : Frame to be checked
	 * @return boolean
	 * 
	 */
	public boolean checkFrame(int seq) {
	    if (has.contains(new Integer(seq)))
		return true;
	    return false;
	}

	/**
	 * 
	 * getNode: get current node Date: May 1, 2013
	 * 
	 * @author: Trushank
	 * @return Node
	 * 
	 */
	public Node getNode() {
	    return n;
	}

	/**
	 * 
	 * addEntry: add info about new frame aquired by node Date: May 1, 2013
	 * 
	 * @author: Trushank
	 * @param seq
	 *            : Frame seq no of new frame
	 * @return boolean
	 * 
	 */
	public boolean addEntry(int seq) {
	    if (has.size() == maxSize)
		has.remove(0);
	    return has.add(new Integer(seq));
	}
    }
}
