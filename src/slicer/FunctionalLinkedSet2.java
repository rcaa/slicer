package slicer;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import slicer.integrating.Util;

public class FunctionalLinkedSet2<T> implements Set<T> {

	private static final long serialVersionUID = 1L;
	protected Set<T> set1;
	protected Set<T> set2;
	protected final int id = SetFactory.SETID++;

	String sourceLine;
	String sourceMethod;
	String sourceClass;

	protected FunctionalLinkedSet2(Set<T> set1, Set<T> set2) {
		if (SetupSlicer.SPL_ON) {// SetupSlicer.CK_FILE != null
			Instruction insn = JVM.getVM().getLastInstruction();
			MethodInfo minfo = insn.getMethodInfo();
			if (minfo != null
					&& Util.instance.getInterest(minfo.getClassInfo()) == 1) {
				sourceLine = insn.getFileLocation();
				sourceMethod = minfo.getName();
				sourceClass = minfo.getClassName();
			}
		}

		this.set1 = set1;
		this.set2 = set2;
	}

	@Override
	public Iterator<T> iterator() {
		return flatten().iterator();
	}

	@Override
	public int getID() {
		return id;
	}

	boolean processed;

	@Override
	public boolean isProcessed() {
		return processed;
	}

	@Override
	public void setProcessed(Boolean value) {
		processed = value;
	}

	@Override
	public void addElements(List<Set<T>> list) {
		list.add(set1);
		list.add(set2);
	}

	private FunctionalFlatSet<T> cache;

	public FunctionalFlatSet<T> getCache() {
		return this.cache;
	}

	public FunctionalFlatSet<T> flatten() {
		if (cache == null) {
			ArrayList<T> elems = process();
			cache = new FunctionalFlatSet<T>(elems);
			setProcessed(true);
			// resetLinks();
		}
		return cache;
	}

	private ArrayList<T> process() {
		ArrayList<T> elems = new ArrayList<T>();
		List<Set<T>> wlist = new ArrayList<Set<T>>();
		addElements(wlist);
		while (!wlist.isEmpty()) {
			Set<T> s = wlist.remove(0);
			if (s.isProcessed()) {
				continue;
			}
			if (s instanceof FunctionalFlatSet<?>) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				FunctionalFlatSet<T> ffs = ((FunctionalFlatSet) s);
				List<T> ts = ffs.getElements();
				elems.addAll(ts);
			} else if (s instanceof FunctionalLinkedSet2<?>) {
				FunctionalLinkedSet2<T> ts = (FunctionalLinkedSet2<T>) s;
				ts.setProcessed(true);
				ts.addElements(wlist);
			} else if (s instanceof OneElementSet<?>) {
				OneElementSet<T> oneElement = ((OneElementSet<T>) s);
				elems.add(oneElement.getElem());
			}
		}
		return elems;
	}

	public void resetLinks() {
		set1 = set2 = null;
	}

	public String print(Set<T> thizz) {
		FunctionalFlatSet<T> ss = flatten();
		StringBuffer sb = new StringBuffer();
		sb.append("#size = ");
		sb.append(ss.size());
		sb.append('\n');
		sb.append(ss.print());
		sb.append("#######################");
		return sb.toString();
	}

	@Override
	public boolean contains(Set<T> arg, int depth) {
		if (set1 == arg) {
			return true;
		} else if (set2 == arg) {
			return true;
		}
		if (depth == 1)
			return false;
		depth = depth - 1;
		return set1.contains(arg, depth) || set2.contains(arg, depth);
	}

	@Override
	public boolean isInteresting() {
		return true;
	}

	@Override
	public void printSet(int ident) {
		String spaces = spaces(ident);
		StringBuffer sb = new StringBuffer();
		sb.append(spaces);
		sb.append("--s2_" + getID() + "(" + this.getSourceLocation() + ")");// Root
		sb.append('\n');
		sb.append(spaces);
		sb.append('|');
		System.out.println(sb.toString());
		if (isProcessed())
			return;
		setProcessed(true);
		set1.printSet(ident + 1);
		set2.printSet(ident + 1);
	}

	protected String spaces(int ident) {
		String spaces = "";
		for (int i = 1; i <= ident; i++) {
			spaces = spaces + " ";
		}
		return spaces;
	}

	/**
	 * Reset path through the set
	 */
	@Override
	public void resetProcessed() {
		List<Set<T>> wlist = new ArrayList<Set<T>>();
		addElements(wlist);
		setProcessed(false);
		while (!wlist.isEmpty()) {
			Set<T> s = wlist.remove(0);
			if (!s.isProcessed()) {
				continue;
			} else {
				s.addElements(wlist);
				s.setProcessed(false);
			}
		}

	}

	@Override
	public String getSourceLocation() {
		return sourceLine;
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
		result = prime * result + ((cache == null) ? 0 : cache.hashCode());
		result = prime * result + id;
		result = prime * result + (processed ? 1231 : 1237);
		result = prime * result + ((set1 == null) ? 0 : set1.hashCode());
		result = prime * result + ((set2 == null) ? 0 : set2.hashCode());
		result = prime * result
				+ ((sourceClass == null) ? 0 : sourceClass.hashCode());
		result = prime * result
				+ ((sourceLine == null) ? 0 : sourceLine.hashCode());
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
		FunctionalLinkedSet2 other = (FunctionalLinkedSet2) obj;
		if (cache == null) {
			if (other.cache != null)
				return false;
		} else if (!cache.equals(other.cache))
			return false;
		if (id != other.id)
			return false;
		if (processed != other.processed)
			return false;
		if (set1 == null) {
			if (other.set1 != null)
				return false;
		} else if (!set1.equals(other.set1))
			return false;
		if (set2 == null) {
			if (other.set2 != null)
				return false;
		} else if (!set2.equals(other.set2))
			return false;
		if (sourceClass == null) {
			if (other.sourceClass != null)
				return false;
		} else if (!sourceClass.equals(other.sourceClass))
			return false;
		if (sourceLine == null) {
			if (other.sourceLine != null)
				return false;
		} else if (!sourceLine.equals(other.sourceLine))
			return false;
		if (sourceMethod == null) {
			if (other.sourceMethod != null)
				return false;
		} else if (!sourceMethod.equals(other.sourceMethod))
			return false;
		return true;
	}

}