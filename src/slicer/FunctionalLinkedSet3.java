package slicer;

import java.util.List;

public class FunctionalLinkedSet3<T> extends FunctionalLinkedSet2<T> {
	private static final long serialVersionUID = 1L;
	protected Set<T> set3;
	
	protected FunctionalLinkedSet3(Set<T> set1, Set<T> set2, Set<T> set3) {
		super(set1, set2);
		this.set3 = set3;
	}
	
	@Override
	public void addElements(List<Set<T>> list) {
		super.addElements(list); list.add(set3);
	}
	
	@Override	
	public void resetLinks() {
		set1 = set2 = set3 = null;
	}
	

	@Override
	public boolean contains(Set<T> arg, int depth) {
		if(this.getCache() != null){
			return this.getCache().contains(arg, depth);
		}
		
		if (set1 == arg) {
			return true;			
		} else if (set2 == arg) {
			return true;
		} else if (set3 == arg) {
			return true;
		}
		if (depth == 1) return false;
		depth = depth - 1;                                    //Added --------------------->
		return set1.contains(arg, depth) || set2.contains(arg, depth);//TODO should have  "|| set3.contains(arg, depth)"?
	}

	
	@Override
	public void printSet(int ident) {
		String spaces = spaces(ident);
		StringBuffer sb = new StringBuffer();
		sb.append(spaces);
		sb.append("--s3_"+getID());//Root
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
	}

	

}