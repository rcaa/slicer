package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import slicer.SetupSlicer;
import slicer.integrating.Util;



/**
 * @author sabrinasouto
 *
 * Implements a Set of 7 sets, where these sets
 * can be a single set, a set of 2, 3, 4, 5, 6 
 * or 7 elements, since they implements ElementIF.
 * In this case, the parent is set1 and it has 6 
 * children (set2, 3, 4, 5, 6 and 7).
 */
public class CompositeTree7<T> extends CompositeTree6<T> {
	
	private static final long serialVersionUID = 1L;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> set7;
	protected Tree<T> child6;
	
	protected CompositeTree7(Tree<T> set1, 
				   Tree<T> set2, 
				   Tree<T> set3, 
				   Tree<T> set4,
				   Tree<T> set5, 
				   Tree<T> set6,
				   Tree<T> set7) {
		if(SetupSlicer.SPL_ON){//SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction(); 
			MethodInfo minfo = insn.getMethodInfo();
			if(minfo != null && Util.instance.getInterest(minfo.getClassInfo()) == 1){
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
//		super(set1, set2, set3, set4, set5, set6);
//		this.set7 = set7;
//		addChild(set7);
//		this.child6 = set7;
//		processInteractions(parent, child6);
		this.parent = set1;
		InteractionProcessor.processInteractions(parent, set2);
		InteractionProcessor.processInteractions(parent, set3);
		InteractionProcessor.processInteractions(parent, set4);
		InteractionProcessor.processInteractions(parent, set5);
		InteractionProcessor.processInteractions(parent, set6);
		InteractionProcessor.processInteractions(parent, set7);
	}
	
	protected CompositeTree7(){}
	
	@Override
	public void print() {
		System.out.println("ID: " + this.id);
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		String result = "CT7\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Children->\n";
		result += this.child1.getSourceLocation() + "\n";
		result += this.child2.getSourceLocation() + "\n";
		result += this.child3.getSourceLocation() + "\n";
		result += this.child4.getSourceLocation() + "\n";
		result += this.child5.getSourceLocation() + "\n";
		result += this.child6.getSourceLocation();
		result += "----------------------------------------------------------\n";
		return result;
	}

}