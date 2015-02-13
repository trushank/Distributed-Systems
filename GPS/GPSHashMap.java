import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * GPSHashMap.java
 * @author Trushank
 * Date Mar 28, 2013
 * Version 1.0
 *
 * 
 */
/**
 * GPSHashMap Provides a data structure and methods to store information about
 * neighbouring offices
 * 
 * @author Trushank
 * 
 */
public class GPSHashMap extends HashMap<GPSOfficeRef, Coordinates> {

    private static final long serialVersionUID = 1L;
    private Coordinates myLocation;

    /**
     * GPSHashMap
     * 
     * @param myLocation
     *            : Location of Office holding this object
     */
    public GPSHashMap(Coordinates myLocation) {
	super();
	this.myLocation = myLocation;
    }

    /**
     * 
     * findNeighbours Called to find closest 3 neighbours
     * 
     * @return GPSHashMap of closest 3 neighbours
     */
    public GPSHashMap findNeighbours() {
	Iterator<GPSOfficeRef> itr = this.keySet().iterator();
	GPSHashMap ret = new GPSHashMap(myLocation);
	// If less than three nodes in the system, return all three
	if (this.size() <= 3) {

	    for (int i = 0; i < size(); i++) {
		GPSOfficeRef n1 = itr.next();
		ret.put(n1, this.get(n1));
	    }
	    return ret;
	}
	// Pick first three
	GPSOfficeRef n1 = itr.next();
	GPSOfficeRef n2 = itr.next();
	GPSOfficeRef n3 = itr.next();
	GPSOfficeRef check;
	while (itr.hasNext()) {
	    check = itr.next();
	    // if check is closer than any of the current neighbours, switch
	    if (this.get(n1).getDistance(myLocation) > this.get(check)
		    .getDistance(myLocation)) {
		n1 = check;
	    } else if (this.get(n2).getDistance(myLocation) > this.get(check)
		    .getDistance(myLocation)) {
		n2 = check;
	    } else if (this.get(n3).getDistance(myLocation) > this.get(check)
		    .getDistance(myLocation)) {
		n3 = check;
	    }

	}
	// store 3 neighbours in a GPSHashMap and return
	ret.put(n1, this.get(n1));
	ret.put(n2, this.get(n2));
	ret.put(n3, this.get(n3));
	return ret;
    }

    /**
     * 
     * checkNewNeighbour Checks if newly added node is closer as compared to old
     * neighbours
     * 
     * @param office
     *            : new node ref
     * @param distance
     *            : new node distance
     */
    public boolean checkNewNeighbour(GPSOfficeRef office, Coordinates cord) {
	if (this.size() > 3)
	    return false;
	// if less than 3 closest, simply add
	if (this.size() < 3) {
	    this.put(office, cord);
	    return true;
	}

	Iterator<GPSOfficeRef> itr = this.keySet().iterator();
	GPSOfficeRef highestDist = itr.next();
	GPSOfficeRef check;

	// find the largest of the 3 current neighbours
	while (itr.hasNext()) {
	    check = itr.next();
	    if (this.get(highestDist).getDistance(myLocation) < this.get(check)
		    .getDistance(myLocation)) {
		highestDist = check;
	    }
	}
	// if largest neighbour > new node, replace
	if (this.get(highestDist).getDistance(myLocation) > cord
		.getDistance(myLocation)) {
	    this.remove(highestDist);
	    this.put(office, cord);
	}
	return true;
    }

    /**
     * 
     * findClosest Finds closest office to the given co-ordinates
     * 
     * @param x
     *            : X cordinate
     * @param y
     *            Y coordinate
     * @return Nearest GPSOffice to the X,Y Coordinate
     */
    public GPSOfficeRef findClosest(double x, double y) {
	Iterator<GPSOfficeRef> itr = this.keySet().iterator();
	// Check if empty
	if (this.size() == 0)
	    return null;
	// if just one element return it
	else if (this.size() == 1)
	    return itr.next();

	// Assume first to be closest
	GPSOfficeRef least = itr.next();
	double dist;
	dist = this.get(least).getDistance(x, y);
	while (itr.hasNext()) {
	    GPSOfficeRef temp = itr.next();
	    double tempDist = this.get(temp).getDistance(x, y);
	    // replace if next neighbour has less distance
	    if (dist > tempDist) {
		dist = tempDist;
		least = temp;
	    }
	}
	// Return closest neighbour
	return least;

    }

    /**
     * 
     * findClosest Returns closest node in the GPSHashMap from the specified
     * co-ordinates
     * 
     * @param cord
     * @return remote reference to closest node
     */
    public GPSOfficeRef findClosest(Coordinates cord) {
	return findClosest(cord.getX(), cord.getY());
    }

    /**
     * 
     * findClosest Returns closest node in the GPSHashMap considering self as
     * well from the specified co-ordinates.
     * 
     * @param cord
     * @return remote reference to closest node
     */
    public GPSOfficeRef findClosest(Coordinates cord, GPSOfficeRef self,
	    Coordinates selfCord) {
	// Temporarily add self to the GPSHashMap
	this.put(self, selfCord);
	// Find closest to given co-ordinates
	GPSOfficeRef closest = findClosest(cord);
	if (selfCord.getDistance(cord) == get(closest).getDistance(cord)) {
	    this.remove(self);
	    return self;
	}
	this.remove(self);
	return closest;
    }

}