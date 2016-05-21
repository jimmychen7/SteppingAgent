import java.util.Comparator;

public class StateComparator implements Comparator<State> {

	@Override
	public int compare(State arg0, State arg1) {
		if (arg0.getTotalCost() < arg1.getTotalCost()) {
			return -1;
		}
		if (arg1.getTotalCost() < arg0.getTotalCost()) {
			return 1;
		}
		return 0;
	}
	
}
