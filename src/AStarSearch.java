import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * 
 * @author Jimmy Chen, Weilon Ying
 * A* Search class. Used to find a path from one location to another
 */
public class AStarSearch {
    //The search will used the Manhattan Distance Heuristic, which is our best admissible heuristic for this game
	Heuristic h = new ManhattanDistanceHeuristic();
	
	//queue will contain a priority queue of possible future steps to take in the path. The ones with the
	//lowest heuristic will be placed first
	PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());	
	
	/**
	 * getPath method searches for the best path from a specific starting and destination coordinate
	 * @param start The agent's starting location
	 * @param dest The agent's destination location
	 * @param agentState The agent's current state
	 * @return An array list of coordinates that, when followed in sequential order will define a path
	 *     from the starting location to the destination location
	 */
	public ArrayList<Coordinate> getPath (Coordinate start, Coordinate dest, AgentState agentMap) {

		ArrayList<Coordinate> path = new ArrayList<Coordinate>();
		
		if (start.equals(dest)) {
			return path;
		}
		
		List<Coordinate> visitedNodes = new ArrayList<Coordinate>();
		
		//initialise first state
		int startingHeuristic = h.getHeuristic(start, dest);
		State initialState = new State(startingHeuristic, null, start);
		visitedNodes.add(initialState.getLocation().clone());
		queue.add(initialState);
		State curState = null;
		
		while (true) {
			curState = queue.poll();			
			//visitedNodes.add (curState.getLocation());
			
			if (curState.getLocation().equals(dest)) {
				break;
			}
			
			List<State> futureStates = getFutureStates (curState, visitedNodes, dest);
			AgentElement[][] elements = agentMap.getAgentElements();
			for (State futureState : futureStates) {
				int x = futureState.getLocation().x;
				int y = futureState.getLocation().y;
				if (elements[y][x].isObstacle()) {
					if(elements[y][x].isDoor() && agentMap.getTools().isHasKey()) {
						queue.add(futureState);
						visitedNodes.add (futureState.getLocation());
					} else if (elements[y][x].isTree() && agentMap.getTools().isHasAxe()) {
						queue.add(futureState);
						visitedNodes.add (futureState.getLocation());
					}  else if (elements[y][x].isWater() && dest.equals(new Coordinate(x,y))) {
						queue.add(futureState);
						visitedNodes.add (futureState.getLocation());
					}
						
				} else if (!elements[y][x].isUnknown()){
					queue.add(futureState);
					visitedNodes.add (futureState.getLocation());
				}
			}
		}
		
		Stack<Coordinate> s = new Stack<Coordinate>();
		
		//create path from state
		while (curState != null) {
			s.push(curState.getLocation());
			curState = curState.getPreviousState();
		}
		while (!s.isEmpty()) {
		    path.add(s.pop());
		}
		return path;
	}
	
	/**
	 * getFutureStates is a helper method for getPath() that gets possible future states (steps in the path) that the agent
	 * could take.
	 * @param curState The current state in the getPath() search being considered
	 * @param visitedNodes A list of nodes "visited" in the getPath() search so far
	 * @param dest The destination location of the search
	 * @return A list of states (possible future steps) that the search could take
	 */
	private List<State> getFutureStates(State curState, List<Coordinate> visitedNodes, Coordinate dest) {
		List<State> futureStates = new LinkedList<State>();
		//Get coordinates
		Coordinate curLocation = curState.getLocation();
		
		Coordinate northCoord = new Coordinate(curLocation.x,curLocation.y-1);
		Coordinate southCoord = new Coordinate(curLocation.x,curLocation.y+1);
		Coordinate eastCoord = new Coordinate(curLocation.x+1,curLocation.y);
		Coordinate westCoord = new Coordinate(curLocation.x-1,curLocation.y);
		
		State northState = new State (h.getHeuristic(northCoord, dest), 
				curState, northCoord);
		State southState = new State (h.getHeuristic(southCoord, dest), 
				curState, southCoord);
		State eastState = new State (h.getHeuristic(eastCoord, dest), 
				curState, eastCoord);
		State westState = new State (h.getHeuristic(westCoord, dest), 
				curState, westCoord);
		
		boolean northVisited = false;
		boolean southVisited = false;
		boolean eastVisited = false;
		boolean westVisited = false;
		
		//checking if north was visited
		for(Coordinate visitedNode : visitedNodes) {
			if (northState.getLocation().equals(visitedNode))
				northVisited = true;
			
			if (southState.getLocation().equals(visitedNode))
				southVisited = true;
			
			if (eastState.getLocation().equals(visitedNode))
				eastVisited = true;
			
			if (westState.getLocation().equals(visitedNode))
				westVisited = true;
		}
		
		if(!northVisited) futureStates.add(northState);
		if(!southVisited) futureStates.add(southState);
		if(!eastVisited) futureStates.add(eastState);	
		if(!westVisited) futureStates.add(westState);	
		
		return futureStates;
	}
}
