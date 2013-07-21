package slicer.tests;

import gov.nasa.jpf.util.SourceRef;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import slicer.SetupSlicer;
import slicer.FunctionalFlatSet;


public class TestSlicer {
	
	/**
	 * TODO: looks like these tests only run in
	 * separate executions.  please identify 
	 * which parts of the state needs to be restores
	 * and add that to a setup() function.  it
	 * is important that we run all tests in a
	 * single run.
	 */
	
	@Test
	public void testANDPrimitiveBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(1));
		
		Assert.assertFalse(lines.contains(8));
		Assert.assertFalse(lines.contains(14));
		
		Assert.assertTrue(lines.contains(9));
		Assert.assertTrue(lines.contains(10));
		Assert.assertTrue(lines.contains(11));
		Assert.assertTrue(lines.contains(12));
	}
	
	@Test
	public void testANDObjectBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(2));
		
		Assert.assertTrue(lines.contains(19));
		Assert.assertTrue(lines.contains(20));
		Assert.assertTrue(lines.contains(21));
		Assert.assertTrue(lines.contains(22));
	}
	
	@Test
	public void testANDMixingPrimitiveAndObjectBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(3));
		
//		System.out.println(lines);
		
		Assert.assertFalse(lines.contains(31));

		Assert.assertTrue(lines.contains(28));
		Assert.assertTrue(lines.contains(29));
		Assert.assertTrue(lines.contains(30));
		Assert.assertTrue(lines.contains(32));
		Assert.assertTrue(lines.contains(33));		
	}
	
	@Test
	public void testANDWithConditional() {
		Set<Integer> lines = filterLines(className, runSlicer(4));
		Assert.assertFalse(lines.contains(40));

		Assert.assertTrue(lines.contains(38));
		Assert.assertTrue(lines.contains(39));
		Assert.assertTrue(lines.contains(43));
		Assert.assertTrue(lines.contains(46));
		Assert.assertTrue(lines.contains(47));
	}
	
	
	@Test
	public void testORPrimitiveBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(5));
		System.out.println(lines);
		
		Assert.assertFalse(lines.contains(55));

		Assert.assertTrue(lines.contains(54));
		Assert.assertTrue(lines.contains(56));
	}
	
	@Test
	public void testORObjectBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(6));
		Assert.assertTrue(lines.contains(64));
	}
	
	@Test
	public void testORMixingPrimitiveAndObjectBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(7));
		Assert.assertFalse(lines.contains(73));
		
		Assert.assertTrue(lines.contains(70));
		Assert.assertTrue(lines.contains(71));
		Assert.assertTrue(lines.contains(72));
		Assert.assertTrue(lines.contains(74));
		Assert.assertTrue(lines.contains(75));
	}
	
	@Test
	public void testORWithConditional() {
		Set<Integer> lines = filterLines(className, runSlicer(8));
		System.out.println(lines);
		Assert.assertFalse(lines.contains(82));
		Assert.assertFalse(lines.contains(83));
		Assert.assertFalse(lines.contains(84));
		
		Assert.assertTrue(lines.contains(80));
		Assert.assertTrue(lines.contains(81));
		Assert.assertTrue(lines.contains(85));
		Assert.assertTrue(lines.contains(88));
		Assert.assertTrue(lines.contains(89));
	}
	
	@Test
	public void testANDWithNullObjects() {
		Set<Integer> lines = filterLines(className, runSlicer(9));
		Assert.assertFalse(lines.contains(97));
		
		Assert.assertTrue(lines.contains(95));
		Assert.assertTrue(lines.contains(96));
		Assert.assertTrue(lines.contains(98));
		Assert.assertTrue(lines.contains(99));
		Assert.assertTrue(lines.contains(100));
	}
	
	@Test
	public void testORWithNullObjects() {
		Set<Integer> lines = filterLines(className, runSlicer(10));
		Assert.assertFalse(lines.contains(110));
		Assert.assertFalse(lines.contains(111));
		
		Assert.assertTrue(lines.contains(109));
		Assert.assertTrue(lines.contains(112));
		Assert.assertTrue(lines.contains(113));
		Assert.assertTrue(lines.contains(114));
	}
	
	@Test
	public void testNestedORPrimitiveBoolean() {
		Set<Integer> lines = filterLines(className, runSlicer(11));
		Assert.assertFalse(lines.contains(126));
		
		Assert.assertTrue(lines.contains(125));
		Assert.assertTrue(lines.contains(127));
		Assert.assertTrue(lines.contains(128));
	}
	
	@Test
	public void testNestedORPrimitiveBoolean2() {
		Set<Integer> lines = filterLines(className, runSlicer(12));		
		Assert.assertTrue(lines.contains(134));		
		Assert.assertTrue(lines.contains(135));
		Assert.assertTrue(lines.contains(136));
		Assert.assertTrue(lines.contains(137));
	}
	
	@Test
	public void testAndPrimitiveBoolean1() {
		Set<Integer> lines = filterLines(className, runSlicer(13));
		Assert.assertFalse(lines.contains(144));
		
		Assert.assertTrue(lines.contains(143));		
		Assert.assertTrue(lines.contains(145));
	}
	
	@Test
	public void testAndPrimitiveBoolean2() {
		Set<Integer> lines = filterLines(className, runSlicer(14));
		Assert.assertTrue(lines.contains(151));
		Assert.assertTrue(lines.contains(152));		
		Assert.assertTrue(lines.contains(153));
	}
	
	@Test
	public void testAndPrimitiveBoolean3() {
		Set<Integer> lines = filterLines(className, runSlicer(15));
		Assert.assertFalse(lines.contains(161));
		
		Assert.assertTrue(lines.contains(159));
		Assert.assertTrue(lines.contains(160));		
		Assert.assertTrue(lines.contains(162));
	}
	
	@Test
	public void testAndPrimitiveBoolean4() {
		Set<Integer> lines = filterLines(className, runSlicer(16));
		
		Assert.assertTrue(lines.contains(168));
		Assert.assertTrue(lines.contains(169));		
		Assert.assertTrue(lines.contains(170));
		Assert.assertTrue(lines.contains(171));
	}
	
	@Test
	public void testPrimitiveSwith() {
		Set<Integer> lines = filterLines(className, runSlicer(17));
		System.out.println(lines);
		Assert.assertFalse(lines.contains(177));
		Assert.assertFalse(lines.contains(179));		
		Assert.assertFalse(lines.contains(180));
		Assert.assertFalse(lines.contains(181));
		Assert.assertFalse(lines.contains(182));
		
		Assert.assertTrue(lines.contains(176));
		Assert.assertTrue(lines.contains(178));		
		Assert.assertTrue(lines.contains(183));
	}

	@Test
	public void testWhileWithBreak() {
		Set<Integer> lines = filterLines(className, runSlicer(18));
		System.out.println(lines);
		Assert.assertTrue(lines.contains(191));
		Assert.assertTrue(lines.contains(192));		
		Assert.assertTrue(lines.contains(193));
		Assert.assertTrue(lines.contains(194));
		Assert.assertTrue(lines.contains(196));		
		Assert.assertTrue(lines.contains(198));
	}
	
	@Test
	public void testDoWhileWithBreak() {
		Set<Integer> lines = filterLines(className, runSlicer(19));
		System.out.println(lines);
		Assert.assertFalse(lines.contains(209));
		
		Assert.assertTrue(lines.contains(203));
		Assert.assertTrue(lines.contains(206));		
		Assert.assertTrue(lines.contains(207));
		Assert.assertTrue(lines.contains(210));
		Assert.assertTrue(lines.contains(211));		
		Assert.assertTrue(lines.contains(212));
	}
	
	@Test
	public void testWhileWithContinue() {
		Set<Integer> lines = filterLines(className, runSlicer(20));

		Assert.assertTrue(lines.contains(217));
		Assert.assertTrue(lines.contains(218));		
		Assert.assertTrue(lines.contains(219));
		Assert.assertTrue(lines.contains(220));
		Assert.assertTrue(lines.contains(221));		
		Assert.assertTrue(lines.contains(223));
	}
	
	@Test
	public void testDoWhileWithContinue1() {
		Set<Integer> lines = filterLines(className, runSlicer(21));
		 
		Assert.assertTrue(lines.contains(229));
		Assert.assertTrue(lines.contains(231));		
		Assert.assertTrue(lines.contains(232));
		Assert.assertTrue(lines.contains(235));
		Assert.assertTrue(lines.contains(236));	
	}
	
	@Test
	public void testDoWhileWithContinue2() {
		Set<Integer> lines = filterLines(className, runSlicer(22));
		Assert.assertTrue(lines.contains(240));
		Assert.assertTrue(lines.contains(242));		
		Assert.assertTrue(lines.contains(243));
		Assert.assertTrue(lines.contains(246));
		Assert.assertTrue(lines.contains(247));	
	}
	
	@Test
	public void testInsertBST() {
		Set<Integer> lines = filterLines("slicer.tests.util.BST", runSlicer(23));
		Assert.assertTrue(lines.contains(9));
		Assert.assertTrue(lines.contains(13));		
		Assert.assertTrue(lines.contains(14));
		Assert.assertTrue(lines.contains(15));
		Assert.assertTrue(lines.contains(19));		
		Assert.assertTrue(lines.contains(20));
		Assert.assertTrue(lines.contains(21));		
		Assert.assertTrue(lines.contains(28));
		Assert.assertTrue(lines.contains(29));
		Assert.assertTrue(lines.contains(31));
		Assert.assertTrue(lines.contains(39));
	}
	
	@Test
	public void testFlowErr() {
		Set<Integer> lines = filterLines(className, runSlicer(24));
		System.out.println(lines);
		Assert.assertTrue(lines.contains(260));		
		Assert.assertTrue(lines.contains(261));
		Assert.assertTrue(lines.contains(262));
		Assert.assertTrue(lines.contains(265));
	}
	
	@Test
	public void testFlowErr1() {
		Set<Integer> lines = filterLines(className, runSlicer(25));
		Assert.assertTrue(lines.contains(273));
	}
	
	@Test
	public void testFlowErr2() {
		Set<Integer> lines = filterLines(className, runSlicer(26));
		Assert.assertTrue(lines.contains(286));		
		Assert.assertTrue(lines.contains(287));
		Assert.assertTrue(lines.contains(288));
		Assert.assertTrue(lines.contains(290));
	}

	@Test
	public void testFlowErr3() {
		Set<Integer> lines = filterLines(className, runSlicer(27));
		System.out.println(lines);
		/************************************
		 * 
		 * originally though this was an error,
		 * but due to the break in the if-the-else
		 * the post-dominance frontier extends 
		 * till outside the loop.
		 * 
		 ************************************/ 
		Assert.assertTrue(lines.contains(302));
		
		Assert.assertTrue(lines.contains(300));		
		Assert.assertTrue(lines.contains(301));
		Assert.assertTrue(lines.contains(308));
	}
	
	@Test
	public void testFlowErr4() {
		Set<Integer> lines = filterLines(className, runSlicer(28));

		Assert.assertTrue(lines.contains(316));		
		Assert.assertTrue(lines.contains(317));
		Assert.assertTrue(lines.contains(318));
		Assert.assertTrue(lines.contains(322));		
	}

	@Test
	public void testFlowErr5() {
		Set<Integer> lines = filterLines(className, runSlicer(29));
		System.out.println(lines);
		Assert.assertTrue(lines.contains(332));		
		Assert.assertTrue(lines.contains(333));
		Assert.assertTrue(lines.contains(334));
		Assert.assertTrue(lines.contains(338));
		Assert.assertTrue(lines.contains(342));	
	}		
	
	@Test
	public void  testIfElseWithReturns(){
		Set<Integer> lines = filterLines(className, runSlicer(30));
		System.out.println(lines);
		Assert.assertTrue(lines.contains(348));
		Assert.assertTrue(lines.contains(349));		
		Assert.assertTrue(lines.contains(352));
		
		Assert.assertFalse(lines.contains(350));
	}
	
	@Test
	public void testInsertBSTChangeSlice() {
		SetupSlicer.CHANGE_SLICE = true;
		SetupSlicer.diffFile = "/home/elton/workspace/jpf-core/src/main/slicer/tests/util/diff-paper-example.txt";
		Set<Integer> lines = filterLines("slicer.tests.util.BST", runSlicer(23));
		System.out.println(lines);
		Assert.assertFalse(lines.contains(9));
		Assert.assertFalse(lines.contains(13));		
		Assert.assertFalse(lines.contains(14));
		Assert.assertFalse(lines.contains(15));
		Assert.assertFalse(lines.contains(19));		
		Assert.assertFalse(lines.contains(20));
		Assert.assertFalse(lines.contains(21));	
		
		Assert.assertTrue(lines.contains(28));
		Assert.assertTrue(lines.contains(29));
		Assert.assertTrue(lines.contains(31));
		Assert.assertTrue(lines.contains(39));
		SetupSlicer.CHANGE_SLICE = false;
	}
	
	public static String className = "slicer.tests.SliceSamples";
	
	public static FunctionalFlatSet<SourceRef> runSlicer(int testCaseId) {
		//TODO: disable output print stream
		SetupSlicer.log = SetupSlicer.Log.ALL;
		SetupSlicer.reportType = SetupSlicer.REPORT.MEMORY;
		String[] params = new String[]{
				"+listener=gov.nasa.jpf.listener.DynamicSlicerElton",
				"+report.console.property_violation=error",
				"+main.package=slicer.tests",
				"+test.package=NO",
				className,
				testCaseId+"",
		};
		gov.nasa.jpf.tool.RunJPF.main(params);
		return SetupSlicer.lastSlice;
	}
	
	public static Set<Integer> filterLines(String cname, FunctionalFlatSet<SourceRef> srs) {
		if (srs == null) {
			throw new RuntimeException("FATAL ERROR: Empty slice!");
		}
		Set<Integer> result = new HashSet<Integer>();
		for (SourceRef sr : srs) {
			String fname = sr.getFileName().substring(0, sr.getFileName().indexOf('.'));
			fname = fname.replace('/', '.');
			if (fname.equals(cname)) {
				result.add(sr.line);
			}
		}
		return result;
	}

}