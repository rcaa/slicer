package slicer.diff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import slicer.diff.Interval.IntervalType;

public class Util {

	public static Map<String, List<Interval>> getDifferencesFromFile(
			String fname) {
		File file = new File(fname);
		Map<String, List<Interval>> result = null;
		try {
			Scanner s = new Scanner(file);
			result = new HashMap<String, List<Interval>>();
			String line;
			String key = "";
			List<Interval> intervals = new ArrayList<Interval>();
			int count = 0;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (line.trim().equals(""))
					continue;
				if (line.contains("END")) {
					result.put(key, intervals);
					key = "";
					intervals = new ArrayList<Interval>();
					continue;
				}
				if (key.equals("")) {
					key = line;
				} else {
					intervals.add(createInterval(line));
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("file com o nome " + fname
					+ ". Não existe");
		}

		return result;
	}

	private static Interval createInterval(String interval) { // 180c199(-1)
																// 50,199a96,98(-1)
																// 179d199(1)
		String tmp = interval.substring(0, interval.indexOf('('));
		StringTokenizer tokens = new StringTokenizer(tmp, "a");
		IntervalType type = IntervalType.a;
		if (tokens.countTokens() == 1) {
			tokens = new StringTokenizer(tmp, "c");
			type = IntervalType.c;
			if (tokens.countTokens() == 1) {
				tokens = new StringTokenizer(tmp, "d");
				type = IntervalType.d;
			}
		}

		StringTokenizer tokenV0 = new StringTokenizer(tokens.nextToken(), ",");
		StringTokenizer tokenV1 = new StringTokenizer(tokens.nextToken(), ",");
		int v0Lo, v0Hi, v1Lo, v1Hi = 0;

		if (tokenV0.countTokens() == 1) {
			v0Lo = v0Hi = Integer.parseInt(tokenV0.nextToken());
		} else {
			v0Lo = Integer.parseInt(tokenV0.nextToken());
			v0Hi = Integer.parseInt(tokenV0.nextToken());
		}

		if (tokenV1.countTokens() == 1) {
			v1Lo = v1Hi = Integer.parseInt(tokenV1.nextToken());
		} else {
			v1Lo = Integer.parseInt(tokenV1.nextToken());
			v1Hi = Integer.parseInt(tokenV1.nextToken());
		}

		Interval result = new Interval(v0Lo, v0Hi, v1Lo, v1Hi, type);
		assert (interval.equals(result.toString()));
		return result;
	}

	// public static void main(String args []){
	// // System.out.println(createInterval("180c199(-1)"));
	// // System.out.println(createInterval("199a96,98(-1)"));
	// // System.out.println(createInterval("179d199(1)"));
	// String file =
	// "/home/elton/svn/chan/iara_results/print_tokens/B01-diff-stmt-changedStmt.log";
	// Map<String, List<Interval>> diffs = getDifferencesFromFile(file);
	// for(Map.Entry<String, List<Interval>> entry : diffs.entrySet() ){
	// System.out.println(entry.getKey());
	// for(Interval interval : entry.getValue()){
	// System.out.println(interval);
	// }
	// System.out.println("END");
	// }
	// }
}
