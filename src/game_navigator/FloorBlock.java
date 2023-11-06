	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  FloorBlock
	 * Programmer: Peter Alpajaro
	 * Date Created: 5/26/2022
	 * Description: This class manages the blocks that make the floor. X and Y position and sides. Extends GameObject
	 */

package game_navigator;

import java.awt.Color;

public class FloorBlock extends GameObject {

	// Creates the FloorBlock object, does not move
	public FloorBlock(int x, int y, int width, int height) {
		
		// Creates the game object
		super(x, y, width, height, 0, 0);
		
		// Floors are black
		setColor(Color.BLACK);
		
	}
	
	// Moves the floor
	@Override
	public void movement() {
		
		// No movement implemented yet
		
	}
	
	// Adjusts for collision
	public void collision(int collisionType, GameObject collisionObject) {
		
		// No collision implemented yet
		
	}

	// Sets the velocity
	@Override
	public void velocitySet() {
		
		// No collision / movement implemented yet
		
	}
	
}
