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
 * 
 * VideoBuffer Acts as a videobuffer
 * 
 * @author Trushank Date: May 1, 2013
 */
public class VideoBuffer extends HashMap<Long, Frame> {
Frame f;
    private static final long serialVersionUID = 1L;
    int maxSize = 0;

    /**
     * 
     * @param maxSize
     *            : Maxsize of buffer
     */
    public VideoBuffer(int maxSize) {
	super();
	this.maxSize = maxSize;
    }

    /**
     * 
     * checkNeeded: Checks which frames would be needed in the future Date: May
     * 1, 2013
     * 
     * @author: Trushank
     * @param lastplayed
     * @return int
     * 
     */
    public int checkNeeded(int lastplayed) {
	for (int i = 0; i < maxSize; i++) {
	    if (get(new Long(lastplayed + i)) == null) {
		return lastplayed + i;
	    }
	}
	return -1;
    }

    /**
     * 
     * put: add frame to buffer Date: May 1, 2013
     * 
     * @author: Trushank
     * @param f
     *            : The frame
     * @return Frame
     * 
     */
    public Frame put(Frame f) {
	if (this.size() == maxSize) {

	    this.remove(new Long(f.getSeq() - maxSize));
	}
	return super.put(f.getSeq(), f);
    }
}
