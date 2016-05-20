

public class AgentMap {
	
	public AgentMap() {
		agentMap = new AgentElement[159][159];
		for(int i = 0; i < 159; i++) 
			for(int j = 0; j < 159; j++)
				agentMap[j][i] = new AgentElement("u"); // u = unknown, haven't seen it yet

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
		
		if(ch == 'F' || ch == 'f' && canGoForward(tools.getNumSteppingStones())) {
			switch ( dirn ) {
			case NORTH: yCurrPos--; break;
			case SOUTH: yCurrPos++; break;
			case EAST: xCurrPos++; break;
			case WEST: xCurrPos--; break;
			}
		}

		this.currPosition = new Coordinate(xCurrPos, yCurrPos);
		updateTools();
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
			tools.setHasGold(true);
		}
		
		
	}
	
	public Coordinate getCurrPosition() {
		return this.currPosition;
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
		if(agentMap[yPos][xPos].isWater() /*&& num_stones_held > 0*/) return true;
		if(agentMap[yPos][xPos].isAxe() || agentMap[yPos][xPos].isGold() || agentMap[yPos][xPos].isKey()) return true;
		if(agentMap[yPos][xPos].isPlacedStone()) return true;
		return false;
	}
	
	
	public void print() {
		String name;
		int i,j;
		
		for (i = 0; i < agentMap.length; i++) {
			for(j = 0; j < agentMap[i].length; j++) {
				
				name = agentMap[i][j].getSymbol();
				
				switch(name) {
                case "u": System.out.print("u"); break;
                case " ": System.out.print(" "); break;
                case "T": System.out.print("T"); break;
                case "-": System.out.print("-"); break;
                case "~": System.out.print("~"); break;
                case "*": System.out.print("*"); break;
                case "a": System.out.print("a"); break;
                case "k": System.out.print("k"); break;
                case "o": System.out.print("o"); break;
                case "O": System.out.print("O"); break;
                case "g": System.out.print("g"); break;
                default: System.out.print(".");
               }
               
			}
			System.out.println();	
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
