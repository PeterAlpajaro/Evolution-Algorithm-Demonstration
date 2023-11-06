	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  GameObject
	 * Programmer: Peter Alpajaro
	 * Date Created: 5/26/2022
	 * Description: This class represents all object present in the game. It extends game rectangle, with
	 * extra variables and methods relating to collision
	 */

package game_navigator;

import java.awt.*;
import java.util.*;

public abstract class GameObject extends GameRectangle {
	
	// This is the previous frame, used for collision
	private GameRectangle previousFrame;
	
	// Collision booleans
	private boolean collidingTop;
	private boolean collidingBottom;
	private boolean collidingLeft;
	private boolean collidingRight;
	
	// Color of the object
	private Color drawColor;
	
	// Creates a new GameObject with the variables given
	GameObject(int x1, int y1, int widthInput, int heightInput, int velXSet, int velYSet) {

		// Creates the game rectangle
		super(x1, y1, widthInput, heightInput, velXSet, velYSet);
		
		// Setting up the previous frame
		previousFrame = new GameRectangle(this.getIntX(), this.getIntY(), this.getIntWidth(), this.getIntHeight(), this.getXVelocity(), this.getYVelocity());
		
		// Color filler
		drawColor = Color.PINK;
		
	}
	
	// Records the previous frame's location
	public void setPreviousFrameLocation() {
		
		// Setting up old variables for the previous frame.
		previousFrame.setLocation(this.getIntX(), this.getIntY());
		
	}
	
	// Returns the previous frame as a rectangle
	public GameRectangle getPreviousFrame() {
		
		return this.previousFrame;
		
	}
	
	// Returns whether the object is colliding from the top
	public boolean collidesBottom() {
		
		return this.collidingBottom;
		
	}
	
	// Returns whether the object is colliding from the top
	public boolean collidesLeft() {
		
		return this.collidingLeft;
		
	}
	
	// Returns whether the object is colliding from the top
	public boolean collidesRight() {
		
		return this.collidingRight;
		
	}
	
	// Returns whether the object is colliding from the top
	public boolean collidesTop() {
		
		return this.collidingTop;
		
	}
	
	// Adds the collision of the object to the designated variables
	public void collisionSet(int collisionType) {
			
		if (collisionType == GameManager.BOTTOM_COLLISION) {
			
			this.collidingBottom = true;
			
		} else if (collisionType == GameManager.TOP_COLLISION) {
			
			this.collidingTop = true;
			
		} else if (collisionType == GameManager.LEFT_COLLISION) {
			
			this.collidingLeft = true;
			
		} else if (collisionType == GameManager.RIGHT_COLLISION) {
			
			this.collidingRight = true;
			
		}

	}
	
	// Clears all collisions.
	public void clearCollisions() {
		
		this.collidingBottom = false;
		this.collidingTop = false;
		this.collidingLeft = false;
		this.collidingRight = false;
		
	}
	
	// Returns the color of the object
	public Color getColor() {
		
		return this.drawColor;
		
	}
	
	// Sets the color of the object
	public void setColor(Color c) {
		
		this.drawColor = c;
		
	}


	
	// All objects should have a movement method
	public abstract void movement();
	
	// All game objects should have a collision adjustment method.
	public abstract void collision(int collisionType, GameObject collisionObject);
	
	// All game objects should also have a velocity set method.
	public abstract void velocitySet();
	

	// Checks whether one object within the game collides with another
	public boolean doesCollide(GameObject r) {
		
		return this.intersects(r);
		
	}
	
}
