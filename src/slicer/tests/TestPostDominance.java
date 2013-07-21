package slicer.tests;

import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import slicer.integrating.UtilInstruction;
import slicer.integrating.UtilMethodInfo;

public class TestPostDominance {
	
	private static String className = "slicer.tests.PostDomSamples";
	
	/***************************************
	 * TODO: improve treatment of multiple 
	 * exit points on slicing
	 ***************************************/
	
	// testSimpleReturn(I)I
	@Test(timeout=2000)
	public void testSimpleReturn() {
		String mName = "testSimpleReturn(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(2));
//		Assert.assertEquals(7, (int) res.get(2));
	}
		
	@Test(timeout=2000)
	public void testIfIfReturn() {
		String mName = "testIfIfReturn(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
//		Assert.assertEquals(10, (int) res.get(4));
//		Assert.assertEquals(10, (int) res.get(7));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(4));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(7));
	}
		
	@Test
	public void testReturnFromInsideLoop() {		
		Map<Integer,Integer> res = showPostDominators(
				"slicer.tests.ReturnFromLoopSample", 
				"returnFromInsideLoop(I)I", 
				true);
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(2));
//		Assert.assertEquals(13, (int) res.get(2));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(6));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(15));
		Assert.assertEquals(25, (int) res.get(22));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(25));
	}
	
	@Test
	public void testReturnFromOutsideLoop() {		
		String mName = "returnFromOutsideLoop(I)I";
		Map<Integer,Integer> res = showPostDominators("slicer.tests.ReturnFromLoopSample", mName, true);
//		Assert.assertEquals(13, (int) res.get(2));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(2));
//		Assert.assertEquals(13, (int) res.get(5));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(5));
//		Assert.assertEquals(13, (int) res.get(6));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(6));
//		Assert.assertEquals(25, (int) res.get(15));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(15));
		Assert.assertEquals(25, (int) res.get(22));
//		Assert.assertEquals(29, (int) res.get(28));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(28));
		Assert.assertEquals(30, (int) res.get(29));
	}

	/** ---------------------------------------------------- **/
	
	@Test
	public void testReturnFromLoopSimple() {		
		String mName = "returnFromLoopSimple(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(8, (int) res.get(6));
		Assert.assertEquals(8, (int) res.get(7));
		Assert.assertEquals(11, (int) res.get(5));
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(12));
	}

	
	@Test
	public void testIfElseWithReturns() {		
		String mName = "testIfElseWithReturns()Z";
		Map<Integer,Integer> res = showPostDominators("slicer.tests.SliceSamples", mName, true);
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(4));
	}
	
	@Test
	public void testBiggerSwitchSample() {		
		String mName = "getWhatToShowSimple(I)I";
		Map<Integer,Integer> res = showPostDominators("slicer.tests.BiggerSwitchSample", mName, true);
		/**
		 * TODO: this could be Integer.MAX_VALUE
		 * 
		 * this is a very specific case of a switch 
		 * with default and without immediate 
		 * post-dominators 
		 */
		Assert.assertEquals(Integer.MAX_VALUE, (int) res.get(4));
	}

	
	@Test(timeout=2000)
	public void testshortCircuitOr() {
		String mName = "shortCircuitOr(ZZ)Z";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(7, (int) res.get(1));
		//TODO: check if this is correct for GOTO
		Assert.assertEquals(7, (int) res.get(3));
		Assert.assertEquals(7, (int) res.get(5));
	}
	
	@Test
	public void testshortCircuitAnd() {
		String mName = "shortCircuitAnd(ZZ)Z";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(7, (int) res.get(1));
		//TODO: check if this is correct for GOTO
		Assert.assertEquals(7, (int) res.get(3));
		Assert.assertEquals(7, (int) res.get(5));
	}
	
	@Test(timeout=2000)
	public void testIf2() {
		String mName = "testIf2(I)V";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(5, (int) res.get(2));
		Assert.assertEquals(10, (int) res.get(7));
	}
	
	@Test(timeout=2000)
	public void testIfIf1() {
		String mName = "testIfIf1(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(10, (int) res.get(4));
		Assert.assertEquals(10, (int) res.get(7));
	}
	
	@Test(timeout=2000)
	public void testIfIf2() {
		String mName = "testIfIf2(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(11, (int) res.get(4));
		Assert.assertEquals(10, (int) res.get(7));
	}
	
	
	@Test(timeout=2000)
	public void testIfElse() {
		String mName = "testIfElse(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(10, (int) res.get(4));
		Assert.assertEquals(10, (int) res.get(7));
	}
	
	/**
	 * start to add back edges
	 */
	
	@Test(timeout=2000)
	public void testBreak() {
		String mName = "testBreak(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(12, (int) res.get(5));
		Assert.assertEquals(12, (int) res.get(8));
		Assert.assertEquals(12, (int) res.get(11));
	}
	
	@Test()
	public void testBreak2() {
		String mName = "testBreak2(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(14, (int) res.get(5));
		Assert.assertEquals(14, (int) res.get(8));
		Assert.assertEquals(14, (int) res.get(13));
	}
	
	@Test(timeout=2000)
	public void testWhileWhile() {
		String mName = "testWhileWhile(II)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(15, (int) res.get(4));
		// back loop of the inner loop takes control
		// to much farther position.
		Assert.assertEquals(15, (int) res.get(7));
		Assert.assertEquals(15, (int) res.get(14));
	}
	
	@Test(timeout=2000)
	public void testBreakOuter() {
		String mName = "testBreakOuter(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(13, (int) res.get(4));
		//TODO: the error seems related to nested while 
		// and not to the outer break.  See: testWhileWhile
		Assert.assertEquals(13, (int) res.get(7));
		Assert.assertEquals(13, (int) res.get(11));
		Assert.assertEquals(13, (int) res.get(12));
	}
	
	@Test(timeout=2000)
	public void testContinue() {
		String mName = "testContinue(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(12, (int) res.get(3));
		Assert.assertEquals(12, (int) res.get(6));
		Assert.assertEquals(12, (int) res.get(9));
		Assert.assertEquals(12, (int) res.get(11));
	}

	
	@Test(timeout=2000)
	public void testBreakContinue() {
		String mName = "testBreakContinue(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(13, (int) res.get(3));
		Assert.assertEquals(13, (int) res.get(6));
		Assert.assertEquals(13, (int) res.get(9));
		Assert.assertEquals(13, (int) res.get(12));
	}
	
	@Test
	public void testSwitch1() {
		String mName = "testSwitch1(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(10, (int) res.get(1));
		Assert.assertEquals(10, (int) res.get(4));
		Assert.assertEquals(10, (int) res.get(7));
	}
	
	@Test(timeout=2000)
	public void testSwitch2() {
		String mName = "testSwitch2(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(11, (int) res.get(1));
		Assert.assertEquals(11, (int) res.get(4));
		Assert.assertEquals(11, (int) res.get(5));
		Assert.assertEquals(11, (int) res.get(8));
	}	
	
	@Test(timeout=2000)
	public void testSwitch3() {
		String mName = "testSwitch3(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(14, (int) res.get(1));
		Assert.assertEquals(14, (int) res.get(4));
		Assert.assertEquals(14, (int) res.get(5));
		Assert.assertEquals(14, (int) res.get(8));
		Assert.assertEquals(14, (int) res.get(11));
	}
	
	@Test
	public void testSwitch4() {
		String mName = "testSwitch4(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(14, (int) res.get(1));
		Assert.assertEquals(6, (int) res.get(4));
		Assert.assertEquals(14, (int) res.get(8));
		Assert.assertEquals(14, (int) res.get(11));
	}
	
	@Test
	public void testSwitch2C() {
		String mName = "testSwitch2C(C)Z";
		Map<Integer,Integer> res = showPostDominators(className, mName, true);
		Assert.assertEquals(6, (int) res.get(3));
		Assert.assertEquals(11, (int) res.get(7));
		Assert.assertEquals(11, (int) res.get(9));
	}
	
	@Test(timeout=2000)
	public void testWhileSwitch1() {
		String mName = "testWhileSwitch1(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(14, (int) res.get(2));
		Assert.assertEquals(14, (int) res.get(4));
		Assert.assertEquals(14, (int) res.get(7));
		Assert.assertEquals(14, (int) res.get(10));
		Assert.assertEquals(14, (int) res.get(13));
	}	
	
	@Test(timeout=2000)
	public void testWhileSwitch2() {
		String mName = "testWhileSwitch2(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(15, (int) res.get(2));
		Assert.assertEquals(15, (int) res.get(4));
		Assert.assertEquals(15, (int) res.get(7));
		Assert.assertEquals(15, (int) res.get(8));
		Assert.assertEquals(15, (int) res.get(11));
		Assert.assertEquals(15, (int) res.get(14));
	}
	
	@Test(timeout=2000)
	public void testWhileSwitch3() {
		String mName = "testWhileSwitch3(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(18, (int) res.get(2));
		Assert.assertEquals(18, (int) res.get(4));
		Assert.assertEquals(18, (int) res.get(7));
		Assert.assertEquals(18, (int) res.get(8));
		Assert.assertEquals(18, (int) res.get(11));
		Assert.assertEquals(18, (int) res.get(14));
		Assert.assertEquals(18, (int) res.get(17));
	}
	
	@Test(timeout=2000)
	public void testFlowErr3() {
		String mName = "testFlowErr3(I)I";
		Map<Integer,Integer> res = showPostDominators(className, mName, false);
		Assert.assertEquals(17, (int) res.get(5));
		/**
		 * this is curious but makes sense.  the 
		 * post-dominator of an if-then-else declared
		 * entirely inside the loop is outside the
		 * loop because one of the branches contain
		 * a break. 
		 */
		Assert.assertEquals(17, (int) res.get(8));
		Assert.assertEquals(17, (int) res.get(11));
		Assert.assertEquals(17, (int) res.get(16));
	}
	
	/*************************************
	 * utility method of this test driver
	 * @param className TODO
	 *************************************/
	
	private static Map<Integer,Integer> showPostDominators(String className, String mName, boolean logScreen) {
		// initialize JPF's data structures
		gov.nasa.jpf.tool.RunJPF.main(new String[]{className});
		ClassInfo aci = ClassInfo.getResolvedClassInfo(className);
		MethodInfo methInfo = aci.getMethod(mName, false);
		// print
		int k = 0;
		if (logScreen) {
			for (Instruction i : methInfo.getInstructions()) {
				System.out.printf("%d: %s\n", k++, i.toString());	
			}
		}
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		// compute postdom
		UtilMethodInfo.getInstance(methInfo).updatePostDominators();
		for (Instruction i : methInfo.getInstructions()) {
			Instruction j = UtilInstruction.getInstance(i).getFirstPosDominator();
			if (j != null) {
				int offFrom = i.getInstructionIndex(), offTo = j.getInstructionIndex();
				if (logScreen) { // show post dominators
					System.out.printf("%d => %d\n", offFrom, offTo);
				}
				result.put(offFrom, offTo);
			}
		}
		return result;
	}

}
