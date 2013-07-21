package slicer;

import java.util.HashSet;
import java.util.List;

/* T must be the OneElementSet
 *
 */
public class FHashSet<T> extends HashSet<T> implements Set<T> {
	private static final long serialVersionUID = 1L;
	protected int id = SetFactory.SETID++;

	public FHashSet(Set<T>... elems) {
		for (Set<T> set : elems) {
			if (set != null) {
				for (T t : set) {
					this.add(t);
				}
			}
		}
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FHashSet other = (FHashSet) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public boolean isProcessed() {
		return true;
	}

	@Override
	public boolean contains(Set<T> set1, int depth) {
		return false;
	}

	@Override
	public FunctionalFlatSet<T> flatten() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addElements(List<Set<T>> list) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setProcessed(Boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isInteresting() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void printSet(int ident) {
		throw new UnsupportedOperationException();
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
