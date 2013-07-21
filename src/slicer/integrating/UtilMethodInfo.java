package slicer.integrating;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.GOTO;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.SwitchInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import slicer.integrating.UtilGOTO.JumpType;

public class UtilMethodInfo {

	private MethodInfo instance;

	private UtilMethodInfo(MethodInfo mi) {
		instance = mi;
	}

	public static UtilMethodInfo getInstance(MethodInfo mi) {
		return new UtilMethodInfo(mi);
	}

	// --------------------------- by elton -------------------------------

	private boolean postDominatorsUpdates = false;

	public void updatePostDominators() {

		if (postDominatorsUpdates) {
			return;
		}

		postDominatorsUpdates = true;

		List<BasicBlock> bblocks = BasicBlock.genBasicBlocks(instance
				.getInstructions());

		// System.out.println(bblocks);
		// for (BasicBlock bb : bblocks) {
		// System.out.println(bb.printInOutEdges());
		// }

		/*******************************************
		 * chaotic iteration to find solution to the set of all post-dominators
		 *******************************************/
		Map<BasicBlock, Set<BasicBlock>> state = new HashMap<BasicBlock, Set<BasicBlock>>();
		List<BasicBlock> wlist = new ArrayList<BasicBlock>();
		wlist.addAll(bblocks);

		// initialize state
		int size = bblocks.size();
		for (int i = 0; i < size; i++) {
			BasicBlock bb = bblocks.get(i);
			Set<BasicBlock> fresh = new HashSet<BasicBlock>();
			fresh.addAll(bblocks);
			state.put(bb, fresh);
		}

		// fix-point computation
		while (wlist.size() > 0) {
			BasicBlock bb = wlist.remove(0);
			Set<BasicBlock> posDoms = state.get(bb);
			/** recalculate **/
			Set<BasicBlock> combined = combine(bb, state);
			if (!combined.equals(posDoms)) {
				// System.out.println("*************");
				// System.out.println("updating" + bb);
				// System.out.println("before: " + posDoms);
				// System.out.println("after: " + combined);
				// System.out.println("*************");
				if (combined.size() > posDoms.size()) {
					System.err.println("unexpected!");
					continue;
				}
				addDistinct(bblocks, wlist, bb);
				state.put(bb, combined);
			}
		}

		// for (Map.Entry<BasicBlock, Set<BasicBlock>> entry : state.entrySet())
		// {
		// System.out.printf("%s => %s\n", entry.getKey(), entry.getValue());
		// }

		/**********************************************
		 * find immediate post-dominator of bb: we implemented this as the first
		 * basic block (meaning the one with smaller value of "lo" field)
		 * different than bb, if one exists. otherwise, it is bb.
		 *********************************************/

		for (Map.Entry<BasicBlock, Set<BasicBlock>> entry : state.entrySet()) {

			BasicBlock key = entry.getKey();

			/**
			 * find immediate post-dom basic block
			 */
			BasicBlock ipDomBB = null;
			for (BasicBlock bb : entry.getValue()) {
				if (bb == key)
					continue;
				int val = bb.getLo();
				int minPos = ipDomBB == null ? Integer.MAX_VALUE : ipDomBB
						.getLo();
				if (val < minPos && val > key.getLo()) {
					ipDomBB = bb;
				}
			}

			/** set immediate post-dom */
			setFirstPosDom(bblocks, key, ipDomBB);

		}

	}

	private void setFirstPosDom(List<BasicBlock> bblocks, BasicBlock key,
			BasicBlock ipDomBB) {

		Instruction[] instructions = instance.getInstructions();

		Instruction ipdom = (ipDomBB == null) ? EOM : instructions[ipDomBB
				.getLo()];
		/**
		 * necessary because of switch statements
		 */
		Instruction insn = instructions[key.getHi()];

		boolean isSwitch = insn instanceof SwitchInstruction;

		boolean isBranch = (insn instanceof IfInstruction)
				|| (insn instanceof GOTO);

		if (isBranch) {

			UtilInstruction.getInstance(insn).setFirstPosDominator(ipdom);

		} else if (isSwitch) {

			if (ipdom != EOM) {
				int position = instructions[ipDomBB.getLo()].getPosition();

				SwitchInstruction si = (SwitchInstruction) insn;
				int[] positions = si.getTargets();
				for (int j = 0; j < positions.length + 1; j++) {
					if (j == positions.length) {
						// TODO: handle default better
						// if (si.getTarget() == offset) {
						// insn.setFirstPosDominator(EOM);
						// break;
						// }
					} else if (positions[j] == position) {
						// insn.setFirstPosDominator(EOM);
						UtilInstruction.getInstance(insn).setFirstPosDominator(
								instructions[ipDomBB.getHi()]);
						break;
					}
				}

			}

			if (UtilInstruction.getInstance(insn).getFirstPosDominator() == null) {
				UtilInstruction.getInstance(insn).setFirstPosDominator(ipdom);
			}

		}

	}

	static Instruction EOM /* end of method */= new Instruction() {
		@Override
		public int getByteCode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getInstructionIndex() {
			return Integer.MAX_VALUE;
		}

		@Override
		public int getPosition() {
			return Integer.MAX_VALUE;
		}

		@Override
		public String toString() {
			return "EOM";
		}
	};

	private void addDistinct(List<BasicBlock> bblocks, List<BasicBlock> wlist,
			BasicBlock bb) {
		List<BasicBlock> targeters = bb.getTargeters();
		for (BasicBlock targeter : targeters) {
			if (!wlist.contains(targeter)) {
				wlist.add(targeter);
			}
		}

	}

	/**
	 * takes the intersection of sets associated with successor nodes and then
	 * the union with itself
	 * 
	 * @param bb
	 * @param state
	 * @return
	 */
	private Set<BasicBlock> combine(BasicBlock bb,
			Map<BasicBlock, Set<BasicBlock>> state) {
		List<BasicBlock> targets = bb.getTargets();
		Set<BasicBlock> result = new HashSet<BasicBlock>();
		boolean firstTime = true;
		/** intersection of all target sets **/
		for (BasicBlock target : targets) {
			Set<BasicBlock> posDom = state.get(target);
			if (posDom != null) {
				if (firstTime) {
					firstTime = false;
					result.addAll(posDom);
				} else {
					result.retainAll(posDom);
				}
			}
		}
		/** then add itself **/
		result.add(bb);
		return result;
	}

	public Instruction getSliceInstructionTarget(IfInstruction ins) {
		Instruction target = null;
		Instruction tmp;
		int index = ins.getInstructionIndex();
		if (index != -1) {
			target = ins.getTarget();
			tmp = instance.getInstructions()[target.getInstructionIndex() - 1];
			if (tmp instanceof GOTO) {
				GOTO goto_ = (GOTO) tmp;
				if (UtilGOTO.getInstance(goto_).getType() == JumpType.AUX
						&& goto_.getTarget().getPosition() > goto_
								.getPosition()) {
					target = goto_.getTarget();
				}
			}
		}
		return target;
	}

	public Instruction getInstructionBefore(Instruction insn) {
		return instance.getInstructions()[insn.getInstructionIndex() - 1];
	}

	/**
	 * Search for the instruction that mark the end of loops as "do-while" and
	 * "for"
	 * 
	 * @param the
	 *            target instruction of continue goto.
	 * @return
	 */
	public Instruction endLoops(Instruction continueTarget) {
		int idx = continueTarget.getInstructionIndex();
		Instruction[] instructions = instance.getInstructions();
		for (int i = idx; i < instance.getNumberOfInstructions(); i++) {
			if (instructions[i] instanceof IfInstruction) { // in case of
															// "do-while"
				IfInstruction if_ = (IfInstruction) instructions[i];
				if (if_.getTarget().getPosition() < if_.getPosition()) {
					return instructions[i + 1];
				}
			} else if (instructions[i] instanceof GOTO) { // in case of "for"
				GOTO goto_ = (GOTO) instructions[i];
				if (goto_.getTarget().getPosition() < goto_.getPosition()) { // *REMOVE
																				// check
																				// later
																				// to
																				// improve
																				// performance
					return instructions[i + 1];
				} else {
					throw new RuntimeException(
							"Check endLoop for do-whiles and fors");// *
				}
			}
		}
		return null;
	}
	// ----------------------------- - - -- -------------------------------

}
