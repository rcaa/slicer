package slicer;

import gov.nasa.jpf.util.SourceRef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FunctionalFlatSet<T> implements Set<T>, Serializable {

	private static final long serialVersionUID = 1L;
	protected ArrayList<T> ts;
	private int id = SetFactory.SETID++;

	FunctionalFlatSet(ArrayList<T> ts) {
		this.ts = ts;
	}

	@Override
	public Iterator<T> iterator() {
		return ts.iterator();
	}

	public String toString() {
		return ts.toString();
	}

	public String print() {
		StringBuffer sb = new StringBuffer();
		for (T stmt : ts) {
			sb.append(stmt);
			sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public boolean isProcessed() {
		return false;
	}

	@Override
	public void setProcessed(Boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addElements(List<Set<T>> list) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FunctionalFlatSet<T> flatten() {
		return this;
	}

	public List<T> getElements() {
		return Collections.unmodifiableList(ts);
	}

	public int size() {
		return ts.size();
	}

	@Override
	public boolean contains(Set<T> set1, int depth) {
		for (T t : ts) {
			if (t == set1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isInteresting() {
		return size() > 1;
	}

	public boolean contains(SourceRef ref) {
		for (T t : ts) {
			if (t == ref) {
				return true;
			}
		}
		return false;
	}

	boolean printed = false;

	@Override
	public void printSet(int ident) {
		String spaces = "";
		for (int i = 1; i <= ident; i++) {
			spaces = spaces + " ";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(spaces);
		sb.append("--sf_" + getID());// Root
		sb.append('\n');
		sb.append(spaces);
		sb.append('|');
		System.out.println(sb.toString());
		if (printed)
			return;
		printed = true;
		for (T t : ts) {
			Set set = (Set) t;
			set.printSet(ident + 1);
		}
	}

	public void replaceElements(ArrayList<T> newElements) {
		ts = newElements;
	}

	@Override
	public void resetProcessed() {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getSourceLocation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSourceMethod() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSourceClass() {
		throw new UnsupportedOperationException();
	}

}
