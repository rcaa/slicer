//
//Copyright (C) 2006 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.
//
//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.
//
//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package slicer.integrating;

import gov.nasa.jpf.jvm.bytecode.GOTO;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;
import gov.nasa.jpf.jvm.bytecode.SwitchInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * originally created to support the discovery of immediate post-dominators of
 * control-flow nodes, an important operation for dynamic forward slicing.
 * 
 * @author damorim
 * 
 */

public class BasicBlock {

	// beginning and end of basic block
	private int lo, hi;

	private List<BasicBlock> targeters = new ArrayList<BasicBlock>();

	private List<BasicBlock> targets = new ArrayList<BasicBlock>();

	public BasicBlock(int lo, int hi) {
		super();
		// this.code = code;
		this.lo = lo;
		this.hi = hi;
	}

	public int getLo() {
		return lo;
	}

	public int getHi() {
		return hi;
	}

	public String toString() {
		return "[" + lo + "," + hi + "]";
	}

	public String printInOutEdges() {
		StringBuffer sb = new StringBuffer();
		sb.append("********************\n");
		for (BasicBlock bb : targeters) {
			sb.append(bb.hashCode());
			sb.append(", ");
		}
		sb.append('\n');
		sb.append(hashCode());
		sb.append(": [" + lo + "," + hi + "]");
		sb.append('\n');
		for (BasicBlock bb : targets) {
			sb.append(bb.hashCode());
			sb.append(", ");
		}
		sb.append('\n');
		return sb.toString();
	}

	public List<BasicBlock> getTargeters() {
		return targeters;
	}

	public List<BasicBlock> getTargets() {
		return targets;
	}

	@Override
	public int hashCode() {
		/**
		 * use the hash of a small string
		 */
		return (lo + "-" + hi).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicBlock other = (BasicBlock) obj;
		if (hi != other.hi)
			return false;
		if (lo != other.lo)
			return false;
		if (targeters == null) {
			if (other.targeters != null)
				return false;
		} else if (!targeters.equals(other.targeters))
			return false;
		if (targets == null) {
			if (other.targets != null)
				return false;
		} else if (!targets.equals(other.targets))
			return false;
		return true;
	}

	protected static List<BasicBlock> genBasicBlocks(Instruction[] code) {
		/*****
		 * find targets: this would not be unnecessary if we had bcel-like
		 * method getTargeter()
		 *****/
		List<Instruction> targets = new ArrayList<Instruction>();
		Map<Instruction, List<Instruction>> pointsTo = new HashMap<Instruction, List<Instruction>>();

		for (Instruction insn : code) {
			boolean isIF = insn instanceof IfInstruction;
			boolean isGOTO = insn instanceof GOTO;
			boolean isSwitch = insn instanceof SwitchInstruction;
			Instruction target = null;
			if (isIF) {
				target = ((IfInstruction) insn).getTarget();
			} else if (isGOTO) {
				target = ((GOTO) insn).getTarget();
			}
			if ((isIF || isGOTO) && target == null) {
				throw new RuntimeException("check this case");
			}
			if (target != null) {
				add(insn, target, pointsTo);
				if (!targets.contains(target)) {
					targets.add(target);
				}
			}
			if (isSwitch) {
				SwitchInstruction si = (SwitchInstruction) insn;
				List<Integer> offsets = new ArrayList<Integer>();
				int[] positions = si.getTargets();
				// find offset
				for (int i = 0; i < positions.length + 1; i++) {
					int pos;
					if (i == positions.length) {
						pos = si.getTarget(); // last iteration, default branch
					} else {
						pos = positions[i];
					}
					int offset = -1;
					for (int j = si.getInstructionIndex() + 1; j < code.length; j++) {
						Instruction tmp = code[j];
						if (tmp.getPosition() == pos) {
							offset = j;
							targets.add(tmp);
							add(insn, tmp, pointsTo);
							offsets.add(offset);
							break;
						}
					}
					if (offset == -1) {
						throw new RuntimeException(
								"could not find switch match");
					}
				}
			}
		}

		/***************************************
		 * build basic blocks
		 ***************************************/
		List<BasicBlock> bblocks = new ArrayList<BasicBlock>();
		int lo = 0;
		for (int i = 0; i < code.length; i++) {
			Instruction insn = code[i];

			boolean isReturnInstruction = insn instanceof ReturnInstruction;
			boolean isBranch = (insn instanceof IfInstruction)
					|| (insn instanceof GOTO)
					|| (insn instanceof SwitchInstruction)
					|| (isReturnInstruction);

			// if (isReturnInstruction) {
			// bblocks.add(new BasicBlock(lo, i - 1));
			// bblocks.add(new BasicBlock(i, i));
			// lo = i + 1;
			// } else

			if (i > lo && targets.contains(insn)) {
				/***************************************
				 * conditions to start a basic block
				 ***************************************/
				bblocks.add(new BasicBlock(lo, i - 1));
				lo = i;
			} else if (isBranch || i == code.length) {
				/***************************************
				 * conditions to terminate a basic block
				 ***************************************/
				bblocks.add(new BasicBlock(lo, i));
				lo = i + 1;
			}
		}
		if (lo != code.length) {
			bblocks.add(new BasicBlock(lo, code.length - 1));
		}

		/**
		 * connect basic blocks
		 */
		// TODO: inefficient
		for (BasicBlock source : bblocks) {
			for (BasicBlock dest : bblocks) {
				Instruction start = code[source.getHi()];
				Instruction end = code[dest.getLo()];
				if (!(start instanceof ReturnInstruction)
						&& !(start instanceof GOTO) && start.getNext() == end) {
					connect(source, dest);
					continue;
				}
				List<Instruction> destinies = pointsTo.get(start);
				if (destinies != null && destinies.contains(end)) {
					connect(source, dest);
				}
			}
		}

		return bblocks;
	}

	private static void connect(BasicBlock source, BasicBlock dest) {
		if (!source.targets.contains(dest)) {
			source.targets.add(dest);
		}
		if (!dest.targeters.contains(source)) {
			dest.targeters.add(source);
		}
	}

	private static void add(Instruction insn, Instruction target,
			Map<Instruction, List<Instruction>> pointsTo) {

		List<Instruction> list = pointsTo.get(insn);
		if (list == null) {
			list = new ArrayList<Instruction>();
			pointsTo.put(insn, list);
		}
		list.add(target);

	}

	public boolean isBranching(Instruction[] code) {
		Instruction insn = code[hi];
		return (insn instanceof IfInstruction)
				|| (insn instanceof SwitchInstruction);
	}

}