/**
* 
* Main.java
* @author Trushank
* Date Apr 4, 2013
* Version 1.0
*
 */
 /**
 * @author Trushank
 *
 */
public class Main {

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
	 final long vidLength=100;
    final VideoSource v=new VideoSource(10, (int)vidLength);
    final long startTime=System.currentTimeMillis()+2000;
   
   Thread t=new Thread (new Runnable() {
        
        @Override
        public void run() {
  
            while(System.currentTimeMillis()<startTime){}
            v.Start();
        }
    });
  
   Thread t1=new Thread (new Runnable() {
       
       @Override
       public void run() {
   	// TODO Auto-generated method stub
	   new Node(v,startTime ,vidLength,"First");
       }
   });
   Thread t2=new Thread (new Runnable() {
       
       @Override
       public void run() {
	   new Node(v, startTime,vidLength,"Second");
       }
   });
   Thread t3=new Thread (new Runnable() {
       
       @Override
       public void run() {
	   new Node(v, startTime,vidLength,"Third");
       }
   });
  
   
   t.start();
   t1.start();
   t2.start();
   t3.start();
    }

}
