/*********************************************
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

	      System.out.print("Enter Action(s): ");

	      try {
	         while ( ch != -1 ) {
	            // read character from keyboard
	            ch  = System.in.read();

	            switch( ch ) { // if character is a valid action, return it
	            case 'F': case 'f':
	            	if(agentMap.canGoForward(num_stones_held)) {
	            		agentMap.updateCurrPosition((char)ch);
	            	}	            	
	            case 'L': case 'R':
	            case 'l': case 'r':
	            	agentMap.updateDirection((char)ch);	            	
	            case 'C': case 'U':
	            case 'c': case 'u':    	
	            	return((char) ch );             
	            }
	         }
	      }
	      catch (IOException e) {
	         System.out.println ("IO error:" + e );
	      }

	      return 0;     
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
   
   	private List<Character> getPath (Coordinate destination) {
   		
   		AStarSearch search = new AStarSearch();
   		List<Coordinate> coordinatePath = search.getPath(agentMap.getCurrPosition(), 
   				destination, agentMap);
   		
   		Coordinate[] coordinatePathArray = coordinatePath.toArray(
   				new Coordinate[coordinatePath.size()]);
   		
   		if (coordinatePathArray.length < 2) {
   			return null;
   		}
   		
   		List<Character> commandList = new LinkedList<Character>();
   		int playerOrientation = agentMap.getDirection();
   		for (int i = 1; i < coordinatePathArray.length; i++) {
   			Coordinate before = coordinatePathArray[i - 1];
   			Coordinate after  = coordinatePathArray[i];
   			
   			//Figure out how to get from before to after
   			int direction = getDirection (before, after);
   			int relativeDirection = direction - playerOrientation;
   			
			while (relativeDirection != 0) { //realign player while not oriented correctly
				playerOrientation++;
				commandList.add('l');
				if (playerOrientation > SOUTH) {
					playerOrientation -= 4;
				}
				
				relativeDirection = direction - playerOrientation;
			}
			commandList.add('f');
   		}
   		
   		return commandList;
   	}
   	/*
   	private char getCommand( Coordinate start, Coordinate dest) {
   		
   	}
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
   
   	
    final static int EAST   = 0;
    final static int NORTH  = 1;
    final static int WEST   = 2;
    final static int SOUTH  = 3;     
    static AgentMap agentMap = new AgentMap();
    boolean have_gold = false;
    int num_stones_held = 0;
    CoordinateSequence pathToGoal = null;
   
}
