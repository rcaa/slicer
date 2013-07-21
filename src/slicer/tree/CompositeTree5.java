package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import slicer.SetupSlicer;
import slicer.integrating.Util;



/**
 * @author sabrinasouto
 *
 * Implements a Set of 5 sets, where these sets
 * can be a single set, a set of 2, 3, 4 or 5 elements, 
 * since they implements ElementIF.
 * In this case, the parent is set1 and it has 4 children
 * (set2, 3, 4 and 5).
 */
public class CompositeTree5<T> extends CompositeTree4<T> {
	
	private static final long serialVersionUID = 1L;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> set5;
	protected Tree<T> child4;
	
	protected CompositeTree5(Tree<T> set1, 
				   Tree<T> set2, 
				   Tree<T> set3, 
				   Tree<T> set4, 
				   Tree<T> set5) {
		if(SetupSlicer.SPL_ON){//SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction(); 
			MethodInfo minfo = insn.getMethodInfo();
			if(minfo != null && Util.instance.getInterest(minfo.getClassInfo()) == 1){
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
//		super(set1, set2, set3, set4);
//		this.set5 = set5;
//		addChild(set5);
//		this.child4 = set5;
//		processInteractions(parent, child4);
		this.parent = this.set1;
		InteractionProcessor.processInteractions(parent, set2);
		InteractionProcessor.processInteractions(parent, set3);
		InteractionProcessor.processInteractions(parent, set4);
		InteractionProcessor.processInteractions(parent, set5);
	}
	
	protected CompositeTree5(){}
	
	@Override
	public void print() {
		System.out.println("ID: " + id);
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		String result = "CT5\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Children->\n";
		result += (child1 == null ? ("null") : (this.child1.getSourceLocation())) + "\n";
		result += (child2 == null ? ("null") : (this.child2.getSourceLocation())) + "\n";
		result += (child3 == null ? ("null") : (this.child3.getSourceLocation())) + "\n";
		result += (child4 == null ? ("null") : (this.child4.getSourceLocation())) + "\n";
		result += "----------------------------------------------------------\n";
		return result;
	}

}