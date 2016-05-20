import java.util.ArrayList;

public class CoordinateSequence {
	private Coordinate goal;
	private ArrayList<Coordinate> path;
	
	public CoordinateSequence (Coordinate goal, ArrayList<Coordinate> path) {
		this.setGoal(goal);
		this.setPath(path);
	}

	public Coordinate getGoal() {
		return goal;
	}

	public void setGoal(Coordinate goal) {
		this.goal = goal;
	}

	public ArrayList<Coordinate> getPath() {
		return path;
	}

	public void setPath(ArrayList<Coordinate> path) {
		this.path = path;
	}
	
	
}
