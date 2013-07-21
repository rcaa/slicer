package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import slicer.SetupSlicer;
import slicer.integrating.Util;

/**
 * @author sabrinasouto
 * 
 *         Implements a Set of 2 sets, where these sets can be a single set or a
 *         set of 2 elements, since they implements ElementIF. In this case, the
 *         parent is set1 and it has just one child (set2).
 */
public class CompositeTree2<T> implements Tree<T> {

	private static final long serialVersionUID = 1L;
	protected Tree<T> set1;
	protected Tree<T> set2;
	protected final int id = TreeFactory.TREE_ID++;
	protected Tree<T> parent;// the container of this object
	protected Tree<T> child1;
	protected Set<Tree> children = new HashSet<Tree>();
	String sourceLine;
	String sourceMethod;
	String sourceClass;

	protected CompositeTree2(Tree<T> set1, Tree<T> set2) {
		if (SetupSlicer.SPL_ON) {// SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction();
			MethodInfo minfo = insn.getMethodInfo();
			if (minfo != null
					&& Util.instance.getInterest(minfo.getClassInfo()) == 1) {
				this.sourceLine = insn.getFileLocation();
				this.sourceMethod = minfo.getName();
				this.sourceClass = minfo.getClassName();
			}
		}
		this.set1 = set1;
		this.set2 = set2;
		this.parent = this.set1;
		this.child1 = set2;
		addChild(set2);
		InteractionProcessor.processInteractions(parent, child1);
	}

	protected CompositeTree2() {
	}

	// protected void processInteractions(Tree parent, Tree child){
	// if((parent != null) && parent != TreeFactory.EMPTY_TREE){
	// if((child != null) && child != TreeFactory.EMPTY_TREE){
	// if((child.getParent() != null) && child.getParent() !=
	// TreeFactory.EMPTY_TREE){
	// InteractionProcessor.put(parent, child.getParent());
	// }
	// }
	// }
	// }

	// protected void processInteractions(Tree parent, Tree child){
	// if((parent != null) && parent != TreeFactory.EMPTY_TREE){
	// if((child != null) && child != TreeFactory.EMPTY_TREE){
	// Set<Tree> reachable = getReachables(child);
	// for (Tree c : reachable) {
	// InteractionProcessor.put(parent, c);
	// }
	// }
	// }
	// }

	// private Set<Tree> getReachables(Tree child){
	// Set<Tree> reachable = new HashSet<Tree>();
	// if(!child.hasChildren()){
	// reachable.add(child);
	// } else {
	// Set<Tree> currentChildren = child.getChildren();
	// for (Tree cChild : currentChildren) {
	// Set<Tree> childReachable = getReachables(cChild);
	// reachable.addAll(childReachable);
	// reachable.add(cChild.getParent());
	// }
	// }
	// return reachable;
	// }
	//
	//
	// protected void processInteractions(Tree parent, Tree child){
	// if((parent != null) && parent != TreeFactory.EMPTY_TREE){
	// if(child instanceof SingleTree){
	// InteractionProcessor.put(parent, child);
	// } else if(child instanceof CompositeTree2){
	// processInteractions(parent, child.getParent());
	// }
	// }
	// }
	protected void addChild(Tree child) {
		if (!children.contains(child)) {
			if ((child != null) && (child != TreeFactory.EMPTY_TREE)) {
				children.add(child);
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) children.iterator();
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public Tree<T> getParent() {
		return this.parent;
	}

	@Override
	public Set<Tree> getChildren() {
		return this.children;
	}

	@Override
	public boolean isInteresting() {
		return true;
	}

	@Override
	public void print() {
		System.out.println("ID: " + id);
		System.out.println(toString());
	}

	@Override
	public String toString() {
		String result = "CT2\n";
		result += "Parent->" + this.parent.getSourceLocation() + "\n";
		result += "Child->" + this.child1.getSourceLocation() + "\n";
		result += "----------------------------------------------------------\n";
		return result;
	}

	@Override
	public String getSourceLocation() {
		return this.sourceLine;
	}

	@Override
	public String getSourceMethod() {
		return this.sourceMethod;
	}

	@Override
	public String getSourceClass() {
		return this.sourceClass;
	}

	@Override
	public boolean hasChildren() {
		if (!children.isEmpty()) {
			return true;
		}
		return false;
	}

}