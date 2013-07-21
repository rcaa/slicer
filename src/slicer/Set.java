package slicer;

import java.io.Serializable;
import java.util.List;

public interface Set<T> extends Iterable<T>, Serializable {

	int getID();

	boolean isProcessed();

	boolean contains(Set<T> set1, int depth);

	FunctionalFlatSet<T> flatten();

	void addElements(List<Set<T>> list);

	void setProcessed(Boolean value);

	boolean isInteresting();

	void printSet(int ident);

	void resetProcessed();

	//Feature infos
	String getSourceLocation();
	String getSourceMethod();
	String getSourceClass();

}
