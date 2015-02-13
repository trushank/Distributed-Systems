import java.util.ArrayList;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.ListXYSeries;
import edu.rit.sim.Simulation;

/**
 * SimRun.java
 * 
 * @author Trushank Date: Apr 29, 2013
 */

/**
 * 
 * SimRun Runs one run of the simulation
 * 
 * @author Trushank Date: May 1, 2013
 */
public class SimRun {
    ListSeries misses = new ListSeries();
    VideoSource vS;
    Node nd;
    int totalNodes;
Tracker trac;
    /**
     * 
     * run Run the simulation Date: May 1, 2013
     * 
     * @author: Trushank
     * @param pushPull
     *            : Standard or Hybrid System
     * @param nodesOrLength
     *            : Knob as viewers or video length
     * @param videoLength
     *            : Number of Frames
     * @param totalNodes
     *            : Number of viewers
     * @param resultMean
     *            : ListSeries to store mean
     * @param resultVar
     *            : ListSeries to store variance
     * @param seed
     *            void: Random seed
     * 
     */
    public void run(boolean pushPull, boolean nodesOrLength, int videoLength,
	    int totalNodes, ListXYSeries resultMean, ListXYSeries resultVar,
	    int seed) {
	this.totalNodes = totalNodes;
	Simulation sim = new Simulation();
	int defaultPeers = 5;
	int start = 15;
	int bufferSize = 20;
	int numberOfFirstLevelNodes = 5;

	ArrayList<Node> firstLevel = new ArrayList<Node>();
	Tracker t = new Tracker(defaultPeers, seed);
	int totalNodes1 = totalNodes - numberOfFirstLevelNodes;
	if (bufferSize <= start) {
	    throw new IllegalArgumentException(
		    "Buffer Size must be more than time to start");
	}

	// Video Start time
	start = (int) sim.time() + start;

	// Create first level nodes
	for (int i = 0; i < numberOfFirstLevelNodes; i++) {
	    Node a = new Node(Integer.toString(i), start, videoLength, sim,
		    bufferSize, t, pushPull, misses, seed);
	    firstLevel.add(a);
	    a.start();
	}
	// Create rest of nodes
	for (int i = numberOfFirstLevelNodes; i < totalNodes1
		+ numberOfFirstLevelNodes; i++) {
	    new Node(Integer.toString(i), start, videoLength, sim, bufferSize,
		    t, pushPull, misses, seed).start();
	}
	// Create new Video Source
	new VideoSource(sim, videoLength, bufferSize, firstLevel, start, seed);
	// Run simulation
	sim.run();

	if (nodesOrLength) {
	    resultMean.add(totalNodes, misses.stats().mean);
	    resultVar.add(totalNodes, misses.stats().var);
	} else {
	    resultMean.add(videoLength, misses.stats().mean);
	    resultVar.add(videoLength, misses.stats().var);
	}

    }

    /**
     * 
     * getSeries Returns the listseries for this simulation Date: May 1, 2013
     * 
     * @author: Trushank
     * @return ListSeries
     * 
     */
    public ListSeries getSeries() {
	return misses;
    }
}
