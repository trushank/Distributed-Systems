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
public class FrameHashMap extends HashMap<Long, Frame>{
int maxSize=0;
public FrameHashMap(int maxSize){
    super();
    this.maxSize=maxSize;
}
 
    @Override
public Frame put(Long seq,Frame f){
    if(this.size()==maxSize){
	this.remove(new Long(seq-maxSize));
    }
    return super.put(seq, f);
}
}

