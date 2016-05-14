
public class AgentElement {
	public AgentElement(String name) {
		this.name = name;
		this.visited = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean getIsVisited() {
		return this.visited;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
	
	//that area hasn't been explored
	public boolean isUnknown() {
		return (this.name.equals("unknown")) ? true: false;
	}
	
	//just a blank path you can walk on
	public boolean isPath() {
		return (this.name.equals("path")) ? true: false;
	}
	
	public boolean isTree() {
		return (this.name.equals("tree")) ? true: false;
	}

	public boolean isDoor() {
		return (this.name.equals("door")) ? true: false;
	}

	public boolean isWater() {
		return (this.name.equals("water")) ? true: false;
	}

	public boolean isWall() {
		return (this.name.equals("wall")) ? true: false;
	}

	public boolean isAxe() {
		return (this.name.equals("axe")) ? true: false;
	}

	public boolean isKey() {
		return (this.name.equals("key")) ? true: false;
	}

	public boolean isStone() {
		return (this.name.equals("stone")) ? true: false;
	}

	public boolean isGold() {
		return (this.name.equals("gold")) ? true: false;
	}
	
	
	
	private String name; // Can be "path", "unknown",       "tree", "water",  "stone", "door", "wall", "axe", "key", "gold". 
	private Boolean visited;
}
