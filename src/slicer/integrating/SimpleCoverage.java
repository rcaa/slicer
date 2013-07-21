//
// Copyright (C) 2006 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package slicer.integrating;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.LDC;
import gov.nasa.jpf.search.Search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import slicer.SetupSlicer;


/**
 * this isn't yet a useful tool, but it shows how to track method calls with
 * their corresponding argument values
 */
public class SimpleCoverage extends ListenerAdapter {

	static HashMap<String, HashSet<Integer>> coverage = new HashMap<String, HashSet<Integer>>();
	public static boolean start = false;
	
	
	public void instructionExecuted (JVM vm) {
		Instruction insn = vm.getLastInstruction();
		if(insn == null) return;
		
		int id = insn.getByteCode();
		if (!start && id == 0x12) {
			LDC ldc = (LDC) insn;
			if (ldc.getStringValue() != null) {
				start = ldc.getStringValue().equals(SetupSlicer.START_STRING);
			}
		}
		if(!start) return;
		
		MethodInfo mi = insn.getMethodInfo();
		if(mi == null) return;
		ClassInfo ci = mi.getClassInfo();
		if(ci == null) return;
		if(Util.instance.getInterest(ci) == 0) return;
		
		ThreadInfo ti = vm.getLastThreadInfo();
		int line;
		String className;
		HashSet<Integer> tmp;
		if (insn.isCompleted(ti) && !ti.isInstructionSkipped()) {
			line = insn.getLineNumber();
			className  = insn.getMethodInfo().getClassName();
			if(className != null && line > 0 ){ //&& className.contains(Slicer.mainPackage) TODO check this later, i have to sincronize this with slice too
				tmp = coverage.get(className); 
				if(tmp == null){
					tmp = new HashSet<Integer>();
					coverage.put(className, tmp);
				}
				tmp.add(line);
			}
		}
	}
	
	private static int countLines(){
		int count = 0;
		for(Map.Entry<String, HashSet<Integer>> entry : coverage.entrySet()){
			count = count + entry.getValue().size();
		}
		
		return count;
	}
	
	public static void printSimpleCoverage(){
		System.out.println("#coverage="+countLines());
	}
	
	public void searchFinished(Search search) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n------- Simple Coverage ---------\n");
		sb.append("total=");sb.append(countLines());sb.append('\n');
		sb.append("main.package="+SetupSlicer.mainPackage);
		System.out.println(sb.toString());
	}
	
}
