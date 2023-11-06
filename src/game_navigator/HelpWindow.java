	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  HelpWindow
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/6/2022
	 * Description: This class opens a help window that displays information to the user about the program.
	 * Either the MapCreationProgram or the main program.
	 */

package game_navigator;

import java.awt.*;
import javax.swing.*;

public class HelpWindow extends JFrame {
	
	// Constants declaring types
	public static final int MAP_CREATION_HELP = 1;
	public static final int GENERAL_PROGRAM_HELP = 2;
	
	// variable representing the type of help to show
	private int helpType;
	
	// Creates the help window
	public HelpWindow(int type) {
		
		// Initializing the window
		super();
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		helpType = type;
		
		this.repaint();
		
	}
	
	// Draws on the screen
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		// Set font and color
		Font textFont = new Font("Heveltica", Font.PLAIN, 30);
		g.setFont(textFont);
		g.setColor(Color.BLACK);
		
		// What to display for the general program
		if (helpType == GENERAL_PROGRAM_HELP) {
			
			g.drawString("Welcome to the Learning Manager. This program is", 50, 80);
			g.drawString("Designed to showcase an evolutionary algorithm.", 50, 115);
			g.drawString("The program begins with a stage. There are then", 50, 150);
			g.drawString("characters which move across the stage. Each ", 50, 185);
			g.drawString("\"generation\" of a creature is mutated randomly", 50, 220);
			g.drawString("and the most successful character (one that made it", 50, 255);
			g.drawString("furthest) is mutated for the next generation. An", 50, 290);
			g.drawString("obstacle (red) will kill the creature, and the finish", 50, 325);
			g.drawString("(green) will finish the learning process and show the", 50, 360);
			g.drawString("sucessful character.", 50, 395);
			g.drawString("Designed by Peter Alpajaro on 6/27/2022", 50, 465);
			
			
		// What to display for the map creation program
		} else if (helpType == MAP_CREATION_HELP) {
			
			g.drawString("Welcome to the Map Creation Program. This program", 50, 80);
			g.drawString("allows you to create stages for the main program to", 50, 115);
			g.drawString("use. Click on one of the selections for objects, and", 50, 150);
			g.drawString("click the starting point of the object. Click again to", 50, 185);
			g.drawString("place the ending point of the object, and a rectangle", 50, 220);
			g.drawString("will form. The remove button will remove the object you", 50, 255);
			g.drawString("click on. The save button will save the map to the", 50, 290);
			g.drawString("number specified in the textbox. You cannot create", 50, 325);
			g.drawString("objects within the menu area, and the character starting", 50, 360);
			g.drawString("zone, specified by the boxes on the screen.", 50, 395);
			g.drawString("Remember, the harder the stage, the longer it will take", 50, 430);
			g.drawString("to learn. Try to avoid making impossible stages.", 50, 465);
			
			
		}
		
	}

}
