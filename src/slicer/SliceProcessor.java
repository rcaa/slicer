package slicer;

import gov.nasa.jpf.util.Pair;
import gov.nasa.jpf.util.SourceRef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import slicer.util.Color;
import slicer.util.Color.InteractionType;
import slicer.util.FeatureUtilities;
import slicer.util.Util;

public class SliceProcessor {

	private static final int EDGE_HITCOUNT_THRESHOLD = 100;

	// input parameters labels
	static final String SEPARATOR_INTO_PARAM = ",";
	// static final String PRODUCT_LABEL = "-product=";
	static final String CK_LABEL = "-ck=";
	static final String SLICES_LABEL = "-slices=";
	static final String MAIN_PACKAGE_LABEL = "-main.package=";
	static final String TEST_PACKAGE_LABEL = "-test.package=";
	static final String VERBOSE_LABEL = "-verbose";
	static final String SPECIFIC_INFORMATION_LABEL = "-si=";
	static final String FILTER_LABEL = "-filter=";
	static int distinctFIs = 0;

	// configuration parameters values
	// static String PRODUCT;
	static String CK_FILE;
	static String[] SLICES_FILES;
	static String MAIN_PACKAGE;
	static String TEST_PACKAGE;
	static boolean VERBOSE;
	static String[] SPECIFIC_INFORMATION;
	static List<Pair<String, String>> FILTER;

	private static String getOption(String option, String args[]) {
		for (String arg : args) {
			if (arg.startsWith(option)) {
				return arg.substring(option.length());
			}
		}
		if (!option.equals(VERBOSE_LABEL)
				&& !option.equals(SPECIFIC_INFORMATION_LABEL)
				&& !option.equals(FILTER_LABEL)) {
			usage();
			throw new RuntimeException("The parameter " + option
					+ " must be setted.");
		} else {
			return null;
		}
	}

	private static String[] treatListParameters(String param) {
		if (param != null) {
			StringTokenizer st = new StringTokenizer(param,
					SEPARATOR_INTO_PARAM);
			int size = st.countTokens();
			if (size == 0)
				throw new RuntimeException("Something wrong, check parameters");
			String[] result = new String[size];
			int i = 0;
			while (st.hasMoreTokens()) {
				result[i] = st.nextToken();
				i++;
			}
			return result;
		} else {
			return null;
		}
	}

	public static void usage() {
		System.out
				.println("This script processes the slices in the order passed by parameter");
		System.out.println("Usage:");
		// System.out.println("-product=F1,F2,..,FN");
		System.out
				.println("-ck=Subject.ck : this file should be contain, in each line, a correspondence from source line and feature line:  “org/prevayler/PrevaylerFactory.java:260 REPLICATION”");
		System.out
				.println("-slices=subject_slice1.ser subject_slice2.ser ... subject_sliceN.ser");
		System.out.println("-main.package=p1.p2");
		System.out.println("-test.package=p1.p2.p3");
		System.out
				.println("[ -verbose  :] this parameter adds detailed information of interactions between not core features");
		System.out
				.println("[ -si=F1[,F2] ] :show detailed information only about features passed.");
		System.out
				.println("[ -filter=filename ]  :this file should be contain, in each line, a expected interaction like, F1->F2");

	}

	/*
	 * -ck=/Users/Rodrigo/Documents/workspaces/Phd/Disciplinas/Sudoku/Sudoku.ck
	 * -slices=/Users/Rodrigo/Documents/workspaces/Phd/Disciplinas/Sudoku/
	 * Sudoku_p4_test3.ser
	 * -main.package=/Users/Rodrigo/Documents/workspaces/Phd/
	 * Disciplinas/Sudoku/src
	 * -test.package=/Users/Rodrigo/Documents/workspaces/Phd
	 * /Disciplinas/Sudoku/src-tests -verbose
	 */

	static int idCounter = 0;

	public static void main(String[] args) {
		long initialTime = System.currentTimeMillis();

		if (args == null || args.length == 0) {
			usage();
			return;
		} else {
			// PRODUCT = getOption(PRODUCT_LABEL, args);
			CK_FILE = getOption(CK_LABEL, args);
			SLICES_FILES = treatListParameters(getOption(SLICES_LABEL, args));
			MAIN_PACKAGE = getOption(MAIN_PACKAGE_LABEL, args);
			TEST_PACKAGE = getOption(TEST_PACKAGE_LABEL, args);
			VERBOSE = (getOption(VERBOSE_LABEL, args) != null) ? true : false;
			SPECIFIC_INFORMATION = treatListParameters(getOption(
					SPECIFIC_INFORMATION_LABEL, args));
			FILTER = FeatureUtilities
					.readEFIFile(getOption(FILTER_LABEL, args));
		}

		// setting variables
		SetupSlicer.testPackage = TEST_PACKAGE;
		SetupSlicer.mainPackage = MAIN_PACKAGE;

		// Use in experiments
		System.out.printf("Test,#Interactions,#%s,#%s,#%s,#Time\n",
				InteractionType.INTERACTIONS_INTRA_METHOD,
				InteractionType.INTERACTIONS_INTER_METHODS,
				InteractionType.INTERACTIONS_INTER_CLASSES);// Test,#Intr,#IntrN,#IntrM,#IntrC
		for (int i = 0; i < SLICES_FILES.length; i++) {
			File file = new File(SLICES_FILES[i]);
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Set<Pair<Color, Color>> rawInteractions = (Set<Pair<Color, Color>>) ois
						.readObject();
				makeDependenciesAwareFeatures(rawInteractions);
				ois.close();
				fis.close();
			} catch (IOException _) {
				throw new RuntimeException("", _);
			} catch (ClassNotFoundException _) {
				throw new RuntimeException("", _);
			}

			// Use in experiments
			countTime += extractTime(i);
			// System.out.printf(file.getName() + "," + g.edgeSet().size() +
			// "\n");
			System.out.printf("%s,%d,%d,%d,%d,%f\n", file.getName(), g
					.edgeSet().size(), countInteractionsIntraMethod,
					countInteractionsInterMethods,
					countInteractionsInterClasses, countTime);
			System.out.printf("distinctFIs--->" + distinctFIs + "\n");
			System.out.printf("TIME--->" + countTime + "\n");

		}
		// findIndirect();
		dotify();
		if (VERBOSE || SPECIFIC_INFORMATION != null) {
			System.out
					.println("===================== INTERACTIONS INFO ==============================");
			Collections.sort(verboseInfo);
			for (String info : verboseInfo) {
				System.out.println(info);
			}
		}

		long finalTime = System.currentTimeMillis();
		long processTime = (finalTime - initialTime) / 1000;
		System.out.println("PROCESS_TIME: " + processTime + "s");
		System.out.println("EXEC_TIME: " + countTime + "s");
		System.out.println("TOTAL_TIME: " + (processTime + countTime) + "s");

	}

	private static void dotify() {
		VertexNameProvider<String> vertexID = new VertexNameProvider<String>() {
			@Override
			public String getVertexName(String arg0) {
				return ids.get(arg0) + "";
			}
		};
		VertexNameProvider<String> vertexLabel = new VertexNameProvider<String>() {
			@Override
			public String getVertexName(String arg0) {
				return arg0;
			}
		};
		EdgeNameProvider<DefaultEdge> edgeLabel = new EdgeNameProvider<DefaultEdge>() {
			@Override
			public String getEdgeName(DefaultEdge arg0) {
				Long v = weight.get(arg0);
				String result = ">" + EDGE_HITCOUNT_THRESHOLD;
				if (v <= EDGE_HITCOUNT_THRESHOLD) {
					result = v + "";
				}
				return result;
			}

		};
		ComponentAttributeProvider<DefaultEdge> attr = new ComponentAttributeProvider<DefaultEdge>() {
			Map<String, String> cteAttr = null;

			public Map<String, String> getComponentAttributes(DefaultEdge arg0) {
				if (cteAttr == null) {
					cteAttr = new HashMap<String, String>();
					cteAttr.put("style", "dashed");
				}
				if (indirectToAdd.contains(arg0)) {
					return cteAttr;
				} else {
					return null;
				}
			}
		};

		DOTExporter<String, DefaultEdge> dotExporter = new DOTExporter<String, DefaultEdge>(
				vertexID, vertexLabel, edgeLabel,
				null /* vertex attributes */, attr);
		dotExporter.export(new OutputStreamWriter(System.out), g);
		/***
		 * then, use: dot -Teps -O bla.dot to generate eps file.
		 * 
		 * could even automate this calling dot directly with Runtime.exec
		 ***/
	}

	static List<String> verboseInfo = new ArrayList<String>();
	static Map<Set<SourceRef>, java.util.Set<String>> visited;// = new
																// HashMap<Set<SourceRef>,
																// java.util.Set<String>>();
	static DirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(
			DefaultEdge.class);
	static HashSet<String> features = new HashSet<String>();
	static HashMap<String, Integer> ids = new HashMap<String, Integer>();
	static HashMap<DefaultEdge, Long> weight = new HashMap<DefaultEdge, Long>();

	/** Counters **/
	static HashSet<Pair<Color, Color>> interactionPair = new HashSet<Pair<Color, Color>>();
	static int numberEdges = 0;
	static int countInteractionsIntraMethod = 0;// N: normal interactions, they
												// occur in the same method
	static int countInteractionsInterMethods = 0;// M: Method interactions, they
													// occur in through
													// different methods
	static int countInteractionsInterClasses = 0;// C: Classes interactions,
													// they occur in through
													// different classes
	static double countTime = 0.0;

	static Map<String, String> indirectEdges = new HashMap<String, String>();
	static java.util.Set<DefaultEdge> indirectToAdd = new HashSet<DefaultEdge>();

	static void makeDependenciesAwareFeatures(
			Set<Pair<Color, Color>> interactions) {
		for (Pair<Color, Color> rawInteraction : interactions) {
			// System.out.println(rawInteraction.toString());
			Color parentColor = rawInteraction._1;
			addFeatures(parentColor);
			Color childColor = rawInteraction._2;
			if (parentColor.equals(childColor))
				continue;
			addFeatures(childColor);
			boolean newInteractionPair = interactionPair.add(rawInteraction);
			for (String pFeature : parentColor) {
				for (String cFeature : childColor) {

					if (!pFeature.equals(cFeature)) {
						if (newInteractionPair) {
							// Count interactions inter, intra - methods; and
							// inter classes
							InteractionType type = parentColor
									.getInteractionType(childColor);
							if (!parentColor.isEmpty() && !childColor.isEmpty()) {
								switch (type) {
								case INTERACTIONS_INTRA_METHOD:
									countInteractionsIntraMethod++;
									break;
								case INTERACTIONS_INTER_METHODS:
									countInteractionsInterMethods++;
									break;
								case INTERACTIONS_INTER_CLASSES:
									countInteractionsInterClasses++;
									break;
								}
							}

							if (SPECIFIC_INFORMATION != null) {
								if (isContained(SPECIFIC_INFORMATION, pFeature)
										|| isContained(SPECIFIC_INFORMATION,
												cFeature)) {
									addVerboseInfo(
											rawInteraction,
											ids.get(pFeature)
													+ " "
													+ FeatureUtilities.INTERACTION_SEPARATOR
													+ " " + ids.get(cFeature));
								} else {
									continue;
								}
							}

							if (VERBOSE) {
								addVerboseInfo(
										rawInteraction,
										ids.get(pFeature)
												+ FeatureUtilities.INTERACTION_SEPARATOR
												+ ids.get(cFeature));
							}

							if (!pFeature.equals(cFeature)) {
								DefaultEdge t = g.addEdge(pFeature, cFeature);
								if (t == null) {
									t = g.getEdge(pFeature, cFeature);
									weight.put(t, weight.get(t) + 1);
								} else {
									weight.put(t, (long) 1);
								}
							}
						}
					}
				}

				// for (String pFeature : parentColor) {
				// for (String cFeature : reachable) {
				// if(pFeature.equals(cFeature)){
				// continue;
				// }
				// indirectEdges.put(pFeature, cFeature.getSourceLine());
				// }
				// }
			}
		}
	}

	private static boolean isExpectedInteraction(String pFeature,
			String cFeature) {
		if (FILTER != null) {
			for (Pair<String, String> inter : FILTER) {
				if (inter._1.equals("*")) {
					if (cFeature.equals(inter._2)) {
						return true;
					}
				} else {
					if (inter._1.equals(pFeature) && inter._2.equals(cFeature)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// private static void findIndirect() {
	// for (Map.Entry<String, String> entry : indirectEdges.entrySet()) {
	// String pFeature = entry.getKey();
	// String cFeature = entry.getValue();
	// if(isExpectedInteraction(pFeature,cFeature)) continue;
	// DefaultEdge t = g.addEdge(pFeature,cFeature );
	// if (t != null) {
	// Long l = weight.get(t);
	// if(l == null){
	// weight.put(t, (long)1);
	// }else{
	// weight.put(t, l+1);
	// }
	// indirectToAdd.add(t);
	// }
	// }
	//
	// }

	private static void addFeatures(Color color) {
		for (String feature : color) {
			if (!features.contains(feature)) {
				if (features.add(feature)) {
					g.addVertex(feature);
					ids.put(feature, idCounter++);
				}
			}
		}
	}

	/**
	 * 
	 * @param position
	 *            of Slice file name that must be extract the time from
	 *            respective jpf output file
	 * @return the time in seconds
	 * @throw a RuntimeException when the Slice file haven't the correspondent
	 *        jpf output file in JPF_OUT_FILE parameter
	 */
	private static double extractTime(int position) {
		File jpfout = new File(SLICES_FILES[position].replace(".ser", ".out"));
		if (!jpfout.exists())
			return -1;// throw new RuntimeException("The file " + jpfout +
						// " not exist.");
		try {
			Scanner s = new Scanner(jpfout);
			String line;
			while (s.hasNextLine()) {
				line = s.nextLine();
				double result = Util.extractTimeFromJPFOutput(line);
				if (result != -1) {
					s.close();
					return result;
				}
			}
			s.close();
			throw new RuntimeException("The file " + jpfout
					+ " doesn't time information");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static void addVerboseInfo(Pair<Color, Color> interaction,
			String edgInfo) {
		if (verboseInfo == null) {
			verboseInfo = new ArrayList<String>();
		}
		StringBuffer info = new StringBuffer();
		info.append("D:");
		info.append(interaction._1.getSetId() - interaction._2.getSetId());
		info.append(' ');
		info.append(edgInfo);
		info.append(' ');
		info.append(interaction._1.toString());
		info.append("->");
		info.append(interaction._2.toString());
		verboseInfo.add(info.toString());
		distinctFIs++;
		// System.out.printf("distinctFIs--->" + distinctFIs);
		// System.out.println(info.toString());
	}

	private static boolean isContained(String[] features, String feature) {
		for (String f : features) {
			if (f.endsWith(feature)) {
				return true;
			}
		}
		return false;
	}

}