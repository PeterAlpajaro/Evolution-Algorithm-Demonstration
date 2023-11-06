	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  LearningManager
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/6/2022
	 * Description: This class manages each generation of neural networks. It weighs the scores of all
	 * the instances and then creates new generations from the sucessful ones. 
	 */

package game_navigator;

import java.io.*;
import java.util.*;

public class LearningManager {

	// Change this to alter the power of the mutation
	public static final int MUTATION_PERCENT = 10;
	
	// Number of moves the instance can make
	public static final int MOVEMENT_NUMBER = 5;
	
	// Number of instances per generation
	public static final int POPULATION_COUNT = 10;
	
	// Number of milliseconds for the instance to move
	public static final int TIME_GIVEN = 7000;
	
	// Constant for code comprehension
	public static final int FINAL_GENERATION_CHANGE = 0;
	
	// Numbers representing the generation
	private int currentGenerationNumber;
	private int latestGenerationNumber;
	
	// If a character has reached the goal
	private boolean generationComplete;
	
	// The generation and the final instance if a character has reached the goal
	private Instance[] currentGeneration;
	private Instance finalInstance;
	
	// Reference to the game
	private GameManager game;
	
	// The map that this generation is learning from
	private int mapNumber;
	
	// Creates the learning manager, with a reference to the game
	public LearningManager(GameManager m, int accessNumber) {
		
		game = m;
		
		mapNumber = accessNumber;
		
		try {
			
			// Reads the general file and gets the latest generation
			FileReader fileReader = new FileReader("final_files/maps/map" + mapNumber + "/general_information.txt");
			BufferedReader input = new BufferedReader(fileReader);
			
			// Generation number and whether the program is complete or not, and if so, the instance that completed it
			currentGenerationNumber = Integer.parseInt(input.readLine());
			latestGenerationNumber = currentGenerationNumber;
			
			generationComplete = input.readLine().equals("true");
			
			// If the generation is complete, just display the successful attempt
			if (generationComplete) {
				
				currentGeneration = new Instance[1];
				
				// Gets the final instance from the file
				String finalInstanceStringRep = input.readLine();
				String[] instanceRepresentation = new String[MOVEMENT_NUMBER * 4];
				StringTokenizer dividedString = new StringTokenizer(finalInstanceStringRep);
				
				for (int i = 0; i < MOVEMENT_NUMBER * 4; i++) {
					
					instanceRepresentation[i] = dividedString.nextToken();
					
				}
				
				// Sets the final instance
				finalInstance = stringToInstance(instanceRepresentation);
				
				// Adds the final instance to the generation
				currentGeneration[0] = finalInstance;
				
				// Adds the one instance to the game
				game.addObject(finalInstance);
				game.addToList(GameManager.GENERATION_LIST, "Successful Character");
				
			}
			
			input.close();
			
		// Print appropriate message in case of error.
		} catch (Exception e) {
			
			System.out.println("File read or format error. check general_information.txt\nIn game_navigator/LearningManager.java");
			
			e.printStackTrace();
			
		}
		
		// If this is the first generation then create a brand new generation to start
		if (currentGenerationNumber == 0 && !generationComplete) {
			
			// Create the new generation
			currentGeneration = newGeneration();

			// Write this generation to a file
			GeneralMethods.writeToFile(generationToString(), GeneralMethods.GENERATION_DATA_TYPE, currentGenerationNumber, mapNumber);
			
		// If one already exists then read the old one and execute
		} else if (!generationComplete) {
			
			// Get the data of the latest generation
			String[][] generationData = GeneralMethods.readFile("final_files/maps/map" + mapNumber + "/character_data/generation" + currentGenerationNumber + ".txt");
			
			currentGeneration = new Instance[generationData.length];
			
			// Looping through and setting each instance
			for (int i = 0; i < currentGeneration.length; i++) {
				
				currentGeneration[i] = stringToInstance(generationData[i]);
				game.addObject(currentGeneration[i]);
				
			}
			
		}
	
	}
	
	// An instance has cleared the stage and will now be shown alone
	public void setFinalInstance(Instance instanceSet) {
		
		this.finalInstance = instanceSet;
		this.generationComplete = true;
		
		String finalInstanceStringRep = "";
		
		// Puts the final instance into a readable format
		for (int i = 0; i < MOVEMENT_NUMBER; i++) {
			
			finalInstanceStringRep += finalInstance.getMovements()[i].doesJump() + " ";
			finalInstanceStringRep += finalInstance.getMovements()[i].movesLeft() + " ";
			finalInstanceStringRep += finalInstance.getMovements()[i].movesRight() + " ";
			finalInstanceStringRep += finalInstance.getMovements()[i].getLength() + " ";
			
		}
		
		// Writes this to a file
		GeneralMethods.writeToFile(new String[][] {{Integer.toString(latestGenerationNumber), Boolean.toString(generationComplete), finalInstanceStringRep}}, GeneralMethods.GENERAL_TYPE, 0, mapNumber);
		
	}
	
	// Returns whether the generation has succeeded or not
	public boolean generationIsComplete() {
		
		return this.generationComplete;
		
	}
	
	// Gets the latest generation and returns it.
	public int getLatestGenerationNumber() {
		
		return this.latestGenerationNumber;
		
	}
	
	// Returns the current generation number
	public int getCurrentGenerationNumber() {
		
		return this.currentGenerationNumber;

	}
	
	// Clears the game of the currentGeneration
	public void clearGeneration() {
		
		for (Instance i : currentGeneration) {
			
			game.removeObject(i);
			
		}
		
	}
	
	// Finds the top performing character. 
	public Instance findTopPerforming() {
		
		Instance topPerforming = currentGeneration[0];
		
		for (int i = 1; i < currentGeneration.length; i++) {
			
			// Gets the best score
			if (topPerforming.getScore() < currentGeneration[i].getScore()) {
				
				topPerforming = currentGeneration[i];
				
			}
			
		}
		
		return topPerforming;
		
	}
	
	// Sets the movements depending on the time passed.
	public void setMovements(int time) {
		
		// All generation instances
		for (int i = 0; i < currentGeneration.length; i++) {

			// Kill after the time has passed
			if (time > LearningManager.TIME_GIVEN) {

				currentGeneration[i].kill();

			} else {

				// Loop through the movements
				for (int j = 0; j < currentGeneration[i].getMovements().length; j++) {
					
					// If in the time frame of that movement call the movement on the character
					if (currentGeneration[i].getMovements()[j].getLength() < time) {
						
						currentGeneration[i].setMovement(currentGeneration[i].getMovements()[j]);

					}

				}

			}
			
		}
		
	}
	
	// Adjusts the scores of all the instances
	public void setAllScores() {
		
		for (int i = 0; i < currentGeneration.length; i++) {
			
			currentGeneration[i].increaseScore();
			
		}
		
	}
	
	// Creates a new mutated generation based on the previous one with a population of 20, or displays the successful instance
	public void nextGeneration() {
		
		// If the generation has been finished, display the successful attempt
		if ((generationComplete && latestGenerationNumber <= currentGenerationNumber + 1) || currentGenerationNumber == FINAL_GENERATION_CHANGE) {
			
			clearGeneration();
			
			currentGeneration = new Instance[1];
			
			// Copies the instance to reset the position
			finalInstance = finalInstance.mutation(0);

			// Adds it to the generation
			currentGeneration[0] = finalInstance;
			game.addObject(finalInstance);
			
			game.setSelectedItem(GameManager.GENERATION_LIST, "Successful Character");
			game.startTimer();
			
			
		// If not a new generation change to the next one
		} else if (currentGenerationNumber + 1 <= latestGenerationNumber) {
			
			currentGenerationNumber++;
			changeGeneration(currentGenerationNumber);
			game.setSelectedItem(GameManager.GENERATION_LIST, Integer.toString(currentGenerationNumber));
			game.startTimer();
			
		// If the latest generation then make a new generation
		} else {
			
			clearGeneration();

			// Finds the most successful instance.
			Instance topPerforming = findTopPerforming();
			
			Instance[] newGeneration = new Instance[POPULATION_COUNT];
			
			// First instance is unaltered
			newGeneration[0] = topPerforming.mutation(0);

			// Loops through the population count
			for (int i = 0; i < newGeneration.length; i++) {

				if (i != 0) {
					
					// New mutation based on the most successful of the previous generation
					newGeneration[i] = topPerforming.mutation(MUTATION_PERCENT);
					
				}
				
				// Adds it to the game
				game.addObject(newGeneration[i]);

			}

			// Increase the generation number
			currentGenerationNumber++;
			latestGenerationNumber++;
			
			// Set the new generation
			currentGeneration = newGeneration;
			
			// Write the old generation to a file
			GeneralMethods.writeToFile(generationToString(), GeneralMethods.GENERATION_DATA_TYPE, currentGenerationNumber, mapNumber);

			// Write the new generation number to the file
			GeneralMethods.writeToFile(new String[][] {{Integer.toString(currentGenerationNumber), Boolean.toString(generationComplete)}}, GeneralMethods.GENERAL_TYPE, 0, mapNumber);


			game.addToList(GameManager.GENERATION_LIST, Integer.toString(currentGenerationNumber));
			game.setSelectedItem(GameManager.GENERATION_LIST, Integer.toString(currentGenerationNumber));
			game.startTimer();

		}
		
	}
	
	// Creates a new, random generation
	public Instance[] newGeneration() {
		
		currentGenerationNumber = 1;
		latestGenerationNumber = 1;
		
		Instance[] newGeneration = new Instance[POPULATION_COUNT];
	
		Movement[] newMovements = new Movement[MOVEMENT_NUMBER];
		
		// Assigning each movements to some value
		for (int i = 0; i < newMovements.length; i++) {
			
			// Base values
			newMovements[i] = new Movement(true, true, true, 1000 * i);
			
		}

		// Create one new instance and add it to the game
		newGeneration[0] = new Instance(newMovements);
		game.addObject(newGeneration[0]);
		
		// Loop from second to final instances in generation
		for (int i = 1; i < newGeneration.length; i++) {
			
			// Mutates the remainder of the generation.
			newGeneration[i] = newGeneration[i - 1].mutation(MUTATION_PERCENT);
			
			// Add it to the game
			game.addObject(newGeneration[i]);
			
		}
		
		return newGeneration;
		
	}
	
	// Changes the generation to the specified input
	public void changeGeneration(int newGen) {
		
		// If changing to the final generation
		if (newGen == FINAL_GENERATION_CHANGE) {
			
			// Clear the current generation from the screen
			clearGeneration();
				
			currentGenerationNumber = FINAL_GENERATION_CHANGE;
			// Non-mutated copy
			currentGeneration = new Instance[1];
			// Resets the position
			finalInstance = finalInstance.mutation(0);
			currentGeneration[0] = finalInstance;
			game.addObject(finalInstance);


		// Print an error if the input is greater than the generation
		} else if (newGen > latestGenerationNumber) {
			
			System.out.println("input number " + newGen + " is greater than the largest generation,\nIn game_navigator/LearningManager.");

		} else {
			
			// Clear the current generation from the screen
			clearGeneration();
			
			// Set new generation number
			currentGenerationNumber = newGen;
			
			currentGeneration = new Instance[POPULATION_COUNT];

			// Get the data of the generation
			String[][] generationData = GeneralMethods.readFile("final_files/maps/map" + mapNumber + "/character_data/generation" + newGen + ".txt");

			// Looping through and setting each generation length
			for (int i = 0; i < currentGeneration.length; i++) {

				currentGeneration[i] = stringToInstance(generationData[i]);
				game.addObject(currentGeneration[i]);

			}

		}

	}
	
	// Returns the current generation represented as text to be written in a file
	private String[][] generationToString() {
		
		/*
		 * Creates the data file. Length the size of the current generation, and width
		 * the amount of movements multiplied by the number of attributes
		 */
		String data[][] = new String[currentGeneration.length][currentGeneration[0].getMovements().length * 4];
		
		// Loop through the current generation
		for (int i = 0; i < currentGeneration.length; i++) {
			
			data[i] = instanceToString(currentGeneration[i]);
			
		}
		
		// Return the string data
		return data;
		
	}
	
	// Returns a string array representing the movements of an instance.
	private String[] instanceToString(Instance inst) {
		
		String[] instanceRep = new String[MOVEMENT_NUMBER * 4];
		
		int z = 0;
		
		for (int j = 0; j < MOVEMENT_NUMBER; j++) {
			
			/*
			 * Grab each value from the instance. Jump, then Left, 
			 * then Right, then Length of the movement
			 */
			instanceRep[z] = Boolean.toString(inst.getMovements()[j].doesJump());
			instanceRep[z+1] = Boolean.toString(inst.getMovements()[j].movesLeft());
			instanceRep[z+2] = Boolean.toString(inst.getMovements()[j].movesRight());
			instanceRep[z+3] = Integer.toString(inst.getMovements()[j].getLength());
			
			z += 4;
			
		}
		
		// Return the string representation
		return instanceRep;
		
	}
	
	// Returns a new instance created from a text representation of its movements.
	private Instance stringToInstance(String[] str) {

		Instance newInstance = null;

		Movement[] instanceMovements = new Movement[MOVEMENT_NUMBER];

		try {

			int z = 0;

			for (int j = 0; j < MOVEMENT_NUMBER; j++) {
				
				// Gets all the information from the data and sets to the movements
				boolean space = str[z].equals("true");
				boolean left = str[z+1].equals("true");
				boolean right = str[z+2].equals("true");
				int length = Integer.parseInt(str[z+3]);

				// Adds to the movements with the gathered data
				instanceMovements[j] = new Movement(space, left, right, length);

				z += 4;

			}

			// Adds the instance to the generation
			newInstance = new Instance(instanceMovements);

		// Number exception handling
		} catch (NumberFormatException e) {

			System.out.println("Format or parsing error,\nIn game_navigator/LearningManager.");
			e.printStackTrace();

		}
		
		return newInstance;

	}
	
	
	
}
