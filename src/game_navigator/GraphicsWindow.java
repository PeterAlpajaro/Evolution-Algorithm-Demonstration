	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  GraphicsWindow
	 * Programmer: Peter Alpajaro
	 * Date Created: 5/26/2022
	 * Description: Handles all graphics of the program. Has the array of GameObjects that it
	 * draws on the screen. Also has a countdown number which displays the time remaining 
	 * in each generation
	 */

package game_navigator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GraphicsWindow extends JPanel {

	// Layout of the window
	LayoutManager myLayout;
	
	// Font and variable for the count down number
	Font countdownFont;
	private int countdownNumber;

	// The objects to draw
	ArrayList<GameObject> objects;

	// Constructs the GraphicsWindow
	public GraphicsWindow(ArrayList<GameObject> o) {
		
		// Creates the JPanel
		super();
		
		// Gets the objects from the game
		objects = o;

		this.setBackground(Color.WHITE); 

		// Layout of this program is null
		myLayout = null;
		this.setLayout(myLayout);
		
		// Gets the initial countdown number and sets the font
		countdownNumber = LearningManager.TIME_GIVEN / 1000;
		countdownFont = new Font("Calibri", 40, 60);

	}
	
	// Updates the objects
	public void updateObjects(ArrayList<GameObject> o) {
		
		objects = o;
		
	}
	
	// Sets the number for the count down
	public void setCountdownNumber(int time) {
		
		countdownNumber = time;
		
	}
	  
	// Draws everything on the screen
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		// Loops through all the objects
		for (GameObject r : objects) {
			
			// Changes the color depending on the type of object drawn
			g.setColor(r.getColor());
			
			// Draws a rectangle representing the object
			g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
			
		}
		
		// This is the menu box in the top left
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 300, 200);
		
		// This is the countdown number
		g.setColor(Color.BLACK);
		g.setFont(countdownFont);
		g.drawString(Integer.toString(countdownNumber), GameManager.SCREEN_WIDTH - 200, 100);


	}

}
