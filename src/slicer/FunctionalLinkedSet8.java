package slicer;

import java.util.List;

public class FunctionalLinkedSet8<T> extends FunctionalLinkedSet7<T> {
	private static final long serialVersionUID = 1L;
	protected Set<T> set8;

	public FunctionalLinkedSet8(Set<T> set1, Set<T> set2, Set<T> set3,
	    Set<T> set4, Set<T> set5, Set<T> set6, Set<T> set7, Set<T> set8) {
		super(set1, set2, set3, set4, set5, set6, set7);
		this.set8 = set8;
	}

	@Override
	public void resetLinks() {
		set1 = set2 = set3 = set4 = set5 = set6 = set7 = set8 = null;
	}

	@Override
	public void addElements(List<Set<T>> list) {
		super.addElements(list);
		list.add(set8);
	}

	@Override
	public boolean contains(Set<T> arg, int depth) {
		if (set1 == arg) {
			return true;
		} else if (set2 == arg) {
			return true;
		} else if (set3 == arg) {
			return true;
		} else if (set4 == arg) {
			return true;
		} else if (set5 == arg) {
			return true;
		} else if (set6 == arg) {
			return true;
		} else if (set7 == arg) {
			return true;
		} else if (set8 == arg) {
			return true;
		}

		if (depth == 1)
			return false;
		depth = depth - 1;
		return set1.contains(arg, depth) || set2.contains(arg, depth);
	}

	@Override
	public void printSet(int ident) {
		String spaces = spaces(ident);
		StringBuffer sb = new StringBuffer();
		sb.append(spaces);
		sb.append("--s8_" + getID());// Root
		sb.append('\n');
		sb.append(spaces);
		sb.append('|');
		System.out.println(sb.toString());
		if (isProcessed())
			return;
		setProcessed(true);
		set1.printSet(ident + 1);
		set2.printSet(ident + 1);
		set3.printSet(ident + 1);
		set4.printSet(ident + 1);
		set5.printSet(ident + 1);
		set6.printSet(ident + 1);
		set7.printSet(ident + 1);
		set8.printSet(ident + 1);
	}

}
