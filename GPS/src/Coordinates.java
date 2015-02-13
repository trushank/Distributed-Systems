import java.io.Serializable;

/**
 * 
 * Coordinates.java
 * @author Trushank
 * Date Mar 28, 2013
 * Version 1.0
 *
 */
/**
 * @author Trushank
 * 
 */
public class Coordinates implements Serializable {

    private static final long serialVersionUID = 1L;
    private double X;
    private double Y;

    /**
     * Coordinates
     * 
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     */
    public Coordinates(double x, double y) {
	X = x;
	Y = y;
    }

    /**
     * 
     * getX Retrieves X value
     * 
     * @return X value
     */
    public double getX() {
	return X;
    }

    /**
     * 
     * getY Retrieves Y value
     * 
     * @return Y value
     */
    public double getY() {
	return Y;
    }

    /**
     * 
     * getDistance Gets Eucladian distance from self to given coordinates
     * 
     * @param x
     *            X desination
     * @param y
     *            Y destination
     * @return Distance
     */
    public double getDistance(double x, double y) {
	try {
	    double dist = Math.sqrt(Math.pow(X - x, 2) + Math.pow(Y - y, 2));
	    return dist;
	} catch (Exception e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    /**
     * 
     * getDistance Gets Eucladian distance from self to given coordinates
     * 
     * @param cord
     *            Location of destination
     * @return Distacne
     */
    public double getDistance(Coordinates cord) {
	return getDistance(cord.getX(), cord.getY());
    }

}
