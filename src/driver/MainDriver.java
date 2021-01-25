package driver;

public class MainDriver {

	public static void main(String[] args) {
		DirInitalizer di = new DirInitalizer();
		FileConverter fc = new FileConverter(di.getPreferences());
	}
	
	public static void menu() {
		System.out.println("");
	}
	
}
