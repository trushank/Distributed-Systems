import java.awt.Label;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* 
* VideoSource.java
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
 public class VideoSource {
     int seed=124444;
     Random r=new Random(seed);
static FrameHashMap frames;
final int numbOfFrames;
  
    public Frame get(long seq){
	//delay
	
	try{
	    Thread.sleep(r.nextInt(200));
	}
	catch(InterruptedException  e){
	    
	}
	return frames.get(new Long(seq));
    }
    /**
     * 
     * generate Generates new Frame for transmission
     * @return Next Frame
     */
    public static Frame generate(){
	return new Frame();
    }
    public VideoSource(int bufferSize,int lengthOfVid){
	
frames=new FrameHashMap(bufferSize);
	
	numbOfFrames=lengthOfVid;
    }
    /**
     * 
     * Start: Starts recording video
     */
    public void Start(){
System.out.println("Streaming Started");
	ExecutorService exec = Executors.newCachedThreadPool();
	exec.execute(new Runnable() {

	    @Override
	    public void run() {
   for(int i=0;i<numbOfFrames;i++){
	try {
	    Thread.sleep(100);
	    Frame temp=VideoSource.generate();
	    frames.put(temp.getSeq(), temp);
	   
	} catch (InterruptedException e) {
	
	    e.printStackTrace();
	}
	
		
	    }
	    }
   });
    }

}

