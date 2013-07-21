package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import slicer.SetupSlicer;
import slicer.integrating.Util;




/**
 * @author sabrinasouto
 *
 * Implements a Set of 3 sets, where these sets
 * can be a single set, a set of 2 elements or 
 * a set of 3 elements, since they implements ElementIF.
 * In this case, the parent is set1 and it has 2 children
 * (set2 and set3).
 */
public class CompositeTree3<T> extends CompositeTree2<T> {

	private static final long serialVersionUID = 1L;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> set3;
	protected Tree<T> child2;

	protected CompositeTree3(Tree<T> set1, 
				   Tree<T> set2, 
				   Tree<T> set3) {
		if(SetupSlicer.SPL_ON){//SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction(); 
			MethodInfo minfo = insn.getMethodInfo();
			if(minfo != null && Util.instance.getInterest(minfo.getClassInfo()) == 1){
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
//		this.set3 = set3;
//		addChild(set3);
//		this.child2 = set3;
//		processInteractions(parent, child2);
		this.parent = this.set1;
		InteractionProcessor.processInteractions(parent, set2);
		InteractionProcessor.processInteractions(parent, set3);
	}
	
	protected CompositeTree3(){}
	
	@Override
	public void print() {
		System.out.println("ID: " + id);
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		String result = "CT3\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Children->\n";
		result += (child1 == null ? ("null") : (this.child1.getSourceLocation())) + "\n";
		result += (child2 == null ? ("null") : (this.child2.getSourceLocation())) + "\n";
		result += "----------------------------------------------------------\n";
		return result;
	}
	
}
