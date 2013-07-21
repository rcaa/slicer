package slicer.tests;

import slicer.Slicer;

public class PostDomSamples {
	
	public static void main(String[] args) {
		/**
		 * DONT NEED TO ADD CODE HERE!
		 */
	}
	


	// testSimpleReturn(I)I
	public static int testSimpleReturn(int val) {
		if (val > 10) {
			return 0;
		} else {
			val++;
		}
		return val;
	}
	
    // testIfReturn(I)I
	public static int testIfIfReturn(int a) {
		int k = 0;
		if (a > 5) {
			if (a < 10) {
				return 1;
			}
		}
		return k+1;
	}

	// returnSimple(I)I
	public static int returnFromLoopSimple(int val) {
		val = val + 1;
		while (true) {
			switch (val) {
			case 0:
				continue;
			case 1:
				break;
			case -1:
				return val + 10;
			}
		}
	}
	
	// shortCircuitOr(BB)B
	public static boolean shortCircuitOr(boolean a, boolean b) {
		return a || b;
	}
	
	// shortCircuitAnd(BB)B
	public static boolean shortCircuitAnd(boolean a, boolean b) {
		return a && b;
	}
	
	// if2(I)V
	public static void testIf2(int val) {
		if(val > 10) {
			val = 5;
		}
		if (val < 5) {
			val = 10;
		}
		val = val + 1;
	}

    // testIfIf1(I)I
	public static int testIfIf1(int a) {
		int k = 0;
		if (a > 10) {
			if (a < 20) {
				k = 1;
			} 
		}
		return k;
	}
	
    // testIfIf2(I)I
	public static int testIfIf2(int a) {
		int k = 0;
		if (a > 10) {
			if (a < 20) {
				k = 1;
			}
			k += 2;
		}
		return k;
	}
	
    // testIfElse(I)I	
	public static int testIfElse(int a) {
		int k = 0;
		if (a > 10) {
			k = 10;
		} else {
			k = 5;
		}
		return k;
	}
	
	//TODO: add if after another if
	
	
	// testBreak(I)I
	public static int testBreak(int value){
		int sz = 1;
		boolean z = true;
		while(z){                
			if (value >= 1){
				sz = 9;
				break;
			} 
		}
		return ++sz;
	}
	
	public static int testBreak2(int value){
		int sz = 1;
		boolean z = true;
		while(z){
			if (value >= 1){
				z = false;  
				sz = 1;
				break;
			}
		}            
		return ++sz;
	}
	
	// testNestedWhiel(II)I
	public static int testWhileWhile(int i, int j) {
		int result = 1;
		while (i > 10) {
			while (j < 5) {
				result += i * j;
			}
		}
		return result;
	}
	
	// testBreakOuter(I)I
	public static int testBreakOuter(int value){
		int sz = 1;
		a : while(sz < 10){                
			while (value >= 1){
				value -= 1;
				if (value == 10) {
					break a;
				}
			} 
		}
		return ++sz;
	}
	
	// testBreakContinue(I)I
	public static int testBreakContinue(int value){
		boolean z = true;
		while(z){                
			if (value <= 1){
				value = 2;
				continue;
			} else {
				value = 1;
				break;
			}
		}
		return value;
	}
	
	// testContinue(I)I
	public static int testContinue(int value){
		boolean z = true;
		while(z){                
			if (value <= 1){
				value = 2;
				continue;
			} 
			value++;
		}
		return value;
	}
	
	// testSwitch1(I)I
	public static int testSwitch1(int value){
		switch (value) {
		case 10:
			value = 11;
			break;
		case 20:
			value = 15;
			break;
		default:
			value = 20;
			break;
		}
		return value;
	}
	
	// testSwitch2(I)I
	public static int testSwitch2(int value){
		switch (value) {
		case 10:
			if (value > 5) {
				break;
			}
			value = 11;
			break;
		default:
			value = 20;
			break;
		}
		return value;
	}
	
	// testSwitch3(I)I
	public static int testSwitch3(int value){
		switch (value) {
		case 10:
			if (value > 5) {
				break;
			}
			value = 11;
			break;
		case 20:
			value = 15;
			break;
		default:
			value = 20;
			break;
		}
		return value;
	}
	
	// testSwitch4(I)I
	public static int testSwitch4(int value){
		switch (value) {
		case 10:
			if (value > 5) {
				value++;
			}
			value = 11;
			break;
		case 20:
			value = 15;
			break;
		default:
			value = 20;
			break;
		}
		return value;
	}
	
	//testSwitch2C(C)Z
	public static boolean testSwitch2C(char testChar) {
		int idx = -1;
		switch (testChar) {
		case ' ':
		case '\n':
		case '\r':
		case '\t':
			idx = 0;
			break;
		}
		return (idx >= 0);
	}

	
	// testWhileSwitch1(I)I
	public static int testWhileSwitch1(int value){
		while(value < 100){                
			switch (value) {
			case 10:
				value = 11;
				break;
			case 20:
				value = 15;
				break;
			default:
				value = 20;
				break;
			}
		}
		return value;
	}
	
	// testWhileSwitch2(I)I
	public static int testWhileSwitch2(int value){
		while(value < 100){                
			switch (value) {
			case 10:
				if (value > 4) {
					break;
				}
				value = 11;
				break;
			default:
				value = 20;
				break;
			}
		}
		return value;
	}
	
	// testWhileSwitch3(I)I
	public static int testWhileSwitch3(int value){
		while(value < 100){                
			switch (value) {
			case 10:
				if (value > 4) {
					break;
				}
				value = 11;
				break;
			case 20:
				value = 15;
				break;
			default:
				value = 20;
				break;
			}
		}
		return value;
	}
    
    public static int testFlowErr3(int value){
        int sz = 1;
        boolean z = true;
        while(z){
        	if (value >= 1){
        		sz = 1;
        		break;
        	} else{
        		sz = 3;
        	}
        	z = false;
        }
        Slicer.reportBoolean(z);
        return ++sz;
    }	

}