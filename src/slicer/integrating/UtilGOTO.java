package slicer.integrating;

import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.GOTO;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;

public class UtilGOTO {

	private GOTO instance;

	private UtilGOTO(GOTO go2) {
		instance = go2;
	}

	public static UtilGOTO getInstance(GOTO go2) {
		return new UtilGOTO(go2);
	}

	// ------------------------------------ by elton
	// --------------------------------------
	public enum JumpType {
		BREAK, CONTINUE, AUX
	};// AUX means that this the goto is a implicit byte code to assist loops
		// and if-elses

	private JumpType type = null;
	private Instruction endLoop = null;

	public JumpType getType() {
		if (type == null) {
			String sourceLine = instance.getSourceLine();
			MethodInfo mi = instance.getMethodInfo();
			ClassInfo ci = mi.getClassInfo();
			sourceLine = checkSourceLine(sourceLine, mi, ci);
			if (sourceLine.contains("break")) {
				type = JumpType.BREAK;
			} else if (sourceLine.contains("continue")) {
				type = JumpType.CONTINUE;
			} else {
				type = JumpType.AUX;
			}
		}
		return this.type;
	}

	private String checkSourceLine(String sourceLine, MethodInfo mi,
			ClassInfo ci) {
		if (sourceLine == null && ci != null) {
			sourceLine = "(" + ci.getSourceFileName() + ":" + mi.getLineNumber(instance) + ")";
		} else if (sourceLine == null && ci == null) {
			sourceLine =  "[synthetic] " + mi.getName();
		}
		return sourceLine;
	}

	public Instruction getEndLoopIn() {
		if (endLoop == null) {
			endLoop = UtilMethodInfo.getInstance(instance.getMethodInfo())
					.endLoops(instance.getTarget());
		}
		return this.endLoop;
	}

	private boolean updatePosDom = false;

	public void updateContinuePosDom() {
		if (updatePosDom)
			return;
		int i = UtilInstruction.getInstance(instance).getFirstPosDominator()
				.getNext().getInstructionIndex();
		Instruction[] code = instance.getMethodInfo().getInstructions();
		for (; i < code.length; i++) {
			Instruction insn = code[i];
			if (insn == null)
				continue;
			if (insn instanceof IfInstruction || insn instanceof GOTO) {
				UtilInstruction.getInstance(instance).setFirstPosDominator(
						UtilInstruction.getInstance(insn)
								.getFirstPosDominator());
				updatePosDom = true;
				break;
			}
		}
	}
}
