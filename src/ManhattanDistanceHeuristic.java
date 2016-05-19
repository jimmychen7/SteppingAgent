
public class ManhattanDistanceHeuristic implements Heuristic {

	@Override
	public int getHeuristic(Coordinate start, Coordinate dest) {
		int x = abs(start.x - dest.x);
		int y = abs(start.y - dest.y);
		
		return x + y;
	}
	
	/**
	 * Absolute value function
	 * @param number The number to operate on
	 * @return The absolute value of the number.
	 */
	private int abs(int number) {
		if (number < 0) {
			return -1 * number;
		}
		return number;
	}
}
