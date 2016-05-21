import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStarSearch {
	Heuristic h = new ManhattanDistanceHeuristic();
	PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());
	
	public ArrayList<Coordinate> getPath (Coordinate start, Coordinate dest, AgentMap agentMap) {
	    System.out.print("Search start: ");
        start.print();

	    System.out.print("Search destination: ");
	    dest.print();
	    
	    if(!agentMap.getAgentElements()[dest.y][dest.x].isReachable()) {
	    	System.out.println("GOAL NOT REACHABLE");
	    }

		ArrayList<Coordinate> path = new ArrayList<Coordinate>();
		
		if (start.equals(dest)) {
			return path;
		}
		
		List<Coordinate> visitedNodes = new ArrayList<Coordinate>();
		
		//initialise first state
		int startingHeuristic = h.getHeuristic(start, dest);
		int startingCost = 0; //zero cost going from start to start;
		State initialState = new State(startingHeuristic, null, start);
		queue.add(initialState);
		State curState = null;
		
		while (!queue.isEmpty()) {
			curState = queue.poll();
			System.out.print("currState: ");
			curState.getLocation().print();
			
			visitedNodes.add (curState.getLocation());
			
			if (curState.getLocation().equals(dest)) {
				break;
			}
			
			List<State> futureStates = getFutureStates (curState, visitedNodes, dest);
			AgentElement[][] elements = agentMap.getAgentElements();
			for (State futureState : futureStates) {
				int x = futureState.getLocation().x;
				int y = futureState.getLocation().y;
				//System.out.printf("futureState x = %d, y = %d\n", x, y); //debug
				if (elements[y][x].isObstacle()) {
					//System.out.printf("futureState to visit x = %d, y = %d\n", x, y);
					if(elements[y][x].isDoor() && agentMap.getTools().isHasKey()) {
						queue.add(futureState);
					} else if (elements[y][x].isTree() && agentMap.getTools().isHasAxe()) {
						queue.add(futureState);
					}
						
				} else {
					queue.add(futureState);
				}
			}
			//System.out.println("\n\n");
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
