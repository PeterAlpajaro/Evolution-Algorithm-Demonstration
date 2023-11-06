	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  MapCreationMain
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/23/2022
	 * Description: This class opens a separate map program that allows the user to create
	 * new maps that the evolutionary algorithm can learn to navigate.
	 */

package game_navigator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MapCreationWindow extends JPanel implements MouseListener, ActionListener {

	// Constants representing the selection by the user
	public static final int NO_SELECTION = 1;
	public static final int DELETE_SELECTION = 2;
	public static final int ADD_SELECTION = 3;
	
	// The selection mode and type
	private int selection;
	private String selectionType;
	
	// Mouse locations
	private Point pointOne;
	private Point pointTwo;
	private boolean selectingFirstPoint;
	
	// Rectangles representing areas that cannot be created inside
	private Rectangle startingZone, menu;
	
	// List of created objects in String form
	private LinkedList<String[]> gameObjectsRep = new LinkedList<String[]>();
	
	// Reference to the game
	private GameManager game;
	
	// User interface components
	private JButton remove, obstacle, floor, finishzone, save, helpButton;
	private JTextField name;
	
	// What map file to save to
	private int mapSaveNumber;
	
	// Layout of the panel
	private LayoutManager creationLayout;
	
	// Creates the window
	public MapCreationWindow(GameManager g) {
		
		// Creates the JPanel
		super();
		
		// No layout
		creationLayout = null;
		this.setLayout(creationLayout);
		
		game = g;
		
		// Parse exception catching
		try {
			
			// Current map to save to
			mapSaveNumber = Integer.parseInt(GeneralMethods.readFile("final_files/starting_document.txt")[0][0]) + 1;
			
		} catch (NumberFormatException err) {
			
			System.out.println("Parse / File Format error\nIn final_files/MapCreationWindow");
			err.printStackTrace();
			
		}
		
		// Images for the buttons
		ImageIcon obstacleIcon = new ImageIcon("final_files/images/obstacle_icon.jpg");
		ImageIcon floorIcon = new ImageIcon("final_files/images/floor_icon.jpg");
		ImageIcon finishIcon = new ImageIcon("final_files/images/finish_icon.jpg");
		ImageIcon removeIcon = new ImageIcon("final_files/images/remove_icon.jpg");
		ImageIcon saveIcon = new ImageIcon("final_files/images/save_icon.jpg");
		ImageIcon helpIcon = new ImageIcon("final_files/images/help_icon.jpg");
		
		// Obstacle button
		obstacle = new JButton(obstacleIcon);
		obstacle.setName("obstacle");
		obstacle.setBounds(10, 10, 50, 50);
		obstacle.addActionListener(this);
		
		// Floor button
		floor = new JButton(floorIcon);
		floor.setName("floor");
		floor.setBounds(70, 10, 50, 50);
		floor.addActionListener(this);
		
		// Finish zone button
		finishzone = new JButton(finishIcon);
		finishzone.setName("finishzone");
		finishzone.setBounds(130, 10, 50, 50);
		finishzone.addActionListener(this);
		
		// Remove Object button
		remove = new JButton(removeIcon);
		remove.setName("remove");
		remove.setBounds(190, 10, 50, 50);
		remove.addActionListener(this);
		
		// Button for file saving
		save = new JButton(saveIcon);
		save.setName("save");
		save.setBounds(10, 70, 50, 50);
		save.addActionListener(this);
		
		// Name for file saving
		name = new JTextField(15);
		name.setBounds(70, 70, 150, 30);
		name.setEditable(false);
		name.setText("Saving to map number " + mapSaveNumber);
		
		// Help button
		helpButton = new JButton(helpIcon);
		helpButton.setName("Help");
		helpButton.setBounds(210, 160, 70, 30);
		helpButton.addActionListener(this);
		
		// Adding elements to the panel
		this.add(obstacle);
		this.add(floor);
		this.add(finishzone);
		this.add(remove);
		this.add(save);
		this.add(name);
		this.add(helpButton);
		
		// Creating the JFrame
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT);
		window.setVisible(true);
		window.add(this);
		
		// Start with no selection and selecting the first point of the rectangle
		selection = NO_SELECTION;
		selectingFirstPoint = true;
		
		this.addMouseListener(this);
		
		pointOne = new Point(0, 0);
		pointTwo = new Point(0, 0);
		
		// Rectangles representing the start and the menu. Cannot draw objects there
		startingZone = new Rectangle(Map.STARTING_X - 10, Map.STARTING_Y - 10, 130, 200);
		menu = new Rectangle(0, 0, 300, 200);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		// Mouse location
		Point tempPoint = new Point(e.getX(), e.getY());
		
		// If placing an object
		if (selection == ADD_SELECTION) {
			
			// If placing the first point
			if (selectingFirstPoint) {
				
				this.pointOne = tempPoint;
				selectingFirstPoint = false;
				
			} else {
				
				this.pointTwo = tempPoint;
				selectingFirstPoint = true;
				
				// Calculating the dimensions of the shape from the points
				int x = (int) Math.min(this.pointOne.getX(), this.pointTwo.getX());
				int y = (int) Math.min(this.pointOne.getY(), this.pointTwo.getY());
				int width = (int) Math.abs(this.pointOne.getX() - this.pointTwo.getX());
				int height = (int) Math.abs(this.pointOne.getY() - this.pointTwo.getY());
				
				// Don't draw the shape if it intersects the menu or the starting location
				if (new Rectangle(x, y, width, height).intersects(startingZone) || new Rectangle(x, y, width, height).intersects(menu)) {
					
					pointTwo = null;
					selectingFirstPoint = false;
					
				} else {
						
					pointOne = null;
					pointTwo = null;
					gameObjectsRep.add(createDrawnObject(x, y, width, height));
					
				}

				
			}
			
		// If removing an object
		} else if (selection == DELETE_SELECTION) {
			
			String[] removal = null;
			
			// Loop through the objects
			for (String[] s : gameObjectsRep) {
				
				try {
					
					// Removes the object if the user clicks on it
					Rectangle objectCreated = new Rectangle(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));
					if (objectCreated.contains(tempPoint)) {
						
						removal = s;
									
					}

				// Parse exception catching
				} catch (NumberFormatException err) {
					
					System.out.println("Parse / Format error\nIn final_files/MapCreationWindow");
					err.printStackTrace();
					
				}
				
			}
			
			// Removes the specified shape from the list
			gameObjectsRep.remove(removal);
			
		}
		
		repaint();
		
	}
	
	// Returns an object from the specified points
	private String[] createDrawnObject(int x, int y, int width, int height) {
		
		String[] createdObject = new String[5];
		
		// String representation of the attributes
		createdObject = new String[] {Integer.toString(x), Integer.toString(y), Integer.toString(width), Integer.toString(height), selectionType};
		
		return createdObject;
		
	}
	
	// Saves the map to a file
	private void save() {
	
		// Get objects into a 2d string array.
		String[][] content = new String[gameObjectsRep.size()][5];
		
		for (int i = 0; i < content.length; i++) {
			
			content[i] = gameObjectsRep.get(i);
			
		}
		
		// Writes this to a file, and sets the new save.
		GeneralMethods.writeToFile(content, GeneralMethods.MAP_TYPE, 0, mapSaveNumber);
		GeneralMethods.writeToFile(new String[][] {{Integer.toString(mapSaveNumber)}}, GeneralMethods.STARTING_TYPE, 0, 0);
		game.addToList(GameManager.MAP_LIST, Integer.toString(mapSaveNumber));
		
		mapSaveNumber++;
		name.setText("Saving to map number " + mapSaveNumber);

	}

	
	// Necessary implementation methods {
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// }
	
	// Button actions
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Gets button name
		String buttonName = ((JComponent) e.getSource()).getName();
		
		if (buttonName.equals("save")) {
			
			save();
			
		} else if (buttonName.equals("Help")) {
				
			// Opens the help window for the subprogram
			HelpWindow openHelp = new HelpWindow(HelpWindow.MAP_CREATION_HELP);
				
		} else {
			
			// Reset points
			pointOne = null;
			pointTwo = null;
			
			selectingFirstPoint = true;
			
			// Get the selection type
			if (buttonName.equals("remove")) {
				
				selection = DELETE_SELECTION;
				
			} else {
				
				selection = ADD_SELECTION;
				selectionType = buttonName;
				
			}
			
		}
		

		
		
	}
	
	// Visuals
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		// Menu Rectangle
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 300, 200);
		
		// Starting zone rectangle
		g.setColor(Color.CYAN);
		g.fillRect((int) startingZone.getX(), (int) startingZone.getY(), (int) startingZone.getWidth(), (int) startingZone.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect((int) startingZone.getX(), (int) startingZone.getY(), (int) startingZone.getWidth(), (int) startingZone.getHeight());
		
		g.setColor(Color.RED);
		
		// Fills the points in. Point one
		if (pointOne != null) {
			
			g.fillOval((int) pointOne.getX(), (int) pointOne.getY(), 10, 10);
			
		}
		
		// Point two
		if (pointTwo != null) {

			g.fillOval((int) pointTwo.getX(), (int) pointTwo.getY(), 10, 10);
			
		}
		

		// Loops through and draws all the created objects
		for (String[] s :  gameObjectsRep) {
			
			// Changes the color depending on the type of object
			if (s[4].equals("obstacle")) {
				
				g.setColor(Color.RED);
				
			} else if (s[4].equals("floor")) {
				
				g.setColor(Color.BLACK);
				
			} else if (s[4].equals("finishzone")) {
				
				g.setColor(Color.GREEN);
				
			}
			
			// Parse exception handling
			try {
				
				g.fillRect(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));
				
			} catch (Exception err) {
				
				System.out.println("File format error\nIn final_files/MapCreationWindow");
				err.printStackTrace();
				
			}
			
		}
		
	}
	
}
