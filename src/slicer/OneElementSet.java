package slicer;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import slicer.integrating.Util;

public class OneElementSet<T> implements Set<T> {

	private static final long serialVersionUID = 1L;
	private T elem;
	private final int id = SetFactory.SETID++;
	private boolean instructionHasChanged;

	private String sourceMethod;
	private String sourceClass;

	public OneElementSet(T elem, boolean instructionHasChanged) {
		super();
		if (SetupSlicer.SPL_ON) {
			Instruction insn = JVM.getVM().getLastInstruction();
			MethodInfo minfo = insn.getMethodInfo();
			if (minfo != null
					&& Util.instance.getInterest(minfo.getClassInfo()) == 1) {
				sourceMethod = minfo.getName();
				sourceClass = minfo.getClassName();
			}
		}
		this.elem = elem;
		this.instructionHasChanged = instructionHasChanged;
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

	boolean isProcessed;

	@Override
	public boolean isProcessed() {
		return isProcessed;
	}

	@Override
	public void setProcessed(Boolean value) {
		isProcessed = value;
	}

	@Override
	public void addElements(List<Set<T>> list) {
		list.add(this);
	}

	@Override
	public boolean contains(Set<T> set1, int depth) {
		if (set1 instanceof OneElementSet) {
			if (((OneElementSet<T>) set1).elem == elem
					|| ((OneElementSet<T>) set1).elem.equals(elem)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public FunctionalFlatSet<T> flatten() {
		ArrayList<T> result = new ArrayList<T>();
		result.add(getElem());
		return new FunctionalFlatSet<T>(result);
	}

	public T getElem() {
		return elem;
	}

	public String toString() {
		return this.elem.toString();
	}

	@Override
	public boolean isInteresting() {
		return instructionHasChanged;
	}

	@Override
	public void printSet(int ident) {
		String spaces = "";
		for (int i = 1; i <= ident; i++) {
			spaces = spaces + " ";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(spaces + "--oe_" + getID());
		sb.append("(" + elem + ")");
		System.out.println(sb);
	}

	@Override
	public void resetProcessed() {
		setProcessed(false);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elem == null) ? 0 : elem.hashCode());
		result = prime * result + id;
		result = prime * result + (instructionHasChanged ? 1231 : 1237);
		result = prime * result + (isProcessed ? 1231 : 1237);
		result = prime * result
				+ ((sourceClass == null) ? 0 : sourceClass.hashCode());
		result = prime * result
				+ ((sourceMethod == null) ? 0 : sourceMethod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OneElementSet other = (OneElementSet) obj;
		if (elem == null) {
			if (other.elem != null)
				return false;
		} else if (!elem.equals(other.elem))
			return false;
		if (id != other.id)
			return false;
		if (instructionHasChanged != other.instructionHasChanged)
			return false;
		if (isProcessed != other.isProcessed)
			return false;
		if (sourceClass == null) {
			if (other.sourceClass != null)
				return false;
		} else if (!sourceClass.equals(other.sourceClass))
			return false;
		if (sourceMethod == null) {
			if (other.sourceMethod != null)
				return false;
		} else if (!sourceMethod.equals(other.sourceMethod))
			return false;
		return true;
	}

}
