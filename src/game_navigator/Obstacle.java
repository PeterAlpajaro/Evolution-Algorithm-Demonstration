	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  Obstacle
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/13/2022
	 * Description: Represents an obstacle the neural network can run into
	 */

package game_navigator;

import java.awt.Color;

public class Obstacle extends GameObject {
	
	// Constructs the obstacle
	public Obstacle(int x, int y, int width, int height) {
		
		super(x, y, width, height, 0, 0);
	
		this.setColor(Color.RED);
		
	}

	// Moves the object
	@Override
	public void movement() {

		// Doesn't move
		
	}
	
	// Adjusts for collision
	@Override
	public void collision(int collisionType, GameObject collisionObject) {
		
		// Doesn't collide at the moment
		
	}

	// Sets the velocity of the object
	@Override
	public void velocitySet() {
		
		// Doesn't move nor collide at the moment.
		
	}
	
}
