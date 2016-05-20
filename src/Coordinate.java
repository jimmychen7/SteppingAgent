
public class Coordinate {
	public int x;
	public int y;
	public Coordinate (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Coordinate k) {
		return (k.x == this.x && k.y == this.y) ? true: false;
	}
	
	public void print() {
	    System.out.println("(" + x + "," + y + ")");
	}
}
