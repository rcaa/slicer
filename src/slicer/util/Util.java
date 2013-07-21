package slicer.util;

import gov.nasa.jpf.util.SourceRef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Util {

	private static final String sizeLabel = "#S:";
	private static final String versionLabel = "#V:";
	private static Integer diff_size;
	private static String errorClass;
	private static List<Integer> errorLines = new ArrayList<Integer>();

	public static void readErrorFile(String errorFile, String v0, String v1)
			throws IOException {
		Integer sizeV0 = -1;
		Integer sizeV1 = -1;

		String experimentInfo = errorFile;
		String identifierVersion = versionLabel + v1;
		FileReader fileReader = new FileReader(experimentInfo);
		BufferedReader reader = new BufferedReader(fileReader);
		String info = null;
		while ((info = reader.readLine()) != null) {
			if (info.charAt(0) == '-')
				continue;
			if (info.contains(sizeLabel)) {
				if (info.contains(v0.charAt(0) + "")) {
					sizeV0 = Integer.parseInt(info.substring(5));
				} else if (info.contains(v1.charAt(0) + "")) {
					sizeV1 = Integer.parseInt(info.substring(5));
				}

			} else if (info.contains(identifierVersion)) {
				StringTokenizer token = new StringTokenizer(reader.readLine(),
						":");
				errorClass = token.nextToken();
				StringTokenizer eLines = new StringTokenizer(token.nextToken(),
						",");
				while (eLines.hasMoreTokens()) {
					String error = eLines.nextToken();
					if (error.contains("-")) {
						StringTokenizer errors = new StringTokenizer(error, "-");
						int start = Integer.parseInt(errors.nextToken());
						int end = Integer.parseInt(errors.nextToken());
						for (; start <= end; start++) {
							errorLines.add(start);
						}
					} else {
						errorLines.add(Integer.parseInt(error));
					}
				}
				break;
			}
		}
		diff_size = Math.abs(sizeV1 - sizeV0);
		reader.close();
		fileReader.close();
	}

	public static String getErrorClass() {
		return errorClass;
	}

	public static List<Integer> getErrorLines() {
		return errorLines;
	}

	public static Integer getDiff_size() {
		return diff_size;
	}

	public static String extractSubjectFromErrorFileName(String file) {
		File errorFile = new File(file);
		String fileName = errorFile.getName();
		int start = fileName.indexOf("-") + 1;
		int end = fileName.indexOf(".");
		return fileName.substring(start, end);
	}

	public static List<SourceRef> getRankingFromFile(String rankingFile,
			String errorFile, String v0, String v1) throws IOException {
		if (rankingFile.startsWith("/not/yet")) {
			return new ArrayList<SourceRef>();
		}

		File file = new File(rankingFile);
		readErrorFile(errorFile, v0, v1);

		List<SourceRef> result = null;
		try {
			Scanner s = new Scanner(file);
			result = new ArrayList<SourceRef>();
			String className;
			int line;
			String score;
			String errorScore = "";
			boolean found = false;
			int count = 0;
			while (s.hasNextLine()) {
				StringTokenizer tokenizer = new StringTokenizer(s.nextLine(),
						":");
				className = tokenizer.nextToken();
				line = Integer.parseInt(tokenizer.nextToken());
				String tmp = tokenizer.nextToken();
				score = tmp.substring(0, tmp.indexOf(','));

				if (!found && errorClass.equals(className)
						&& errorLines.contains(line)) {
					found = true;
				}
				if (found) {
					if (errorScore.equals("")) {
						errorScore = score;
					} else if (!score.equals(errorScore)) {
						break;
					}
				}
				result.add(new SourceRef(className, line));
				count++;
			}
			if (!found) {
				throw new RuntimeException("OOOOPs check ranking");
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("file com o nome " + rankingFile
					+ ". Não existe");
		}

		return result;
	}

	/**
	 * @param line
	 *            from a jpf default output
	 * @return -1 when the line doesn't contains the time information
	 * @return time in seconds
	 */
	public static double extractTimeFromJPFOutput(String line) {/* 0m7.530s */

		String timeLabel = "elapsed time:       ";
		if (!line.contains(timeLabel))
			return -1;
		line = line.substring(timeLabel.length());

		StringTokenizer tz = new StringTokenizer(line, ":");
		double h = Double.parseDouble(tz.nextToken());
		double m = Double.parseDouble(tz.nextToken());
		double s = Double.parseDouble(tz.nextToken());
		return ((h * 60) + m) * 60 + s;

	}

	public static void main(String[] args) throws IOException {
		String rankingFile = "/home/elton/svn/chan/iara_results/ant/result-Jun_d14_h02_m04/B01-ranking-stmt.log";
		String errorFile = "/home/elton/svn/chan/iara/experiments/errors-ant.txt";
		List<SourceRef> bla = getRankingFromFile(rankingFile, errorFile, "C00",
				"D13");
		System.out.println(bla.size());
	}
}
