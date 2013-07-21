package slicer.integrating;

import gov.nasa.jpf.jvm.ClassInfo;
import slicer.SetupSlicer;

public class Util {

	public static final Util instance = new Util();
	
	private Util() {}
	
	private int interest = -1;// -1:Not visited,0:Not interesting,1:Is
								// interesting

	public int getInterest(ClassInfo classInfo) {
		if (interest == -1) {
			interest = (classInfo.getName().startsWith(SetupSlicer.mainPackage) && !classInfo.getName()
					.startsWith(SetupSlicer.testPackage)) ? 1 : 0;
		}
		return this.interest;
	}
}