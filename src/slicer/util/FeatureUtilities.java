package slicer.util;

import gov.nasa.jpf.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import slicer.SetupSlicer;
import slicer.tree.Tree;

public class FeatureUtilities {
	// Feature
	/************************
	 * List that represent a CK file
	 ************************/
	public final static String COMMENT = "#";
	public final static String INTERACTION_SEPARATOR = "->";
	public final static String FEATURE_INFO_SEPARATOR = "_";
	public final static String OPTIONAL_FEATURE_INDICATOR = "_op";
	public final static String XOR_FEATURE_INDICATOR = "_xor";

	public final static String FEATURE_CORE = "CORE";
		

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static HashSet<Pair<String, String>> readCKFile(String fileName, String product) {
//		HashSet<Pair<String, String>> ck = new HashSet<Pair<String,String>>();
//		Scanner s;
//		try {
//			s = new Scanner(new File(fileName));
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException(e);
//		}
//		StringTokenizer tz;
//		while (s.hasNextLine()) {
//			String line = s.nextLine();
//
//			if (line.startsWith(COMMENT))
//				continue;
//			// Extract CK info
//			tz = new StringTokenizer(line, " ");
//			String sl = tz.nextToken();//Source line
//			String f = tz.nextToken();//feature name
//			if(product != null){
//				HashSet<String> features = getFeatures(product);
//				if(!features.contains(f)) continue;
//			}
//			Pair p = new Pair<String, String>(sl,f);
//			ck.add(p);
//		}
//		s.close();
//		return ck;
//	}
	
	public static Map<String, List<String>> readCKFile(String fileName, String product) {
		Map<String, List<String>> ck = new TreeMap<String, List<String>>();
		Scanner s;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		StringTokenizer tz;
		while (s.hasNextLine()) {
			String line = s.nextLine();

			if (line.startsWith(COMMENT))
				continue;
			// Extract CK info
			tz = new StringTokenizer(line, " ");
			String sl = tz.nextToken();//Source line
			String f = tz.nextToken();//feature name
			if(product != null){
				HashSet<String> features = getFeatures(product);
				if(!features.contains(f)) continue;
			}
//			Pair p = new Pair<String, String>(sl,f);
//			ck.add(p);
			if(ck.containsKey(sl)){
				ck.get(sl).add(f);
			} else {
				List<String> values = new ArrayList<String>();
				values.add(f);
				ck.put(sl, values);
			}
		}
		s.close();
		return ck;
	}
	
	private static HashSet<String> getFeatures(String product){
		StringTokenizer st = new StringTokenizer(product,",");
		int size = st.countTokens();
		if(size <= 0){
			throw new RuntimeException("Cant prune unused features");
		}
		HashSet<String> features = new HashSet<String>(size) ;
		while(st.hasMoreTokens()){
			features.add(st.nextToken());
		}
		if(features.isEmpty()) throw new RuntimeException("Cant prune unused features");
		return features;
	}
	
	
	public static Color getFeatureName(Tree elem, Map<String, List<String>> ck) {
		String sourceRef = elem.getSourceLocation();
		
		Color result = new Color(elem.getSourceClass(), elem.getSourceMethod(), sourceRef, elem.getID());
		if (sourceRef != null) {
			
//			for (Pair<String, String> ckPair : ck) {
//				if (ckPair.a.equals(sourceRef)) {
//					result.add(ckPair.b);
//				}
//			}
			
			if(ck.containsKey(sourceRef)){
				result.addAll(ck.get(sourceRef));
			}
			
			if (result.isEmpty()
					&& sourceRef.startsWith(SetupSlicer.mainPackage.replace('.', '/'))
					&& !sourceRef.startsWith(SetupSlicer.testPackage.replace('.', '/'))) {
				result.add(FEATURE_CORE);
			}
		}
		return result;
	}
	
	public static List <Pair<String, String>> readEFIFile(String fileName) {
		if (fileName == null){
			return null;
		}
		Scanner s;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		StringTokenizer tz;
		List <Pair<String, String>> efi = new ArrayList<Pair<String,String>>();
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if(line == null  || line.trim().equals("")){
				continue;
			}
			if (line.startsWith(COMMENT)){
				continue;
			}
			// Extract expected interactions
			tz = new StringTokenizer(line, INTERACTION_SEPARATOR);
			String f1 = tz.nextToken();
			String f2 = tz.nextToken();
			efi.add(new Pair<String,String>(f1, f2));
		}
		return efi;
	}

}
