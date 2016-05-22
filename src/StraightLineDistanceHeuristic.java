/**
 * 
 * @author Weilon Ying
 * StraightLineDistanceHeuristic is an implementation of the Heuristic interface.
 * Heuristic is calculated by squaring the difference between the the x coordinates, and the y coordinates,
 * then adding them together. This is a version of the straight line formula
 */
public class StraightLineDistanceHeuristic implements Heuristic {

	@Override
	/**
	 * Calculates the heuristic using a version of the straight line formula
	 * Heuristic = (x1 - x2)^2 + (y1 - y2)^2.
	 */
	public int getHeuristic(Coordinate start, Coordinate dest) {
		int x = (start.x - dest.x) * (start.x - dest.x);
		int y = (start.y - dest.y) * (start.y - dest.y);
		
		return x + y;
	}

}
