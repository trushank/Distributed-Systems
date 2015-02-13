import java.util.HashMap;



/**
 * 
 * FrameHashMap.java
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
public class VideoBuffer extends HashMap<Long, Frame> {

    private static final long serialVersionUID = 1L;
    int maxSize = 0;

    public VideoBuffer(int maxSize) {
	super();
	this.maxSize = maxSize;
    }

    public int checkNeeded(int lastplayed) {
	for (int i = 0; i < maxSize; i++) {
	    if (get(new Long(lastplayed + i)) == null) {
		return lastplayed + i;
	    }
	}
	return -1;
    }

    public Frame put(Frame f) {
	if (this.size() == maxSize) {
	    // System.out.println("Removing frame " + (f.getSeq() - maxSize));
	    this.remove(new Long(f.getSeq() - maxSize));
	}
	return super.put(f.getSeq(), f);
    }
}
