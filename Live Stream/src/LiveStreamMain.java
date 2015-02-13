import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.rit.numeric.ListSeries;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.Statistics;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;

/**
 * 
 * LiveStreamMain.java
 * @author Trushank
 * Date Apr 14, 2013
 * Version 1.0
 * 
 * 
 */
/**
 * 
 * LiveStreamMain Runs the entire simulation
 * 
 * @author Trushank Date: Apr 30, 2013
 */
public class LiveStreamMain {

    SimRun sRun;

    /**
     * main
     * 
     * @param args
     */
    public static void main(String[] args) {
	// Simulation with Video Length as a Knob
	ListXYSeries missesStandardVidLength;
	ListXYSeries missesHybridVidLength;
	// Simulation with Number of nodes as knob
	ListXYSeries missesStandardNodes;
	ListXYSeries missesHybridNodes;

	// To store variance values
	ListXYSeries missesStandardVidLengthVar;
	ListXYSeries missesHybridVidLengthVar;
	// Simulation with Number of nodes as knob
	ListXYSeries missesStandardNodesVar;
	ListXYSeries missesHybridNodesVar;
	double threshold = 0.05;
	// check args
	if (args.length != 8) {
	    System.out
		    .println("Usage: java LiveStreamMain <Video Length Lower Bound> <Video Length Upper Bound> <Video Length Step> <Number of Nodes Lower Bound> <Number of Nodes Upper Bound> <Number of Nodes Step> <Seed> <Threshold>");
	    System.exit(0);
	}
	// Simulation with Video Length as a Knob
	missesHybridVidLength = new ListXYSeries();
	missesStandardVidLength = new ListXYSeries();
	// Simulation with Number of nodes as knob
	missesHybridNodes = new ListXYSeries();
	missesStandardNodes = new ListXYSeries();

	// to store variance
	missesStandardVidLengthVar = new ListXYSeries();
	missesHybridVidLengthVar = new ListXYSeries();
	missesStandardNodesVar = new ListXYSeries();
	missesHybridNodesVar = new ListXYSeries();

	// to store t and p values
	ListSeries tValueVid = new ListSeries();
	ListSeries pValueVid = new ListSeries();
	ListSeries tValueNod = new ListSeries();
	ListSeries pValueNod = new ListSeries();

	// Video length parameters
	int videoLengthMin = 0; // min
	int videoLengthMax = 0; // max
	int videoLengthStep = 0; // step
	int videoLength = 0; // loop variable

	// Number of nodes parameters
	int totalNodesMin = 0; // min
	int totalNodesMax = 0; // max
	int totalNodesStep = 0; // step
	int totalNodes = 0; // loop variable
	int seed = 0;

	// initialize to args
	try {
	    threshold = Double.parseDouble(args[7]);
	    videoLengthMin = Integer.parseInt(args[0]);// 10;
	    videoLengthMax = Integer.parseInt(args[1]);// 500;
	    videoLengthStep = Integer.parseInt(args[2]);// 10;
	    videoLength = videoLengthMin;
	    totalNodesMin = Integer.parseInt(args[3]);// 10;
	    totalNodesMax = Integer.parseInt(args[4]);// 500;
	    totalNodesStep = Integer.parseInt(args[5]);// 10;
	    totalNodes = totalNodesMin;
	    seed = Integer.parseInt(args[6]);
	} catch (NumberFormatException e) {
	    System.out
		    .println("Usage: java LiveStreamMain <Video Length Lower Bound> <Video Length Upper Bound> <Video Length Step> <Number of Nodes Lower Bound> <Number of Nodes Upper Bound> <Number of Nodes Step> <Seed> <Threshold>");
	    System.exit(0);
	}

	// Run simulation with video length as Knob
	while (videoLength <= videoLengthMax) {
	    SimRun a = new SimRun(); // Pull System
	    a.run(false, false, videoLength, 50, missesStandardVidLength,
		    missesStandardVidLengthVar, seed); // Standard Pull

	    SimRun b = new SimRun(); // Push-Pull System
	    b.run(true, false, videoLength, 50, missesHybridVidLength,
		    missesHybridVidLengthVar, seed); // Hybrid Push-Pull

	    // t-test
	    double[] tTestResult = Statistics.tTestUnequalVariance(
		    a.getSeries(), b.getSeries());
	    tValueVid.add(tTestResult[0]);
	    pValueVid.add(tTestResult[1]);

	    // increment vid length
	    videoLength = videoLength + videoLengthStep;
	}

	// Run simulation with number of viewers as Knob
	while (totalNodes <= totalNodesMax) {
	    SimRun a = new SimRun(); // Pull System
	    a.run(false, true, 100, totalNodes, missesStandardNodes,
		    missesStandardNodesVar, seed); // Standard
						   // Pull
	    SimRun b = new SimRun(); // Push Pull System
	    b.run(true, true, 100, totalNodes, missesHybridNodes,
		    missesHybridNodesVar, seed); // Hybrid
						 // Push-Pull

	    // t-test
	    double[] tTestResult = Statistics.tTestUnequalVariance(
		    a.getSeries(), b.getSeries());
	    tValueNod.add(tTestResult[0]);
	    pValueNod.add(tTestResult[1]);

	    // Incrementing number of nodes
	    totalNodes = totalNodes + totalNodesStep;
	}

	// Results

	Iterator<Double> nodesHItr = missesHybridNodes.ySeries().iterator();
	Iterator<Double> nodesSItr = missesStandardNodes.ySeries().iterator();
	Iterator<Double> nodesItr = missesHybridNodes.xSeries().iterator();
	Iterator<Double> vidHItr = missesHybridVidLength.ySeries().iterator();
	Iterator<Double> vidSItr = missesStandardVidLength.ySeries().iterator();
	Iterator<Double> vidItr = missesHybridVidLength.xSeries().iterator();

	// Variance
	// Video length
	// Standard
	Iterator<Double> vidSItrVar = missesStandardVidLengthVar.ySeries()
		.iterator();
	// Hybrid
	Iterator<Double> vidHItrVar = missesHybridVidLengthVar.ySeries()
		.iterator();
	// Number of viewers
	// Standard
	Iterator<Double> nodesSItrVar = missesStandardNodesVar.ySeries()
		.iterator();

	// Hybrid
	Iterator<Double> nodesHItrVar = missesHybridNodesVar.ySeries()
		.iterator();

	// t-value
	Iterator<Double> tvalNod = tValueNod.iterator();
	Iterator<Double> tvalVid = tValueVid.iterator();
	// p-value
	Iterator<Double> pvalNod = pValueNod.iterator();
	Iterator<Double> pvalVid = pValueVid.iterator();

	// HTML output
	File out = new File("Output.html");
	PrintWriter writer;
	try {
	    writer = new PrintWriter(out);

	    writer.printf("<html><head><title>Live Stream Simulation Results</title></head><body><h1>Live Stream Simulation Results</h1><h2>Varying Number of Viewers</h2>");
	    writer.println("Parameters:<br/>Number of Viewers Lower Bound: "
		    + totalNodesMin + "<br/>Number of Viewers Upper Bound: "
		    + totalNodesMax + "<br/>Number of Viewers Step: "
		    + totalNodesStep);
	    writer.println("</br>Constant Video Length: 100<br/>");
	    writer.printf("<table border=1><tr><th rowspan=2>Number of Viewers</th><th colspan=2>Frame Misses</th><th colspan=2>Variance</th><th colspan=2>Unequal Variance t-Test</th></tr><tr><th>Push System</th><th>Hybrid Push-Pull</th><th>Push System</th><th>Hybrid Push-Pull</th><th>t-Value</th><th>p-Value</th></tr>");

	    while (nodesHItr.hasNext()) {

		double hyb = nodesHItr.next();
		double std = nodesSItr.next();
		double varH = nodesHItrVar.next();
		double varS = nodesSItrVar.next();
		double noOfNodes = nodesItr.next();
		double p = pvalVid.next();
		double t = tvalVid.next();
		if (p > threshold)
		    writer.printf(
			    "<tr><td>%.0f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td><font color='red'>%.3f</font></td></tr>",
			    noOfNodes, std, hyb, varS, varH, t, p);
		else
		    writer.printf(
			    "<tr><td>%.0f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td></tr>",
			    noOfNodes, std, hyb, varS, varH, t, p);

	    }
	    writer.println("</table>");
	    writer.println("<h2>Varying Length of Video</h2>");
	    writer.println("Parameters:<br/>Number of Frames Lower Bound: "
		    + videoLengthMin + "<br/>Number of Frames Upper Bound: "
		    + videoLengthMax + "<br/>Number of Frame Step: "
		    + videoLengthStep);
	    writer.println("<br/>Constant Number of Viewers: 50<br/>");
	    writer.printf("<table border=1><tr><th rowspan=2>Number of Frames</th><th colspan=2>Frame Misses</th><th colspan=2>Variance</th><th colspan=2>Unequal Variance t-Test</th></tr><tr><th>Push System</th><th>Hybrid Push-Pull</th><th>Push System</th><th>Hybrid Push-Pull</th><th>t-Value</th><th>p-Value</th></tr>");
	    // System.out.println("Video Length\tAverage Number of missed frames\n\t\tPush\tPush-Pull");
	    while (vidHItr.hasNext()) {
		double hyb = vidHItr.next();
		double std = vidSItr.next();
		double varH = vidHItrVar.next();
		double varS = vidSItrVar.next();
		double noOfFrames = vidItr.next();
		double p = pvalNod.next();
		double t = tvalNod.next();
		if (p > threshold)
		    writer.printf(
			    "<tr><td>%.0f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td><font color=red>%.3f</font></td></tr>",
			    noOfFrames, std, hyb, varS, varH, t, p);
		else
		    writer.printf(
			    "<tr><td>%.0f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td><td>%.3f</td></tr>",
			    noOfFrames, std, hyb, varS, varH, t, p);
	    }
	    writer.println("</table></body></html>");
	    writer.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

	// Plotting Video length graph
	new Plot()
		.rightMargin(36)

		.xAxisTitle("Video Length")
		.yAxisTitle("Number of Missed Frames")
		.seriesDots(null)
		.seriesStroke(Strokes.solid(2))
		.seriesColor(Color.RED)
		.xySeries(missesHybridVidLength)
		.seriesDots(null)
		.seriesStroke(Strokes.solid(2))
		.seriesColor(Color.BLUE)
		.xySeries(missesStandardVidLength)
		.frameTitle("Simulation for varying video length")
		.plotTitle("Varying length of video")
		.labelPosition(Plot.RIGHT)
		.labelOffset(6)
		.labelColor(Color.RED)
		.label("Push-Pull", missesHybridVidLength.maxX(),
			missesHybridVidLength.maxY())
		.labelPosition(Plot.RIGHT)
		.labelOffset(6)
		.labelColor(Color.BLUE)
		.label("Pull", missesStandardVidLength.maxX(),
			missesStandardVidLength.maxY())

		.getFrame().setVisible(true);

	// Plotting Number of nodes simulation
	new Plot()
		.rightMargin(36)
		.xAxisTitle("Number of Nodes")
		.yAxisTitle("Number of Missed Frames")
		.seriesDots(null)
		.seriesStroke(Strokes.solid(2))
		.seriesColor(Color.RED)
		.xySeries(missesHybridNodes)
		.seriesDots(null)
		.seriesStroke(Strokes.solid(2))
		.seriesColor(Color.BLUE)
		.frameTitle("Simulation for varying number of viewers")
		.plotTitle("Varying number of viwers")
		.xySeries(missesStandardNodes)
		.labelPosition(Plot.RIGHT)
		.labelOffset(6)
		.labelColor(Color.RED)
		.label("Push-Pull", missesHybridNodes.maxX(),
			missesHybridNodes.maxY())
		.labelPosition(Plot.RIGHT)
		.labelOffset(6)
		.labelColor(Color.BLUE)
		.label("Pull", missesStandardNodes.maxX(),
			missesStandardNodes.maxY()).getFrame().setVisible(true);

    }

}
