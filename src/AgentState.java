import java.util.LinkedList;

/**
 * 
 * @author Jimmy Chen, Weilon Ying
 * AgentState class
 * Tracks the agent's current state, including location, inventory (tools and items) and a 
 * map of all visited/seen nodes
 */
public class AgentState {
	
	/**
	 * Constructor for AgentMap. Initialises the map 
	 */
	public AgentState() {
		agentMap = new AgentElement[159][159];
		for(int i = 0; i < 159; i++) 
			for(int j = 0; j < 159; j++)
				agentMap[j][i] = new AgentElement("."); //. = unknown, haven't seen it yet

				currPosition = new Coordinate(79,79); //start Position is (79,79)
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
	
	/**
	 * Method that determines whether a certain coordinate is a valid map coordinate or not
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return True if coordinate is a valid map coordinate, and false otherwise
	 */
	boolean withinMap(int x, int y) {
		if(x >=0 && x < 159 && y >= 0 && y < 159) return true;
		return false;
	}
	
	/**
	 * Method that updates coordinates to be reachable as the agent moves into new areas
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	private void updateReachable(int x, int y) {
		//set currNode and neigbours as reachable = true.
		agentMap[y][x].setReachable(true);	
		if(withinMap(x-1,y)) agentMap[y][x-1].setReachable(true);	
		if(withinMap(x+1,y)) agentMap[y][x+1].setReachable(true);
		if(withinMap(x,y-1)) agentMap[y-1][x].setReachable(true);
		if(withinMap(x,y+1)) agentMap[y+1][x].setReachable(true);
	
	}
	
	/**
	 * Sets agent's current location to be "visited"
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	private void updateVisited(int x, int y) {
		//set currNode to visited = true.
		agentMap[y][x].setVisited(true);
	}
	
	/**
	 * Checks agent's current location and updates inventory if the location contains an item.
	 * The location is then updated to no longer contain that item.
	 */
	private void updateTools() {
		AgentElement currElement = agentMap[currPosition.y][currPosition.x];
		
		if(currElement.isAxe()) {
			tools.setHasAxe(true);
			currElement.setSymbol(" ");
		} else if (currElement.isKey()) {
			tools.setHasKey(true);
			currElement.setSymbol(" ");
		} else if (currElement.isStone()) {
			int numStones = tools.getNumSteppingStones();
			tools.setNumSteppingStones(numStones+1);
			currElement.setSymbol(" ");
		} else if (currElement.isWater()) {
			tools.setNumSteppingStones(tools.getNumSteppingStones()-1);
			currElement.setSymbol("O");
		} else if (currElement.isGold()) {
			tools.setHasGold(true);
			currElement.setSymbol(" ");
		} 		
	}
	
	/**
	 * Returns agent's current location
	 * @return agent's current location
	 */
	public Coordinate getCurrPosition() {
		return this.currPosition.clone();
	}
	
	 /**
	  * Returns agent's current inventory (tools)
	  * @return agent's current inventory (tools)
	  */
	ToolBox getTools() {
		return this.tools;
	}
	
	/**
	 * Method that determines the next location (node) to visit. This is determined by whether the agent
	 * has gold or not, proximity to items of interest, known obstacles and locations that have not been visited yet.
	 * @return The coordinate of the next location (node) to visit
	 */
	public Coordinate getNodeToVisit() {
	    if (getTools().isHasGold()) {
	        return new Coordinate(79, 79); //set goal to home if agent possesses gold
	    }
		//Gold
		Coordinate goldPos = null;
		Coordinate axePos = null;
		Coordinate keyPos = null;
		//LinkedList<Coordinate> stonePos = new LinkedList<Coordinate>();
		LinkedList<Coordinate> stonePos = new LinkedList<Coordinate>();
		LinkedList<Coordinate> treePos = new LinkedList<Coordinate>();
		LinkedList<Coordinate> doorPos = new LinkedList<Coordinate>();
		Heuristic h = new ManhattanDistanceHeuristic();
		
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
						stonePos.add(new Coordinate (x, y));
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
				Coordinate closestStonePos;
				int closestStoneDist;
				
				closestStonePos = stonePos.getFirst();
				closestStoneDist = h.getHeuristic(currPosition, closestStonePos);
				
				for (Coordinate compare : stonePos) {
					int compareStoneDist = h.getHeuristic(currPosition, compare);
					if (compareStoneDist < closestStoneDist) {
						closestStonePos = compare;
					}
				}
				
				return closestStonePos;
			}
			
			//If we get here, we're going to deal with reachable landmarks that are obstacles
			//We only set our goal to the obstacles if we have the tools to eliminate it
			
			if (tools.isHasAxe()) {
				Coordinate closestTreePos;
				int closestTreeDist;
				
				if(!treePos.isEmpty()) {
					closestTreePos = treePos.getFirst();
					closestTreeDist = h.getHeuristic(currPosition, closestTreePos);
					
					for (Coordinate compare : treePos) {
						int compareStoneDist = h.getHeuristic(currPosition, compare);
						if (compareStoneDist < closestTreeDist) {
							closestTreePos = compare;
						}
					}
					
					return closestTreePos;
				}				
			} 
			if(tools.isHasKey()) {
				Coordinate closestDoorPos;
				int closestDoorDist;
				
				if(!doorPos.isEmpty()) {
					closestDoorPos = doorPos.getFirst();
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
			
		}
		
		//Else visit unvisited and reachable nodes
		for(int y = 0; y < agentMap.length; y++) {
			for(int x = 0; x < agentMap[y].length; x++) {
				if(!agentMap[y][x].getIsVisited() && agentMap[y][x].isReachable() &&
				        !agentMap[y][x].isObstacle()) {
						return new Coordinate(x,y);
				}
			}
		}		
		
		//Else make decision where in the water to go to
				
		for(int y = 0; y < agentMap.length; y++) {
			for(int x = 0; x < agentMap[y].length; x++) {
				if(agentMap[y][x].isGold()) goldPos = new Coordinate(x,y);
				if(agentMap[y][x].isAxe()) axePos = new Coordinate(x,y);
				if(agentMap[y][x].isKey()) keyPos = new Coordinate(x,y);
				if(agentMap[y][x].isStone()) stonePos.add(new Coordinate(x,y));		
			}
		}
		
		
		int manDistToItems, minManDistToItems = 1000;
		Coordinate coordToPlaceStone = null;
		
		
		for(int y = 0; y < agentMap.length; y++) {
			for(int x = 0; x < agentMap[y].length; x++) {
				
				if(agentMap[y][x].isWater() && agentMap[y][x].isReachable()) {
					if(tools.getNumSteppingStones() == 1) {
						if(!agentMap[y-1][x].isObstacle() && !agentMap[y-1][x].getIsVisited()) {
							manDistToItems = calculateManDistToItems(new Coordinate(x,y), goldPos, axePos, keyPos,
									stonePos);
							
							if(manDistToItems <= minManDistToItems) {
								minManDistToItems = manDistToItems;
								coordToPlaceStone = (new Coordinate(x,y)); 
							}
			
						} else if(!agentMap[y+1][x].isObstacle() && !agentMap[y+1][x].getIsVisited()) {
							manDistToItems = calculateManDistToItems(new Coordinate(x,y), goldPos, axePos, keyPos,
									stonePos);
							
							if(manDistToItems <= minManDistToItems) {
								minManDistToItems = manDistToItems;
								coordToPlaceStone = (new Coordinate(x,y)); 
							}
						}  else if(!agentMap[y][x-1].isObstacle() && !agentMap[y][x-1].getIsVisited()) {
							manDistToItems = calculateManDistToItems(new Coordinate(x,y), goldPos, axePos, keyPos,
									stonePos);
							
							if(manDistToItems <= minManDistToItems) {
								minManDistToItems = manDistToItems;
								coordToPlaceStone = (new Coordinate(x,y)); 
							}
						}  else if(!agentMap[y][x+1].isObstacle() && !agentMap[y][x+1].getIsVisited()) {
							manDistToItems = calculateManDistToItems(new Coordinate(x,y), goldPos, axePos, keyPos,
									stonePos);
							
							if(manDistToItems <= minManDistToItems) {
								minManDistToItems = manDistToItems;
								coordToPlaceStone = (new Coordinate(x,y)); 
							}
						}
					} else {
						manDistToItems = calculateManDistToItems(new Coordinate(x,y), goldPos, axePos, keyPos,
								stonePos);
						
						if(manDistToItems < minManDistToItems) {
							minManDistToItems = manDistToItems;
							coordToPlaceStone = (new Coordinate(x,y)); 
						}			
					}
				}		
					
			}			
		}
		
		
		return coordToPlaceStone;
	}
	
	/**
	 * Calculates the manDist to all objects to be picked up for a specific Coordinate
	 * @param testPos
	 * @param goldPos
	 * @param axePos
	 * @param keyPos
	 * @param stonePos
	 * @return totalManDist to all objects
	 */
	public int calculateManDistToItems(Coordinate testPos, Coordinate goldPos, Coordinate axePos, Coordinate keyPos,
			LinkedList<Coordinate> stonePos) {
		int totalManDist = 0;
		Heuristic h = new ManhattanDistanceHeuristic();
		
		if(goldPos != null) totalManDist += h.getHeuristic(testPos, goldPos);
		if(axePos != null) totalManDist += h.getHeuristic(testPos, axePos);
		if(keyPos != null) totalManDist += h.getHeuristic(testPos, keyPos);
		
		for(Coordinate currStonePos: stonePos) {
			totalManDist += h.getHeuristic(testPos, currStonePos);
		}
		
		return totalManDist;
	}
	
	/**
	 * Updates the agent's map for future reference
	 * @param view The agent's current view which is then used to add to the map
	 */
	public void updateMap(char[][] view) {
		int xCurrPos = currPosition.x;
		int yCurrPos = currPosition.y;
		int i, j, k, l;
		
		switch( dirn ) {
        case NORTH: 
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
	
	/**
	 * Returns the agent's current facing direction
	 * @return the agent's current facing direction
	 */
	public int getDirection() {
		return this.dirn;
	}
	
	/**
	 * Updates the agent's current facing direction
	 * @param ch character represents the direction the agent faced (left/right)
	 */
	void updateDirection(char ch) {
		if(( ch == 'L' )||( ch == 'l' )) {
			dirn = ( dirn + 1 ) % 4;
	    }
	    else if(( ch == 'R' )||( ch == 'r' )) {
	    	dirn = ( dirn + 3 ) % 4;
	    }
	}	
	
	/**
	 * Method that determines whether the agent can go forward.
	 * @param num_stones_held The number of stepping stones the agent currently has
	 * @return True if the agent is able to move forward unhindered, and false otherwise
	 */
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
		if(agentMap[yPos][xPos].isWater() && num_stones_held > 0) return true;
		
		if(agentMap[yPos][xPos].isAxe() || agentMap[yPos][xPos].isGold() 
				|| agentMap[yPos][xPos].isKey() || agentMap[yPos][xPos].isStone()) return true;
		if(agentMap[yPos][xPos].isPlacedStone()) return true;
		return false;
	}
	
	/**
	 * Prints out the agent's map
	 */
	public void print() {
        String name;
        int i,j;
        
        for (i = 0; i < agentMap.length; i++) {
            name = "";
            boolean printLine = false;
            for(j = 0; j < agentMap[i].length; j++) {
                String add = "";   
            
                switch(agentMap[i][j].getSymbol()) {
                    case UNKNOWN:
                    case EMPTY:
                    case TREE:
                    case DOOR:
                    case WATER:
                    case WALL:
                    case AXE:
                    case KEY:
                    case STONE_OBJECT:
                    case STONE_PLACED:
                    case GOLD:
                        add = agentMap[i][j].getSymbol();
                        break;
                    default: 
                        System.out.print("?"); //If none of the possible symbols, print out "?". This should never happen and should be
                                                //debugged if it does
                        break;
                }
                
                if (!add.equals(UNKNOWN)) {
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
	
	/**
	 * Returns the agent's map as a matrix of AgentElements
	 * @return
	 */
	public AgentElement[][] getAgentElements() {
		return agentMap;
	}
	
	private AgentElement[][] agentMap; //The agent's map
	private Coordinate currPosition; //Agent's current position
	private ToolBox tools;  //Agent's current inventory (tools)
	private int dirn; //Agent's current facing direction
	
	//Cardinal directions
	final static int EAST   = 0;
    final static int NORTH  = 1;
    final static int WEST   = 2;
    final static int SOUTH  = 3;  
    
    //Possible map element symbols
    public final static String UNKNOWN      = AgentElement.UNKNOWN;
    public final static String EMPTY        = AgentElement.EMPTY;
    public final static String TREE         = AgentElement.TREE;
    public final static String DOOR         = AgentElement.DOOR;
    public final static String WATER        = AgentElement.WATER;
    public final static String WALL         = AgentElement.WALL;
    public final static String AXE          = AgentElement.AXE;
    public final static String KEY          = AgentElement.KEY;
    public final static String STONE_OBJECT = AgentElement.STONE_OBJECT;
    public final static String STONE_PLACED = AgentElement.STONE_PLACED;
    public final static String GOLD         = AgentElement.GOLD;  
}
