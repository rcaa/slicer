package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import slicer.SetupSlicer;
import slicer.integrating.Util;



/**
 * @author sabrinasouto
 *
 * Implements a Set of 6 sets, where these sets
 * can be a single set, a set of 2, 3, 4, 5 or 6 
 * elements, since they implements ElementIF.
 * In this case, the parent is set1 and it has 5 
 * children (set2, 3, 4, 5 and 6).
 */
public class CompositeTree6<T> extends CompositeTree5<T> {


	private static final long serialVersionUID = 1L;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> set6;
	protected Tree<T> child5;
	
	protected CompositeTree6(Tree<T> set1, 
				   Tree<T> set2, 
				   Tree<T> set3, 
				   Tree<T> set4,
				   Tree<T> set5, 
				   Tree<T> set6) {
		if(SetupSlicer.SPL_ON){//SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction(); 
			MethodInfo minfo = insn.getMethodInfo();
			if(minfo != null && Util.instance.getInterest(minfo.getClassInfo()) == 1){
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
//		super(set1, set2, set3, set4, set5);
//		this.set6 = set6;
//		addChild(set6);
//		this.child5 = set6;
//		processInteractions(parent, child5);
		this.parent = this.set1;
		InteractionProcessor.processInteractions(parent, set2);
		InteractionProcessor.processInteractions(parent, set3);
		InteractionProcessor.processInteractions(parent, set4);
		InteractionProcessor.processInteractions(parent, set5);
		InteractionProcessor.processInteractions(parent, set6);
	}
	
	protected CompositeTree6(){}
	
	@Override
	public void print() {
		System.out.println("ID: " + this.id);
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		String result = "CT6\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Children->\n";
		result += (child1 == null ? ("null") : (this.child1.getSourceLocation())) + "\n";
		result += (child2 == null ? ("null") : (this.child2.getSourceLocation())) + "\n";
		result += (child3 == null ? ("null") : (this.child3.getSourceLocation())) + "\n";
		result += (child4 == null ? ("null") : (this.child4.getSourceLocation())) + "\n";
		result += (child5 == null ? ("null") : (this.child5.getSourceLocation()));
		result += "----------------------------------------------------------\n";
		return result;
	}

}
