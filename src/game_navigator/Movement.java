	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  Movement
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/7/2022
	 * Description: This class represents a movement made by an iteration of a character
	 */

package game_navigator;

public class Movement {

	// Variables representing the movement
	private boolean space;
	private boolean left;
	private boolean right;
	private int length;

	// Creates the movement with the specified parameters
	Movement(boolean s, boolean l, boolean r, int d) {
		
		// Variables defining the movement
		space = s;
		left = l;
		right = r;
		length = d;
		
	}
	
	// Gets whether it jumps
	public boolean doesJump() {
		
		return space;
		
	}
	
	// Gets whether it movesLeft
	public boolean movesLeft() {
		
		return left;
		
	}
	
	// Gets whether it movesRight
	public boolean movesRight() {
		
		return right;
		
	}
	
	// Gets the length of that movement
	public int getLength() {
		
		return this.length;
		
	}
	
	// Sets whether it should move left
	public void setLeft(boolean set) {
		
		this.left = set;
		
	}
	
	// Sets whether it should move right
	public void setRight(boolean set) {
		
		this.right = set;
		
	}
	
	// Sets whether it should jump or not
	public void setJump(boolean set) {
		
		this.space = set;
		
	}
	
	// Sets the length of the movement
	public void setLength(int set) {
		
		this.length = set;
		
	}
	
}
