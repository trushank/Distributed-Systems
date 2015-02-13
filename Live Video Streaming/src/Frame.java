/**
* 
* Frame.java
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
 public class Frame {
long seq_no;
long deliveryDeadline;
static long cur_seq=0;
public Frame(){
    this.seq_no=++cur_seq;
    deliveryDeadline=System.currentTimeMillis()+500;
    
}

public long getSeq(){
    return seq_no;
}

}

