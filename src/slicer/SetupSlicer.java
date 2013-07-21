package slicer;

import gov.nasa.jpf.util.Pair;
import gov.nasa.jpf.util.SourceRef;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import slicer.diff.Interval;
import slicer.util.Color;


public class SetupSlicer {

	public static final boolean makeUnionInLoad = false;
	public static boolean debug = false;

	public static final String START_STRING = "!START_SLICING!";
	public static String testPackage = "USE TESTS";
	public static String mainPackage = "";

	// log execution on screen
	public enum Log {NO_, ALL, IN_FILE};
	public static Log log = Log.NO_;


	// implementation of differencing
	public enum DiffType {DIFFJ, JAVA_DIFF};
	public static DiffType diffType = DiffType.DIFFJ;
	public static Map<String, List<Interval>> differences;

	// print slicer or store in field
	public enum REPORT {SCREEN, MEMORY, FILE, DEBUG, MEMORY_LIST};
	public static REPORT reportType = REPORT.MEMORY_LIST;
	public static FunctionalFlatSet<SourceRef> lastSlice;
	public static HashSet<Set> memoryList;
	public static java.util.Set<Pair<Color, Color>> interactionSet = new HashSet<Pair<Color, Color>>();

	public static boolean CHANGE_SLICE = false;
	public static final boolean TREAT_EXCEPTION = false;
 
	public static String diffFile = null;



	public static final boolean considerSourceInDiffForClassification = false;

	public static final boolean CFLOW = false;
	public static final boolean CALLING_CONTEXT = false;
	
	
	/******************************/
	//        FEATURES
	/******************************/
	public static boolean SPL_ON = false; //chage for true by sabrina

	public static String saveSliceIdIn = null;
	public static String outputSlices = null;
	public static  String PRODUCT = null;

}
