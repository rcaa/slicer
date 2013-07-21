package slicer.tests;

import slicer.SetupSlicer;

public class Removemem {

	
	public static void main(String[] args) {
		
		String className = "sirDataStructures.RunDataStructures";
		
//		SetupSlicer.log = SetupSlicer.Log.NO;
		SetupSlicer.reportType = SetupSlicer.REPORT.MEMORY;
//		SetupSlicer.slicingPackage = "sirDataStructures";
		String[] params = new String[]{
				"+listener=gov.nasa.jpf.listener.DynamicSlicerElton",
				"+report.console.property_violation=error",
				className,
				"100",
				"INSERTION_SORT"
		};
		gov.nasa.jpf.tool.RunJPF.main(params);		
	}

}
