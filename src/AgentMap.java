import java.util.ArrayList;
import java.util.LinkedList;

public class AgentMap {
    
    
    public AgentMap() {
        agentMap = new AgentElement[MAPSIZE][MAPSIZE];
        for(int i = 0; i < agentMap.length; i++) 
            for(int j = 0; j < agentMap[i].length; j++)
                agentMap[j][i] = new AgentElement("."); // . = unknown, haven't seen it yet

                currPosition = new Coordinate(79,79); //start Position is (79,79)
                dirn = NORTH;
    }
    
    public void updateCurrPosition(char ch) {       
        int xCurrPos = currPosition.x;
        int yCurrPos = currPosition.y;
        
        if(ch == 'F' || ch == 'f') {
            switch ( dirn ) {
            case NORTH: yCurrPos--; break;
            case SOUTH: yCurrPos++; break;
            case EAST: xCurrPos++; break;
            case WEST: xCurrPos--; break;
            }
        }

        this.currPosition = new Coordinate(xCurrPos, yCurrPos);
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
    
    /**
     * Gets the coordinate of the closet unvisited and unobstructed node that it can get to.
     * Uses A* to determine if it is reachable (Not reachable if A* expands more than N nodes, where N is proportionate to the observable map size)
     * @return 
     */
    public Coordinate toVisit() {
        int level = 1;
        Coordinate topLeft, topRight, botLeft, botRight;
        Coordinate currCoord;
        
        while(level < 10) {     
        topLeft = new Coordinate(currPosition.x - level, currPosition.y - level);
        topRight = new Coordinate(currPosition.x + level, currPosition.y - level);
        botLeft = new Coordinate(currPosition.x - level, currPosition.y + level);
        botRight = new Coordinate(currPosition.x + level, currPosition.y + level);
        
        currCoord = new Coordinate(topLeft.x, topLeft.y);
        while(currCoord != topRight) {
            if(agentMap[currCoord.y][currCoord.x].isObstacle() == false && !agentMap[currCoord.y][currCoord.x].getIsVisited()) {
                return currCoord;
            }
            currCoord.x++;
        }
        
        currCoord = new Coordinate(topRight.x, topRight.y);
        while(currCoord != botRight) {
            if(agentMap[currCoord.y][currCoord.x].isObstacle() == false && !agentMap[currCoord.y][currCoord.x].getIsVisited()) {
                return currCoord;
            }
            currCoord.y++;
        }
        
        currCoord = new Coordinate(botRight.x, botRight.y);
        while(currCoord != botLeft) {
            if(agentMap[currCoord.y][currCoord.x].isObstacle() == false && !agentMap[currCoord.y][currCoord.x].getIsVisited()) {
                return currCoord;
            }
            currCoord.x--;
        }
        
        currCoord = new Coordinate(botLeft.x, botLeft.y);
        while(currCoord != botLeft) {
            if(agentMap[currCoord.y][currCoord.x].isObstacle() == false && !agentMap[currCoord.y][currCoord.x].getIsVisited()) {
                return currCoord;
            }
            currCoord.y--;
        }   
        
        level++;    
        }
        
        currCoord = new Coordinate(-1, -1);
        return currCoord;
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
    public int getDirection() {
        return dirn;
    }  
    public AgentElement[][] getAgentElements() {
        return agentMap;
    }
        
    private AgentElement[][] agentMap;
    private Coordinate currPosition;
    int dirn; //player direction
    final static int EAST   = 0;
    final static int NORTH  = 1;
    final static int WEST   = 2;
    final static int SOUTH  = 3;
    final static int MAPSIZE = 159; 
    //Set to 159 so that the map will never run out of space, 
    //regardless of whether the Agent is in the middle of an 
    //80x80 area or on the corner of it.
    
}
