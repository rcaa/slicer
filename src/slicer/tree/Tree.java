package slicer.tree;

import java.io.Serializable;
import java.util.Set;

/**
 * @author sabrinasouto This interface is an abstraction for an element of a
 *         dependency chain, it can be: a single element (just wraps a
 *         SourceRef) or a composite element, which can be composed by
 *         singleElements and/or composedElements.
 * @param <T>
 */
public interface Tree<T> extends Iterable<T>, Serializable {

	// public List<ElementIF> children = new ArrayList<ElementIF>();

	int getID();

	Tree<T> getParent();

	Set<Tree> getChildren();

	boolean isInteresting();

	public void print();

	boolean equals(Object o);

	String getSourceLocation();

	String getSourceMethod();

	String getSourceClass();

	boolean hasChildren();

	String toString();

}
