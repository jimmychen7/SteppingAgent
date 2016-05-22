/**
 * 
 * @author Weilon Ying
 * ManhattanDistanceHeuristic is an implementation of the Heuristic interface.
 * Heuristic is calculated by adding the absolute value of the difference in the x coordinates and
 * the absolute value of the difference in the y coordinates
 */
public class ManhattanDistanceHeuristic implements Heuristic {

	@Override
	/**
	 * Calculates the heuristic using the Manhattan Distance formula.
	 * Heuristic = abs(x1 - x2) + abs(y1 - y2)
	 */
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
		if (number < 0) { //if negative number, make it positive
			return -1 * number;
		}
		return number;
	}
}
