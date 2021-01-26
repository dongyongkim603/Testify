package driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileConverter {

	final private static String BASE_FORMAT = "<labels _FORMAT= _JOBNAME= _QUANTITY= _PRINTERNAME=>\r\n"
			+ "   <label>\r\n";
	final private static String VARIABLE = "<variable name=></variable>\r\n";
	final private static String END_TAGS = "   </label>\r\n" + "</labels>";
	final private static String START_DELIMINATOR = "__";
	final private static String END_DELIMINATOR = "**";
	final private static String SOAP_HEADER = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pub=\"http://published.webservices.loftware.com/\">\r\n"
			+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n" + "      <pub:submitJob>\r\n"
			+ "         <!--Optional:-->\r\n" + "         <jobFile><![CDATA[\n";
	final private static String SOAP_FOOTER = "]]>\r\n" + "</jobFile>\r\n" + "         <!--Optional:-->\r\n"
			+ "         <jobType>LXML</jobType>\r\n" + "      </pub:submitJob>\r\n" + "   </soapenv:Body>\r\n"
			+ "</soapenv:Envelope>\r\n";

	private HashMap<String, String> preferenceMap;
	private HashMap<String, File> fileMap;

	// default constructor
	public FileConverter() {
		this(null);
	}

	// constructor that loads preferences
	public FileConverter(String preferencesPathString) {
		if (!preferencesPathString.isEmpty() || !preferencesPathString.equals(null)) {
			preferenceMap = mapPreferences(preferencesPathString);
			fileMap = mapFiles(DirInitalizer.getDefaultinput());
		}
	}

//--------------------- private methods --------------------------------

	/**
	 * Finds the preferences file and maps the option types with their corresponding
	 * arguments
	 * 
	 * @param filePathString the absolute path of the file
	 * @return a HashMap of the options and arguments
	 */
	private HashMap<String, String> mapPreferences(String filePathString) {
		try {
			File file = new File(filePathString);
			Scanner myReader = new Scanner(file);
			HashMap<String, String> contents = new HashMap<>();

			// flag used to skip initial put method since K,V are empty
			boolean firstFlag = true;
			String key = "", value = "";
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				// System.out.println(data);
				// checks for the __ delimeter signifying option
				if (data.contains(START_DELIMINATOR)) {
					if (!firstFlag) {
						// does a post-put before re-initializing K,V
						contents.put(key, value);
						System.out.println("key: " + key + " value: " + value);
					}
					value = "";
					key = "";
					key = data.substring(data.indexOf(START_DELIMINATOR) + 2, data.indexOf(END_DELIMINATOR));

					// initial value captured, if there is more data to this option it is caught in
					// the else
					value = data.substring(data.indexOf(END_DELIMINATOR) + 2, data.length());
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
			System.out.println("An error occurred. " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}

//---------------------- public methods----------------------------------

	/**
	 * Reads a file and creates a string of the contents of the file
	 * 
	 * @param filePathString the absolute path of the string
	 * @return the contents of the file as a string
	 */
	public String readFileContents(String filePathString) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(filePathString);
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				sb.append(data);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred. " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Lists the files in the target folder and uses the full path to create files
	 * which are then mapped to memory
	 * 
	 * @param inputFolder the parent target directory of files
	 */
	public HashMap<String, File> mapFiles(String inputFolder) {
		HashMap<String, File> fileMap = new HashMap<>();

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;
		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(inputFolder);

		// Populates the array with names of files and directories
		pathnames = f.list();

		// For each pathname in the pathnames array
		for (String pathname : pathnames) {
			// Print the names of files and directories
			System.out.println(pathname);
			fileMap.put(pathname, (new File(readFileContents(inputFolder + "/" + pathname))));
		}
		return fileMap;
	}

	/**
	 * prints the contents of a specific key in the file map
	 * 
	 * @param key
	 */
	public void printFileFromMap(String key) {
		File file = fileMap.get(key);
		System.out.println("The contents of the file " + key + " are:\n" + file.toString());
	}

	/**
	 * gets the content
	 * 
	 * @param key
	 * @return
	 */
	public String fileFromMapToString(String key) {
		File file = fileMap.get(key);
		return file.toString();
	}

	/**
	 * prints out all of the keys in the file map
	 */
	public void printKeys() {
		System.out.println("The files in the input folder are:\n" + fileMap.keySet());
	}

	public void printMap(Map<String, String> map) {
		map.forEach((key, value) -> System.out.println(key + " : " + value));
	}

	public void printPreferences() {
		preferenceMap.forEach((key, value) -> System.out.println(key + ":" + value));
	}

	public Map<String, String> formatFilesFileDrop(Map<String, File> files) {
		Map<String, String> fDMap = new HashMap<>();
		ArrayList<String> pList = new ArrayList<>();
		String baseForm = BASE_FORMAT;
		StringBuilder sb = new StringBuilder();
		int prevKey = 0;
		preferenceMap.forEach((key, value) -> {

//			if (baseForm.contains(key)) {
//				sb.append(baseForm.substring(prevKey, baseForm.indexOf("=")));
//			}
			pList.add(key + "=" + value);
			files.forEach((k, v) -> {
				String data = fileFromMapToString(k);
				int index = data.indexOf("_" + key);
				sb.append(data.substring(data.indexOf("<labels"), (data.indexOf("<\\labels>"))));

				String updated = formatSubstitution(sb.toString(), k.replace("_Spectrum.txt", ""));
				updated = jobnameSubstitution(updated);
				updated = printernameSubstitution(updated);

				System.out.println("\n" + updated);
				fDMap.put(k, updated);
			});
		});
		printMap(fDMap);

		return fDMap;
	}

	/**
	 * Takes in a xml string and splits the string at the closing angle bracket ">".
	 * Then builds the string by appending it to a StringBuilder while also
	 * appending the "\n" escape character TODO: fix regex
	 * 
	 * @param xmlString the XML String
	 * @return the formated xml String
	 */
	public String xmlFormater(String xmlString) {
		StringBuilder sb = new StringBuilder();
		String[] split = xmlString.split("\\.>");
		for (String s : split) {
			sb.append(s + "\n");
		}
		System.out.println(sb);
		return sb.toString();
	}

	/**
	 * finds the format variable in the Xml label header and produces a new string
	 * with that replaces the format with the preferences format
	 * 
	 * @param xmlString a string of the xml file
	 * @return an updated xml with new format variable in the header
	 */
	public String formatSubstitution(String xmlString, String key) {
		String format = xmlString.substring(xmlString.indexOf("_FORMAT="), xmlString.indexOf("_JOBNAME="));
		String formatArgument = format.substring("_FORMAT=".length(), format.length());
		String updatedForamt = xmlString.replace(formatArgument, "\"" +preferenceMap.get("FORMAT") + key + "\" ");
		System.out.println("here is the updated format:\n" + updatedForamt + "\n");
		return updatedForamt;
	}

	/**
	 * finds the jobname variable in the Xml label header and produces a new string
	 * with that replaces the jobname with the preferences format
	 * 
	 * @param xmlString a string of the xml file
	 * @return an updated xml with new jobname variable in the header
	 */
	public String jobnameSubstitution(String xmlString) {
		String format = xmlString.substring(xmlString.indexOf("_JOBNAME="), xmlString.indexOf("_PRINTERNAME="));
		String formatArgument = format.substring("_JOBNAME=".length(), format.length());
		String updatedForamt = xmlString.replace(formatArgument, "\"" +preferenceMap.get("JOBNAME") + "\" ");
		System.out.println("here is the updated format:\n" + updatedForamt + "\n");
		return updatedForamt;
	}

	/**
	 * finds the printername variable in the Xml label header and produces a new
	 * string with that replaces the printername with the preferences format
	 * 
	 * @param xmlString a string of the xml file
	 * @return an updated xml with new printername variable in the header
	 */
	public String printernameSubstitution(String xmlString) {
		String format = xmlString.substring(xmlString.indexOf("_PRINTERNAME="), xmlString.indexOf("\">"));
		String formatArgument = format.substring("_PRINTERNAME=".length(), format.length());
		String updatedForamt = xmlString.replace(formatArgument, "\"" +preferenceMap.get("PRINTERNAME"));
		System.out.println("here is the updated format:\n" + updatedForamt + "\n");
		return updatedForamt;
	}

	/**
	 * takes in a string and writes it to an XML file. Will replace .txt extension
	 * therefore must be given an argument of this type of extension
	 * 
	 * @param xmlSource the String of data to be written
	 * @param pathName  the String of the full path of where file is to be written
	 * @throws IOException if file is unable to be written
	 */
	public void stringToDom(String xmlSource, String pathName) throws IOException {
		java.io.FileWriter fw = new java.io.FileWriter(pathName.replace(".txt", ".xml"));
		fw.write(xmlSource);
		fw.close();
	}

//-------------------------getters and setters -----------------------------------

	public HashMap<String, String> getPreferenceMap() {
		return preferenceMap;
	}

	public void setPreferenceList(HashMap<String, String> preferenceList) {
		this.preferenceMap = preferenceList;
	}

	public HashMap<String, File> getFileMap() {
		return fileMap;
	}

	public void setFileMap(HashMap<String, File> fileMap) {
		this.fileMap = fileMap;
	}

	public static String getBaseFormat() {
		return BASE_FORMAT;
	}

	public static String getStartDeliminator() {
		return START_DELIMINATOR;
	}

	public static String getEndDeliminator() {
		return END_DELIMINATOR;
	}
}// end of class
