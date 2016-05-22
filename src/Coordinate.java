/**
 * 
 * @author Weilon Ying
 * Coordinate class is a data structure that stores a 2 dimensional coordinate
 */
public class Coordinate {
    //fields are not encapsulated as Coordinate class is meant to be a simple data structure
	public int x;
	public int y;
	
	/**
	 * Constructor for Coordinate class
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Coordinate (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Method that determines whether an instance of a Coordinate object
	 *  is equal to another specific instance of a Coordinate object
	 * @param k The Coordinate object to be compared against
	 * @return True if the coordinates share the same x and y coordinates, and False otherwise
	 */
	public boolean equals(Coordinate k) {
		return (k.x == this.x && k.y == this.y) ? true: false;
	}
	
	/**
	 * Converts the x and y coordinates to string
	 */
	public String toString() {
	    return ("(" + x + "," + y + ")");
	}
	
	/**
	 * Overridden clone method
	 * Creates a deep copy of a Coordinate object
	 * @return A deep copy of a Coordinate object
	 */
	@Override
	public Coordinate clone() {
	    Coordinate c = new Coordinate(this.x, this.y);
	    return c;
	}
}
