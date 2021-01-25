package driver;

public class MainDriver {

	public static void main(String[] args) {
		DirInitalizer di = new DirInitalizer();
		System.out.println(di.directoryExists("C:/Users/john_/Desktop/Frostburg"));
	}
	
	public static void menu() {
		System.out.println("");
	}
	
}
