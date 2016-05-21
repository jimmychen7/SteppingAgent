
public class ToolBox {
	
	private boolean hasAxe;
	private boolean hasKey;
	private boolean hasGold;
	private int numSteppingStones;
	
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
