package slicer;

import gov.nasa.jpf.util.SourceRef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import slicer.SetupSlicer.REPORT;
import slicer.util.SplSets;

public final class SetFactory<T> {
	protected static int SETID;
	static {
		if (SetupSlicer.saveSliceIdIn != null) {
			SETID = getSerializedSliceId(SetupSlicer.saveSliceIdIn);
		} else {
			SETID = 1;
		}
		System.out.println("SETID: " + SETID);
	}

	public static int countEmpty;
	public static int countS1;
	public static int countS2;
	public static int countS3;
	public static int countS4;
	public static int countS5;
	public static int countS6;
	public static int countS7;
	public static int countS8;
	public static int countS9;
	public static int countS10;
	public static int countS11;
	public static int countS12;

	public static void serializeSliceId(String filename) {
		try {
			File ser = new File(filename);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(ser));
			oos.writeObject(new Integer(SETID));
			oos.flush();
			oos.close();
		} catch (IOException e) {
			throw new RuntimeException("ERROR: Could not save slice Id");
		}
	}

	public static int getSerializedSliceId(String filename) {
		try {
			File ser = new File(filename);
			if (!ser.exists()) {
				return 1;
			}
			ObjectInputStream oos = new ObjectInputStream(new FileInputStream(
					ser));
			Object tmp = oos.readObject();
			oos.close();
			return ((Integer) tmp);
		} catch (Exception e) {
			throw new RuntimeException("ERROR: Could not read last sliceId", e);
		}
	}

	public static void serlializeSlices(SplSets splSlices, String filename) {
		try {
			if (!splSlices.sets.isEmpty()) {
				File ser = new File(filename);
				// ObjectOutputStream oos = new ObjectOutputStream(new
				// GZIPOutputStream(new FileOutputStream(ser)));
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(ser));
				oos.writeObject(splSlices);
				oos.flush();
				oos.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("ERROR: Could not serialize the slices",
					e);
		}
	}

	// public static Collection<Set> readSerializedSlices(String filename){
	// try {
	// File ser = new File(filename);
	// ObjectInputStream oos = new ObjectInputStream(new FileInputStream(ser));
	// Object tmp = oos.readObject();
	// oos.close();
	// return ((Collection<Set>) tmp);
	// } catch (Exception e) {
	// throw new RuntimeException("ERROR: Could not read serialized slices", e);
	// }
	// }
	/************************
	 * case for 2 input sets
	 ************************/

	public static Set<SourceRef> union(Set<SourceRef> set1, Set<SourceRef> set2) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2);

	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2) {
		Set<SourceRef>[] result = new Set[3];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}

		return makeUnionSet(result, nextPos);
	}

	/************************
	 * case for 3 input sets
	 ************************/

	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3);

	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[3];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		return makeUnionSet(result, nextPos);

	}

	/************************
	 * case for 4 input sets
	 ************************/

	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4);

	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[4];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		return makeUnionSet(result, nextPos);
	}

	/**************************
	 * case with 5 input sets
	 **************************/

	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5);

	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5) {

		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[5];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		return makeUnionSet(result, nextPos);
	}

	/**************************
	 * case with 6 input sets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[6];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		return makeUnionSet(result, nextPos);

	}

	/**************************
	 * case with 7 input sets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()
					&& !set7.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6, set7);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[7];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		if (set7 != EMPTY_SET) {
			if (set7 != result[0] && set7 != result[1] && set7 != result[2]
					&& set7 != result[3] && set7 != result[4]
					&& set7 != result[5]) {
				result[nextPos++] = set7;
			}
		}
		return makeUnionSet(result, nextPos);
	}

	/**************************
	 * case with 8 inputSets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()
					&& !set7.isInteresting() && !set8.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6, set7, set8);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[8];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		if (set7 != EMPTY_SET) {
			if (set7 != result[0] && set7 != result[1] && set7 != result[2]
					&& set7 != result[3] && set7 != result[4]
					&& set7 != result[5]) {
				result[nextPos++] = set7;
			}
		}
		if (set8 != EMPTY_SET) {
			if (set8 != result[0] && set8 != result[1] && set8 != result[2]
					&& set8 != result[3] && set8 != result[4]
					&& set8 != result[5] && set8 != result[6]) {
				result[nextPos++] = set8;
			}
		}
		return makeUnionSet(result, nextPos);
	}

	/**************************
	 * case with 9 inputSets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()
					&& !set7.isInteresting() && !set8.isInteresting()
					&& !set9.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6, set7, set8, set9);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[9];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		if (set7 != EMPTY_SET) {
			if (set7 != result[0] && set7 != result[1] && set7 != result[2]
					&& set7 != result[3] && set7 != result[4]
					&& set7 != result[5]) {
				result[nextPos++] = set7;
			}
		}
		if (set8 != EMPTY_SET) {
			if (set8 != result[0] && set8 != result[1] && set8 != result[2]
					&& set8 != result[3] && set8 != result[4]
					&& set8 != result[5] && set8 != result[6]) {
				result[nextPos++] = set8;
			}
		}
		if (set9 != EMPTY_SET) {
			if (set9 != result[0] && set9 != result[1] && set9 != result[2]
					&& set9 != result[3] && set9 != result[4]
					&& set9 != result[5] && set9 != result[6]
					&& set9 != result[7]) {
				result[nextPos++] = set9;
			}
		}
		return makeUnionSet(result, nextPos);

	}

	/**************************
	 * case with 10 inputSets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9, Set<SourceRef> set10) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()
					&& !set7.isInteresting() && !set8.isInteresting()
					&& !set9.isInteresting() && !set10.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6, set7, set8, set9,
				set10);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9, Set<SourceRef> set10) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[10];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		if (set7 != EMPTY_SET) {
			if (set7 != result[0] && set7 != result[1] && set7 != result[2]
					&& set7 != result[3] && set7 != result[4]
					&& set7 != result[5]) {
				result[nextPos++] = set7;
			}
		}
		if (set8 != EMPTY_SET) {
			if (set8 != result[0] && set8 != result[1] && set8 != result[2]
					&& set8 != result[3] && set8 != result[4]
					&& set8 != result[5] && set8 != result[6]) {
				result[nextPos++] = set8;
			}
		}
		if (set9 != EMPTY_SET) {
			if (set9 != result[0] && set9 != result[1] && set9 != result[2]
					&& set9 != result[3] && set9 != result[4]
					&& set9 != result[5] && set9 != result[6]
					&& set9 != result[7]) {
				result[nextPos++] = set9;
			}
		}
		if (set10 != EMPTY_SET) {
			if (set10 != result[0] && set10 != result[1] && set10 != result[2]
					&& set10 != result[3] && set10 != result[4]
					&& set10 != result[5] && set10 != result[6]
					&& set10 != result[7] && set10 != result[8]) {
				result[nextPos++] = set10;
			}
		}
		return makeUnionSet(result, nextPos);
	}

	/**************************
	 * case with 11 inputSets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9, Set<SourceRef> set10,
			Set<SourceRef> set11) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()
					&& !set7.isInteresting() && !set8.isInteresting()
					&& !set9.isInteresting() && !set10.isInteresting()
					&& !set11.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6, set7, set8, set9,
				set10, set11);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9, Set<SourceRef> set10,
			Set<SourceRef> set11) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[11];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		if (set7 != EMPTY_SET) {
			if (set7 != result[0] && set7 != result[1] && set7 != result[2]
					&& set7 != result[3] && set7 != result[4]
					&& set7 != result[5]) {
				result[nextPos++] = set7;
			}
		}
		if (set8 != EMPTY_SET) {
			if (set8 != result[0] && set8 != result[1] && set8 != result[2]
					&& set8 != result[3] && set8 != result[4]
					&& set8 != result[5] && set8 != result[6]) {
				result[nextPos++] = set8;
			}
		}
		if (set9 != EMPTY_SET) {
			if (set9 != result[0] && set9 != result[1] && set9 != result[2]
					&& set9 != result[3] && set9 != result[4]
					&& set9 != result[5] && set9 != result[6]
					&& set9 != result[7]) {
				result[nextPos++] = set9;
			}
		}
		if (set10 != EMPTY_SET) {
			if (set10 != result[0] && set10 != result[1] && set10 != result[2]
					&& set10 != result[3] && set10 != result[4]
					&& set10 != result[5] && set10 != result[6]
					&& set10 != result[7] && set10 != result[8]) {
				result[nextPos++] = set10;
			}
		}
		if (set11 != EMPTY_SET) {
			if (set11 != result[0] && set11 != result[1] && set11 != result[2]
					&& set11 != result[3] && set11 != result[4]
					&& set11 != result[5] && set11 != result[6]
					&& set11 != result[7] && set11 != result[8]
					&& set11 != result[9]) {
				result[nextPos++] = set11;
			}
		}
		return makeUnionSet(result, nextPos);
	}

	/**************************
	 * case with 12 inputSets
	 **************************/
	public static Set<SourceRef> union(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9, Set<SourceRef> set10,
			Set<SourceRef> set11, Set<SourceRef> set12) {

		if (SetupSlicer.CHANGE_SLICE) {
			if (!set1.isInteresting() && !set2.isInteresting()
					&& !set3.isInteresting() && !set4.isInteresting()
					&& !set5.isInteresting() && !set6.isInteresting()
					&& !set7.isInteresting() && !set8.isInteresting()
					&& !set9.isInteresting() && !set10.isInteresting()
					&& !set11.isInteresting()) {
				countEmpty++;
				return EMPTY_SET;
			}
		}
		return union_(set1, set2, set3, set4, set5, set6, set7, set8, set9,
				set10, set11, set12);
	}

	private static Set<SourceRef> union_(Set<SourceRef> set1,
			Set<SourceRef> set2, Set<SourceRef> set3, Set<SourceRef> set4,
			Set<SourceRef> set5, Set<SourceRef> set6, Set<SourceRef> set7,
			Set<SourceRef> set8, Set<SourceRef> set9, Set<SourceRef> set10,
			Set<SourceRef> set11, Set<SourceRef> set12) {
		@SuppressWarnings("unchecked")
		Set<SourceRef>[] result = new Set[12];
		int nextPos = 0;
		if (set1 != EMPTY_SET) {
			result[nextPos++] = set1;
		}
		if (set2 != EMPTY_SET) {
			if (set2 != result[0]) {
				result[nextPos++] = set2;
			}
		}
		if (set3 != EMPTY_SET) {
			if (set3 != result[0] && set3 != result[1]) {
				result[nextPos++] = set3;
			}
		}
		if (set4 != EMPTY_SET) {
			if (set4 != result[0] && set4 != result[1] && set4 != result[2]) {
				result[nextPos++] = set4;
			}
		}
		if (set5 != EMPTY_SET) {
			if (set5 != result[0] && set5 != result[1] && set5 != result[2]
					&& set5 != result[3]) {
				result[nextPos++] = set5;
			}
		}
		if (set6 != EMPTY_SET) {
			if (set6 != result[0] && set6 != result[1] && set6 != result[2]
					&& set6 != result[3] && set6 != result[4]) {
				result[nextPos++] = set6;
			}
		}
		if (set7 != EMPTY_SET) {
			if (set7 != result[0] && set7 != result[1] && set7 != result[2]
					&& set7 != result[3] && set7 != result[4]
					&& set7 != result[5]) {
				result[nextPos++] = set7;
			}
		}
		if (set8 != EMPTY_SET) {
			if (set8 != result[0] && set8 != result[1] && set8 != result[2]
					&& set8 != result[3] && set8 != result[4]
					&& set8 != result[5] && set8 != result[6]) {
				result[nextPos++] = set8;
			}
		}
		if (set9 != EMPTY_SET) {
			if (set9 != result[0] && set9 != result[1] && set9 != result[2]
					&& set9 != result[3] && set9 != result[4]
					&& set9 != result[5] && set9 != result[6]
					&& set9 != result[7]) {
				result[nextPos++] = set9;
			}
		}
		if (set10 != EMPTY_SET) {
			if (set10 != result[0] && set10 != result[1] && set10 != result[2]
					&& set10 != result[3] && set10 != result[4]
					&& set10 != result[5] && set10 != result[6]
					&& set10 != result[7] && set10 != result[8]) {
				result[nextPos++] = set10;
			}
		}
		if (set11 != EMPTY_SET) {
			if (set11 != result[0] && set11 != result[1] && set11 != result[2]
					&& set11 != result[3] && set11 != result[4]
					&& set11 != result[5] && set11 != result[6]
					&& set11 != result[7] && set11 != result[8]
					&& set11 != result[9]) {
				result[nextPos++] = set11;
			}
		}
		if (set12 != EMPTY_SET) {
			if (set12 != result[0] && set12 != result[1] && set12 != result[2]
					&& set12 != result[3] && set12 != result[4]
					&& set12 != result[5] && set12 != result[6]
					&& set12 != result[7] && set12 != result[8]
					&& set12 != result[9] && set12 != result[10]) {
				result[nextPos++] = set12;
			}
		}
		return makeUnionSet(result, nextPos);

	}

	private static Set<SourceRef> makeUnionSet(Set<SourceRef>[] result,
			int nextPos) {
		Set<SourceRef> resultSet;
		switch (nextPos) {
		case 0:
			countEmpty++;
			resultSet = EMPTY_SET;
			break;
		case 1:
			resultSet = result[0];
			break;
		case 2:
			resultSet = gen2(result[0], result[1]);// new
			break;
		case 3:
			countS3++;
			resultSet = new FunctionalLinkedSet3<SourceRef>(result[0],
					result[1], result[2]);
			break;
		case 4:
			countS4++;
			resultSet = new FunctionalLinkedSet4<SourceRef>(result[0],
					result[1], result[2], result[3]);
			break;
		case 5:
			countS5++;
			resultSet = new FunctionalLinkedSet5<SourceRef>(result[0],
					result[1], result[2], result[3], result[4]);
			break;
		case 6:
			countS6++;
			resultSet = new FunctionalLinkedSet6<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5]);
			break;
		case 7:
			countS7++;
			resultSet = new FunctionalLinkedSet7<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5],
					result[6]);
			break;
		case 8:
			countS8++;
			resultSet = new FunctionalLinkedSet8<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5],
					result[6], result[7]);
			break;
		case 9:
			countS9++;
			resultSet = new FunctionalLinkedSet9<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5],
					result[6], result[7], result[8]);
			break;
		case 10:
			countS10++;
			resultSet = new FunctionalLinkedSet10<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5],
					result[6], result[7], result[8], result[9]);
			break;
		case 11:
			countS11++;
			resultSet = new FunctionalLinkedSet11<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5],
					result[6], result[7], result[8], result[9], result[10]);
			break;
		case 12:
			countS12++;
			resultSet = new FunctionalLinkedSet12<SourceRef>(result[0],
					result[1], result[2], result[3], result[4], result[5],
					result[6], result[7], result[8], result[9], result[10],
					result[11]);
			break;
		default:
			throw new UnsupportedOperationException("unexpected value!");
		}
		addMemoryList(resultSet);
		return resultSet;
	}

	private static int SEARCH_DEPTH = 2;

	private static Set<SourceRef> gen2(Set<SourceRef> set1, Set<SourceRef> set2) {
		if (set1.getID() > set2.getID()) {
			if (set1.contains(set2/* older */, SEARCH_DEPTH)) {
				return set1;
			}
		} else if (set2.contains(set1/* older */, SEARCH_DEPTH)) {
			return set2;
		}
		countS2++;
		Set<SourceRef> result = new FunctionalLinkedSet2<SourceRef>(set1, set2);
		SEARCH_DEPTH = Math.max(2, (int) Math.log10(SETID) - 4);
		/*************************************
		 * try to store elements in hash table unsuccessfully. create leaks.
		 ************************************/
		return result;
	}

	public static Set<SourceRef> EMPTY_SET = new Set<SourceRef>() {
		@Override
		public Iterator<SourceRef> iterator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getID() {
			return -1;
		}

		@Override
		public boolean isProcessed() {
			return true;
		}

		@Override
		public void setProcessed(Boolean value) {
		}

		@Override
		public void addElements(List<Set<SourceRef>> list) {
			throw new UnsupportedOperationException();
		}

		@Override
		public FunctionalFlatSet<SourceRef> flatten() {
			return new FunctionalFlatSet<SourceRef>(new ArrayList<SourceRef>());
		}

		@Override
		public boolean contains(Set<SourceRef> set1, int depth) {
			return false;
		}

		@Override
		public boolean isInteresting() {
			return false;
		}

		@Override
		public String toString() {
			return "EMPTY";
		}

		@Override
		public void printSet(int ident) {
		}

		public int hashCode() {
			return -1;
		}

		@Override
		public void resetProcessed() {
		}

		@Override
		public String getSourceLocation() {
			return null;
		}

		@Override
		public String getSourceMethod() {
			return null;
		}

		@Override
		public String getSourceClass() {
			return null;
		}
	};

	private static void addMemoryList(Set s) {
		if (SetupSlicer.reportType == REPORT.MEMORY_LIST) {
			if (SetupSlicer.memoryList == null) {
				SetupSlicer.memoryList = new HashSet<Set>();
			}
			if (s != EMPTY_SET) {
				SetupSlicer.memoryList.add(s);
			}
		}
	}

	// public static void main(String[] args) {
	// Collection<Set> test =
	// readSerializedSlices("/Users/sabrinasouto/tmp/spl_results/Berkeley/MHHD/mhhd20_1111111111/BerkeleyDb_p20_test2.mhhd20_1111111111.ser");
	// System.out.println("vale");
	// }
}
