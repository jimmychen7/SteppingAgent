import java.util.Comparator;

public class StateComparator implements Comparator<State> {

	@Override
	public int compare(State arg0, State arg1) {
		if (arg0.getHeuristic() < arg1.getHeuristic()) {
			return -1;
		}
		if (arg1.getHeuristic() < arg0.getHeuristic()) {
			return -1;
		}
		return 0;
	}
	
}
