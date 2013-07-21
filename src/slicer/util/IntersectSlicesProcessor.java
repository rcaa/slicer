package slicer.util;

import gov.nasa.jpf.util.SourceRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import slicer.FunctionalFlatSet;

public class IntersectSlicesProcessor extends SliceFileProcessor {

  public static void main(String[] args) {

    if (args == null || args.length==0) {
    	//testing
    	throw new RuntimeException("Ops !!!");//TODO make a good message.
//      args = new String []{
//          "/home/elton/tmp/slices-18-04-2011/ant-E04-32_cis.ser",
//          "/home/elton/tmp/slices-18-04-2011/ant-E04-33_cis.ser",
//          "@v1=E04",
//          "@v0=D00",
//          "@error=/home/elton/svn/chan/iara-subjects/ant/errors-ant.txt",
//          "@ranking=/home/elton/svn/chan/iara_results/ant/E04-ranking-stmt.log",
//          "@diff.file=/home/elton/svn/chan/iara_results/ant/E04-diff-stmt-changedStmt.log"
//      };

    }

    SliceFileProcessor proc = new IntersectSlicesProcessor();
    proc.run(args, "intersect", false);

  }

  protected Set<SourceRef> process(List<FunctionalFlatSet<SourceRef>> slices) {
    Set<SourceRef> set = new HashSet<SourceRef>();
    List<SourceRef> tmp = slices.get(0).getElements();

    for (int i = 0; i < slices.size(); i++) {
      tmp = slices.get(i).getElements();
      HashSet<SourceRef> realSet = new HashSet<SourceRef>(tmp);
      boolean valid = !inspect(realSet).isEmpty();
      System.out.println(print(realSet,fileNames.get(i),valid));
      if(valid){
        if(set.isEmpty()){
          set.addAll(tmp); 
        }else{
          set.retainAll(tmp);  
        }
      }
    }
    return set;
  }

}
