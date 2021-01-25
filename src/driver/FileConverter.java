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
			preferenceList = mapPreferences(preferencesPathString);
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
	public HashMap<String, String> mapPreferences(String filePathString) {
		try {
			File file = new File(filePathString);
			Scanner myReader = new Scanner(file);
			HashMap<String, String> contents = new HashMap<>();
			
			//flag used to skip initial put method since K,V are empty
			boolean firstFlag = true;
			String key = "" , value = "";
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				// System.out.println(data);
				//checks for the __ delimeter signifying option
				if (data.contains(START_DELIMINATOR)) {
					if(!firstFlag) {
						//does a post-put before re-initializing K,V
						contents.put(key, value);
						System.out.println("key: " + key + " value: " + value);
					}
					value = "";
					key = "";
					key = data.substring(data.indexOf(START_DELIMINATOR) + 1, data.indexOf(END_DELIMINATOR));
					
					//initial value captured, if there is more data to this option it is caught in the else
					value = data.substring(data.indexOf(END_DELIMINATOR) + 1, data.length());
					firstFlag = false;
				} else {
					value += data;
				}
			}
			contents.put(key, value);
			System.out.println("key: " + key + " value: " + value);
			myReader.close();
			return contents;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred." + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
}
