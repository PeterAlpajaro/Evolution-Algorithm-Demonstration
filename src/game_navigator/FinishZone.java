	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  FinishZone
	 * Programmer: Peter Alpajaro
	 * Date Created: 5/26/2022
	 * Description: This class is another game object that outlines the finish of where the instances need to
	 * reach. Extends GameObject
	 */

package game_navigator;

import java.awt.Color;

public class FinishZone extends GameObject {

	// Reference to the game
	private GameManager game;
	
	// Constructs the object
	FinishZone(int x, int y, int width, int height, GameManager g) {
		
		// Super class GameObject, does not move
		super(x, y, width, height, 0, 0);
		
		game = g;

		// Finish zones are green
		this.setColor(Color.GREEN);
		
	}

	// Moves the object
	@Override
	public void movement() {

		// No movement implemented yet
		
	}

	// Adjusts for collision
	@Override
	public void collision(int collisionType, GameObject collisionObject) {
		
		// Complete the learning if the character reached the end
		if (collisionObject instanceof Instance) {
			
			game.endReached((Instance)collisionObject);
			
		}
		
	}

	// Sets the velocity
	@Override
	public void velocitySet() {
		
		// No movement implemented yet
		
	}
	
}
