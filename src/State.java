
public class State {
	private int heuristic;
	private int cost;
	private State previousState;
	private Coordinate location;
	
	public State(int cost, int heuristic, State previousState, Coordinate location) {
		this.setCost(cost);
		this.setHeuristic(heuristic);
		this.setPreviousState(previousState);
		this.setLocation(location);
	}
	
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

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getTotalCost() {
		return this.cost + this.heuristic;
	}
	
}
