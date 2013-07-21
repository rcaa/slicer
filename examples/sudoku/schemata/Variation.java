package sudoku.schemata;

public class Variation {

	public enum Feature {
		BASE, COLOR, EXTENDEDSUDOKU, GENERATOR, SOLVER, STATES, UNDO
	};
	
	public static boolean BASE = true;
	public static boolean COLOR = true;
	public static boolean EXTENDEDSUDOKU = true;
	public static boolean GENERATOR = true;
	public static boolean SOLVER = true;
	public static boolean STATES = true;
	public static boolean UNDO = true;
	
	
	public static void set(Feature feature) {
		switch (feature) {
		case BASE:
			BASE = true;
			break;
		case COLOR:
			COLOR = true;
			break;
		case EXTENDEDSUDOKU:
			EXTENDEDSUDOKU = true;
			break;
		case GENERATOR:
			GENERATOR = true;
			break;
		case SOLVER:
			SOLVER = true;
		case STATES:
			STATES = true;
		case UNDO:
			UNDO = true;
			break;
		default:
			throw new RuntimeException("Add new feature to Variation");
		}
	}

	/**
	 * Turns all the values of the variations to false. 
	 */
	public static void reset() {
		boolean BASE = false;
		boolean COLOR = false;
		boolean EXTENDEDSUDOKU = false;
		boolean GENERATOR = false;
		boolean SOLVER = false;
		boolean STATES = false;
		boolean UNDO = false;
	}
	
}
