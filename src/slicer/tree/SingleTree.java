package slicer.tree;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import slicer.SetupSlicer;

public class SingleTree<T> implements Tree<T> {

	private static final long serialVersionUID = 1L;
	private T elem;
	protected final int id = TreeFactory.TREE_ID++;
	private boolean instructionHasChanged;
	protected T parent;// the container of this object
	private String sourceMethod;
	private String sourceClass;
	protected Set<Tree> children = new HashSet<Tree>();

	public SingleTree(T elem, boolean instructionHasChanged) {
		super();
		if (SetupSlicer.SPL_ON) {
			Instruction insn = JVM.getVM().getLastInstruction();
			MethodInfo minfo = insn.getMethodInfo();
			// if(minfo != null &&
			// Util.instance.getInterest(minfo.getClassInfo()) == 1){
			// this.sourceMethod = minfo.getName();
			// this.sourceClass = minfo.getClassName();
			// }
		}
		this.elem = elem;
		this.parent = elem;
		this.instructionHasChanged = instructionHasChanged;
		// InteractionProcessor.put(this, children); ???
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			boolean read = false;

			@Override
			public boolean hasNext() {
				boolean result = !read;
				read = true;
				return result;
			}

			@Override
			public T next() {
				return elem;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int getID() {
		return id;
	}

	public T getElem() {
		return elem;
	}

	@Override
	public boolean isInteresting() {
		return instructionHasChanged;
	}

	@Override
	public String getSourceLocation() {
		return this.elem.toString();
	}

	@Override
	public String getSourceMethod() {
		return sourceMethod;
	}

	@Override
	public String getSourceClass() {
		return sourceClass;
	}

	@Override
	public Tree<T> getParent() {
		return this;
	}

	@Override
	public Set<Tree> getChildren() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void print() {
		System.out.println("ID: " + id);
		System.out.println("Parent->");
		if (this.parent != null) {
			System.out.println(this.elem.toString());
		}
		System.out
				.println("----------------------------------------------------------\n");
	}

	@Override
	public String toString() {
		String result = "ST\n";
		result += "Parent->" + this.elem.toString() + "\n";
		result += "Children-> --- \n";
		result += "----------------------------------------------------------\n";
		return result;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
}
