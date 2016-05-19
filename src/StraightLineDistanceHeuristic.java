
public class StraightLineDistanceHeuristic implements Heuristic {

	@Override
	/**
	 * Heuristic = (x1 - x2)^2 + (y1 - y2)^2.
	 */
	public int getHeuristic(Coordinate start, Coordinate dest) {
		int x = (start.x - dest.x) * (start.x - dest.x);
		int y = (start.y - dest.y) * (start.y - dest.y);
		
		return x + y;
	}

}
