/** 16s1 COMP3411 hw3 (Assignment 3) by Jimmy Chen and Weilon Ying 
 *  
 *  =========================================================
 *  Briefly describe how your program works, 
 *  including any algorithms and data structures employed, 
 *  and explain any design decisions you made along the way.
 *  ---------------------------------------------------------
 *  While the game has not yet been finished the program
 *  determines what its goal should be, and navigates towards 
 *  the goal accordingly. The goal can be a nearby item,
 *  a certain location that hasn't been visited yet etc.
 *  The goal is determined in the getNodeToVisit method in
 *  AgentState class, which returns a Coordinate object
 *  describing the location of the agent's next goal destination.
 *  
 *  The determination of the agent's next goal is done
 *  in a hierarchical fashion. For example, the highest
 *  priority condition is whether the agent possesses the gold.
 *  If the agent does, then the goal is immediately set to
 *  the agent's original (home) coordinate in order to win
 *  the game. If this condition fails, then a subsequent
 *  condition check is done on whether the location of a
 *  collectible object (e.g. axe, key, stone) is known, and
 *  if so, the agent's goal would be to collect it. If this
 *  fails, the next goal is simply set to visiting a reachable
 *  but unvisited node (i.e. location that is seen, can be
 *  reached but has not been stepped on yet).
 *  
 *  An A* Search then occurs, which creates a list of Coordinates
 *  that describe the path to reach the goal destination. A
 *  list of characters is then generated, which translates the
 *  Coordinate path list into a series of char commands that
 *  the agent can use to carry out navigation of the path,
 *  including usage of axe, key and stone items. Once the
 *  agent has carried out all actions needed to collect
 *  the gold, the agent will move back to its original
 *  position, where the game will automatically finish.
 *  =========================================================
 */

import java.util.*;
import java.io.*;
import java.net.*;

/**
 *  @author Jimmy Chen, Weilon Ying
 *  Agent.java 
 *  Agent for Text-Based Adventure Game
 *  COMP3411 Artificial Intelligence
 *  UNSW Session 1, 2016
 *  Jimmy Chen & Weilon Ying
 */
public class Agent {
	
	/**
     * get_action determine's the agent's next action in order to achieve the overall objective of
     * obtaining gold and returning to agent's original location
     * @param view An array of characters which represents the agent's current viewable portion
     *     of the game world
     * @return a char command that tells the game what the agent would like to do next
     */
     public char get_action( char view[][] ) { 
	   int ch = 0; //ch is the char commad that will be returned at the end of this method
	   Coordinate goal = null;
	   if (homeCoordinate == null) { //set home coordinate to agent's first known location
	       homeCoordinate = agentMap.getCurrPosition();
	   }
	   checkOnGold(); //If agent is standing on gold, update agent's inventory (tools)
	   
	   //Find goal(Coordinate) to go to.
	   if(agentMap.getTools().isHasGold()) {
		   //have gold, go to start.
		   goal = new Coordinate(79, 79);
	   } else {
		   //find a coordinate to go to.
		   goal = agentMap.getNodeToVisit();
	   }
	   
	   // get character to use to get to goal
	   if(goal.equals(currentGoal)) {
		   //path was already found previously
		   try {
			ch = getCommand();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	   } else {
		   //we have a new goal, and hence we should generate a new path towards
           //the new goal
		   try {
            ch = getCommand(goal);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
		   currentGoal = goal.clone(); //set current goal to a copy of the new goal
	   }
	   
	   switch( ch ) { // if character is a valid action update our agentMap accordingly.
       case 'F': case 'f':
	   //move currPosition forward in agentMap if possible and check if a tool has been picked up.
	   agentMap.update((char)ch);            	
       case 'L': case 'R':
       case 'l': case 'r':
	   //update the direction in agentMap.
	   agentMap.updateDirection((char)ch);	 
	   //Chop tree/ Unlock gate
       case 'C': case 'U':
       case 'c': case 'u':
           break;
       default:
           //If none of the above have been detected, it means that an
           //invalid command was sent. Hence we should stop the program
           System.exit(-1);
	   }
	   return((char) ch);
     }

     /**
      * print_view prints the agent's current view of the map
      * @param view the view to print out
      */
     void print_view( char view[][] ) {
    	 int i,j;

	 	System.out.println("\n+-----+");
	 	for( i=0; i < 5; i++ ) {
	 		System.out.print("|");
	 		for( j=0; j < 5; j++ ) {
	 			if(( i == 2 )&&( j == 2 )) {
	 				System.out.print('^');
 				}
	 			else {
	 				System.out.print( view[i][j] );
	 			}
 			}
	 		System.out.println("|");
	      }
	      System.out.println("+-----+");
      }
     /**
      * This method does the overall operation of the program
      * @param args Arguments passed in defining the port to connect to
      */
	 public static void main( String[] args ) {
		 InputStream in  = null;
		 OutputStream out= null;
		 Socket socket   = null;
		 Agent  agent    = new Agent();
		 char   view[][] = new char[5][5];
		 char   action   = 'F';
		 int port;
		 int ch;
		 int i,j;

		 if( args.length < 2 ) {
			 System.out.println("Usage: java Agent -p <port>\n");
			 System.exit(-1);
		 }

	   port = Integer.parseInt( args[1] );

	   try { // open socket to Game Engine
		   socket = new Socket( "localhost", port );
		   in  = socket.getInputStream();
		   out = socket.getOutputStream();
	   }
	   catch( IOException e ) {
		   System.out.println("Could not bind to port: "+port);
		   System.exit(-1);
	   }	

	   try { // scan 5-by-5 wintow around current location
         
		   while( true ) {
			   for( i=0; i < 5; i++ ) {
				   for( j=0; j < 5; j++ ) {
					   if( !(( i == 2 )&&( j == 2 ))) {
						   ch = in.read();
						   if( ch == -1 ) {
							   System.exit(-1);
						   }
                     view[i][j] = (char) ch;
				   }
			   }
		   }
		   //agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
            
		   agentMap.updateMap(view);
		   agentMap.update();
		   //agentMap.print();
            
		   action = agent.get_action( view );
		   out.write( action );
		   }
	   }    
	   catch( IOException e ) {
		   System.out.println("Lost connection to port: "+ port );
		   System.exit(-1);
	   }
	   finally {
		   try {
			   socket.close();
		   }
		   catch( IOException e ) {}
	   }
   	}
   	
	 /**
     * getCommand without parameters returns the next command on the commands
     * stack
     * 
     * @return The next command on the commands queue
     * @throws Exception If an unexpected event occurs and was unable to be resolved, an exception
     *      will be thrown. See exception messages (thrown from getCommand(Coordinate goal)) for more information
     */
   	private char getCommand() throws Exception {
   	    if (commands.isEmpty()) {
   	        //throw new IndexOutOfBoundsException ("Command list empty");
   	    	Coordinate goal = agentMap.getNodeToVisit();
   	    	currentGoal = goal;
   	    	return getCommand (goal);
   	    }
   	    return commands.poll();
   	}
   	
   	/**
     * getCommand with new goal parameter. First searches for the path, and
     * converts it into a series of commands. The first command on the stack is
     * returned.
     * 
     * @param newGoal
     * @return the first command from the commands queue
     * @throws Exception
     *             Exception thrown when unexpected events happen. See exception
     *             messages for more information.
     */
   	private char getCommand (Coordinate newGoal) throws Exception {
   		//clear our commands list
   		commands.clear();
   		
   	    if (newGoal.equals(agentMap.getCurrPosition())) {
   	        throw new Exception ("New goal cannot be agent's current position");
   	    }
   	    AStarSearch search = new AStarSearch();
   	    ArrayList<Coordinate> coordinatePath = search.getPath(
   	            agentMap.getCurrPosition(), newGoal, agentMap);
   	    
   	    Coordinate[] coordinatePathArray = new Coordinate[coordinatePath.size()];
   	    coordinatePath.toArray(coordinatePathArray);
   	    
   	    if (coordinatePathArray.length < 2) {
   	        throw new Exception ("Invalid path. Path must be of length 2 or greater");
   	    }
   	    int currentPlayerDirection = agentMap.getDirection();
   	    for (int i = 1; i < coordinatePathArray.length; i++) {
   	        //determine whether we need to change directions
   	        Coordinate currCoord = coordinatePathArray[i-1];
   	        Coordinate nextCoord = coordinatePathArray[i];
   	        int directionToNext = getDirection (currCoord, nextCoord);
   	        
   	        while (currentPlayerDirection != directionToNext) {
   	            //determine which direction to turn to if player not facing same direction.
   	            
   	            //turn left
   	            commands.add('l');
   	            currentPlayerDirection++;
   	            if (currentPlayerDirection > 3) {
   	                currentPlayerDirection -= 4;
   	            }
   	        }
   	        //determine whether we need to do a certain action (e.g. cut wood, unlock door)
   	        AgentElement[][] mapElements = agentMap.getAgentElements();
   	        AgentElement nextElement = mapElements[nextCoord.y][nextCoord.x]; //[y][x]
   	        if (nextElement.isObstacle()) { 
   	            ToolBox currentTools = agentMap.getTools();
   	            if (nextElement.isTree()) {
   	                if (currentTools.isHasAxe()) {
   	                    commands.add('c');
   	                } else {
   	                    throw new Exception ("Agent attempting to go through tree without an axe");
   	                }
   	            } else if (nextElement.isDoor()) {
   	                if (currentTools.isHasKey()) {
   	                    commands.add('u');
   	                } else {
   	                    throw new Exception ("Agent attempting to go through door without a key");
   	                }
   	            } else if (nextElement.isWater()) {
   	                if (!(currentTools.getNumSteppingStones() > 0)) {
   	                    throw new Exception ("Agent attempting to go into water without any stepping stones");
   	                }
   	            } else if (nextElement.isWall()) {
   	                throw new Exception ("Agent attempting to walk into a wall");
   	            } else {
   	                throw new Exception ("Agent attemptping to walk into unknown obstacle");
   	            }
   	        }
   	        
   	        //go forward and make the move.
   	        commands.add('f');
   	        
   	    }
   	    return commands.poll();
   	    
   	}
   	
	/**
	 * Determines the direction from the start coordinate to the destination coordinate
	 * @param start The starting coordinate
	 * @param dest The destination coordinate
	 * @return An constant integer representing a cardinal direction (N, S, E, W)
	 */
   	private int getDirection (Coordinate start, Coordinate dest) {
   		if (start.x < dest.x) {
   			return EAST;
   		}
   		if (start.y > dest.y) {
   			return NORTH;
   		}
   		if (start.x > dest.x) {
   			return WEST;
   		}
   		if (start.y < dest.y) {
   			return SOUTH;
   		}
   		
   		return -1; //if shit hits the fan, this happens
   	}
   	
   	/**
     * Checks if the agent is currently on gold and sets the agent's tools inventory respectively
     */
   	private void checkOnGold() {
   	    int x = agentMap.getCurrPosition().x;
   	    int y = agentMap.getCurrPosition().y;
   	    if (agentMap.getAgentElements()[y][x].isGold()) {
   	        agentMap.getTools().setHasGold(true);
   	    }
   	}
   
 	//Cardinal directions
    final static int EAST   = 0;
    final static int NORTH  = 1;
    final static int WEST   = 2;
    final static int SOUTH  = 3;   
    
    //agentState stores the agent's own map of the world, as well as its tools. It's also used to determine the agent's next goal
    static AgentState agentMap = new AgentState();

    Queue<Character> commands = new LinkedList<Character>();
    Coordinate currentGoal = new Coordinate(79, 79); //agent's starting position
    Coordinate homeCoordinate = null;
   
}
