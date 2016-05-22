/**
 * 
 * @author Jimmy Chen, Weilon Ying
 * AgentElement represents a single location (node) on the map and contains its information
 */
public class AgentElement {
	public AgentElement(String symbol) {
		this.symbol = symbol;
		this.visited = false;
		this.setReachable(false);
		
	}
	
	public String getSymbol() {
		return this.symbol;
	}
	
	public boolean getIsVisited() {
		return this.visited;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public void updateAgentElement(char ch) {
		String symbol = String.valueOf(ch);
		setSymbol(symbol);
	}
	
	
	//that area hasn't been explored
	public boolean isUnknown() {
		return (this.symbol.equals(UNKNOWN)) ? true: false;
	}
	
	//just a blank path you can walk on
	public boolean isPath() {
		return (this.symbol.equals(EMPTY)) ? true: false;
	}
	
	public boolean isTree() {
		return (this.symbol.equals(TREE)) ? true: false;
	}

	public boolean isDoor() {
		return (this.symbol.equals(DOOR)) ? true: false;
	}

	public boolean isWater() {
		return (this.symbol.equals(WATER)) ? true: false;
	}

	public boolean isWall() {
		return (this.symbol.equals(WALL)) ? true: false;
	}

	public boolean isAxe() {
		return (this.symbol.equals(AXE)) ? true: false;
	}

	public boolean isKey() {
		return (this.symbol.equals(KEY)) ? true: false;
	}

	public boolean isStone() {
		return (this.symbol.equals(STONE_OBJECT)) ? true: false;
	}

	public boolean isPlacedStone() {
		return (this.symbol.equals(STONE_PLACED)) ? true: false;
	}
	public boolean isGold() {
		return (this.symbol.equals(GOLD)) ? true: false;
	}
	
	public boolean isObstacle() {
		if(isWater() || isWall() || isTree() || isDoor()) return true;
		return false;
	}	
	
	public boolean isReachable() {
		return reachable;
	}

	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}



	private String symbol; // Can be "path", "unknown",       "tree", "water",  "stone", "door", "wall", "axe", "key", "gold". 
	private Boolean visited;
	private boolean reachable;
	
	//Possible element symbols
	public final static String UNKNOWN      = ".";
    public final static String EMPTY        = " ";
    public final static String TREE         = "T";
    public final static String DOOR         = "-";
    public final static String WATER        = "~";
    public final static String WALL         = "*";
    public final static String AXE          = "a";
    public final static String KEY          = "k";
    public final static String STONE_OBJECT = "o";
    public final static String STONE_PLACED = "O";
    public final static String GOLD         = "g";
}
