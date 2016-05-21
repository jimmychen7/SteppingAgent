
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
		return (this.symbol.equals("u")) ? true: false;
	}
	
	//just a blank path you can walk on
	public boolean isPath() {
		return (this.symbol.equals(" ")) ? true: false;
	}
	
	public boolean isTree() {
		return (this.symbol.equals("T")) ? true: false;
	}

	public boolean isDoor() {
		return (this.symbol.equals("-")) ? true: false;
	}

	public boolean isWater() {
		return (this.symbol.equals("~")) ? true: false;
	}

	public boolean isWall() {
		return (this.symbol.equals("*")) ? true: false;
	}

	public boolean isAxe() {
		return (this.symbol.equals("a")) ? true: false;
	}

	public boolean isKey() {
		return (this.symbol.equals("k")) ? true: false;
	}

	public boolean isStone() {
		return (this.symbol.equals("o")) ? true: false;
	}

	public boolean isPlacedStone() {
		return (this.symbol.equals("O")) ? true: false;
	}
	public boolean isGold() {
		return (this.symbol.equals('g')) ? true: false;
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
}
