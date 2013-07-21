package slicer;

import gov.nasa.jpf.util.SourceRef;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import slicer.SetupSlicer.REPORT;
import slicer.util.Util;

public class RunSlicer {

	public static void printArgs(String[] args) {
		for (String arg : args) {
			if (arg.charAt(0) == '+' || arg.charAt(0) == '-') {
				System.out.println(arg);
			} else {
				System.out.print(arg);
			}
		}
		System.out
				.println("\n-------------------------------------------------------------");
	}

	public static void main(String args[]) {
		int test = -1;
		List<String> params = new ArrayList<String>();
		String errorFile = "", v0 = "", v1 = "";
		String outser = "/tmp/";
		String ranking = "";
		String diffFile = "";
		if (args.length == 0) {
			String msg = " pass something like:\n"
					+ "@v1=C10 "
					+ "@v0=B00 "
					+ "@outser=/tmp/jtopas_JPF_TIME "
					+ "@ranking=/home/elton/svn/chan/iara_results/jtopas/result-Jun_d14_h01_m49/C10-ranking-stmt.log "
					+ "@diff.file=/home/elton/svn/chan/iara_results/jtopas/result-Jun_d14_h01_m49/C10-diff-stmt-changedStmt.log "
					+ "@error=/home/elton/svn/chan/iara/experiments/errors-jtopas.txt "
					+ "+report.console.property_violation=error "
					+ "+listener=gov.nasa.jpf.listener.DynamicSlicerElton "
					+ "+main.package=de.susebox "
					+ "+test.package=slice "
					+ "+use.changes=true "
					+ "+diff.file=/home/elton/svn/chan/iara_results/jtopas/result-Jun_d14_h01_m49/D09-diff-stmt-changedStmt.log "
					+ "+slicer.log=NO_ "
					+ "+slicer.report.type=MEMORY "
					+ "+sourcepath=/home/elton/svn/chan/iara-subjects/jtopas/versions/D09/src;/home/elton/svn/chan/iara-subjects/jtopas/versions/D09/src-tests "
					+ "+target=slice.AllSliceTests "
					+ "+classpath=/home/elton/svn/chan/iara-subjects/jtopas/versions/D09/bin "
					+ "1 ";
			throw new RuntimeException(msg);
		}

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.charAt(0) == '@') {
				if (arg.contains("v0")) {
					v0 = arg.substring(4);
				} else if (arg.contains("v1")) {
					v1 = arg.substring(4);
				} else if (arg.contains("error")) {
					errorFile = arg.substring(7);
				} else if (arg.contains("outser")) {
					outser = arg.substring(8);
				} else if (arg.contains("ranking")) {
					ranking = arg.substring(9);
				} else if (arg.contains("diff.file")) {
					diffFile = arg.substring(11);
				}
			} else if (arg.charAt(0) == '+') {
				params.add(arg);
			} else {
				test = Integer.parseInt(arg);
			}
		}
		if (test == -1) {
			throw new RuntimeException(
					"Expected the test number as last parameter");
		}

		// Results
		SetupSlicer.lastSlice = null;
		List<FunctionalFlatSet<SourceRef>> slices = new ArrayList<FunctionalFlatSet<SourceRef>>();

		args = params.toArray(new String[params.size() + 1]);
		args[args.length - 1] = test + "";
		System.out.println("V1=" + v1);
		System.out.println("V0=" + v0);
		System.out.println("ERRORS=" + errorFile);
		System.out.println("outset=" + outser);
		System.out.println("ranking=" + ranking);
		System.out.println("diff.file=" + diffFile);
		printArgs(args);
		gov.nasa.jpf.tool.RunJPF.main(args);
		if (SetupSlicer.lastSlice == null) {
			throw new RuntimeException("You have to generate 1 slice per test");
		}

		SetupSlicer.reportType = REPORT.FILE;

		switch (SetupSlicer.reportType) {
		case MEMORY: // TODO hange this to use the class in util
			throw new UnsupportedOperationException();
		case FILE:
			String fname = outser
					+ "/"
					+ errorFile.substring(errorFile.indexOf("-") + 1,
							errorFile.lastIndexOf(".")) + "-" + v1 + "-" + test;
			if (!SetupSlicer.CHANGE_SLICE) {
				fname = fname + "_fs";
			} else {
				fname = fname + "_cis";
			}
			fname = fname + ".ser";

			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(fname));
				oos.writeObject(SetupSlicer.lastSlice);
				oos.flush();
				oos.close();
			} catch (IOException e) {
				throw new RuntimeException("ERROR: Could not save slice");
			}
			break;
		case SCREEN:
			throw new UnsupportedOperationException();
		default:
			break;
		}

	}

	private static int inspect(Collection<SourceRef> slice, Util readErrorFile) {
		String errorClass = readErrorFile.getErrorClass();
		List<Integer> errorLines = readErrorFile.getErrorLines();
		for (SourceRef ref : slice) {
			if (ref.getFileName().contains(errorClass)
					&& errorLines.contains(ref.line)) {
				return slice.size();
			}
		}
		return -1;
	}

	private static Collection<SourceRef> unionSlices(
			List<FunctionalFlatSet<SourceRef>> slices) {
		HashSet<SourceRef> hashSet = new HashSet<SourceRef>();

		for (FunctionalFlatSet<SourceRef> set : slices) {
			for (SourceRef ref : set) {// .getElements()
				hashSet.add(ref);
			}
		}
		return hashSet;
	}

	private static Collection<SourceRef> intercessionSlices(
			List<FunctionalFlatSet<SourceRef>> slices) {
		List<SourceRef> result = new ArrayList<SourceRef>();

		for (SourceRef ref : slices.get(0)) {
			boolean inAll = true;
			for (int i = 1; i < slices.size(); i++) {
				if (!slices.get(i).contains(ref)) {
					inAll = false;
					break;
				}
			}
			if (inAll) {
				result.add(ref);
			}
		}

		return result;
	}

}
