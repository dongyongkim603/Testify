package driver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirInitalizer {

	// Default paths for directories
	final private static String HOME = "C:/Testify";
	final private static String PREFERENCES = "C:/Testify/Preferences.txt";
	final private static String DEFAULT_INPUT = "C:/Testify/_Input";
	final private static String DEFAULT_OUTPUT = "C:/Testify/_Output";
	final private static String PREFERENCE_FIELDS = "__FORMAT**\n__JOBNAME**\n__PRINTERNAME**\n__QUANTITY**\n";

	// Dynamic paths for directories TODO: add custom path setting ability
	String inputFolder = "";
	String outputFolder = "";
	String homeFolder = "";

	// constructors
	public DirInitalizer() {
		this(DEFAULT_OUTPUT, DEFAULT_INPUT);
	}

	public DirInitalizer(String output, String input) {
		this(output, input, HOME);
	}

	public DirInitalizer(String output, String input, String home) {
		this.inputFolder = input;
		this.outputFolder = output;
		this.homeFolder = home;
		initializeHome(homeFolder);
		initializeOutput(output);
		initializeInput(input);
		initializePreferences();
	}

//---------------- private methods --------------------------

	/**
	 * Will create the home directory if one does not already exist
	 */
	private void initializeHome(String home) {
		if (!directoryExists(home))
			createDir(home);
	}

	/**
	 * Creates a Preferences file in the home directory if there is none present
	 */
	private void initializePreferences() {
		if (!fileExists(PREFERENCES)) {
			createFile(PREFERENCES);
			writeToFile(PREFERENCES, PREFERENCE_FIELDS);
		}
	}

	/**
	 * Creates the Output directory. TODO: create a way to persist the path of a
	 * custom directory.
	 */
	private void initializeOutput(String outputDir) {
		if (!directoryExists(outputDir))
			createDir(outputDir);
		else
			return;

	}

	/**
	 * Creates the input directory. TODO: create a way to persist the path of a
	 * custom directory.
	 */
	private void initializeInput(String inputDir) {
		if (!directoryExists(inputDir))
			createDir(inputDir);
		else
			return;

	}

//--------------- protected methods --------------------------

	/**
	 * allows for the modification of the prefernces file with custom options TODO:
	 * finish logic
	 * 
	 * @return true if file was successfully changed
	 */
	protected boolean modifyPreferences() {
		return false;
	}

//---------------- public methods ----------------------------

	/**
	 * Checks given path to see if the directory exists
	 * 
	 * @param dirPath
	 * @return true if the path is an existing directory
	 */
	public boolean directoryExists(String dirPath) {
		Path path = Paths.get(dirPath);
		if (Files.exists(path))
			return true;
		else
			return false;
	}

	/**
	 * Creates a folder directory at the given String path
	 * 
	 * @param filePathString
	 */
	public void createDir(String filePathString) {
		try {
			Path path = Paths.get(filePathString);
			Files.createDirectory(path);
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Checks the path of the String to see if a file exists
	 * 
	 * @param filePathString the full path of the file
	 * @return true if a file exists
	 */
	public boolean fileExists(String filePathString) {
		File f = new File(filePathString);
		if (f.exists() && !f.isDirectory())
			return true;
		else
			return false;

	}

	/**
	 * Creates a file at a given location
	 * 
	 * @param filePathString
	 */
	public void createFile(String filePathString) {
		try {
			File file = new File(filePathString);
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/**
	 * Takes a full path and write the contents to the target file
	 * 
	 * @param filePathString the full path of the target file
	 * @param contents       the contents that is to be written
	 */
	public void writeToFile(String filePathString, String contents) {
		try {
			FileWriter myWriter = new FileWriter(filePathString);
			myWriter.write(contents);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

//--------------------- getters and setters --------------------------

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getHomeFolder() {
		return homeFolder;
	}

	public void setHomeFolder(String homeFolder) {
		this.homeFolder = homeFolder;
	}

	public static String getHome() {
		return HOME;
	}

	public static String getPreferences() {
		return PREFERENCES;
	}

	public static String getDefaultinput() {
		return DEFAULT_INPUT;
	}

	public static String getDefaultoutput() {
		return DEFAULT_OUTPUT;
	}

	public static String getPreferencefields() {
		return PREFERENCE_FIELDS;
	}
}
