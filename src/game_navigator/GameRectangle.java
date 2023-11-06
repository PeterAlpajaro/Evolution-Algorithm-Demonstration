	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  Movement
	 * Programmer: Peter Alpajaro
	 * Date Created: 5/26/2022
	 * Description: This class outlines the dimensions and velocity of rectangles in the game. It
	 * extends rectangle and adds velocity variables
	 */

package game_navigator;

import java.awt.*;

public class GameRectangle extends Rectangle {

	// Variables representing velocity
	private int velocityX;
	private int velocityY;
	
	// Constructs the object.
	public GameRectangle(int xSet, int ySet, int widthSet, int heightSet, int velXSet, int velYSet) {
		
		// Rectangle dimensions
		super(xSet, ySet, widthSet, heightSet);
		
		// Setting velocity
		velocityX = velXSet;
		velocityY = velYSet;
		
	}
	
	// Returns the x coordinate as an integer
	public int getIntX() {
		
		return (int) this.getX();
		
	}
	
	// Returns the y coordinate as an integer
	public int getIntY() {
		
		return (int) this.getY();
		
	}
	
	// Returns the width as an integer
	public int getIntWidth() {
		
		return (int) this.getWidth();
		
	}
	
	// Returns the height as an integer
	public int getIntHeight() {
		
		return (int) this.getHeight();
		
	}
	
	// Sets the horizontal velocity
	public void setXVelocity(int velSet) {
		
		this.velocityX = velSet;
		
	}
	
	// Returns the horizontal speed of the object
	public int getXVelocity() {
		
		return this.velocityX;
		
	}
	
	// Sets the vertical velocity
	public void setYVelocity(int velSet) {
		
		this.velocityY = velSet;
		
	}
	
	// Returns the vertical speed of the object
	public int getYVelocity() {
		
		return this.velocityY;
		
	}
	
}
