package slicer.tests;

import slicer.Slicer;

public class SliceSamples {

	public static void testANDPrimitiveBoolean() {
		boolean result = false;
		boolean a = true;
		boolean b = true;
		if (b) {
			result = a;
		}
		Slicer.stop();
		Slicer.reportBoolean(result);
	}

	public static void testANDObjectBoolean() { // For an object it is only
												// enough to know the moment of
												// its creation. For example, if
												// I call Slicer.reportObject
												// method passing result as
												// parameter, just the line of
												// creation object result will
												// be interesting.
		boolean a = true;
		boolean b = true;
		boolean c = true;
		boolean result = a && c && b;
		Slicer.reportBoolean(result);
	}

	public static void testANDMixingPrimitiveAndObjectBoolean() {
		Boolean result;
		Boolean a = new Boolean(true);
		Boolean b = new Boolean(true);
		boolean c = false;
		boolean d = true;
		result = new Boolean(a.booleanValue() && b.booleanValue() && c && d);
		Slicer.reportBoolean((result.booleanValue()));// [29,30,Boolean.java:70,28,194,33,
														// 32]
	}

	public static void testANDWithConditional() {
		boolean result;
		boolean a = true;
		boolean b = false;
		int c = 0;
		int d = 10;
		int e = d + 4;
		if (a == b) {
			c = e;
		} else if (a != b) {
			c = 10;
		}
		Slicer.report(c);// [43,195,46,39,38,47]
	}

	public static void testORPrimitiveBoolean() {
		boolean result;
		boolean a = true;
		boolean b = false;
		result = a || b;
		Slicer.reportBoolean(result);
	}

	public static void testORObjectBoolean() {
		Boolean result;
		Boolean a = new Boolean(true);
		Boolean b = new Boolean(true);
		result = new Boolean(a.booleanValue() || b.booleanValue());
		Slicer.reportObject(result);
	}

	public static void testORMixingPrimitiveAndObjectBoolean() {// Test with OR
																// is not
																// working well.
																// It should
																// show the
																// lines 70, 71,
																// 72 and 74 but
																// it is just
																// shown the
																// lines 70 and
																// 74 in the
																// slice
		Boolean result;
		Boolean a = new Boolean(false);
		Boolean b = new Boolean(false);
		boolean c = true;
		boolean d = false;
		result = new Boolean(a.booleanValue() || b.booleanValue() || c || d);
		Slicer.reportBoolean((result.booleanValue()));// [Boolean.java:70,198,74,71,70,72,75]
	}

	public static void testORWithConditional() {// Test with OR is not working
												// well. It should show the
												// lines 80, 81, 85, 88 and 89
												// but it is just shown the
												// lines 80, 81, 85 and 89 in
												// the slice
		boolean result;
		boolean a = true;
		boolean b = false;
		int c = 0;
		int d = 10;
		int e = d + 4;
		if (a == b) {
			c = e;
		} else if (a || b) {
			c = 10;
		}
		Slicer.report(c);
	}

	public static void testANDWithNullObjects() {
		Boolean a = null;
		Boolean b = new Boolean(true);
		int x = 0;
		int y = 2;
		if (a == null && b.booleanValue()) {
			x = y + 2;
		} else {
			x = 10;
		}
		Slicer.report(x);
	}

	public static void testORWithNullObjects() {// Test with OR is not working
												// well. It should show the
												// lines 109, 102, 113 and 114
												// but it is just shown the
												// lines 112 and 114 in the
												// slice
		Boolean a = null;
		Boolean b = new Boolean(false);
		int x = 0;
		int y = 2;
		if (a == null || b.booleanValue()) {
			x = y + 2;
		}
		Slicer.report(x);
	}

	// public static void testMixingANDORPrimitiveBoolean(){
	//
	// }

	public static void testNestedORPrimitiveBoolean() {// ERROR, should show
														// 127,126,125
		boolean result;
		boolean a = false;
		boolean b = false;
		boolean c = true;
		result = a || c || b;
		Slicer.reportBoolean(result);
	}

	public static void testNestedORPrimitiveBoolean2() {// ERROR, should show
														// 133, 134, 135, 136
		boolean result;
		boolean a = false;
		boolean b = false;
		boolean c = false;
		result = a || b || c;
		Slicer.reportBoolean(result);
	}

	public static void testAndPrimitiveBoolean1() {
		boolean result;
		boolean a = false;
		boolean b = false;
		result = a && b;
		Slicer.reportBoolean(result);
	}

	public static void testAndPrimitiveBoolean2() {
		boolean result;
		boolean a = true;
		boolean b = false;
		result = a && b;
		Slicer.reportBoolean(result);
	}

	public static void testAndPrimitiveBoolean3() {
		boolean result;
		boolean a = true;
		boolean b = false;
		boolean c = false;
		result = a && b && c;
		Slicer.reportBoolean(result);
	}

	public static void testAndPrimitiveBoolean4() {
		boolean result;
		boolean a = true;
		boolean b = true;
		boolean c = false;
		result = a && b && c;
		Slicer.reportBoolean(result);
	}

	public static void testPrimitiveSwith() {
		int key = 3;
		int a = 0;
		switch (key) {
		case 2:
			a = 3;
			break;
		case 3:
			a = 2;
			break;
		default:
			break;
		}
		Slicer.report(a);
	}

	public static void testWhileWithBreak() {
		int a = 0;
		while (a < 4) {
			if (a == 2) {
				break;
			}
			a++;
		}
		int b = 3;
		Slicer.report(b);
	}

	public static void testDoWhileWithBreak() {
		int a = 0;
		int b = 9;
		do {
			if (a == 2) {
				break;
			}
			b++;
			a++;
		} while (a < 4);
		int c = 3;
		Slicer.report(c);
	}

	public static void testWhileWithContinue() {
		int a = 0;
		while (a < 5) {
			a++;
			if (a < 3) {
				continue;
			}
			a = a + 2;
		}
		Slicer.report(a);
	}

	public static void testDoWhileWithContinue1() {
		int a = 0;
		do {
			a++;
			if (a < 3) {
				continue;
			}
			a = a + 10;
		} while (a < 5);
		Slicer.report(a);
	}

	public static void testDoWhileWithContinue2() {
		int a = 0;
		do {
			a++;
			if (a < 3) {
				continue;
			}
			a = a + 2;
		} while (a < 5);
		Slicer.report(a);
	}

	public static void testInserBST() {
		slicer.tests.util.BST bst = new slicer.tests.util.BST();
		bst.insert(1);
		bst.insert(1);
		Slicer.report(bst.size);
	}

	public static int testFlowErr(int value) {
		int sz = 1;
		boolean z = true;
		while (z) {
			if (value >= 1) {
				break;
			}
			z = false;
		}
		Slicer.reportBoolean(z);
		return ++sz;
	}

	public static int testFlowErr1(int value) {
		int sz = 1;
		boolean z = true;
		while (z) {
			if (value >= 1) {
				break;
			}
			z = false;
		}
		Slicer.reportBoolean(z);
		return ++sz;
	}

	public static int testFlowErr2(int value) {
		int sz = 1;
		boolean z = true;
		while (z) {
			if (value >= 1) {
				z = false;
				sz = 1;
				break;
			}
		}
		Slicer.report(sz);
		return ++sz;
	}

	public static int testFlowErr3(int value) {
		int sz = 1;
		boolean z = true;
		while (z) {
			if (value >= 1) {
				sz = 1;
				break;
			} else {
				sz = 3;
			}
			z = false;
		}
		Slicer.reportBoolean(z);
		return ++sz;
	}

	public static int testFlowErr4(int value) {
		int sz = 1;
		boolean z = true;
		while (z) {
			if (value >= 1) {
				sz = 1;
				break;
			} else {
				sz = 3;
			}
			z = false;
		}
		Slicer.report(sz);
		return ++sz;
	}

	public static int testFlowErr5(int value) {
		int sz = 1;
		boolean z = true;
		while (z) {
			if (value >= 1) {
				sz = 1;
				break;
			} else {
				sz = 3;
			}
			z = false;
		}
		++sz; // new definition depends on control flow
		Slicer.report(sz);
		return sz;
	}

	public static boolean testIfElseWithReturns() {
		int x = 14;
		if (x == 15) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		int i = Integer.parseInt(args[0]);
		String s = "START123";
		switch (i) {
		case 1:
			testANDPrimitiveBoolean();
			break;
		case 2:
			testANDObjectBoolean();
			break;
		case 3:
			testANDMixingPrimitiveAndObjectBoolean();
			break;
		case 4:
			testANDWithConditional();
			break;
		case 5:
			testORPrimitiveBoolean();
			break;
		case 6:
			testORObjectBoolean();
			break;
		case 7:
			testORMixingPrimitiveAndObjectBoolean();
			break;
		case 8:
			testORWithConditional();
			break;
		case 9:
			testANDWithNullObjects();
			break;
		case 10:
			testORWithNullObjects();
			break;
		case 11:
			testNestedORPrimitiveBoolean();
			break;
		case 12:
			testNestedORPrimitiveBoolean2();
			break;
		case 13:
			testAndPrimitiveBoolean1();
			break;
		case 14:
			testAndPrimitiveBoolean2();
			break;
		case 15:
			testAndPrimitiveBoolean3();
			break;
		case 16:
			testAndPrimitiveBoolean4();
			break;
		case 17:
			testPrimitiveSwith();
			break;
		case 18:
			testWhileWithBreak();
			break;
		case 19:
			testDoWhileWithBreak();
			break;
		case 20:
			testWhileWithContinue();
			break;
		case 21:
			testDoWhileWithContinue1();
			break;
		case 22:
			testDoWhileWithContinue2();
			break;
		case 23:
			testInserBST();
			break;
		case 24:
			testFlowErr(0);
			break;
		case 25:
			testFlowErr1(1);
			break;
		case 26:
			testFlowErr2(1);
			break;
		case 27:
			testFlowErr3(0);
			break;
		case 28:
			testFlowErr4(0);
			break;
		case 29:
			testFlowErr5(0);
			break;
		case 30:
			Slicer.reportBoolean(testIfElseWithReturns());
			break;
		default:
			break;
		}
	}

}
