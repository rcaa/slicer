package slicer.util;

import gov.nasa.jpf.util.SourceRef;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import slicer.FunctionalFlatSet;
import slicer.diff.Interval;
import slicer.diff.IntervalMembership;

public abstract class SliceFileProcessor {

	List<String> fileNames;
	String v0;
	String v1;
	String errorFile;
	String rankingFile;
	String diffFile;

	public SliceFileProcessor() {
	}

	protected void run(String[] args, String kind, boolean printSlice) {
		extractArgs(args);
		List<FunctionalFlatSet<SourceRef>> slices = load(fileNames);

		Set<SourceRef> set = process(slices);

		System.out.printf("number of inputs: %d\n", fileNames.size());
		System.out.printf("size of %s slice: %d\n", kind, set.size());
		System.out.println("error found: " + !inspect(set).isEmpty());
		System.out.printf("Tarantula ~ All slice %s : %s\n", kind,
				makeTarantulaAnalisys(set));
		if (printSlice) {
			System.out.println("slice");
			for (SourceRef sr : set) {
				System.out.println(sr);
			}
		}
	}

	protected abstract Set<SourceRef> process(
			List<FunctionalFlatSet<SourceRef>> slices);

	@SuppressWarnings("unchecked")
	public List<FunctionalFlatSet<SourceRef>> load(List<String> args) {
		List<FunctionalFlatSet<SourceRef>> result = new ArrayList<FunctionalFlatSet<SourceRef>>();
		for (int i = 0; i < args.size(); i++) {
			String fname = args.get(i);
			ObjectInputStream oos;
			try {
				oos = new ObjectInputStream(new FileInputStream(fname));
				Object tmp = oos.readObject();
				oos.close();
				result.add((FunctionalFlatSet<SourceRef>) tmp);
				args.set(
						i,
						fname.substring(fname.lastIndexOf('-') + 1,
								fname.indexOf('.')));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("ERROR: Could not read slice");
			}
		}
		return result;
	}

	protected List<SourceRef> inspect(Iterable<SourceRef> slice) {
		List<SourceRef> result = new ArrayList<SourceRef>();
		if (!errorFile.equals("")) {
			try {
				Util.readErrorFile(errorFile, v0, v1);
			} catch (IOException e) {
				throw new RuntimeException("Can't read file:" + errorFile);
			}
			String errorClass = Util.getErrorClass();
			List<Integer> errorLines = Util.getErrorLines();
			for (SourceRef ref : slice) {
				if (ref.getFileName().contains(errorClass)
						&& errorLines.contains(ref.line)) {
					result.add(ref);
				}
			}
		}
		return result;
	}

	private List<SourceRef> cloneTarantula(List<SourceRef> a) {
		ArrayList<SourceRef> result = new ArrayList<SourceRef>();
		if (!a.isEmpty()) {
			for (SourceRef ref : a) {
				result.add(ref);
			}
		}
		return result;
	}

	protected String makeTarantulaAnalisys(Set<SourceRef> slice) {
		int notInSliceCount = 0;
		int notChangedCount = 0;
		try {
			List<SourceRef> tarantulaResult = Util.getRankingFromFile(
					rankingFile, errorFile, v0, v1);
			List<SourceRef> tarantulaInterSlice = new ArrayList<SourceRef>();

			for (int i = 0; i < tarantulaResult.size(); i++) {
				SourceRef tarSourceRef = tarantulaResult.get(i);
				boolean inSlice = false;
				for (SourceRef element : slice) {
					String fname = element.getFileName();
					int line = element.line;
					int javaIdx = fname.indexOf(".java");
					if (javaIdx > -1) {
						fname = fname.substring(0, javaIdx);
					}
					if (tarSourceRef.getFileName().equals(fname)
							&& tarSourceRef.line == line) {
						inSlice = true;
						tarantulaInterSlice.add(tarSourceRef);
						break;
					}
				}
				if (!inSlice) {
					notInSliceCount++;
				}
			}

			List<SourceRef> TSC = cloneTarantula(tarantulaInterSlice);
			if (diffFile != null && (new File(diffFile)).exists()) {
				Map<String, List<Interval>> differences = slicer.diff.Util
						.getDifferencesFromFile(diffFile);
				for (int i = 0; i < tarantulaInterSlice.size(); i++) {
					SourceRef ref = tarantulaInterSlice.get(i);
					boolean inTarInterSlice = false;
					String fname = ref.fileName;
					List<Interval> intervals = differences.get(fname);
					int line = ref.line;
					if (intervals != null) {
						IntervalMembership im = new IntervalMembership(
								intervals);
						inTarInterSlice = im.contains(line);
					}
					if (!inTarInterSlice) {
						notChangedCount++;
						if (i < TSC.size())
							TSC.remove(i);
					}
				}

			}
			String result = "";
			if (tarantulaResult.isEmpty()) {
				result = "Tarantula result: ---\n Tarantula with slice: ---\n";
			} else {
				result = " - T:" + tarantulaResult.size() + " - T~S:"
						+ (tarantulaResult.size() - notInSliceCount);
				if (diffFile != null && (new File(diffFile)).exists()) {
					result = result
							+ " - T~S~C:"
							+ (tarantulaResult.size() - notInSliceCount - notChangedCount); // +
																							// "-"+
																							// !inspect(TSC).isEmpty();
																							// TODO
				}
			}
			return result;
		} catch (IOException e) {
			throw new RuntimeException("Can't read rankingFile:" + rankingFile);
		}
	}

	private void extractArgs(String[] args) {
		this.fileNames = new ArrayList<String>();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.charAt(0) == '@') {
				if (arg.contains("v0")) {
					v0 = arg.substring(4);
				} else if (arg.contains("v1")) {
					v1 = arg.substring(4);
				} else if (arg.contains("error")) {
					errorFile = arg.substring(7);
				} else if (arg.contains("ranking")) {
					rankingFile = arg.substring(9);
				} else if (arg.contains("diff.file")) {
					diffFile = arg.substring(11);
				}
			} else {
				fileNames.add(arg);
			}
		}
		if (fileNames.isEmpty() || v1 == null || v0 == null
				|| errorFile == null) {
			StringBuilder msg = new StringBuilder(
					"Pass correct params: [files]+ @v0=... @v1=... @error=...");
			msg.append("\nparameters sent:\n");
			if (fileNames.isEmpty()) {
				msg.append("files=[]\n");
			}
			if (v1 == null) {
				msg.append("v1=null\n");
			}
			if (v0 == null) {
				msg.append("v0=null\n");
			}
			if (errorFile == null) {
				msg.append("errorFile=null\n");
			}

			throw new RuntimeException(msg.toString());
		}

	}

	protected String print(HashSet<SourceRef> set, String fname, boolean valid) {
		String out = "(test" + fname + "-" + valid + ")" + " S:" + set.size()
				+ " " + makeTarantulaAnalisys(set);
		return out;
	}

}
