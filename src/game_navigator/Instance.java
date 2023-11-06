	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  Instance
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/6/2022
	 * Description: This class represents a given character and its movements. It will be mutated frequently in order to
	 * improve its performance.
	 */

package game_navigator;

import java.awt.event.*;
import javax.swing.*;

public class Instance extends Character {
	
	// The movements made by said character.
	private Movement[] movements;
	
	// The score of the instance. determined as a function of distance, time and survival
	private int score;
	
	// Constructs a new instance
	public Instance (Movement[] m) {
		
		// Creates the character at the starting point
		super(Map.STARTING_X, Map.STARTING_Y);

		this.movements = m;
		
	}
	
	// Returns the characters movements
	public Movement[] getMovements() {
		
		return this.movements;
		
	}
	
	// Returns the score value
	public int getScore() {
		
		return this.score;
		
	}
	
	// Increases the score
	public void increaseScore() {
		
		// Reward living instances
		if (this.isAlive()) {
			
			score += this.getX();
			
		} else {
			
			score += (this.getX() / 2);
			
		}
		
		
	}
	
	// Creates a mutated version of the instance with slightly altered traits
	public Instance mutation(double mutationPercent) {
		
		Movement[] newMovements = new Movement[this.movements.length];
		
		for (int i = 0; i < movements.length; i++) {
			
			boolean newJump;
			boolean newLeft;
			boolean newRight;
			int newLength;
			
			// Small percentage to change movement directions
			// Jump change
			if (Math.random() * 100 < mutationPercent / 4) {
				
				newJump = !movements[i].doesJump();
				
			} else {
				
				newJump = movements[i].doesJump();
				
			}
			
			// Right change
			if (Math.random() * 100 < mutationPercent / 4) {
				
				newRight = !movements[i].movesRight();
				
			} else {
				
				newRight = movements[i].movesRight();
				
			}
			
			// Left change
			if (Math.random() * 100 < mutationPercent / 4) {
				
				newLeft = !movements[i].movesLeft();
				
			} else {
				
				newLeft = movements[i].movesLeft();
				
			}
			
			// Random number of milliseconds to add to the length of the movement. Dependent on the mutation percent
			newLength = movements[i].getLength() + (int) (Math.random() * (2000 * (mutationPercent / 100)) - (1000 * (mutationPercent / 100)));
			
			// Cannot have negative time on a movement
			if (newLength <= 0) {
				
				newLength = 1;
				
			}
			
			newMovements[i] = new Movement(newJump, newLeft, newRight, newLength);
			
		}
		
		// Returns the new instance that is mutated
		return new Instance(newMovements);

		
	}
	
}
