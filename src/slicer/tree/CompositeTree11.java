package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import slicer.SetupSlicer;
import slicer.integrating.Util;



/**
 * @author sabrinasouto
 *
 * Implements a Set of 11 sets, where these sets
 * can be a single set, a set of 2, 3, 4, 5, 6, 7,   
 * 8, 9, 10 or 11 elements, since they implements 
 * ElementIF.
 * In this case, the parent is set1 and it has 10 
 * children (set2, 3, 4, 5, 6, 7, 8, 9, 10 and 11).
 */
public class CompositeTree11<T> extends CompositeTree10<T> {

	private static final long serialVersionUID = 1L;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> set11;
	protected Tree<T> child10;
	
	protected CompositeTree11(){}
			
	protected CompositeTree11(Tree<T> set1, 
				   Tree<T> set2, 
				   Tree<T> set3, 
				   Tree<T> set4,
				   Tree<T> set5, 
				   Tree<T> set6,
				   Tree<T> set7,
				   Tree<T> set8,
				   Tree<T> set9,
				   Tree<T> set10,
				   Tree<T> set11) {
		if(SetupSlicer.SPL_ON){//SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction(); 
			MethodInfo minfo = insn.getMethodInfo();
			if(minfo != null && Util.instance.getInterest(minfo.getClassInfo()) == 1){
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
//		super(set1, set2, set3, set4, set5, set6, set7, set8, set9, set10);
//		this.set11 = set11;
//		addChild(set11);
//		this.child10 = set11;
//		processInteractions(parent, child10);
		InteractionProcessor.processInteractions(parent, set2);
		InteractionProcessor.processInteractions(parent, set3);
		InteractionProcessor.processInteractions(parent, set4);
		InteractionProcessor.processInteractions(parent, set5);
		InteractionProcessor.processInteractions(parent, set6);
		InteractionProcessor.processInteractions(parent, set7);
		InteractionProcessor.processInteractions(parent, set8);
		InteractionProcessor.processInteractions(parent, set9);
		InteractionProcessor.processInteractions(parent, set10);
		InteractionProcessor.processInteractions(parent, set11);
	}
	
	@Override
	public void print() {
		System.out.println("ID: " + this.id);
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		String result = "CT11\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Children->\n";
		result += this.child1.getSourceLocation() + "\n";
		result += this.child2.getSourceLocation() + "\n";
		result += this.child3.getSourceLocation() + "\n";
		result += this.child4.getSourceLocation() + "\n";
		result += this.child5.getSourceLocation() + "\n";
		result += this.child6.getSourceLocation() + "\n";
		result += this.child7.getSourceLocation() + "\n";
		result += this.child8.getSourceLocation() + "\n";
		result += this.child9.getSourceLocation() + "\n";
		result += this.child10.getSourceLocation();
		result += "----------------------------------------------------------\n";
		return result;
	}
}
