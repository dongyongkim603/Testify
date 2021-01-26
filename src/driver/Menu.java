package driver;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Menu {

//	FileConverter fc = new FileConverter();
	boolean exit = false;
	DirInitalizer di;
	FileConverter fc;

	public Menu() {
		di = new DirInitalizer();
		fc = new FileConverter(DirInitalizer.getPreferences());
		fc.mapFiles(di.inputFolder);
		fc.printPreferences();
	}

//-------------------------- private methods --------------------------------

	private void mainMenu() {
		System.out.println("\nEnter one of the following\n" + "1 to convert files to file drop format");

	}

	/**
	 * takes in input from scanner System.in reads data as String. String is then
	 * parsed and wrapped as an integer which is then passed to choice
	 * 
	 * @return integer choice
	 */
	private int getInputInt() {
		int choice = -1; // create choice integer set to -1
		Scanner kb = new Scanner(System.in); // create new scanner object with system.in as parameter
		while (choice < 0 || choice > 13) {
			try // may throw exception if a non-Integer is passed in
			{
				System.out.println("\n enter number: ");
				choice = Integer.parseInt(kb.nextLine()); // takes in keyboard input and sets choice to the return input
			} catch (NumberFormatException e) // invalid format passed in scanner object
			{
				System.out.println("invalid selection try agian");
			}
		}
		return choice; // return choice int
	}

	/**
	 * takes in input from scanner System.in reads data as String.
	 * 
	 * @return integer choice
	 */
	private String getInputString() {
		String choice = ""; // initalize blank string
		Scanner kb = new Scanner(System.in); // create new scanner object with system.in as parameter
		while (choice.equals("")) // if choice is empty continue to call nextLine method
		{
			choice = kb.nextLine(); // takes in keyboard input from scanner
		}
		if (choice.equals("")) // checks to ensure the choice is a non-empty String
		{
			getInputString(); // recursively calls method if choice is empty
		}
		return choice; // return choice
	}

	/**
	 * takes in the user input and does switch comparison to see which methods to
	 * make a call to.
	 * 
	 * @param choice as an integer
	 */
	private void performAction(int choice) {
		switch (choice) {
		case 0:
			exit = true;
			System.out.print("case 0");
			break;
		case 1:
			Map<String, String> m = fc.formatFilesFileDrop(fc.getFileMap());
			m.forEach((k, v) ->{
				try {
					fc.stringToDom(v, DirInitalizer.getDefaultoutput() + "/"+ k);
					System.out.println("file " + k + " was successfully converted");
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage());
					e.printStackTrace();
				}
			});
			break;
		case 2:
			exit();
			break;
		case 9:
			exit();
			break;
		default:
			System.out.println("An unknown error has occurred");
			exit();
		}
	}

//---------------------------public methods----------------------------------------

	public void runMenu() {
		while (!exit) {
			mainMenu();
			int choice = getInputInt();
			performAction(choice);
		}
	}

	public void exit() {
		exit = true;
	}

}
