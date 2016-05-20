import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStarSearch {
	Heuristic h = new StraightLineDistanceHeuristic();
	PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());
	
	public ArrayList<Coordinate> getPath (Coordinate start, Coordinate dest, AgentMap agentMap) {
	    System.out.print("Search start: ");
        start.print();

	    System.out.print("Search destination: ");
	    dest.print();

		ArrayList<Coordinate> path = new ArrayList<Coordinate>();
		
		if (start.equals(dest)) {
			return path;
		}
		
		List<State> visitedNodes = new ArrayList<State>();
		
		//initialise first state
		int startingHeuristic = h.getHeuristic(start, dest);
		State initialState = new State(startingHeuristic, null, start);
		queue.add(initialState);
		State curState = null;
		
		while (!queue.isEmpty()) {
			curState = queue.poll();
			visitedNodes.add (curState);
			
			if (curState.getLocation().equals(dest)) {
				break;
			}
			
			List<State> futureStates = getFutureStates (curState, visitedNodes, dest);
			AgentElement[][] elements = agentMap.getAgentElements();
			for (State futureState : futureStates) {
				int x = futureState.getLocation().x;
				int y = futureState.getLocation().y;
				if (!elements[y][x].isObstacle()) {
					queue.add(futureState);
				}
			}
		}
		System.out.print("curState = ");
		curState.getLocation().print();
		Stack<Coordinate> s = new Stack<Coordinate>();
		//create path from state
		while (curState != null) {
			s.push(curState.getLocation());
			curState = curState.getPreviousState();
		}
		while (!s.isEmpty()) {
		    path.add(s.pop());
		}
		System.out.println("Path to goal: ");
		for (Coordinate c : path) {
			System.out.print ("(" + c.x + ", " + c.y + "); ");
		}
		return path;
	}

	private List<State> getFutureStates(State curState, List<State> visitedNodes, Coordinate dest) {
		List<State> futureStates = new LinkedList<State>();
		//Get coordinates
		Coordinate curLocation = curState.getLocation();
		
		Coordinate northCoord = curLocation.clone();
		northCoord.y--;
		Coordinate southCoord = curLocation.clone();
		southCoord.y++;
		Coordinate eastCoord = curLocation.clone();
		eastCoord.x++;
		Coordinate westCoord = curLocation.clone();
		westCoord.x--;
		
		State northState = new State (h.getHeuristic(northCoord, dest), 
				curState, dest);
		State southState = new State (h.getHeuristic(southCoord, dest), 
				curState, dest);
		State eastState = new State (h.getHeuristic(eastCoord, dest), 
				curState, dest);
		State westState = new State (h.getHeuristic(westCoord, dest), 
				curState, dest);
		
		for(State visitedNode : visitedNodes) {
			if (!northState.equals(visitedNode)) {
				futureStates.add(northState);
			}
			if (!eastState.equals(visitedNode)) {
				futureStates.add(eastState);
			}
			if (!southState.equals(visitedNode)) {
				futureStates.add(southState);
			}
			if (!westState.equals(visitedNode)) {
				futureStates.add(westState);
			}
			
		}
		return futureStates;
	}
}
