import java.util.ArrayList;

import edu.rit.sim.Simulation;

/**
 * 
 * Main.java
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
public class Main {

    /**
     * main
     * 
     * @param args
     */
    public static void main(String[] args) {
	//run(false);	//Standard Pull
	 run(true);	//Hybrid Push-Pull

    }

    public static void run(boolean pushPull) {

	Simulation sim = new Simulation();
	int defaultPeers = 5;
	int videoLength = 500;
	int start = 15;
	int bufferSize = 20;
	int numberOfFirstLevelNodes = 10;
	int totalNodes = 50;
	if (pushPull) {
	    System.out
		    .println("Running Simulation for \"Hybrid Push-Pull\" with "
			    + totalNodes
			    + " nodes, Video Length: "
			    + videoLength + "\n");
	} else {
	    System.out.println("Running Simulation for \"Standard Push\" with "
		    + totalNodes + " nodes, Video Length: " + videoLength
		    + "\n");
	}
	ArrayList<Node> firstLevel = new ArrayList<Node>();
	Tracker t = new Tracker(defaultPeers);
	totalNodes = totalNodes - numberOfFirstLevelNodes;
	if (bufferSize <= start) {
	    throw new IllegalArgumentException(
		    "Buffer Size must be more than time to start");
	}
	start = (int) sim.time() + start;
	for (int i = 0; i < numberOfFirstLevelNodes; i++) {
	    firstLevel.add(new Node(Integer.toString(i), start, videoLength,
		    sim, bufferSize, t, pushPull));
	}
	for (int i = numberOfFirstLevelNodes; i < totalNodes
		+ numberOfFirstLevelNodes; i++) {
	    new Node(Integer.toString(i), start, videoLength, sim, bufferSize,
		    t, pushPull);
	}
	VideoSource v = new VideoSource(sim, videoLength, bufferSize,
		firstLevel, start);

	sim.run();
	System.out
		.println("Total Misses: "
			+ (Node.getTotalMisses() / (totalNodes + numberOfFirstLevelNodes)));
	System.out.println("Total Dropped: "
		+ (Node.getDropped() / (totalNodes + numberOfFirstLevelNodes)));
    }
}
