import java.util.LinkedList;

public class AgentMap {
	
	public AgentMap() {
		agentMap = new AgentElement[159][159];
		for(int i = 0; i < 159; i++) 
			for(int j = 0; j < 159; j++)
				agentMap[j][i] = new AgentElement("."); //. = unknown, haven't seen it yet

				currPosition = new Coordinate(79,79); //start Position is (79.79)
				dirn = NORTH;
				tools = new ToolBox();
	}
	
	/**
	 * Agent has chosen to move forward. Update the current position if he can go forward and check if 
	 * he has picked up an item. 
	 * @param ch
	 */
	public void update(char ch) {		
		int xCurrPos = currPosition.x;
		int yCurrPos = currPosition.y;
		
		updateReachable(xCurrPos, yCurrPos);
		updateVisited(xCurrPos, yCurrPos);
		
		if(ch == 'F' || ch == 'f' && canGoForward(tools.getNumSteppingStones())) {
			switch ( dirn ) {
    			case NORTH: yCurrPos--; break;
    			case SOUTH: yCurrPos++; break;
    			case EAST: xCurrPos++; break;
    			case WEST: xCurrPos--; break;
			}
		}

		this.currPosition = new Coordinate(xCurrPos, yCurrPos);
		updateReachable(xCurrPos, yCurrPos);
		updateVisited(xCurrPos, yCurrPos);
		updateTools();
	}
	
	/**
	 * Update with no parameter called at the start of the program when Agent is first placed on the map
	 */
	public void update() {
	    int xCurrPos = currPosition.x;
        int yCurrPos = currPosition.y;
        
        updateReachable(xCurrPos, yCurrPos);
        updateVisited(xCurrPos, yCurrPos);
	}
	
	boolean withinMap(int x, int y) {
		if(x >=0 && x < 160 && y >= 0 && y < 160) return true;
		return false;
	}
	
	private void updateReachable(int x, int y) {
		//set currNode and neigbours as reachable = true.
		agentMap[y][x].setReachable(true);
		if(withinMap(y-1, x)) agentMap[y-1][x].setReachable(true);
		if(withinMap(y+1, x)) agentMap[y+1][x].setReachable(true);
		if(withinMap(y, x+1)) agentMap[y][x-1].setReachable(true);
		if(withinMap(y, x-1)) agentMap[y][x+1].setReachable(true);
	}
	private void updateVisited(int x, int y) {
		//set currNode to visited = true.
		agentMap[y][x].setVisited(true);
	}
	
	private void updateTools() {
		AgentElement currElement = agentMap[currPosition.y][currPosition.x];
		
		if(currElement.isAxe()) {
			tools.setHasAxe(true);
		} else if (currElement.isKey()) {
			tools.setHasKey(true);
		} else if (currElement.isStone()) {
			tools.setNumSteppingStones(tools.getNumSteppingStones()+1);
		} else if (currElement.isGold()) {
			System.out.println("GOT GOLD#############");
			tools.setHasGold(true);
		}		
	}
	
	public Coordinate getCurrPosition() {
		return this.currPosition.clone();
	}
	
	ToolBox getTools() {
		return this.tools;
	}
	
	public Coordinate getNodeToVisit() {
	    if (getTools().isHasGold()) {
	        return new Coordinate(79, 79); //set goal to home if agent possesses gold
	    }
		//Gold
		Coordinate goldPos = null;
		Coordinate axePos = null;
		Coordinate keyPos = null;
		//LinkedList<Coordinate> stonePos = new LinkedList<Coordinate>();
		Coordinate stonePos = null;
		LinkedList<Coordinate> treePos = new LinkedList<Coordinate>();
		LinkedList<Coordinate> doorPos = new LinkedList<Coordinate>();
		
		Landmark goal = null;
		boolean reachableLandmark = false;
		
		//If item of interest is reachable go to it
		for(int y = 0; y < agentMap.length; y++) {
			for(int x = 0; x < agentMap[y].length; x++) {
				if(agentMap[y][x].isReachable()) {
					if (agentMap[y][x].isGold()) {
						goldPos = new Coordinate(x, y);
						reachableLandmark = true;
						goal = Landmark.GOLD;
					} else if (agentMap[y][x].isAxe()) {
						reachableLandmark = true;
						axePos = new Coordinate (x, y);
						goal = Landmark.AXE;
					} else if (agentMap[y][x].isKey()) {
						reachableLandmark = true;
						keyPos = new Coordinate (x, y);
						goal = Landmark.KEY;
					} else if (agentMap[y][x].isStone()) {
						reachableLandmark = true;
						//stonePos.add(new Coordinate(x, y));
						stonePos = new Coordinate (x, y);
						goal = Landmark.STONE;
					} else if (agentMap[y][x].isTree()) {
						reachableLandmark = true;
						treePos.add(new Coordinate(x, y));
					} else if (agentMap[y][x].isDoor()) {
						reachableLandmark = true;
						doorPos.add(new Coordinate(x, y));
					}
				}
			}
		}
		
		if (reachableLandmark) {
			if (goal == Landmark.GOLD) {
				return goldPos;
			}
			if (goal == Landmark.AXE) {
				return axePos;
			}
			if (goal == Landmark.KEY) {
				return keyPos;
			}
			if (goal == Landmark.STONE) {
				//Get closest stone and return it
				/*Coordinate closestStonePos;
				int closestStoneDist;
				
				closestStonePos = stonePos.getFirst();
				Heuristic h = new ManhattanDistanceHeuristic();
				closestStoneDist = h.getHeuristic(currPosition, closestStonePos);
				
				for (Coordinate compare : stonePos) {
					int compareStoneDist = h.getHeuristic(currPosition, compare);
					if (compareStoneDist < closestStoneDist) {
						closestStonePos = compare;
					}
				}
				
				return closestStonePos;*/
				return stonePos;
			}
			
			//If we get here, we're going to deal with reachable landmarks that are obstacles
			//We only set our goal to the obstacles if we have the tools to eliminate it
			
			if (tools.isHasAxe()) {
				Coordinate closestTreePos;
				int closestTreeDist;
				
				closestTreePos = treePos.getFirst();
				Heuristic h = new ManhattanDistanceHeuristic();
				closestTreeDist = h.getHeuristic(currPosition, closestTreePos);
				
				for (Coordinate compare : treePos) {
					int compareStoneDist = h.getHeuristic(currPosition, compare);
					if (compareStoneDist < closestTreeDist) {
						closestTreePos = compare;
					}
				}
				
				return closestTreePos;
			} 
			if(tools.isHasKey()) {
				Coordinate closestDoorPos;
				int closestDoorDist;
				
				closestDoorPos = doorPos.getFirst();
				Heuristic h = new ManhattanDistanceHeuristic();
				closestDoorDist = h.getHeuristic(currPosition, closestDoorPos);
				
				for (Coordinate compare : treePos) {
					int compareStoneDist = h.getHeuristic(currPosition, compare);
					if (compareStoneDist < closestDoorDist) {
						closestDoorPos = compare;
					}
				}
				
				return closestDoorPos;
			}
			
		}
		
		//Else visit unvisited and reachable nodes
		for(int y = 0; y < agentMap.length; y++) {
			for(int x = 0; x < agentMap[y].length; x++) {
			    /*
			    if (agentMap[y][x].getIsVisited() || agentMap[y][x].isReachable()) {
			        System.out.println("visited: " + agentMap[y][x].getIsVisited() + " reachable: " +
	                        agentMap[y][x].isReachable());
			        System.out.println("x: " + x + " y: " + y);
			    }
			    */ //DEBUG
				if(!agentMap[y][x].getIsVisited() && agentMap[y][x].isReachable() &&
				        !agentMap[y][x].isObstacle()) {
				    //System.out.println("Node to visit = x: " + x + " y: " + y);
					return new Coordinate(x,y);
				}
			}
		}		
		
		//Else make decision 
		System.out.println("NULL DECISION!");
		System.exit(-1);
		
		return null;
	}
	
	public void updateMap(char[][] view) {
		int xCurrPos = currPosition.x;
		int yCurrPos = currPosition.y;
		int i, j, k, l;
		
		switch( dirn ) {
        case NORTH: 
            System.out.println("NORTH");
            System.out.println("currPos: (" + xCurrPos + "," + yCurrPos + ")");
            k = 0;
            l = 0;
        	for(i = yCurrPos - 2; i < yCurrPos + 3; i++) {
        		for(j = xCurrPos - 2; j < xCurrPos + 3; j++) {
        			if (!(j == xCurrPos && i == yCurrPos)) {
        				agentMap[i][j].updateAgentElement(view[k][l]);
        			}
        			if(l < 4) l++;     			 
        		}
        		l = 0;
        		if(k < 4) k++;
        	}
        	break;       
        case SOUTH: 
        	System.out.println("SOUTH");
        	k = 4;
        	l = 4;
        	for(i = yCurrPos - 2; i < yCurrPos + 3; i++) {
        		for(j = xCurrPos - 2; j < xCurrPos + 3; j++) {
        			if (!(j == xCurrPos && i == yCurrPos)) {
        				agentMap[i][j].updateAgentElement(view[k][l]);
        			}
        			if(l > 0) l--;     			 
        		}
        		l = 4;
        		if(k > 0) k--;
        	}
        	break;
        case EAST:  
        	System.out.println("EAST");
        	k = 4;
        	l = 0;
        	for(i = yCurrPos - 2; i < yCurrPos + 3; i++) {
        		for(j = xCurrPos - 2; j < xCurrPos + 3; j++) {     			
        			if (!(j == xCurrPos && i == yCurrPos)) {
        				agentMap[i][j].updateAgentElement(view[k][l]);
        			}
        			if(k > 0) k--;     			 
        		}
        		k = 4;
        		if(l < 4) l++;
        	}
        	break;
        case WEST:  
        	System.out.println("WEST");
        	k = 0;
        	l = 4;
        	for(i = yCurrPos - 2; i < yCurrPos + 3; i++) {
        		for(j = xCurrPos - 2; j < xCurrPos + 3; j++) {     			
        			if (!(j == xCurrPos && i == yCurrPos)) {
        				agentMap[i][j].updateAgentElement(view[k][l]);
        			}
        			if(k < 4) k++;     			 
        		}
        		k = 0;
        		if(l > 0) l--;
        	}
        	break;
        }    
	}
	
	public int getDirection() {
		return this.dirn;
	}
	
	void updateDirection(char ch) {
		if(( ch == 'L' )||( ch == 'l' )) {
			dirn = ( dirn + 1 ) % 4;
	    }
	    else if(( ch == 'R' )||( ch == 'r' )) {
	    	dirn = ( dirn + 3 ) % 4;
	    }
	}	
	
	public boolean canGoForward(int num_stones_held) {
		
		int xPos = currPosition.x;
		int yPos = currPosition.y;

		switch ( dirn ) {
		case NORTH: yPos--; break;
		case SOUTH: yPos++; break;
		case EAST: xPos++; break;
		case WEST: xPos--; break;
		}
		
		if(agentMap[yPos][xPos].isPath()) return true;
		if(agentMap[yPos][xPos].isWater() /*&& num_stones_held > 0*/) return false; //TO DO
		if(agentMap[yPos][xPos].isAxe() || agentMap[yPos][xPos].isGold() || agentMap[yPos][xPos].isKey()) return true;
		if(agentMap[yPos][xPos].isPlacedStone()) return true;
		return false;
	}
	
	
	public void print() {
        String name;
        int i,j;
        
        for (i = 0; i < agentMap.length; i++) {
            name = "";
            boolean printLine = false;
            for(j = 0; j < agentMap[i].length; j++) {
                String add = "";
                
            
                switch(agentMap[i][j].getSymbol()) {
                    case ".":
                    case " ":
                    case "T":
                    case "-":
                    case "~":
                    case "*":
                    case "a":
                    case "k":
                    case "o":
                    case "O":
                    case "g":
                        add = agentMap[i][j].getSymbol();
                        break;
                    default: 
                        System.out.print("?");
                        break;
                }
                
                if (!add.equals(".")) {
                    printLine = true; //only print this line if there is anything except unknown char
                }
                if (currPosition.x == j && currPosition.y == i) {
                    add = "%"; //if this position is player position, add player marker
                }
                name = name + add;
                    
            }
            if (printLine) {
                System.out.println(name);   
            }
        }
            
    }
	public AgentElement[][] getAgentElements() {
		return agentMap;
	}
	
	
    	
	private AgentElement[][] agentMap;
	private Coordinate currPosition;
	private ToolBox tools; 
	private int dirn;
	final static int EAST   = 0;
    final static int NORTH  = 1;
    final static int WEST   = 2;
    final static int SOUTH  = 3;  
    
    
}
