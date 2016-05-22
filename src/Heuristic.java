/**
 * 
 * @author Weilon Ying
 * Heuristic interface. Provides a common method where an implemented heuristic class is able to calculate
 * the heuristic given a specific starting and destination coordinate. Allows use for the Strategy design pattern.
 */
public interface Heuristic {
	/**
     * getHeuristic calculates the heuristic between two coordinates
     * @param start The starting coordinate
     * @param dest The destination coordinate
     * @return The heuristic value as an integer
     */
	public int getHeuristic (Coordinate start, Coordinate dest);
}
