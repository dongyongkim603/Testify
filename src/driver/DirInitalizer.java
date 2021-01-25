package driver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirInitalizer {

	// Default paths for directories
	final private static String DEFAULTINPUT = "C:\\Testify_Input";
	final private static String DEFAULTOUTPUT = "C:\\Testify_Output";

	// Dynamic paths for directories
	String inputFolder = "";
	String outputFolder = "";

	// constructors
	public DirInitalizer() {
	}

	public DirInitalizer(String output, String input) {
		this.inputFolder = input;
		this.outputFolder = output;
	}

//---------------- public methods ------------------------

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
	 * Creates the input directory. 
	 * TODO: create a way to persist the path of a custom directory.
	 */
	public void initializeInput() {
		if (!inputFolder.isEmpty()) {
			if (!directoryExists(inputFolder)) {
				try {
					Path path = Paths.get(inputFolder);
					Files.createDirectory(path);
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage()); 
				}

			} else {
				return;
			}
		} else {
			if (!directoryExists(DEFAULTINPUT)) {
				try {
					Path path = Paths.get(DEFAULTINPUT);
					Files.createDirectory(path);
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage()); 
				}
			} else {
				return;
			}
		}
	}
	
	/**
	 * Creates the Output directory. 
	 * TODO: create a way to persist the path of a custom directory.
	 */
	public void initializeOutput() {
		if (!inputFolder.isEmpty()) {
			if (!directoryExists(outputFolder)) {
				try {
					Path path = Paths.get(outputFolder);
					Files.createDirectory(path);
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage()); 
				}

			} else {
				return;
			}
		} else {
			if (!directoryExists(DEFAULTOUTPUT)) {
				try {
					Path path = Paths.get(DEFAULTOUTPUT);
					Files.createDirectory(path);
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage()); 
				}
			} else {
				return;
			}
		}
	}
}
