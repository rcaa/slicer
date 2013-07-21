package sudoku.main.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sudoku.main.Structure;
import sudoku.main.SudokuFacade;
import sudoku.schemata.Variation;
import sudoku.schemata.Variation.Feature;

public class TestAll {

	public static boolean doCoverage = false;// NAO COMITAR COM TRUE

	private static String treatPath(String path) {
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		return path;
	}

	private static String projectPath;
	private static List<String> features = new ArrayList<String>();

	/**
	 * @param args
	 *            - number of the test - product = enabled features
	 */
	public static void main(String[] args) {
		String start = "!START_SLICING!";
		int test = Integer.parseInt(args[0]);
		projectPath = treatPath(args[1]);

		// enabling features and setting test input
		for (int i = 2; i < args.length; i++) {
			String feature = args[i];
			features.add(feature);
			Variation.set(Feature.valueOf(feature));
		}

		SudokuFacade sf = new SudokuFacade();

		if (doCoverage) {
			System.out
					.println(" >>>>>>>>>> Fazendo a cobertura <<<<<<<<<<<<<<<");
			test1(sf);
			test2(sf);
			test3(sf);
			test4(sf);
			System.exit(0);
		}

		// running tests with the input
		switch (test) {
		case 1:
			test1(sf);
			break;
		case 2:
			test2(sf);
			break;
		case 3:
			test3(sf);
			break;
		case 4:
			test4(sf);
			break;
		default:
			throw new RuntimeException("This test does not exist");
		}
	}

	public static void test1(SudokuFacade sf) {
		if (Variation.BASE && Variation.SOLVER && Variation.STATES
				&& Variation.UNDO) {
			sf.loadFile(new File(
					"/Users/sabrinasouto/svn/spl/FSlicer/Sudoku/src-tests/inputTest1.txt"));
			sf.solutionHint();
			sf.SaveState(new File(
					"/Users/sabrinasouto/svn/spl/FSlicer/Sudoku/src-tests/state1.txt"));
			sf.LoadState(new File(
					"/Users/sabrinasouto/svn/spl/FSlicer/Sudoku/src-tests/state1.txt"));
			System.out.println("test1");
		}
	}

	public static void test2(SudokuFacade sf) {
		if (Variation.BASE && Variation.EXTENDEDSUDOKU && Variation.SOLVER) {
			sf.setPossibilities(16);
			sf.setField(Structure.BOX, 2, 4, 1);
			sf.setField(Structure.BOX, 1, 2, 4);
			sf.setField(Structure.BOX, 0, 0, 1);
			sf.setField(Structure.BOX, 3, 3, 3);
			System.out.println("test2");
		}
	}

	public static void test3(SudokuFacade sf) {
		if (Variation.BASE && Variation.UNDO && Variation.STATES
				&& Variation.SOLVER) {
			sf.setField(Structure.BOX, 2, 4, 1);
			sf.setField(Structure.BOX, 1, 2, 4);
			sf.undo();
			sf.setField(Structure.BOX, 0, 0, 1);
			// sf.setField(Structure.BOX, 8, 8, 8);
			sf.undo();
			System.out.println("test3");
		}
	}

	public static void test4(SudokuFacade sf) {
		if ((features.contains("BASE")) && (features.contains("GENERATOR"))
				&& (features.contains("SOLVER")) && (features.contains("UNDO"))
				&& (features.contains("STATES"))) {
			sf.GenerateSudoku();
			System.out.println("test4");
		}
	}
}
