package slicer.integrating;

import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.util.SourceRef;

import java.util.List;

import slicer.SetupSlicer;
import slicer.diff.Interval;
import slicer.diff.IntervalMembership;
import slicer.tree.SingleTree;
import slicer.tree.Tree;
import slicer.tree.TreeFactory;

public class UtilInstruction {

	private Instruction instance;

	private UtilInstruction(Instruction ins) {
		instance = ins;
	}

	public static UtilInstruction getInstance(Instruction ins) {
		return new UtilInstruction(ins);
	}

	// ------------------------ by Sabrina

	protected Instruction firstPostDominator;

	public void setFirstPosDominator(Instruction insn) {
		firstPostDominator = insn;
	}

	public Instruction getFirstPosDominator() {
		return this.firstPostDominator;
	}

	public Tree<SourceRef> getTree() {
		return getTree(false);
	}

	public Tree<SourceRef> getDefaultTree() {
		return getTree(true);
	}

	static MethodInfo lastMI;
	static int lastLine;
	static SingleTree<SourceRef> lastSimpleSet;
	SingleTree<SourceRef> simpleSet;

	private Tree<SourceRef> getTree(boolean defaultSet) {

		// check if this instruction already has a set (important for loops)
		if (simpleSet != null) {
			return simpleSet;
		}

		Tree<SourceRef> EMPTY = TreeFactory.EMPTY_TREE;
		// check if it is possible to read source
		if (instance.getMethodInfo() == null)
			return EMPTY;
		ClassInfo mci = instance.getMethodInfo().getClassInfo();
		if (mci == null)
			return EMPTY;

		if (Util.instance.getInterest(instance.getMethodInfo().getClassInfo()) == 0) {
			return TreeFactory.EMPTY_TREE;
		}

		int line = instance.getLineNumber(); // TODO: please, check if this is
												// ok
		// int line = ti.getLine();
		// MethodInfo mi = ti.getMethod();
		if (lastMI == instance.getMethodInfo() && lastLine == line) {
			simpleSet = lastSimpleSet;
			return simpleSet;
		}
		lastMI = instance.getMethodInfo();
		lastLine = line;

		SourceRef sr;
		String fname = mci.getSourceFileName();
		if (fname == null) {
			throw new RuntimeException("this can really happen");
		}

		sr = (defaultSet) ? new SourceRef("<>" + fname, line) : new SourceRef(
				fname, line);
		simpleSet = new SingleTree<SourceRef>(sr, hasChanged());

		if (simpleSet == null) {
			throw new RuntimeException("could not create source ref ");
		}

		lastSimpleSet = simpleSet;
		return simpleSet;
	}

	int hasChanged = -1; // -1=not tested, 0=not changed, 1=changed

	public boolean hasChanged() {// ThreadInfo ti
		if (!SetupSlicer.CHANGE_SLICE) {
			hasChanged = 1;
			return true;
		}
		if (hasChanged == -1) {
			hasChanged = 0;

			String fname = instance.getMethodInfo().getSourceFileName(); // ti.getMethod().getSourceFileName();
			int line = instance.getLineNumber(); // ti.getLine();
			fname = fname.substring(0, fname.length() - 5);
			// TODO: possible hotspot
			List<Interval> intervals = SetupSlicer.differences.get(fname);
			if (intervals != null) {
				IntervalMembership im = new IntervalMembership(intervals);
				hasChanged = im.contains(line) ? 1 : 0;
			}
		}
		return hasChanged == 1;
	}
}
