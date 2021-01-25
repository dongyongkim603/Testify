package driver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirInitalizer {

	String inputFolder = "";
	String outputFolder = "";
	
	public DirInitalizer() {}
	public DirInitalizer(String output, String input) {
		this.inputFolder = input;
		this.outputFolder = output;
	}
	
//---------------- public methods ------------------------
	
	public boolean directoryExists(String dirPath) {
		Path path = Paths.get(dirPath);
		if(Files.exists(path)) 
			return true;
		else
			return false;
	}
}
