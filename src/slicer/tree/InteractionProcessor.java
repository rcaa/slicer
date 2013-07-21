package slicer.tree;

import gov.nasa.jpf.util.Pair;

import java.util.List;
import java.util.Map;

import slicer.SetupSlicer;
import slicer.integrating.DynamicSlicer;
import slicer.util.Color;
import slicer.util.FeatureUtilities;

public class InteractionProcessor {
	
//	public static Map<String,List<String>> rawInteractions = new HashMap<String,List<String>>();
	
//	private static long totalTime = 0;
	public static String product = SetupSlicer.PRODUCT;
//	static HashSet<Pair<String, String>> ck = FeatureUtilities.readCKFile(DynamicSlicer.ck_file, product);
	static Map<String, List<String>> ck = FeatureUtilities.readCKFile(DynamicSlicer.ck_file, product);
	
	public InteractionProcessor(String product, String ck_file) {
		if(SetupSlicer.interactionSet == null || product == null /*|| SetupSlicer.interactionSet.isEmpty()*/){
			throw new RuntimeException("Check the slices: " + SetupSlicer.interactionSet +" or check the product: " + product + " passed to serialization");
		}
		this.product = product;
		this.ck = FeatureUtilities.readCKFile(ck_file, product);
	}
	
	
//	public static void put(String parent, String child){
//		if(rawInteractions.containsKey(parent)){
//			rawInteractions.get(parent).add(parent);
//		}
//
//	}
	
	public static void processInteractions(Tree parent, Tree child){
		if((parent != null) && parent != TreeFactory.EMPTY_TREE){
			if((child != null) && child != TreeFactory.EMPTY_TREE){
				if((child.getParent() != null) && child.getParent() != TreeFactory.EMPTY_TREE){
					put(parent, child.getParent());
				}
			}
		}
	}
	
	private static void put(Tree parent, Tree child){
//		Color parentColor = FeatureUtilities.getFeatureName(parent, ck);
//		Color childColor = FeatureUtilities.getFeatureName(child, ck);
		Color parentColor = FeatureUtilities.getFeatureName(parent, ck);
		Color childColor = FeatureUtilities.getFeatureName(child, ck);
		Pair<Color, Color> currentInteraction = new Pair<Color, Color>(parentColor, childColor);
		if(!SetupSlicer.interactionSet.contains(currentInteraction)) {
			if(!parentColor.equals(childColor)
					&& !(parentColor.getFeatures().equals(childColor.getFeatures()))
					&& !parentColor.getFeatures().isEmpty() && !childColor.getFeatures().isEmpty()
					&& parentColor != null && childColor != null){
				SetupSlicer.interactionSet.add(currentInteraction);
//				System.out.println(currentInteraction.toString());//TODO: remove later
			}
		}
	}
	
	public String toString(){
		String result = "";
		for (Pair p : SetupSlicer.interactionSet) {
			result += p.toString() + "\n";
		}
		return result;
	}

}
