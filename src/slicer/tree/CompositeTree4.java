package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import slicer.SetupSlicer;
import slicer.integrating.Util;



/**
 * @author sabrinasouto
 *
 * Implements a Set of 4 sets, where these sets
 * can be a single set, a set of 2, 3 or 4 elements, 
 * since they implements ElementIF.
 * In this case, the parent is set1 and it has 3 children
 * (set2, 3 and 4).
 */
public class CompositeTree4<T> extends CompositeTree3<T> {

	private static final long serialVersionUID = 1L;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> set4;
	protected Tree<T> child3;
	
	protected CompositeTree4(Tree<T> set1, 
			       Tree<T> set2, 
			       Tree<T> set3, 
			       Tree<T> set4) {
		if(SetupSlicer.SPL_ON){//SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction(); 
			MethodInfo minfo = insn.getMethodInfo();
			if(minfo != null && Util.instance.getInterest(minfo.getClassInfo()) == 1){
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
//		this.set4 = set4;
//		addChild(set4);
//		this.child3 = set4;
//		processInteractions(parent, child3);
		this.parent = this.set1;
		InteractionProcessor.processInteractions(parent, set2);
		InteractionProcessor.processInteractions(parent, set3);
		InteractionProcessor.processInteractions(parent, set4);
	}
	
	protected CompositeTree4(){}
	
	@Override
	public void print() {
		System.out.println("ID: " + id);
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		String result = "CT4\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Children->\n";
		result += (child1 == null ? ("null") : (this.child1.getSourceLocation())) + "\n";
		result += (child2 == null ? ("null") : (this.child2.getSourceLocation())) + "\n";
		result += (child3 == null ? ("null") : (this.child3.getSourceLocation())) + "\n";
		result += "----------------------------------------------------------\n";
		return result;
	}
}
