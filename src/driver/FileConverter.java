package driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FileConverter {

	final private static String BASE_FORMAT = "<labels _FORMAT=# _JOBNAME=# _QUANTITY=# _PRINTERNAME=#>\r\n"
			+ "   <label>\r\n" + "<variable name=#></variable>\r\n" + "   </label>\r\n" + "</labels>";
	final private static String START_DELIMINATOR = "__";
	final private static String END_DELIMINATOR = "**";

	private HashMap<String, String> preferenceList;
	private String preferences = "";

	// default constructor
	public FileConverter() {
		this(null);
	}

	// constructor that loads preferences
	public FileConverter(String preferencesPathString) {
		if (!preferencesPathString.isEmpty() || !preferencesPathString.equals(null)) {
			preferenceList = readFile(preferencesPathString);
		}
	}

//--------------------- public methods --------------------------------

	/**
	 * reads a txt file at a given path and produces a string of the contents of the
	 * file
	 * 
	 * @param filePathString the absolute path of the file
	 * @return a String of the file contents
	 */
	public HashMap<String, String> readFile(String filePathString) {
		try {
			File myObj = new File(filePathString);
			Scanner myReader = new Scanner(myObj);
			HashMap<String, String> contents = new HashMap<>();
			boolean currentFlag = false;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String key, value = "";
				// System.out.println(data);
				if (data.contains(START_DELIMINATOR)) {
					key = data.substring(data.indexOf(START_DELIMINATOR) + 1, data.indexOf(END_DELIMINATOR));
					value = data.substring(data.indexOf(END_DELIMINATOR) + 1, data.length());
					System.out.println(key + " " + value);
				} else {
					value += data;
					System.out.println(value);
				}
			}
			myReader.close();
			return contents;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred." + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
}
