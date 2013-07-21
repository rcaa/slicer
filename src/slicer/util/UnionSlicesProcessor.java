package slicer.util;

import gov.nasa.jpf.util.SourceRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import slicer.FunctionalFlatSet;

public class UnionSlicesProcessor extends SliceFileProcessor {
	
	public static void main(String[] args) {

		if (args.length==0) {
			throw new RuntimeException("missing fnames");
		}
		
		SliceFileProcessor proc = new UnionSlicesProcessor();
		proc.run(args, "union", false);

	}

	protected Set<SourceRef> process(List<FunctionalFlatSet<SourceRef>> slices) {
		Set<SourceRef> set = new HashSet<SourceRef>();
		for (int i = 0; i < slices.size(); i++) {
			List<SourceRef> tmp = slices.get(i).getElements();
			HashSet<SourceRef> realSet = new HashSet<SourceRef>(tmp);
			boolean valid = !inspect(set).isEmpty();
			System.out.println(print(realSet,fileNames.get(i),valid));
			if(valid){
			  set.addAll(tmp);  
			}
		}
		return set;
	}
}
