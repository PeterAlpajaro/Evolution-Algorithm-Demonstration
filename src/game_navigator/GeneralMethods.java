	/*
	 * Project: Game Navigator
	 * Package: game_navigator
	 * Class:  GeneralMethods
	 * Programmer: Peter Alpajaro
	 * Date Created: 6/13/2022
	 * Description: Has static methods used by multiple classes in the project. Specifically file
	 * writing and reading.
	 */


package game_navigator;

import java.io.*;
import java.util.*;

public class GeneralMethods {
	
	// Types for file writing
	public static final int MAP_TYPE = 0;
	public static final int GENERATION_DATA_TYPE = 1;
	public static final int GENERAL_TYPE = 2;
	public static final int STARTING_TYPE = 3;
	
	// Reads the file and puts its contents into a 2d string array of its contents
	public static String[][] readFile(String fileName) {
		
		String[][] file = null;
		
		// Number parse error catch
		try {
			
			// File reading error catch
			try {
				
				// File readers
				FileReader fileRead = new FileReader(fileName);
				BufferedReader input = new BufferedReader(fileRead);
				
				// Number of objects
				int lineNumber = Integer.parseInt(input.readLine());
				// Number of attributes
				int attributeNumber = Integer.parseInt(input.readLine());
				
				file = new String[lineNumber][attributeNumber];
				
				String line;
				
				for (int i = 0; i < file.length; i++) {
					
					// Get the next line and split it by spaces
					line = input.readLine();
					StringTokenizer lineSplit = new StringTokenizer(line);
					
					// Loop through and fill the 2d array with the line's contents.
					for (int j = 0; j < attributeNumber; j++) {
						
						file[i][j] = lineSplit.nextToken().trim();
						
					}
					
				}
				
				// Close reader
				input.close();
				
			// File is missing
			} catch (IOException err) {
				
				// If the missing file is starting_document.txt, create the document and return the new document.
				if (fileName.equals("final_files/starting_document.txt")) {
					
					writeToFile(new String[][] {{"0"}}, STARTING_TYPE, 0, 0);
					file = readFile(fileName);
					
				// Otherwise print an error
				} else {

					System.out.println("IO Error in " + fileName);
					err.printStackTrace();
					
				}
				
				
			}
		
		// Parse error
		} catch (NumberFormatException e) {

			System.out.println("Parse / Formar Error in " + fileName);
			e.printStackTrace();
			
		}
		
		return file;
		
	}

	// Creates a new file and writes the contents of a 2d string array into it
	public static void writeToFile(String[][] content, int type, int accessNumber, int mapNumber) {
		
		try {
			
			String fileName = null;
			boolean writeGrid = false;
			
			// Chooses the appropriate name for the file depending on the type
			if (type == MAP_TYPE) {
				
				fileName = "final_files/maps/map" + mapNumber + "/map.txt";
				
				// If the directory is missing make the general information file
				if (new File("final_files/maps/map" + mapNumber).mkdirs()) {
					
					writeToFile(new String[][] {{Integer.toString(0), "false"}}, GENERAL_TYPE, 0, mapNumber);
					
				}
				
				writeGrid = true;
				
			} else if (type == GENERATION_DATA_TYPE) {
				
				fileName = "final_files/maps/map" + mapNumber + "/character_data/generation" + accessNumber + ".txt";
				
				// Creates the generation folder if it has not been created yet.
				new File("final_files/maps/map" + mapNumber + "/character_data").mkdirs();
				
				writeGrid = true;
				
			} else if (type == GENERAL_TYPE) {
				
				fileName = "final_files/maps/map" + mapNumber + "/general_information.txt";
				
			} else if (type == STARTING_TYPE) {
				
				fileName = "final_files/starting_document.txt";
				
				// Creates the initial folder if it has not been created yet
				new File("final_files").mkdirs();
				
				writeGrid = true;
				
			}

			// If data is to be written in a grid layout...
			if (writeGrid) {
				
				// Create the file and file writer
				FileWriter fileWriter = new FileWriter(fileName);
				PrintWriter output = new PrintWriter(fileWriter);
				
				// Recording the length and width of the file for reading purposes
				output.println(content.length);
				output.println(content[0].length);
				
				String line = "";
				
				// Loop through the content
				for (int i = 0; i < content.length; i++) {
					
					for (int j = 0; j < content[0].length; j++) {
						
						// Add the content and a space in between
						line += content[i][j] + " ";
						
					}
					
					// Prints that string to the file
					output.println(line);
					
					line = "";
					
				}
				
				// Close writer
				output.close();
				
			} else {
				
				// Create the file and file writer
				FileWriter fileWriter = new FileWriter(fileName);
				PrintWriter output = new PrintWriter(fileWriter);
				
				// Print the generation number
				output.println(content[0][0]);
				
				// Print the completion representation
				output.println(content[0][1]);
				
				// Print the final instance if the program is complete
				if (content[0][1].equals("true")) {
					
					output.println(content[0][2]);
					
				}
				
				// Close the writer
				output.close();
				
			}
			
			
		// Print appropriate message in case of error
		} catch (Exception e) {

			System.out.println("File write/format error.\nIn GeneralMethods.java");
			e.printStackTrace();
			
		}
		
	}
	
	
	
}
