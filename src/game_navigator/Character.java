	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  Character
	 * Programmer: Peter Alpajaro
	 * Date Created: 5/26/2022
	 * Description: This class manages the position and movement of characters in the game. It extends
	 * the GameObject class.
	 */

package game_navigator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Character extends GameObject {

	// Height and width of the characters
	public static final int CHARACTER_HEIGHT = 30;
	public static final int CHARACTER_WIDTH = 20;
	
	// Acceleration variables
	private int xAcceleration;
	private int yAcceleration;
	
	// Movement commands
	private boolean moveLeft;
	private boolean moveRight;
	private boolean doJump;

	private boolean isAlive;


	// Creates the character
	public Character(int x, int y) {
		
		// Creates the game object.
		super(x, y, CHARACTER_WIDTH, CHARACTER_HEIGHT, 0, 0);
		
		// Start with no collision
		this.collisionSet(GameManager.NO_COLLISION);
		
		// Start with gravity for acceleration
		this.xAcceleration = 0;
		this.yAcceleration = -1;
		
		// Start living
		isAlive = true;
		
		// Characters are represented with a blue color
		setColor(Color.BLUE);
		
	}
	
	// Stops the character
	public void kill() {
		
		this.isAlive = false;
		
	}
	
	// Returns whether the character is living
	public boolean isAlive() {
		
		return this.isAlive;
		
	}
	
	// Sets the vertical acceleration of the character
	public void setYAccel(int accel) {
		
		this.yAcceleration = accel;
		
	}
	
	// Collision of characters
	@Override
	public void collision(int collisionType, GameObject collisionObject) {
		
		// Kill the character if they touched an Obstacle
		if (collisionObject instanceof Obstacle) {

			// Stop if obstacle hit
			kill();

		// Adjust for each floor collision
		} else if (collisionObject instanceof FloorBlock) {
			
			// Adds a collision depending on the type
			this.collisionSet(collisionType);
			
			// If colliding from the top move down out of the object
			if (collisionType == GameManager.TOP_COLLISION) {

				this.setLocation(getIntX(), collisionObject.getIntY() + collisionObject.getIntHeight());

			// If colliding from the bottom move up out of the object
			} else if (collisionType == GameManager.BOTTOM_COLLISION) {

				this.setLocation(this.getIntX(), collisionObject.getIntY() - this.getIntHeight());
			
			// If colliding from the left move right out of the object
			} else if (collisionType == GameManager.LEFT_COLLISION) {

				this.setLocation(collisionObject.getIntX() + collisionObject.getIntWidth(), this.getIntY());

			// If colliding from the right move left out of the object
			} else if (collisionType == GameManager.RIGHT_COLLISION) {

				this.setLocation(collisionObject.getIntX() - this.getIntWidth(), this.getIntY());

			}
			
		}
		
		

		
		
	}
	
	// Adjusts the velocity based on the collision
	@Override
	public void velocitySet() {
		
		// If it collides on the top reverse the velocity
		if (this.collidesTop()){

			this.setYVelocity(-this.getPreviousFrame().getYVelocity());

		}
		
		// Stop the character if they are colliding from either the left or right
		if (this.collidesLeft() || this.collidesRight()) {
			
			this.setXVelocity(0);
			
			// Kill if crushed by left and right
			if (this.collidesLeft() && this.collidesRight()) {
				
				kill();
				
			}
			
		}
		
		// Stop vertical movement if they collide from the bottom
		if (this.collidesBottom()) {
			
			this.setYVelocity(0);
			
			// Kill if crushed from top and bottom
			if (this.collidesTop()) {
				
				kill();
				
			}
			
		// Gravity if not a bottom collision
		} else {
			
			this.setYAccel(-1);
			
		}
		
	}
	
	// Moves the character depending on the time passed
	@Override
	public void movement() {
		
		// Only move if the character is alive
		if (this.isAlive) {

			// Sets velocity based on the given commands.
			if (moveRight) {

				this.setXVelocity(10);

			} 
			
			if (moveLeft) {

				this.setXVelocity(-10);

			}
			
			if (moveLeft && moveRight) {
				
				this.setXVelocity(0);
				
			}

			// Jump if on the ground
			if (doJump && this.collidesBottom()) {

				this.setYVelocity(20);

			}

			// Sets the new velocities
			this.setYVelocity(this.getYVelocity() + yAcceleration);
			this.setXVelocity(this.getXVelocity() + xAcceleration);
			
			// Set the previous frame location before each movement, but not before each collision
			this.setPreviousFrameLocation();

			// Moves according to them
			this.setLocation(this.getIntX() - this.getXVelocity(), this.getIntY() - this.getYVelocity());
			
			// Kill a character falling off the map
			if (this.getIntY() > GameManager.SCREEN_HEIGHT) {

				kill();

			}

		// Stop the character if dead
		} else {
			
			this.setXVelocity(0);
			this.setYVelocity(0);
			
		}
		
	}
	
	// Applies the movement to the character
	public void setMovement(Movement m) {
		
		this.moveLeft = m.movesLeft();
		this.moveRight = m.movesRight();
		this.doJump = m.doesJump();
		
	}

}
