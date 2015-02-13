
/**
* 
* Node.java
* @author Trushank
* Date Apr 4, 2013
* Version 1.0
*
 */
 /**
 * @author Trushank
 *
 */
 public class Node {
     String name;
     VideoSource vidSource;
     long startTime;
     long vidLength;
     int misses=0;
     Frame current;
     long lastPlayed;
     int frameRate=100;
   public Node(VideoSource vidSource, long startTime, long vidLength, String name){
       this.startTime=startTime;
       this.vidSource=vidSource;
       this.vidLength=vidLength;
       this.name=name;
       lastPlayed=0;
       start();
   }
   public void start(){
       while(vidLength!=0){
	   long timeToNext=startTime+(lastPlayed+1)*frameRate;
	   while((System.currentTimeMillis()<startTime)||(System.currentTimeMillis()<timeToNext)){
	       //do nothing
	       try {
		   if(System.currentTimeMillis()<startTime)
		Thread.sleep(startTime-(System.currentTimeMillis()-20));
		   else
		       Thread.sleep(timeToNext-(System.currentTimeMillis()-20));
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    //System.out.println(name+" Waiting");
	   }
	   long nextReq=(((System.currentTimeMillis()-10)-startTime)/frameRate);
	   int skipped=(int)(nextReq-(lastPlayed+1));
	   if(skipped>=vidLength)
	       break;
	   misses=misses+skipped;
	   current=vidSource.get(nextReq);
	  lastPlayed=nextReq;
	   play(current);
	   vidLength=vidLength-skipped;
	   vidLength--;
       }
       System.out.println("########################"+name+" missed "+misses+" frames");
   }
   
   public void play(Frame f){
       try{
	   if(System.currentTimeMillis()<=current.deliveryDeadline)
	   System.out.println(name+" Playing Frame "+f.getSeq());
	   else{
	       System.out.println(name+" Frame Missed");
	       misses++;
	       }
       }catch(NullPointerException e){
	   System.out.println(name+" Frame Missed");
	   misses++;
       }
   }
}

