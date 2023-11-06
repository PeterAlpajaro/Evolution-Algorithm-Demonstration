	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class: Map
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/13/2022
	 * Description: Creates and represents a list of objects that is used to represent the different objects
	 * that exist in the game.
	 */

package game_navigator;

import java.util.*;
import java.io.*;

public class Map {

	// Character and initial platform will always begin at the same place.
	public static final int STARTING_X = 5;
	public static final int STARTING_Y = 400;
	
	// List of game objects
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	// Reference to the game
	private GameManager game;
	
	// Creates an already existing map
	public Map(int accessNumber, GameManager g) {
		
		// Access to the game
		game = g;
		
		// Read file if the files are present
		if (accessNumber != 0) {

			String accessFile = "final_files/maps/map" + accessNumber + "/map.txt";

			String[][] objects = GeneralMethods.readFile(accessFile);

			// Number format catch
			try {

				// Loops through the objects and their attributes
				for (int i = 0; i < objects.length; i++) {

					// Get attributes
					int x = Integer.parseInt(objects[i][0]);
					int y = Integer.parseInt(objects[i][1]);
					int width = Integer.parseInt(objects[i][2]);
					int height = Integer.parseInt(objects[i][3]);

					// Add the object depending on the type
					if (objects[i][4].equals("floor")) {

						gameObjects.add(new FloorBlock(x, y, width, height));

					} else if (objects[i][4].equals("obstacle")) {

						gameObjects.add(new Obstacle(x, y, width, height));

					} else if (objects[i][4].equals("finishzone")) {

						gameObjects.add(new FinishZone(x, y, width, height, game));

					}

				}

			// Display error message (In this case program may still function as intended)
			} catch (Exception e) {

				System.out.println("Map 1 is empty or an incorrect format. Check map" + accessNumber + "/map.txt if program does not function correctly");

			}

		} else {
			
			GeneralMethods.writeToFile(new String[][] {{}}, GeneralMethods.MAP_TYPE, 0, 1);
			GeneralMethods.writeToFile(new String[][] {{"1"}}, GeneralMethods.STARTING_TYPE, 0, 1);
			
		}

		// Beginning platform
		gameObjects.add(new FloorBlock(STARTING_X - 20, STARTING_Y + Character.CHARACTER_HEIGHT + 100, 100, 20));

	}

	// Returns the list of objects
	public ArrayList<GameObject> getObjects() {
		
		return this.gameObjects;
		
	}
	
}
