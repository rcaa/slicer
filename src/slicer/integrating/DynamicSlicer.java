package slicer.integrating;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.FieldInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.StaticElementInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Types;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.GOTO;
import gov.nasa.jpf.jvm.bytecode.IINC;
import gov.nasa.jpf.jvm.bytecode.INVOKESTATIC;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.LDC;
import gov.nasa.jpf.jvm.bytecode.LocalVariableInstruction;
import gov.nasa.jpf.jvm.bytecode.MULTIANEWARRAY;
import gov.nasa.jpf.jvm.bytecode.NEW;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.PUTSTATIC;
import gov.nasa.jpf.jvm.bytecode.SwitchInstruction;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.util.SourceRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import slicer.SetupSlicer;
import slicer.SetupSlicer.Log;
import slicer.SetupSlicer.REPORT;
import slicer.tree.InteractionProcessor;
import slicer.tree.Tree;
import slicer.tree.TreeFactory;

public class DynamicSlicer extends PropertyListenerAdapter implements
		PublisherExtension {

	private List<CflowInfo> cflowControl = new ArrayList<CflowInfo>();
	private List<CflowInfo> callingContext = new ArrayList<CflowInfo>();
	public static DynamicSlicer hack;

	public DynamicSlicer(Config config, JPF jpf) {
		if (hack != null) {
			System.out.println();
			throw new RuntimeException("FIX THIS");
		}
		hack = this;
		jpf.addPublisherExtension(ConsolePublisher.class, this);
	}

	private void pushCallingContext(InvokeInstruction invoke) {
		if (!SetupSlicer.CALLING_CONTEXT)
			return;
		CflowInfo info = new CflowInfo(invoke);
		if (!callingContext.isEmpty()) {
			CflowInfo top = peekCallingContextInfo();
			if (!top.equals(info)) {
				info.setTree(TreeFactory.insert(info.getTree(), top.getTree()));
				callingContext.add(info);
			}
		} else {
			callingContext.add(info);
		}
	}

	private CflowInfo peekCallingContextInfo() {
		return callingContext.get(callingContext.size() - 1);
	}

	private void popCallingContext() {
		if (!callingContext.isEmpty()) {
			callingContext.remove(callingContext.size() - 1);
		}
	}

	private CflowInfo peekCflowControl() {
		return this.cflowControl.get(cflowControl.size() - 1);
	}

	private void pushCflowControl(Tree<SourceRef>[] sets, CflowInfo cf) {
		if (!SetupSlicer.CFLOW)
			return;
		Tree<SourceRef> topSet = TreeFactory.EMPTY_TREE;
		if (!cflowControl.isEmpty()) {
			CflowInfo topInfo = peekCflowControl();
			if (topInfo.equals(cf)) {// take care of loop reenters
				popCflow();
			}
			if (!cflowControl.isEmpty()) {
				topSet = topInfo.getTree();
			}
		}

		Tree<SourceRef> result;
		switch (sets.length) {
		case 0:
			result = TreeFactory.insert(cf.getTree(), topSet);
			break;
		case 1:
			result = TreeFactory.insert(cf.getTree(), sets[0], topSet);
			break;
		case 2:
			result = TreeFactory.insert(cf.getTree(), sets[0], sets[1], topSet);
			break;
		case 3:
			result = TreeFactory.insert(cf.getTree(), sets[0], sets[1],
					sets[2], topSet);
			break;
		default:
			throw new RuntimeException("unexpected");
		}

		cf.setTree(result);
		cflowControl.add(cf);

	}

	private void popCflow() {
		// cflowStack.pop();
		cflowControl.remove(cflowControl.size() - 1);
	}

	private void checkCFlow(ThreadInfo ti) {
		// Control flow
		freeCflow(insn);
		if (throwException) {
			this.catchException(ti);
		}

		// Calling Context
		if (!callingContext.isEmpty()) {
			CflowInfo top = peekCallingContextInfo();
			if (top.free(insn)) {
				popCallingContext();
			}
		}
	}

	private void freeCflow(Instruction insn) {
		while (!cflowControl.isEmpty() && peekCflowControl().free(insn)) {
			popCflow();
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1) {
		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2) {
		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, context[0],
				context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3) {
		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, context[0],
				context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4) {
		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4,
				context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4, Tree<SourceRef> arg5) {

		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4, arg5,
				context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4, Tree<SourceRef> arg5, Tree<SourceRef> arg6) {

		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4, arg5,
				arg6, context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4, Tree<SourceRef> arg5, Tree<SourceRef> arg6,
			Tree<SourceRef> arg7) {

		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4, arg5,
				arg6, arg7, context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4, Tree<SourceRef> arg5, Tree<SourceRef> arg6,
			Tree<SourceRef> arg7, Tree<SourceRef> arg8) {

		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4, arg5,
				arg6, arg7, arg8, context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4, Tree<SourceRef> arg5, Tree<SourceRef> arg6,
			Tree<SourceRef> arg7, Tree<SourceRef> arg8, Tree<SourceRef> arg9) {

		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4, arg5,
				arg6, arg7, arg8, arg9, context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	private void saveOperandAttr(ThreadInfo ti, boolean isLongOperand,
			Tree<SourceRef> arg1, Tree<SourceRef> arg2, Tree<SourceRef> arg3,
			Tree<SourceRef> arg4, Tree<SourceRef> arg5, Tree<SourceRef> arg6,
			Tree<SourceRef> arg7, Tree<SourceRef> arg8, Tree<SourceRef> arg9,
			Tree<SourceRef> arg10) {

		Tree<SourceRef>[] context = getContext();
		Tree<SourceRef> tree = TreeFactory.insert(arg1, arg2, arg3, arg4, arg5,
				arg6, arg7, arg8, arg9, arg10, context[0], context[1]);
		if (isLongOperand) {
			ti.setLongOperandAttr(tree);
		} else {
			ti.setOperandAttr(tree);
		}
	}

	@SuppressWarnings("unchecked")
	private Tree<SourceRef>[] getContext() {
		return new Tree[] {
				(!cflowControl.isEmpty()) ? peekCflowControl().getTree()
						: TreeFactory.EMPTY_TREE,
				(!callingContext.isEmpty()) ? peekCallingContextInfo()
						.getTree() : TreeFactory.EMPTY_TREE };
	}

	Instruction insn = null;
	boolean start = false;
	boolean unsupportedOperation = false;
	boolean initializer = false;
	boolean ignore = false;
	boolean throwException = false;

	Tree<SourceRef>[] args;
	Tree<SourceRef>[] nativeArgs;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void executeInstruction(JVM jvm) {
		ignore = false;

		insn = jvm.getNextInstruction();
		if (insn == null) {
			ignore = true;
			return;
		}
		MethodInfo insnMethodInfo = insn.getMethodInfo();
		ClassInfo insnClassInfo = insnMethodInfo.getClassInfo();

		int id = insn.getByteCode();
		if (!start && (id == 0x12 || id == 0x13)) {
			LDC ldc = (LDC) insn;
			if (ldc.getStringValue() != null) {
				UtilMethodInfo.getInstance(insnMethodInfo)
						.updatePostDominators();
				start = ldc.getStringValue().equals(SetupSlicer.START_STRING);
			}
		}
		if (!start) {
			ignore = true;
			return;
		}

		ThreadInfo ti = ThreadInfo.getCurrentThread();
		if (SetupSlicer.log == Log.ALL) {
			printInstructions(ti);
		}

		checkCFlow(ti);

		switch (id) {
		case 0x32 /* AALOAD */: // ..., array(1), index(0) => ..., value
		case 0x35 /* SALOAD */:
		case 0x2E /* IALOAD */:
		case 0x30 /* FALOAD */:
		case 0x34 /* CALOAD */:
		case 0x33 /* BALOAD */:
		case 0x31 /* DALOAD */:
		case 0x2F /* LALOAD */:
			args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr(1),
					(Tree<SourceRef>) ti.getOperandAttr(0) };
			break;
		case 0x53 /* AASTORE */: // ... array, index, <value> => ...
		case 0x56 /* SASTORE */:
		case 0x4F /* IASTORE */:
		case 0x51 /* FASTORE */:
		case 0x55 /* CASTORE */:
		case 0x54 /* BASTORE */:
			arrayStore(ti, false);
			break;
		case 0x50 /* LASTORE */:
		case 0x52 /* DASTORE */:
			arrayStore(ti, true);
			break;

		case 0xBD /* ANEWARRAY */:
		case 0xBC /* NEWARRAY */: {
			args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr(0) };// get
			// dependences
			// of
			// array
			// size;
			break;
		}
		case 0xBE /* ARRAYLENGTH */: // arrayref => ..., length.
			args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr() };// We
			// should
			// keep
			// the
			// arrayref
			// dependence
			break;
		case 0xC5 /* MULTIANEWARRAY */: {
			MULTIANEWARRAY mult = (MULTIANEWARRAY) insn;
			int dimensions = mult.getDimensions();
			// get dependences of array size;
			if (dimensions > 0) {
				args = new Tree[dimensions];
				for (int i = dimensions - 1; i >= 0; i--) {
					args[i] = (Tree<SourceRef>) ti.getOperandAttr(i);
				}
			} else {
				args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr() };
			}

			break;
		}
		// case 259: /*EXECUTENATIVE*/
		// //DO NOTHING the arguments are caught in INVOKE instruction
		// //But there are cases that a EXECUTENATIVE is done without invoke
		// instruction before
		// break;
		case 0xB6 /* INVOKEVIRTUAL */:
		case 256 /* INVOKECLINIT */:
		case 0xB7 /* INVOKESPECIAL */:
		case 0xB9 /* INVOKEINTERFACE */:
			break;
		case 0xB8 /* INVOKESTATIC */: {
			INVOKESTATIC ie = (INVOKESTATIC) insn;
			MethodInfo mInfo = ie.getInvokedMethod(ti);
			ClassInfo ci = mInfo.getClassInfo();
			if (requiresClinitCalls(ti, ci, ie)) {
				ignore = true;
				break;
			}
			if (mInfo.isMJI()) {
				MethodInfo m = ie.getInvokedMethod();
				String name = m.getFullName();
				if (name.startsWith("slicer.Slicer.report")) {
					boolean finalReport = (name
							.startsWith("slicer.Slicer.reportFinal"));
					if (m.getSignature().contains("D")
							|| m.getSignature().contains("J")) {
						reportSliceOnTopOfStack(ti, true, finalReport);
					} else {
						reportSliceOnTopOfStack(ti, false, finalReport);
					}
				}
			}
			break;
		}
		case 0xB5 /* PUTFIELD */: {
			PUTFIELD pf = (PUTFIELD) insn;
			FieldInfo fi = pf.getFieldInfo();
			if (fi != null) {
				int storageSize = fi.getStorageSize();
				Tree<SourceRef> loc = UtilInstruction.getInstance(insn)
						.getTree();
				Tree<SourceRef> objRef = (Tree<SourceRef>) ti
						.getOperandAttr((storageSize == 1) ? 1 : 2);
				Tree<SourceRef> value;
				switch (storageSize) {
				case 1:
					value = (Tree<SourceRef>) ti.getOperandAttr();
					this.saveOperandAttr(ti, false, loc, objRef, value);
					break;
				case 2:
					value = (Tree<SourceRef>) ti.getLongOperandAttr();
					this.saveOperandAttr(ti, true, loc, objRef, value);
					break;
				default:
					throw new JPFException("invalid field type");
				}

			}
			break;
		}
		case 0xB3 /* PUTSTATIC */: {
			PUTSTATIC pf = (PUTSTATIC) insn;
			FieldInfo fi = pf.getFieldInfo();
			ClassInfo ci = fi.getClassInfo();
			if (!insnMethodInfo.isClinit(ci) && requiresClinitCalls(ti, ci, pf)) {
				ignore = true;
			} else {
				if (fi != null) {
					int storageSize = fi.getStorageSize();
					Tree<SourceRef> loc = UtilInstruction.getInstance(insn)
							.getTree();
					Tree<SourceRef> value;
					switch (storageSize) {
					case 1:
						value = (Tree<SourceRef>) ti.getOperandAttr();
						this.saveOperandAttr(ti, false, loc, value);
						break;
					case 2:
						value = (Tree<SourceRef>) ti.getLongOperandAttr();
						this.saveOperandAttr(ti, true, loc, value);
						break;
					default:
						throw new JPFException("invalid field type");
					}
				}
			}
			break;
		}
		// Moved to exectutedInstruction
		case 0xB2 /* GETSTATIC */:// check if this field already visited
		{
			GETSTATIC gs = (GETSTATIC) insn;
			FieldInfo fi = gs.getFieldInfo();
			ClassInfo ci = fi.getClassInfo();
			if (!insnMethodInfo.isClinit(ci) && requiresClinitCalls(ti, ci, gs)) {
				break;
			}
			StaticElementInfo ei = ci.getStaticElementInfo();
			Object attr = ei.getFieldAttr(fi);
			if (attr == null)
				ei.setFieldAttrNoClone(fi, UtilInstruction.getInstance(insn)
						.getDefaultTree());
			break;
		}
		// ..., objectref => ..., value
		case 0xB4 /* GETFIELD */:// check if this field already visited
		{
			GETFIELD gf = (GETFIELD) insn;
			FieldInfo fi = gf.getFieldInfo();
			int objRef = ti.peek(); // don't pop yet, we might re-execute
			if (objRef == -1) {
				throw new RuntimeException("NPE");
			}
			ElementInfo ei = ti.getHeap().get(objRef);
			Object attr = ei.getFieldAttr(fi);
			if (attr == null) {
				attr = UtilInstruction.getInstance(insn).getDefaultTree();
				ei.setFieldAttrNoClone(fi, attr);
			}
			if (SetupSlicer.makeUnionInLoad)
				args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr(),
						(Tree<SourceRef>) attr };// objRef, value
			break;
		}
		case 0x7E /* IAND */:// Get two integer of stack
		case 0x80 /* IOR */:
		case 0x82 /* IXOR */:
		case 0x78 /* ISHL */:
		case 0x7A /* ISHR */:
		case 0x7C /* IUSHR */:
		case 0x60 /* IADD */:
		case 0x64 /* ISUB */:
		case 0x68 /* IMUL */:
		case 0x6C /* IDIV */:
		case 0x70 /* IREM */:
		case 0x62 /* FADD */:
		case 0x66 /* FSUB */:
		case 0x6A /* FMUL */:
		case 0x6E /* FDIV */:
		case 0x72 /* FREM */:
		case 0x95 /* FCMPL */:
		case 0x96 /* FCMPG */: {

			Tree<SourceRef> pop1 = (Tree<SourceRef>) ti.getOperandAttr(0);
			Tree<SourceRef> pop2 = (Tree<SourceRef>) ti.getOperandAttr(1);
			args = new Tree[] { pop1, pop2 };
			break;
		}
		case 0x81 /* LOR */: // Get 2 longs of stack
		case 0x83 /* LXOR */:
		case 0x61 /* LADD */:
		case 0x65 /* LSUB */:
		case 0x69 /* LMUL */:
		case 0x6D /* LDIV */:
		case 0x71 /* LREM */:
		case 0x7F /* LAND */:
		case 0x94 /* LCMP */:
		case 0x63 /* DADD */:
		case 0x67 /* DSUB */:
		case 0x6B /* DMUL */:
		case 0x6F /* DDIV */:
		case 0x97 /* DCMPL */:
		case 0x98 /* DCMPG */:
		case 0x73 /* DREM */: {
			Tree<SourceRef> pop1 = (Tree<SourceRef>) ti.getOperandAttr(1);
			Tree<SourceRef> pop2 = (Tree<SourceRef>) ti.getOperandAttr(3);
			args = new Tree[] { pop1, pop2 };
			break;
		}
		case 0x79 /* LSHL */:
		case 0x7B /* LSHR */:
		case 0x7D /* LUSHR */: {
			Tree<SourceRef> pop1 = (Tree<SourceRef>) ti.getOperandAttr(0);
			Tree<SourceRef> pop2 = (Tree<SourceRef>) ti.getOperandAttr(2);
			args = new Tree[] { pop1, pop2 };
			break;
		}
		case 0xA0 /* IF_ICMPNE */: // ..., value1, value2 => ...
		case 0xA1 /* IF_ICMPLT */:
		case 0xA2 /* IF_ICMPGE */:
		case 0xA3 /* IF_ICMPGT */:
		case 0xA4 /* IF_ICMPLE */:
		case 0xA5 /* IF_ACMPEQ */:
		case 0xA6 /* IF_ACMPNE */:
		case 0x9F /* IF_ICMPEQ */: {
			Tree<SourceRef> pop1 = (Tree<SourceRef>) ti.getOperandAttr(0);
			Tree<SourceRef> pop2 = (Tree<SourceRef>) ti.getOperandAttr(1);
			// Set<SourceRef> loc = insn.getSet();
			CflowInfo cf = new CflowInfo((IfInstruction) insn);
			pushCflowControl(new Tree[] { pop1, pop2 }, cf);
			break;
		}
		case 0x99 /* IFEQ */: // ..., value => ...
		case 0x9A /* IFNE */:
		case 0x9B /* IFLT */:
		case 0x9C /* IFGE */:
		case 0x9D /* IFGT */:
		case 0x9E /* IFLE */:
		case 0xC6 /* IFNULL */:
		case 0xC7 /* IFNONNULL */: {
			Tree<SourceRef> pop = (Tree<SourceRef>) ti.getOperandAttr();
			CflowInfo cf = new CflowInfo((IfInstruction) insn);
			pushCflowControl(new Tree[] { pop }, cf);
			break;
		}
		case 0xAA /* TABLESWITCH */:
		case 0xAB /* LOOKUPSWITCH */: {
			Tree<SourceRef> pop = (Tree<SourceRef>) ti.getOperandAttr();
			CflowInfo cf = new CflowInfo((SwitchInstruction) insn);
			pushCflowControl(new Tree[] { pop }, cf);
			break;
		}
		case 0x84 /* IINC */: {
			IINC iinc = (IINC) insn;
			int idx = iinc.getIndex();
			Tree<SourceRef> var = (Tree<SourceRef>) ti.getLocalAttr(idx);
			Tree<SourceRef> loc = UtilInstruction.getInstance(insn).getTree();
			Tree<SourceRef> tree;

			if (!cflowControl.isEmpty() && !callingContext.isEmpty()) {
				tree = TreeFactory.insert(loc, var, peekCflowControl()
						.getTree(), peekCallingContextInfo().getTree());
			} else if (!cflowControl.isEmpty()) {
				tree = TreeFactory.insert(loc, var, peekCflowControl()
						.getTree());
			} else if (!callingContext.isEmpty()) {// only calling context
				tree = TreeFactory.insert(loc, var, peekCallingContextInfo()
						.getTree());
			} else {
				tree = TreeFactory.insert(loc, var);
			}

			ti.setLocalAttr(idx, tree);
			break;
		}
		case 0xBF /* ATHROW */: {
			args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr() };
			break;
		}
		case 0xC1 /* INSTANCEOF */:
			args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr() };
			break;
		case 0x00 /* NOP */:// Do nothing instructions.
		case 0xC0 /* CHECKCAST */:
		case 0xA9 /* RET */:
			break;
		case 0xA7 /* GOTO */:
		case 0xc8 /* GOTO_W */: {
			GOTO goto_ = (GOTO) insn;
			switch (UtilGOTO.getInstance(goto_).getType()) {
			case BREAK:
				if (!cflowControl.isEmpty()) {
					Tree<SourceRef> breackDependencies = peekCflowControl()
							.getTree();

					freeCflow(goto_.getTarget());
					if (cflowControl.isEmpty()) {
						pushCflowControl(new Tree[] { breackDependencies },
								new PERMANENT_FLOW(goto_));
					} else {
						CflowInfo top = peekCflowControl();
						top.setTree(TreeFactory.insert(UtilInstruction
								.getInstance(goto_).getTree(), top.getTree(),
								breackDependencies));
					}
				}
				break;
			case CONTINUE:
				// the controlFlow must be popped only in the end of while
				CflowInfo cf = new CflowInfo(goto_);
				// check if is a "do-while" or "for", to "while" nothing will
				// needed to do
				if (goto_.getTarget().getPosition() > goto_.getPosition()) {
					UtilGOTO.getInstance(goto_).updateContinuePosDom();
				}
				pushCflowControl(new Tree[] {}, cf);
				break;
			default:
				break;
			}
			break;
		}
		case 0x91 /* I2B */: // take a integer value from stack
		case 0x92 /* I2C */:
		case 0x87 /* I2D */:
		case 0x86 /* I2F */:
		case 0x85 /* I2L */:
		case 0x93 /* I2S */:
		case 0x8B /* F2I */:
		case 0x8D /* F2D */:
		case 0x8C /* F2L */:
		case 0x74 /* INEG */:
		case 0x76 /* FNEG */:
			args = new Tree[] { (Tree<SourceRef>) ti.getOperandAttr() };
			break;
		case 0x8E /* D2I */: // take a long value from stack
		case 0x90 /* D2F */:
		case 0x8F /* D2L */:
		case 0x88 /* L2I */:
		case 0x8A /* L2D */:
		case 0x77 /* DNEG */:
		case 0x75 /* LNEG */:
			args = new Tree[] { (Tree<SourceRef>) ti.getLongOperandAttr() };
			break;
		case 0xC2 /* MONITORENTER */:
		case 0xC3 /* MONITOREXIT */:
		case 0x57 /* POP */:
		case 0x58 /* POP2 */:
			break;
		case 0x59 /* DUP */:
		case 0x5C /* DUP2 */:
		case 0x5A /* DUP_X1 */:
		case 0x5B /* DUP_X2 */:
		case 0x5D /* DUP2_X1 */:
		case 0x5E /* DUP2_X2 */:
		case 0x5F /* SWAP */:
			break;
		case 257 /* RUNSTART */:
			break;
		case 0xBB /* NEW */: {
			NEW newInsn = (NEW) insn;
			String cname = newInsn.getClassName();
			ClassInfo ci = ClassInfo.getResolvedClassInfo(cname);
			ignore = requiresClinitCalls(ti, ci, insn);
			break;
		}

		default:
			unsupportedOperation = true;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void instructionExecuted(JVM jvm) {
		if (ignore) {
			return;
		}

		// Instruction insn = jvm.getLastInstruction();
		// int id = insn.getByteCode();
		ThreadInfo ti = ThreadInfo.getCurrentThread();
		int id = insn.getByteCode();
		switch (id) {
		case 0x01 /* ACONST_NULL */:
		case 0x2 /* ICONST_-1 */:
		case 0x3 /* ICONST_0 */:
		case 0x4 /* ICONST_1 */:
		case 0x5 /* ICONST_2 */:
		case 0x6 /* ICONST_3 */:
		case 0x7 /* ICONST_4 */:
		case 0x8 /* ICONST_5 */:
		case 0x10 /* BIPUSH */:
		case 0x11 /* SIPUSH */:
		case 0x12 /* LDC */:
		case 0x13 /* LDC_W */:
		case 0x0B /* FCONST */:
			this.saveOperandAttr(ti, false, UtilInstruction.getInstance(insn)
					.getTree());
			break;
		case 0x09 /* LCONST value = 0 */:
		case 0x0a /* LCONST value != 0 */:
		case 0x14 /* LDC2_W */:
		case 0x0E /* DCONST */:
			this.saveOperandAttr(ti, true, UtilInstruction.getInstance(insn)
					.getTree());
			break;
		case 0xA8 /* JSR */: // ... => ...,address
		case 0xC9 /* JSR_w */:
		case 0xBB /* NEW */:
			this.saveOperandAttr(ti, false, UtilInstruction.getInstance(insn)
					.getTree());
			break;
		case 0xBD /* ANEWARRAY */:// ..., const => ..., arrayref
		case 0xBC /* NEWARRAY */: {
			Tree<SourceRef> tree = args[0];// get dependences of array size;
			Tree<SourceRef> constTree = UtilInstruction.getInstance(insn)
					.getTree();// location to constant;
			this.saveOperandAttr(ti, false, constTree, tree);
			break;
		}
		case 0xC5 /* MULTIANEWARRAY */: {
			switch (args.length) {
			case 2:
				this.saveOperandAttr(ti, false,
						UtilInstruction.getInstance(insn).getTree(), args[0],
						args[1]);
				break;
			case 3:
				this.saveOperandAttr(ti, false,
						UtilInstruction.getInstance(insn).getTree(), args[0],
						args[1], args[2]);
				break;
			default:
				throw new UnsupportedOperationException(
						"unexpected: args.length = " + args.length);
			}
			break;
		}
		case 0x4d /* ASTORE_2 */:
		case 0x3f /* LSTORE_0 */:
		case 0x40 /* LSTORE_1 */:
		case 0x41 /* LSTORE_2 */:
		case 0x42 /* LSTORE_3 */:
		case 0x37 /* LSTORE WIDE */:
		case 0x3b /* ISTORE_0 */:
		case 0x3c /* ISTORE_1 */:
		case 0x3d /* ISTORE_2 */:
		case 0x3e /* ISTORE_3 */:
		case 0x36 /* ISTORE WIDE */:
		case 0x4b /* ASTORE_0 */:
		case 0x4c /* ASTORE_1 */:
		case 0x4e /* ASTORE_3 */:
		case 0x3A /* ASTORE_WIDE */:
		case 0x43 /* FSTORE_0 */:
		case 0x44 /* FSTORE_1 */:
		case 0x45 /* FSTORE_2 */:
		case 0x46 /* FSTORE_3 */:
		case 0x38 /* FSTORE WIDE */:
		case 0x47 /* DSTORE_0 */:
		case 0x48 /* DSTORE_1 */:
		case 0x49 /* DSTORE_2 */:
		case 0x4a /* DSTORE_3 */:
		case 0x39 /* DSTORE WIDE */: {
			LocalVariableInstruction ins = (LocalVariableInstruction) insn;
			int idx = ins.getLocalVariableIndex();
			Tree<SourceRef> loc = UtilInstruction.getInstance(ins).getTree();
			Tree<SourceRef> set = (Tree<SourceRef>) ti.getLocalAttr(idx);
			Tree<SourceRef>[] arr = getContext();
			Tree<SourceRef> result = TreeFactory.insert(loc, set, arr[0],
					arr[1]);
			ti.setLocalAttr(idx, result);
			break;
		}

		case 0x32 /* AALOAD */: // ..., array, index => ..., value
		case 0x35 /* SALOAD */:
		case 0x2E /* IALOAD */:
		case 0x30 /* FALOAD */:
		case 0x34 /* CALOAD */:
		case 0x33 /* BALOAD */:// args must be two values
		{
			Tree<SourceRef> value = (Tree<SourceRef>) ti.getOperandAttr();
			if (value == null) {
				value = TreeFactory.EMPTY_TREE;
			}
			if (SetupSlicer.makeUnionInLoad) {
				this.saveOperandAttr(ti, false,
						UtilInstruction.getInstance(insn).getTree(), value,
						args[0], args[1]);
			} else {
				ti.setOperandAttr(TreeFactory.insert(value, args[0], args[1]));
			}

			break;
		}
		case 0x31 /* DALOAD */:
		case 0x2F /* LALOAD */: {

			Tree<SourceRef> value = (Tree<SourceRef>) ti.getLongOperandAttr();
			if (value == null) {
				value = TreeFactory.EMPTY_TREE;
			}
			if (SetupSlicer.makeUnionInLoad) {
				this.saveOperandAttr(ti, true, UtilInstruction
						.getInstance(insn).getTree(), value, args[0], args[1]);
			} else {
				ti.setLongOperandAttr(TreeFactory.insert(value, args[0],
						args[1]));
			}

			break;
		}
		case 0x7E /* IAND */:// Put a integer on stack
		case 0x80 /* IOR */:
		case 0x82 /* IXOR */:
		case 0x78 /* ISHL */:
		case 0x7A /* ISHR */:
		case 0x7C /* IUSHR */:
		case 0x60 /* IADD */:
		case 0x64 /* ISUB */:
		case 0x68 /* IMUL */:
		case 0x6C /* IDIV */:
		case 0x70 /* IREM */:
		case 0x62 /* FADD */:
		case 0x66 /* FSUB */:
		case 0x6A /* FMUL */:
		case 0x6E /* FDIV */:
		case 0x72 /* FREM */:
		case 0x95 /* FCMPL */:
		case 0x96 /* FCMPG */:
		case 0x94 /* LCMP */:
		case 0x97 /* DCMPL */:
		case 0x98 /* DCMPG */: {
			this.saveOperandAttr(ti, false, args[0], args[1]);
			break;
		}
		case 0x81 /* LOR */:// Put a long on stack
		case 0x83 /* LXOR */:
		case 0x79 /* LSHL */:
		case 0x7B /* LSHR */:
		case 0x7D /* LUSHR */:
		case 0x61 /* LADD */:
		case 0x65 /* LSUB */:
		case 0x69 /* LMUL */:
		case 0x6D /* LDIV */:
		case 0x71 /* LREM */:
		case 0x7F /* LAND */:
		case 0x63 /* DADD */:
		case 0x67 /* DSUB */:
		case 0x6B /* DMUL */:
		case 0x6F /* DDIV */:
		case 0x73 /* DREM */: {
			this.saveOperandAttr(ti, true, args[0], args[1]);
			break;
		}

		case 0x91 /* I2B */:// put a integer value in stack
		case 0x92 /* I2C */:
		case 0x86 /* I2F */:
		case 0x93 /* I2S */:
		case 0x8B /* F2I */:
		case 0x8E /* D2I */:
		case 0x90 /* D2F */:
		case 0x88 /* L2I */:
		case 0x74 /* INEG */:
		case 0x76 /* FNEG */:
			this.saveOperandAttr(ti, false, args[0]);
			break;
		case 0x8D /* F2D */: // put a long value in stack
		case 0x8C /* F2L */:
		case 0x85 /* I2L */:
		case 0x87 /* I2D */:
		case 0x8F /* D2L */:
		case 0x8A /* L2D */:
		case 0x77 /* DNEG */:
		case 0x75 /* LNEG */:
			this.saveOperandAttr(ti, true, args[0]);
			break;
		case 0xBE /* ARRAYLENGTH */:
			this.saveOperandAttr(ti, false, args[0]);
			break;
		case 0xB7 /* INVOKESPECIAL */:
		case 0xB9 /* INVOKEINTERFACE */:
		case 0xB6 /* INVOKEVIRTUAL */:
		case 256 /* INVOKECLINIT */:
		case 0xB8 /* INVOKESTATIC */: {
			InvokeInstruction ie = (InvokeInstruction) insn;
			MethodInfo mInfo = ie.getInvokedMethod();
			UtilMethodInfo.getInstance(mInfo).updatePostDominators();
			this.pushCallingContext(ie);// when native, this set was put in
			// nativeArgs
			if (mInfo.isMJI() || mInfo.isNative()) {
				byte returnType = mInfo.getReturnTypeCode();
				if (Types.T_VOID != returnType) {
					Object[] attrs = ie.getArgumentAttrs(ti);
					if (attrs != null) {
						nativeArgs = new Tree[attrs.length + 1];
						List<Tree<SourceRef>> tmp = new ArrayList<Tree<SourceRef>>();
						if (SetupSlicer.makeUnionInLoad) {
							tmp.add(UtilInstruction.getInstance(insn).getTree());
						} else {
							tmp.add(TreeFactory.EMPTY_TREE);
						}

						for (int i = 0; i < attrs.length; i++) {// for(;
							// attrs.length;i++){
							tmp.add((Tree<SourceRef>) attrs[i]);
						}
						tmp.toArray(nativeArgs);
					}
				}
			}
			break;
		}
		case 259: /* EXECUTENATIVE */
			// DO NOTHING the arguments are caught in INVOKE instruction
			break;
		case 0xB0 /* ARETURN */:
		case 0xAF /* DRETURN */:
		case 0xAE /* FRETURN */:
		case 0xAC /* IRETURN */:
		case 0xAD /* LRETURN */:
		case 0xB1 /* RETURN */: {
			MethodInfo mi = insn.getMethodInfo();

			while (!cflowControl.isEmpty() && peekCflowControl().free(mi)) {
				popCflow();
			}

			byte returnType = mi.getReturnTypeCode();
			if (returnType != Types.T_VOID) {
				if ((returnType == Types.T_DOUBLE || returnType == Types.T_LONG)) {
					if (SetupSlicer.makeUnionInLoad) {
						this.saveOperandAttr(ti, true, UtilInstruction
								.getInstance(insn).getTree(),
								(Tree<SourceRef>) ti.getLongOperandAttr());
					} else {
						this.saveOperandAttr(ti, true,
								(Tree<SourceRef>) ti.getLongOperandAttr());
					}
				} else {
					if (SetupSlicer.makeUnionInLoad) {
						this.saveOperandAttr(ti, false, UtilInstruction
								.getInstance(insn).getTree(),
								(Tree<SourceRef>) ti.getOperandAttr());
					} else {
						this.saveOperandAttr(ti, false,
								(Tree<SourceRef>) ti.getOperandAttr());
					}
				}
			}
			break;
		}
		case 260: /* NATIVERETURN */
			byte returnType = insn.getMethodInfo().getReturnTypeCode();
			if (returnType != Types.T_VOID) {
				boolean doublePrecision = (returnType == Types.T_DOUBLE || returnType == Types.T_LONG);
				if (nativeArgs != null) {
					switch (nativeArgs.length) {
					case 1:
						this.saveOperandAttr(ti, doublePrecision, nativeArgs[0]);
						break;
					case 2:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1]);
						break;
					case 3:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2]);
						break;
					case 4:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3]);
						break;
					case 5:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3], nativeArgs[4]);
						break;
					case 6:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3], nativeArgs[4], nativeArgs[5]);
						break;
					case 7:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3], nativeArgs[4], nativeArgs[5],
								nativeArgs[6]);
						break;
					case 8:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3], nativeArgs[4], nativeArgs[5],
								nativeArgs[6], nativeArgs[7]);
						break;
					case 9:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3], nativeArgs[4], nativeArgs[5],
								nativeArgs[6], nativeArgs[7], nativeArgs[8]);
						break;
					case 10:
						this.saveOperandAttr(ti, doublePrecision,
								nativeArgs[0], nativeArgs[1], nativeArgs[2],
								nativeArgs[3], nativeArgs[4], nativeArgs[5],
								nativeArgs[6], nativeArgs[7], nativeArgs[8],
								nativeArgs[9]);
						break;

					default:
						throw new UnsupportedOperationException(
								"unexpected: nativeArgs.length = "
										+ nativeArgs.length);
					}
					nativeArgs = null;
				} else {
					// sometimes a execute native instruction is called
					// without a invoke instruction before.
					this.saveOperandAttr(ti, doublePrecision,
							TreeFactory.EMPTY_TREE);
				}
			}
			break;
		case 261: /* DIRECTCALLRETURN */
			// Synthetic bytecode (ghost return), often identify by classInfo ==
			// null
			break;
		case 0xC1 /* INSTANCEOF */:
			Tree<SourceRef> loc = UtilInstruction.getInstance(insn).getTree();
			this.saveOperandAttr(ti, false, loc, args[0]);
			break;

		// Put context in loaded fields and local variables
		case 0xB2 /* GETSTATIC */:
			if (SetupSlicer.makeUnionInLoad) {

				GETSTATIC gs = (GETSTATIC) insn;
				FieldInfo fi = gs.getFieldInfo();
				ClassInfo ci = fi.getClassInfo();
				if (!insn.getMethodInfo().isClinit(ci)
						&& requiresClinitCalls(ti, ci, gs)) {
					break;
				}
				StaticElementInfo ei = ci.getStaticElementInfo();
				Object attr = ei.getFieldAttr(fi);
				if (attr == null) {
					attr = UtilInstruction.getInstance(insn).getDefaultTree();
					ei.setFieldAttrNoClone(fi, attr);
				}

				switch (gs.getFieldSize()) {
				case 1:
					this.saveOperandAttr(ti, false, UtilInstruction
							.getInstance(insn).getTree(),
							(Tree<SourceRef>) attr);
					break;
				case 2:
					this.saveOperandAttr(ti, true, (Tree<SourceRef>) attr);
					break;
				default:
					throw new JPFException("invalid field type");
				}
			}
			break;
		case 0xB4 /* GETFIELD */:
			if (SetupSlicer.makeUnionInLoad) {
				GETFIELD gf = (GETFIELD) insn;
				FieldInfo fi = gf.getFieldInfo();
				switch (fi.getStorageSize()) {
				case 1:
					this.saveOperandAttr(ti, false, UtilInstruction
							.getInstance(insn).getTree(), args[0], args[1]);
					break;
				case 2:
					this.saveOperandAttr(ti, true,
							UtilInstruction.getInstance(insn).getTree(),
							args[0], args[1]);
					break;
				default:
					throw new JPFException("invalid field type");
				}
			}
			break;
		case 0x1a /* ILOAD_0 */:
		case 0x1b /* ILOAD_1 */:
		case 0x1c /* ILOAD_2 */:
		case 0x1d /* ILOAD_3 */:
		case 0x15 /* ILOAD_WIDE */:
		case 0x2a /* ALOAD_0 */:
		case 0x2b /* ALOAD_1 */:
		case 0x2c /* ALOAD_2 */:
		case 0x2d /* ALOAD_3 */:
		case 0x19 /* ALOAD_WIDE */:
		case 0x22 /* FLOAD_0 */:
		case 0x23 /* FLOAD_1 */:
		case 0x24 /* FLOAD_2 */:
		case 0x25 /* FLOAD_3 */:
		case 0x17 /* FLOAD WIDE */: {
			Object attr = checkLocalVar(ti);
			if (SetupSlicer.makeUnionInLoad) {
				this.saveOperandAttr(ti, false,
						UtilInstruction.getInstance(insn).getTree(),
						(Tree<SourceRef>) attr);
			} else {
				ti.setOperandAttr(attr);
			}
			break;
		}
		case 0x1e /* LLOAD_0 */:
		case 0x1f /* LLOAD_1 */:
		case 0x20 /* LLOAD_2 */:
		case 0x21 /* LLOAD_3 */:
		case 0x16 /* LLOAD WIDE */:
		case 0x26 /* DLOAD_0 */:
		case 0x27 /* DLOAD_1 */:
		case 0x28 /* DLOAD_2 */:
		case 0x29 /* DLOAD_3 */:
		case 0x18 /* DLOAD WIDE */: {
			Object attr = checkLocalVar(ti);
			if (SetupSlicer.makeUnionInLoad) {
				this.saveOperandAttr(ti, true, UtilInstruction
						.getInstance(insn).getTree(), (Tree<SourceRef>) attr);
			} else {
				ti.setLongOperandAttr(attr);
			}
			break;
		}

		default:
			if (unsupportedOperation) {
				throw new UnsupportedOperationException(insn.getClass() + ": "
						+ insn.toString());
			}
			break;
		}
		unsupportedOperation = false;

	}

	private Object checkLocalVar(ThreadInfo ti) {
		// take care with not covered values
		LocalVariableInstruction ins = (LocalVariableInstruction) insn;
		int idx = ins.getLocalVariableIndex();
		Object attr = ti.getLocalAttr(idx);
		if (attr == null) {
			attr = TreeFactory.EMPTY_TREE;
			ti.setLocalAttr(idx, attr);
		}
		return attr;
	}

	public static String ck_file;

	private void init() {
		// Configure Slice
		String mainPackage = (String) JVM.getVM().getConfig()
				.get("main.package");
		String testPackage = (String) JVM.getVM().getConfig()
				.get("test.package");
		String useChanges = (String) JVM.getVM().getConfig().get("use.changes");
		String diffFile = (String) JVM.getVM().getConfig().get("diff.file");
		String log = (String) JVM.getVM().getConfig().get("slicer.log");
		String reportType = (String) JVM.getVM().getConfig()
				.get("slicer.report.type");
		String debug = (String) JVM.getVM().getConfig().get("slicer.debug");
		String spl_on = (String) JVM.getVM().getConfig().get("SPL_ON");
		String product = (String) JVM.getVM().getConfig().get("PRODUCT");
		String saveSliceId = (String) JVM.getVM().getConfig()
				.get("save.sliceIdIn");
		String outputSlices = (String) JVM.getVM().getConfig()
				.get("output.slices");
		ck_file = (String) JVM.getVM().getConfig().get("ck");
		if (mainPackage != null) {
			SetupSlicer.mainPackage = mainPackage;
			System.out.println("main package: " + mainPackage);
		}
		if (testPackage != null) {
			SetupSlicer.testPackage = testPackage;
			System.out.println("test package: " + testPackage);
		}
		if (useChanges != null) {
			SetupSlicer.CHANGE_SLICE = Boolean.parseBoolean(useChanges);
			System.out.println("usechages: " + useChanges);
		}
		if (diffFile != null) {
			SetupSlicer.diffFile = diffFile;
			System.out.println("difffile: " + diffFile);
		}
		if (debug != null) {
			SetupSlicer.debug = Boolean.parseBoolean(debug);
			System.out.println("debug: " + debug);
		}
		if (SetupSlicer.CHANGE_SLICE) {
			if (SetupSlicer.diffFile != null) {
				SetupSlicer.differences = slicer.diff.Util
						.getDifferencesFromFile(SetupSlicer.diffFile);
			} else {
				throw new RuntimeException("!!! diff.file must be setted !!!");
			}
		}
		if (log != null) {
			SetupSlicer.log = Log.valueOf(log);
			System.out.println("log: " + log);
		}
		if (reportType != null) {
			SetupSlicer.reportType = REPORT.valueOf(reportType);
			System.out.println("reportType: " + reportType);
		}
		if (saveSliceId != null) {
			SetupSlicer.saveSliceIdIn = saveSliceId;
			System.out.println("saveSliceId: " + saveSliceId);
		}

		if (outputSlices != null) {
			SetupSlicer.outputSlices = outputSlices;
			System.out.println("outputSlices: " + outputSlices);
		}

		if (spl_on != null) {
			SetupSlicer.SPL_ON = Boolean.parseBoolean(spl_on);
			if (product == null || product.isEmpty()) {
				throw new RuntimeException(
						"Pass a valid product: lick product=F1,F2");
			}
			SetupSlicer.PRODUCT = product;
			System.out.println("product: " + product);
		}
	}

	@SuppressWarnings("unchecked")
	private void printInstructions(ThreadInfo ti) {
		StringBuffer br = new StringBuffer();

		br.append(insn.getPosition());
		br.append(": ");
		br.append(insn.getClass().getSimpleName());
		br.append('(');
		br.append(insn);
		br.append(')');
		if (insn instanceof IfInstruction) {
			br.append(": " + ((IfInstruction) insn).getTarget().getPosition());
		}

		String s = br.toString();
		int operandTop = ti.getTopFrame().getTopPos();

		String report = "";
		for (int i = operandTop; i >= 0; i--) {
			Tree<SourceRef> stackSet = (Tree<SourceRef>) ti.getOperandAttr(i);
			if (stackSet != null) {
				report = report + " s" + i + "=" + stackSet.getID() + '('
						+ stackSet.isInteresting() + ')';
			} else {
				report = report + " s" + i + "=" + "null";
			}
		}

		if (operandTop > -1) {
			operandTop = ti.peek();
		}

		Tree<SourceRef> tree = UtilInstruction.getInstance(insn).getTree();
		System.out.printf("%s sId=%d %s\n", s, tree.getID(),
				insn.getFileLocation());
		if (s.contains("INVOKEVIRTUAL(invokevirtual java.net.URI.toASCIIString()Ljava/lang/String")) {
			System.out.print("");
		}
	}

	private void arrayStore(ThreadInfo ti, boolean isLongValue) {
		int longInc = isLongValue ? 1 : 0;
		@SuppressWarnings("unchecked")
		Tree<SourceRef> aref = (Tree<SourceRef>) ti.getOperandAttr(2 + longInc);
		@SuppressWarnings("unchecked")
		Tree<SourceRef> index = (Tree<SourceRef>) ti
				.getOperandAttr(1 + longInc);
		@SuppressWarnings("unchecked")
		Tree<SourceRef> value = isLongValue ? (Tree<SourceRef>) ti
				.getLongOperandAttr() : (Tree<SourceRef>) ti.getOperandAttr();
		Tree<SourceRef> set = TreeFactory
				.insert(UtilInstruction.getInstance(insn).getTree(), aref,
						index, value);
		if (isLongValue) {
			ti.setLongOperandAttr(set);
		} else {
			ti.setOperandAttr(set);
		}

	}

	@SuppressWarnings("unchecked")
	public void reportSliceOnTopOfStack(ThreadInfo ti, boolean isLong,
			boolean finalReport) {
		Tree<SourceRef> trees;

		if (isLong) {
			trees = (Tree<SourceRef>) ti.getLongOperandAttr();
		} else {
			trees = (Tree<SourceRef>) ti.getOperandAttr();
		}

		switch (SetupSlicer.reportType) {
		case SCREEN: {
			// FunctionalFlatSet<SourceRef> sset = elems.flatten();
			// System.out.println(sset.toString());
			break;
		}
		case MEMORY: {
			// FunctionalFlatSet<SourceRef> sset = elems.flatten();
			// SetupSlicer.lastSlice = sset;
			// System.out.println("SetupSlicer.lastSlice\n" +
			// SetupSlicer.lastSlice);
			// trees.print();
			break;
		}
		case DEBUG:
			// trees.print();
			break;
		case MEMORY_LIST:// This option get the slices in SetFactory
			// trees.print();
			// break;
		case FILE:
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exceptionHandled(JVM jvm) {
		System.out.println("exceptionHandled");
		/**
		 * there are not throw bytecodes for runtime exceptions. therefore, no
		 * object have been explicitly pushed onto the stack. we need to do it
		 */
		ThreadInfo ti = ThreadInfo.getCurrentThread();

		Tree<SourceRef> loc = UtilInstruction.getInstance(insn).getTree();
		Tree<SourceRef> operandSet = (Tree<SourceRef>) ti.getOperandAttr();
		if (operandSet == null) {
			operandSet = TreeFactory.EMPTY_TREE;
		}
		if (args != null) {
			switch (args.length) {
			case 1:
				this.saveOperandAttr(ti, false, loc, args[0], operandSet);
				break;
			case 2:
				this.saveOperandAttr(ti, false, loc, args[0], args[1],
						operandSet);
				break;
			default:
				throw new UnsupportedOperationException();
			}
		}
		this.throwException = true;
		this.ignore = true;
	}

	@SuppressWarnings("unchecked")
	private void catchException(ThreadInfo ti) {
		// the dependencies correctly
		MethodInfo mi = insn.getMethodInfo();
		while (!cflowControl.isEmpty() && peekCflowControl().free(mi)) { // clean
			// controlFlow
			popCflow();
		}

		if (SetupSlicer.TREAT_EXCEPTION) {
			// exceptions
			Instruction beforeInstruction = UtilMethodInfo.getInstance(mi)
					.getInstructionBefore(insn); // TODO
			// optimize
			// this

			if (beforeInstruction instanceof GOTO) {// Normal case
				GOTO goto_ = (GOTO) beforeInstruction;
				CflowInfo cf = new CflowInfo(goto_);
				cf.setTree(TreeFactory.EMPTY_TREE);// This statement was not
				// executed

				Tree<SourceRef>[] sets = new Tree[] {
						UtilInstruction.getInstance(insn).getTree(),
						(Tree<SourceRef>) ti.getOperandAttr() };
				pushCflowControl(sets, cf);
			} else {

				if (!cflowControl.isEmpty()) {
					CflowInfo top = peekCflowControl();
					Tree<SourceRef> set = TreeFactory.insert(UtilInstruction
							.getInstance(insn).getTree(), top.getTree(),
							(Tree<SourceRef>) ti.getOperandAttr());
					top.setTree(set);
				} else {
					Tree[] trees = new Tree[] { (Tree<SourceRef>) ti
							.getOperandAttr() };
					pushCflowControl(trees, new PERMANENT_FLOW(insn));
				}

			}

		}
		this.throwException = false;
	}

	@Override
	public void searchFinished(Search search) {
		System.out.println("searchFinished");
		StringBuffer sb = new StringBuffer();
		sb.append("\n------- ConfigInfo ---------\n");
		sb.append("Use dynamic impact=");
		sb.append(SetupSlicer.CHANGE_SLICE);
		sb.append('\n');
		System.out.println(sb.toString());
		// System.out.println("EMPTY_SETS:" + SetFactory.countEmpty);
		// System.out.println("SET1:" + SetFactory.countS1);
		// System.out.println("SET2:" + SetFactory.countS2);
		// System.out.println("SET3:" + SetFactory.countS3);
		// System.out.println("SET4:" + SetFactory.countS4);
		// System.out.println("SET5:" + SetFactory.countS5);
		// System.out.println("SET6:" + SetFactory.countS6);
		// System.out.println("SET7:" + SetFactory.countS7);
		// System.out.println("SET8:" + SetFactory.countS8);
		// System.out.println("SET9:" + SetFactory.countS9);
		// System.out.println("SET10:" + SetFactory.countS10);
		// System.out.println("SET11:" + SetFactory.countS11);
		// System.out.println("SET12:" + SetFactory.countS12);

		if (SetupSlicer.lastSlice != null) {
			HashSet<SourceRef> set = new HashSet<SourceRef>();
			set.addAll(SetupSlicer.lastSlice.getElements());
			ArrayList<SourceRef> arrayList = new ArrayList<SourceRef>(
					set.size());
			for (SourceRef s : set) {
				arrayList.add(s);
			}
			SetupSlicer.lastSlice.replaceElements(arrayList);
		}
		resetSlice();

		if (SetupSlicer.SPL_ON && SetupSlicer.reportType == REPORT.MEMORY_LIST) {

			if (SetupSlicer.outputSlices == null) {
				throw new RuntimeException("set the output.slices config param");
			}
			InteractionProcessor iproc = new InteractionProcessor(
					SetupSlicer.PRODUCT, ck_file);
			System.out.println("nInteractions--->"
					+ SetupSlicer.interactionSet.size());
			TreeFactory.serlializeSlices(SetupSlicer.interactionSet,
					SetupSlicer.outputSlices);
			// System.out.println(iproc.toString());
		}

		if (SetupSlicer.saveSliceIdIn != null) {
			TreeFactory.serializeSliceId(SetupSlicer.saveSliceIdIn);
		}

	}

	private void resetSlice() {
		/** RESET data **/

		cflowControl = new ArrayList<CflowInfo>();
		callingContext = new ArrayList<CflowInfo>();
		hack = null;
		insn = null;
		start = false;
		unsupportedOperation = false;
		initializer = false;
		ignore = false;
		throwException = false;
		args = null;
		nativeArgs = null;

	}

	// ********* ********** ****** Handle calls cinit ********* **********
	private boolean requiresClinitCalls(ThreadInfo ti, ClassInfo ci,
			Instruction insn) {
		if (!ci.isRegistered()) {
			ci.registerClass(ti);
		}
		// since this is a NEW, we also have to pushClinit
		if (!ci.isInitialized()) {
			if (initializeClass(ci, ti, insn)) {
				return true;
			}
		}

		return false;
	}

	private boolean initializeClass(ClassInfo ci, ThreadInfo ti,
			Instruction continuation) {
		int countFrames = 0;// pushedFrames
		// push clinits of class hierarchy (upwards, since call stack is LIFO)
		for (; ci != null; ci = ci.getSuperClass()) {
			if (checkClinit(ci, ti, continuation)) {
				continuation = null;
				countFrames++;
			}
		}
		return (countFrames > 0);
	}

	private boolean checkClinit(ClassInfo ci, ThreadInfo ti,
			Instruction continuation) {// pushClinit
		int stat = ci.getStaticElementInfo().getStatus();
		if (stat != ClassInfo.INITIALIZED) {
			if (stat != ti.getGlobalId()) {

				MethodInfo mi = ci.getMethod("<clinit>()V", false);
				if (mi != null) {
					return true;
				}
			}
		}
		return false;
	}

	// ********* ********** ****** ********* **********

	@Override
	public void searchStarted(Search search) {// TODO move setup init to here
		System.out.println("searchStarted");
		this.setMaxSearchDepth(search);
		super.searchStarted(search);
		init();
	}

	/*
	 * ** this method defines the depth limit policy
	 */
	public void setMaxSearchDepth(Search search) {
		int searchDepth = Integer.MAX_VALUE;
		System.out.println("Get depth: " + search.getDepthLimit());
		if (search.getDepthLimit() > 0) {
			int initialDepth = search.getVM().getPathLength();

			if ((Integer.MAX_VALUE - initialDepth) > search.getDepthLimit()) {
				searchDepth = search.getDepthLimit() + initialDepth;
			}
		}
		System.out.println("Profundidade final: " + searchDepth);
		// int searchDepth = 25;
		search.setDepthLimit(searchDepth);
	}
}

class PERMANENT_FLOW extends CflowInfo {

	public PERMANENT_FLOW(Instruction insn) {
		super(insn);
	}

	public boolean free(Instruction insn) {
		return false;
	}

	public Instruction getPosdom() {
		return null;
	}
}

class CflowInfo {
	protected Instruction branch;
	protected Tree<SourceRef> tree;

	public boolean free(Instruction insn) {
		return insn.getMethodInfo() == this.branch.getMethodInfo()
				&& insn.getPosition() >= this.getPosdom().getPosition();
	}

	public boolean free(MethodInfo mi) {
		return mi == this.branch.getMethodInfo();
	}

	public CflowInfo(Instruction insn) {
		this.branch = insn;
		this.tree = UtilInstruction.getInstance(insn).getTree();
	}

	public Tree<SourceRef> getTree() {
		return this.tree;
	}

	public void setTree(Tree<SourceRef> tree) {
		this.tree = tree;
	}

	public Instruction getPosdom() {
		return UtilInstruction.getInstance(this.branch).getFirstPosDominator();
	}

	public String toString() {
		StringBuffer br = new StringBuffer();
		br.append("*(");
		br.append(getPosdom().getPosition());
		br.append(',');
		br.append(this.branch.getMethodInfo());
		br.append(")*");
		return br.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((tree == null) ? 0 : tree.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CflowInfo)) {
			throw new RuntimeException("Other isn't a Cflow object");
		}
		return ((CflowInfo) other).getPosdom() == this.getPosdom();
	}
}
