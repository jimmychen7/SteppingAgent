import java.util.Comparator;
/**
 * 
 * @author Weilon Ying
 * StateComparator is an implementation of the Comparator interface with the State class.
 * Used to sort the State class in a PriorityQueue in AStarSearch class, based on heuristic
 */
public class StateComparator implements Comparator<State> {

	@Override
	public int compare(State arg0, State arg1) {
		if (arg0.getHeuristic() < arg1.getHeuristic()) {
			return -1;
		}
		if (arg1.getHeuristic() < arg0.getHeuristic()) {
			return 1;
		}
		return 0;
	}
	
}
