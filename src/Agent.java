
/*
 *
 *  Agent.java 
 *  Agent for Text-Based Adventure Game
 *  COMP3411 Artificial Intelligence
 *  UNSW Session 1, 2016
 *  Jimmy Chen & Weilon Ying
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class Agent {

     public char get_action( char view[][] ) { 
	   int ch=0;
	   Coordinate goal = null;
	   if (homeCoordinate == null) {
	       homeCoordinate = agentMap.getCurrPosition();
	   }
	   checkOnGold();
	   //Find goal(Coordinate) to go to.
	   if(agentMap.getTools().isHasGold()) {
		   //have gold, go to start.
		   goal = new Coordinate(79, 79);
	   } else {
		   //find a coordinate to go to.
		   goal = agentMap.getNodeToVisit();
	   }
	   
	   /*debug
	   if (goal == null) {
	       System.out.println("NULL!");
	   } else {
	       System.out.println("NOT NULL!");
	   }*/
	   
	   // get character to use to get to goal
	   if(goal.equals(currentGoal)) {
		   //path was already found previously
	       System.out.println("Going to previous Goal");
		   try {
			ch = getCommand();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	   } else {
		   //make new goal path 
	       System.out.println("Going to new Goal");
		   try {
            ch = getCommand(goal);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
		   currentGoal = goal.clone();
	   }
	   
	   switch( ch ) { // if character is a valid action update our agentMap accordingly.
       case 'F': case 'f':
	   //move currPosition forward in agentMap if possible and check if a tool has been picked up.
	   agentMap.update((char)ch);            	
       case 'L': case 'R':
       case 'l': case 'r':
	   //update the direction in agentMap.
       	agentMap.updateDirection((char)ch);	            	
       case 'C': case 'U':
       case 'c': case 'u':
	   //Chop tree/ Unlock gate
       
	   }
	   System.out.println("Current location = x: " + agentMap.getCurrPosition().x + 
	           " y:" + agentMap.getCurrPosition().y);
	   return((char) ch);
    }

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
	   //coordSeqToGoal = new CoordinateSequence(new Coordinate(79,79), new ArrayList<Coordinate>()); //No goal and coordSeq defined

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
		   agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
            
		   agentMap.updateMap(view);
		   agentMap.update();
		   agentMap.print();
            
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
    * getCommand without parameters returns the next command on the commands stack
    * @return The next command on the commands queue
    * @throws Exception Throws exception when something goes wrong.
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
   	 * getCommand with new goal parameter. First searches for the path, and converts it into a series of
   	 * commands. The first command on the stack is returned.
   	 * @param newGoal
   	 * @return the first command from the commands queue
   	 * @throws Exception Exception thrown when unexpected events happen. See exception messages for
   	 *      more information.
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
   	
   	private void checkOnGold() {
   	    int x = agentMap.getCurrPosition().x;
   	    int y = agentMap.getCurrPosition().y;
   	    if (agentMap.getAgentElements()[y][x].isGold()) {
   	        agentMap.getTools().setHasGold(true);
   	    }
   	}
   
   	
    final static int EAST   = 0;
    final static int NORTH  = 1;
    final static int WEST   = 2;
    final static int SOUTH  = 3;     
    static AgentMap agentMap = new AgentMap();
    boolean have_gold = false;
    int num_stones_held = 0;
    static CoordinateSequence coordSeqToGoal;
    Queue<Character> commands = new LinkedList<Character>();
    Coordinate currentGoal = new Coordinate(79, 79); //agent's starting position
    static Coordinate homeCoordinate = null;
   
}
