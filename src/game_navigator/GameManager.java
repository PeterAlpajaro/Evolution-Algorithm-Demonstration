/*
 * Project: Game Navigator
 * Package: game_navigator
 * Class:  GameManager
 * Programmer: Peter Alpajaro
 * Date Created: 5/26/2022
 * Description: This class begins the game, manages all characters and obstacles and creates the graphics pane.
 * It also manages the User Interface and launches the map creation window and help windows.
 */

package game_navigator;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;

public class GameManager implements ActionListener {

	// Collision types
	public static final int NO_COLLISION = 0;
	public static final int LEFT_COLLISION = 1;
	public static final int RIGHT_COLLISION = 2;
	public static final int TOP_COLLISION = 3;
	public static final int BOTTOM_COLLISION = 4;
	
	// Types for the selection boxes
	public static final int MAP_LIST = 11;
	public static final int GENERATION_LIST = 12;

	public static final int SCREEN_WIDTH = 1300;
	public static final int SCREEN_HEIGHT = 700;

	// List of game objects
	private ArrayList<GameObject> gameObjects;

	// Map and its number
	private Map currentMap;
	private int currentMapNumber;
	
	// Graphics panel and frame
	private GraphicsWindow graphicsPane;
	private JFrame graphicsFrame;
	
	// JComponents in the panel
	private JButton startButton, pauseButton, mapCreationButton, helpButton;
	private JLabel mapImageLabel, generationImageLabel;
	private BufferedImage mapImage, generationImage;
	private ImageIcon startIcon, pauseIcon, mapCreationIcon, helpIcon;
	private JComboBox<String> generationSelection, mapSelection;
	
	// Whether to call action performed
	private boolean callActionPerformed;
	
	LearningManager startLearning;
	Move movementTimer;

	// Constructs the game manager
	GameManager() {
		
		// Parse exception catching
		try {
			
			// Gets the first map number
			currentMapNumber = Integer.parseInt(GeneralMethods.readFile("final_files/starting_document.txt")[0][0]);
			
		} catch (NumberFormatException err) {
			
			System.out.println("Parse / File error\nIn game_navigator/GameManager.");
			err.printStackTrace();
			
		}
		
		// Creates the map and gets its objects
		currentMap = new Map(currentMapNumber, this);
		
		// Saves to the first map if there is no map already created
		if (currentMapNumber == 0) {
			
			currentMapNumber++;
			
		}
		
		gameObjects = currentMap.getObjects();
		
		// Creating JPanel
		graphicsPane = new GraphicsWindow(gameObjects);
		
		// Creating and modifying the selection box
		generationSelection = new JComboBox<String>();
		generationSelection.setName("generation");
		generationSelection.addActionListener(this);
		generationSelection.setBounds(130, 50, 150, 30);
		
		try {
			
			// Getting the images
			generationImage = ImageIO.read(new File("final_files/images/generation_image.jpg"));
			mapImage = ImageIO.read(new File("final_files/images/map_image.jpg"));
			
		} catch (IOException e) {
			
			System.out.println("File read error for JLabel images\nIn game_navigator/GameManager");
			e.printStackTrace();
			
		}
		
		// The labels for the selection boxes
		generationImageLabel = new JLabel(new ImageIcon(generationImage));
		generationImageLabel.setBounds(10, 50, 120, 30);
		mapImageLabel = new JLabel(new ImageIcon(mapImage));
		mapImageLabel.setBounds(10, 100, 120, 30);
		
		// Map selection box
		mapSelection = new JComboBox<String>();
		mapSelection.setName("map");
		mapSelection.addActionListener(this);
		mapSelection.setBounds(130, 100, 150, 30);
		
		// Action performed should be called
		callActionPerformed = true;
		
		movementTimer = new Move();
		startLearning = new LearningManager(this, currentMapNumber);
		
		// Add the generations to the generation selection box
		for (int i = 1; i <= startLearning.getLatestGenerationNumber(); i++) {
			
			addToList(GENERATION_LIST, Integer.toString(i));
			
		}
		
		// Add the maps to the map selection box
		for (int i = 1; i <= currentMapNumber; i++) {
			
			addToList(MAP_LIST, Integer.toString(i));
			
		}
		
		// Set the current map number as the selected item on the list
		setSelectedItem(MAP_LIST, Integer.toString(currentMapNumber));
		
		// If the generation is complete set the selection default to the successful attempt.
		if (startLearning.generationIsComplete()) {
			
			setSelectedItem(GENERATION_LIST, "Successful Character");
			
		// Otherwise replace with the latest generation
		} else {
			
			System.out.println(Integer.toString(startLearning.getLatestGenerationNumber()));
			
			setSelectedItem(GENERATION_LIST, Integer.toString(startLearning.getLatestGenerationNumber()));
			
		}
		
		// Setting up JFrame
		graphicsFrame = new JFrame("Learning Program");
		graphicsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set size here
		graphicsFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// Start button
		startIcon = new ImageIcon("final_files/images/start_icon.jpg");
		startButton = new JButton(startIcon);
		startButton.setName("Start");
		startButton.setBounds(10, 10, 70, 30);
		startButton.addActionListener(this);
		
		// Pause button
		pauseIcon = new ImageIcon("final_files/images/pause_icon.jpg");
		pauseButton = new JButton(pauseIcon);
		pauseButton.setName("Pause");
		pauseButton.setBounds(90, 10, 70, 30);
		pauseButton.addActionListener(this);
		pauseButton.setEnabled(false);
		
		// Button to create a new map
		mapCreationIcon = new ImageIcon("final_files/images/map_creation_icon.jpg");
		mapCreationButton = new JButton(mapCreationIcon);
		mapCreationButton.setName("Create New Map");
		mapCreationButton.setBounds(10, 160, 180, 30);
		mapCreationButton.addActionListener(this);
		
		// Help button
		helpIcon = new ImageIcon("final_files/images/help_icon.jpg");
		helpButton = new JButton(helpIcon);
		helpButton.setName("Help");
		helpButton.setBounds(210, 160, 70, 30);
		helpButton.addActionListener(this);
		
		// Add JPanel to the JFrame
		graphicsFrame.add(graphicsPane);
		
		// Add all the components to the JPanel
		graphicsPane.add(startButton);
		graphicsPane.add(mapCreationButton);
		graphicsPane.add(mapSelection);
		graphicsPane.add(generationSelection);
		graphicsPane.add(helpButton);
		graphicsPane.add(pauseButton);
		graphicsPane.add(mapImageLabel);
		graphicsPane.add(generationImageLabel);

		// Sets visible and repaints.
		graphicsFrame.setVisible(true);
		graphicsFrame.setResizable(false);
		graphicsFrame.repaint();
		graphicsFrame.setFocusable(false);

		graphicsPane.repaint();

	}

	// Gets the current map
	public Map getMap() {

		return this.currentMap;

	}
	
	// Changes the map to the specified one
	private void changeMap(int mapNumber) {

		currentMapNumber = mapNumber;		
		
		// Creates the new map and gets its objects
		currentMap = new Map(mapNumber, this);
		gameObjects = currentMap.getObjects();
		graphicsPane.updateObjects(gameObjects);
		
		// Remove components
		graphicsPane.removeAll();
		
		// Recreate the learning manager and the timer
		startLearning = new LearningManager(this, currentMapNumber);
		movementTimer.resetTimer();
		movementTimer.pause();
		
		// Recreating and modifying the selection box
		generationSelection = new JComboBox<String>();
		generationSelection.setName("generation");
		generationSelection.addActionListener(this);
		generationSelection.setBounds(130, 50, 150, 30);
		
		// Add the values to the selection box
		for (int i = 1; i <= startLearning.getLatestGenerationNumber(); i++) {
			
			addToList(GENERATION_LIST, Integer.toString(i));
			
		}
		
		// If the generation is complete set the selection default to the successful attempt.
		if (startLearning.generationIsComplete()) {
			
			addToList(GENERATION_LIST, "Successful Character");
			setSelectedItem(GENERATION_LIST, "Successful Character");
			
		// Otherwise replace with the latest generation
		} else {
			
			setSelectedItem(GENERATION_LIST, Integer.toString(startLearning.getLatestGenerationNumber()));
			
		}
		
		// Re add the components to the Panel
		graphicsPane.add(startButton);
		graphicsPane.add(mapCreationButton);
		graphicsPane.add(mapSelection);
		graphicsPane.add(generationSelection);
		graphicsPane.add(helpButton);
		graphicsPane.add(pauseButton);
		graphicsPane.add(mapImageLabel);
		graphicsPane.add(generationImageLabel);
		
		graphicsFrame.add(graphicsPane);
		
		graphicsPane.repaint();
		graphicsPane.setVisible(true);
		graphicsFrame.repaint();
		
		
		
	}
	
	// Stops the learning if a character reaches the end
	public void endReached(Instance i) {
		
		// If the generation is not complete...
		if (!startLearning.generationIsComplete()) {
			
			// Set the final instance and adjust the selection box
			startLearning.setFinalInstance(i);
			addToList(GENERATION_LIST, "Successful Character");
			setSelectedItem(GENERATION_LIST, "Successful Character");
			
		}
		
	}
	
	// Adds an element to the list
	public void addToList(int type, String addition) {
		
		// This method should not call action listener
		callActionPerformed = false;
		
		if (type == GENERATION_LIST) {
			
			generationSelection.addItem(addition);
			
		} else if (type == MAP_LIST) {
			
			mapSelection.addItem(addition);
			
		}
		
		callActionPerformed = true;
		
	}
	
	// Selects the correct item in the drop down menu
	public void setSelectedItem(int type, String item) {
		
		// This method should not call action performed
		callActionPerformed = false;
		
		if (type == GENERATION_LIST) {
			
			generationSelection.setSelectedItem(item);
			
		} else if (type == MAP_LIST) {
			
			mapSelection.setSelectedItem(item);
			
		}
		
		callActionPerformed = true;
		
	}

	// Adds a new GameObject to the graphics window
	public void addObject(GameObject r) {

		gameObjects.add(r);

		graphicsPane.repaint();

	}

	// Removes a game object from the graphics window
	public void removeObject(GameObject r) {

		GameObject remove = null;

		for (GameObject g : gameObjects) {
			// Loops to find the object

			if (r.equals(g)) {
				// Removes it if present

				remove = g;

			}

		}

		gameObjects.remove(remove);

		graphicsPane.repaint();

	}

	/*
	 * This method checks the collision of a GameObject with respect to another, and
	 * returns the type of collision according to the constants above.
	 */
	private int checkCollision(GameObject movingCollisionObject, GameObject collisionObject) {

		int collision = NO_COLLISION;

		if (collisionObject != null) {
			
			/*
			 * Direction is determined based on the previous frame and its location relative to the floor
			 * colliding with it
			 */
			if (collisionObject.doesCollide(movingCollisionObject) && collisionObject != movingCollisionObject) {

				/*
				 * If the object's right side's x coordinate collided but
				 * the previous frame's right side did not, assume a collision from the right
				 * side
				 */
				if (collisionObject.getIntX() <= movingCollisionObject.getIntX() + movingCollisionObject.getIntWidth()
						&& collisionObject.getIntX() >= movingCollisionObject.getPreviousFrame().getIntX()
								+ movingCollisionObject.getPreviousFrame().getWidth()) {

					collision = RIGHT_COLLISION;

					/*
					 * If the objects's left side's x coordinate collides and the
					 * previous frame's did not, assume a collision from the left side.
					 */
				} else if (collisionObject.getIntX() + collisionObject.getIntWidth() >= movingCollisionObject.getIntX()
						&& collisionObject.getIntX() + collisionObject.getIntWidth() <= movingCollisionObject
								.getPreviousFrame().getX()) {

					collision = LEFT_COLLISION;

					/*
					 * If the objects's top side's y coordinate collides, and the
					 * previous frame's did not, assume a collision from the top.
					 */
				} else if (collisionObject.getIntY() + collisionObject.getIntHeight() >= movingCollisionObject.getIntY()
						&& collisionObject.getIntY() + collisionObject.getIntHeight() <= movingCollisionObject
								.getPreviousFrame().getY()) {

					collision = TOP_COLLISION;

					/*
					 * If the objects bottom side's y coordinate collides, but the previous frame's
					 * y coordinate did not, then assume a collision from the bottom
					 */
				} else if (movingCollisionObject.getIntY() + movingCollisionObject.getIntHeight() >= collisionObject.getIntY()
						&& movingCollisionObject.getPreviousFrame().getIntY() + movingCollisionObject.getPreviousFrame().getIntHeight() <= collisionObject.getIntY()
						) {

					collision = BOTTOM_COLLISION;
					
				}

			}

		}

		return collision;

	}

	/*
	 * This sets the collision objects of the given GameObject.
	 */
	private void setCollisionObjects(GameObject c) {

		for (GameObject o : gameObjects) {

			// If colliding an object then add a collision object.
			if (o.doesCollide(c) && o != c) {

				c.collision(checkCollision(c, o), o);

			}

		}

	}

	/*
	 * Timer class that moves every object every tick. SPEED variable within
	 * determines the speed of the timer, as in how many milliseconds before its
	 * next tick
	 */
	private class Move implements ActionListener {

		public static final double SPEED = 13;
		private int timePassed;

		Timer innerTimer;

		// Creates fall timer and begins gravity on character
		public Move() {

			innerTimer = new Timer((int) SPEED, this);
			innerTimer.setInitialDelay(0);
			timePassed = 0;

		}
		
		// Starts the timer
		public void begin() {
			
			innerTimer.start();
			
		}
		
		// Stops the timer
		public void pause() {
			
			innerTimer.stop();
			
		}
		
		// Resets the timer
		public void resetTimer() {
			
			timePassed = 0;
			graphicsPane.setCountdownNumber((LearningManager.TIME_GIVEN - timePassed) / 1000);
			graphicsPane.repaint();
			
		}

		// Action Performed called every tick of the timer
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// Adds to the time passed.
			timePassed += SPEED;
			
			// Set the movement and scores of each instance every timer tick
			startLearning.setMovements(timePassed);
			startLearning.setAllScores();
			
			// Stops the timer if the full time has passed
			if (timePassed > LearningManager.TIME_GIVEN) {
				
				timePassed = 0;
				graphicsPane.setCountdownNumber((LearningManager.TIME_GIVEN - timePassed) / 1000);
				startLearning.nextGeneration();
				
				
			} else {
				
				// Sets the countdown number
				graphicsPane.setCountdownNumber((LearningManager.TIME_GIVEN - timePassed) / 1000);

				for (int i = 0; i < gameObjects.size(); i++) {

					// Moves the object
					gameObjects.get(i).movement();
					
					// Clears existing collisions
					gameObjects.get(i).clearCollisions();
					
					// Sets the new collision objects
					setCollisionObjects(gameObjects.get(i));

					// Sets the velocity for the following movement
					gameObjects.get(i).velocitySet();

				}
				
			}
			
			// Repaint the screen
			graphicsPane.repaint();
			
		}

	}

	// Starts the timer
	public void startTimer() {
		
		movementTimer.begin();
		
	}
	
	// Interacts with buttons and text boxes with the user interface
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Only call it if specified
		if (callActionPerformed) {
			
			String action = ((JComponent) e.getSource()).getName();
			
			// Exception catching for parsing and casting
			try {
				
				// If its one of the selection boxes
				if (e.getSource() instanceof JComboBox) {
					
					startButton.setEnabled(true);
					pauseButton.setEnabled(false);
					
					// Generation selection
					if (action.equals("generation")) {
						
						String selection = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
						
						// Changes the generation to the specified input
						if (selection.equals("Successful Character")) {
							
							startLearning.changeGeneration(LearningManager.FINAL_GENERATION_CHANGE);
							
						} else {
							
							startLearning.changeGeneration(Integer.parseInt(selection));
							
						}
						
						movementTimer.resetTimer();
						movementTimer.pause();
						
					// Map selection
					} else if (action.equals("map")) {
						
						String selection = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
						
						changeMap(Integer.parseInt(selection));
						
					}
					
				// Starts the game or resumes it
				} else if (action.equals("Start")) {
					
					movementTimer.begin();
					pauseButton.setEnabled(true);
					startButton.setEnabled(false);
					
				// Pauses the game
				} else if (action.equals("Pause")) {
						
					startButton.setEnabled(true);	
					pauseButton.setEnabled(false);
					movementTimer.pause();
					
				} else if (action.equals("Create New Map")) {
					
					// Opens the map creation program
					MapCreationWindow createMap = new MapCreationWindow(this);
					
				} else if (action.equals("Help")) {
					
					// Opens the help window
					HelpWindow getHelp = new HelpWindow(HelpWindow.GENERAL_PROGRAM_HELP);
					
				}
				
			// Print appropriate message if exception occurs
			} catch (Exception err) {
				
				System.out.println("Parse or cast error\nIn game_navigator/GameManager.");
				err.printStackTrace();
				
			}
			
		}

		
		

	}

}
