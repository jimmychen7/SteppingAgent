/**
 * 
 * @author Jimmy Chen
 * ToolBox is a data structure class that stores an agent's current inventory (i.e. their tools)
 */
public class ToolBox {
	
	private boolean hasAxe; //whether the agent has an axe or not
	private boolean hasKey; //whether the agent has a key or not
	private boolean hasGold; //whether the agent has a gold or not
	private int numSteppingStones; //the number of stepping stones the agent has
	
	/**
	 * Constructor for the ToolBox class
	 */
	public ToolBox() {
		this.setHasAxe(false);
		this.setHasKey(false);
		this.setHasGold(false);
		this.setNumSteppingStones(0);
	}

    public boolean isHasAxe() {
        return hasAxe;
    }

    public void setHasAxe(boolean hasAxe) {
        this.hasAxe = hasAxe;
    }

    public boolean isHasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public boolean isHasGold() {
        return hasGold;
    }

    public void setHasGold(boolean hasGold) {
        this.hasGold = hasGold;
    }

    public int getNumSteppingStones() {
        return numSteppingStones;
    }

    public void setNumSteppingStones(int numSteppingStones) {
        this.numSteppingStones = numSteppingStones;
    }
}
