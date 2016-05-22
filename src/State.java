/**
 * 
 * @author Weilon Ying
 * State class is used solely in AStarSearch class to store a certain state of a pathfinding search
 */
public class State {
	private int heuristic;
	private State previousState; //The previous state that led to this state
	private Coordinate location; //The location of the path search at this state
	
	/**
	 * Constructor for the State class
	 * @param heuristic Search heuristic value. Used for A* search and comparing when added to a PriorityQueue
	 * @param previousState The previous state that led to this state
	 * @param location The location of the path search at this state
	 */
	public State(int heuristic, State previousState, Coordinate location) {
		this.setHeuristic(heuristic);
		this.setPreviousState(previousState);
		this.setLocation(location);
	}
	
	/**
	 * Equals method determines whether a state is equal to another state.
	 * @param compare The state to compare with
	 * @return True if the states share the same location and False otherwise
	 */
	public boolean equals(State compare) {
		if (this.location.equals(compare.getLocation())) {
			return true;
		}
		return false;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}

	public State getPreviousState() {
		return previousState;
	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}
}
