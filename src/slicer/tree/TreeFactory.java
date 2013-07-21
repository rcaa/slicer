package slicer.tree;

import gov.nasa.jpf.util.Pair;
import gov.nasa.jpf.util.SourceRef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Set;

import slicer.SetupSlicer;
import slicer.util.Color;

public class TreeFactory {

	public static int TREE_ID;
	static {
		if (SetupSlicer.saveSliceIdIn != null) {
			TREE_ID = getSerializedSliceId(SetupSlicer.saveSliceIdIn);
		} else {
			TREE_ID = 1;
		}
		System.out.println("SETID: " + TREE_ID);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2) {
		Tree<SourceRef>[] result = new Tree[2];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3) {
		Tree<SourceRef>[] result = new Tree[3];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4) {
		Tree<SourceRef>[] result = new Tree[4];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5) {
		Tree<SourceRef>[] result = new Tree[5];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6) {
		Tree<SourceRef>[] result = new Tree[6];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t6;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6, Tree t7) {
		Tree<SourceRef>[] result = new Tree[7];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t5;
			}
		}
		if (t7 != EMPTY_TREE) {
			if (t7 != result[0] && t7 != result[1] && t7 != result[2]
					&& t7 != result[3] && t7 != result[4] && t7 != result[5]) {
				result[nextPos++] = t7;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6, Tree t7, Tree t8) {
		Tree<SourceRef>[] result = new Tree[8];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t5;
			}
		}
		if (t7 != EMPTY_TREE) {
			if (t7 != result[0] && t7 != result[1] && t7 != result[2]
					&& t7 != result[3] && t7 != result[4] && t7 != result[5]) {
				result[nextPos++] = t7;
			}
		}
		if (t8 != EMPTY_TREE) {
			if (t8 != result[0] && t8 != result[1] && t8 != result[2]
					&& t8 != result[3] && t8 != result[4] && t8 != result[5]
					&& t8 != result[6]) {
				result[nextPos++] = t8;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6, Tree t7, Tree t8, Tree t9) {
		Tree<SourceRef>[] result = new Tree[9];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t5;
			}
		}
		if (t7 != EMPTY_TREE) {
			if (t7 != result[0] && t7 != result[1] && t7 != result[2]
					&& t7 != result[3] && t7 != result[4] && t7 != result[5]) {
				result[nextPos++] = t7;
			}
		}
		if (t8 != EMPTY_TREE) {
			if (t8 != result[0] && t8 != result[1] && t8 != result[2]
					&& t8 != result[3] && t8 != result[4] && t8 != result[5]
					&& t8 != result[6]) {
				result[nextPos++] = t8;
			}
		}
		if (t9 != EMPTY_TREE) {
			if (t9 != result[0] && t9 != result[1] && t9 != result[2]
					&& t9 != result[3] && t9 != result[4] && t9 != result[5]
					&& t9 != result[6] && t9 != result[7]) {
				result[nextPos++] = t9;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6, Tree t7, Tree t8, Tree t9, Tree t10) {
		Tree<SourceRef>[] result = new Tree[10];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t5;
			}
		}
		if (t7 != EMPTY_TREE) {
			if (t7 != result[0] && t7 != result[1] && t7 != result[2]
					&& t7 != result[3] && t7 != result[4] && t7 != result[5]) {
				result[nextPos++] = t7;
			}
		}
		if (t8 != EMPTY_TREE) {
			if (t8 != result[0] && t8 != result[1] && t8 != result[2]
					&& t8 != result[3] && t8 != result[4] && t8 != result[5]
					&& t8 != result[6]) {
				result[nextPos++] = t8;
			}
		}
		if (t9 != EMPTY_TREE) {
			if (t9 != result[0] && t9 != result[1] && t9 != result[2]
					&& t9 != result[3] && t9 != result[4] && t9 != result[5]
					&& t9 != result[6] && t9 != result[7]) {
				result[nextPos++] = t8;
			}
		}
		if (t10 != EMPTY_TREE) {
			if (t10 != result[0] && t10 != result[1] && t10 != result[2]
					&& t10 != result[3] && t10 != result[4] && t10 != result[5]
					&& t10 != result[6] && t10 != result[7] && t10 != result[8]) {
				result[nextPos++] = t10;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6, Tree t7, Tree t8, Tree t9, Tree t10, Tree t11) {
		Tree<SourceRef>[] result = new Tree[11];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t5;
			}
		}
		if (t7 != EMPTY_TREE) {
			if (t7 != result[0] && t7 != result[1] && t7 != result[2]
					&& t7 != result[3] && t7 != result[4] && t7 != result[5]) {
				result[nextPos++] = t7;
			}
		}
		if (t8 != EMPTY_TREE) {
			if (t8 != result[0] && t8 != result[1] && t8 != result[2]
					&& t8 != result[3] && t8 != result[4] && t8 != result[5]
					&& t8 != result[6]) {
				result[nextPos++] = t8;
			}
		}
		if (t9 != EMPTY_TREE) {
			if (t9 != result[0] && t9 != result[1] && t9 != result[2]
					&& t9 != result[3] && t9 != result[4] && t9 != result[5]
					&& t9 != result[6] && t9 != result[7]) {
				result[nextPos++] = t8;
			}
		}
		if (t10 != EMPTY_TREE) {
			if (t10 != result[0] && t10 != result[1] && t10 != result[2]
					&& t10 != result[3] && t10 != result[4] && t10 != result[5]
					&& t10 != result[6] && t10 != result[7] && t10 != result[8]) {
				result[nextPos++] = t10;
			}
		}
		if (t11 != EMPTY_TREE) {
			if (t11 != result[0] && t11 != result[1] && t11 != result[2]
					&& t11 != result[3] && t11 != result[4] && t11 != result[5]
					&& t11 != result[6] && t11 != result[7] && t11 != result[8]
					&& t11 != result[9]) {
				result[nextPos++] = t11;
			}
		}
		return makeUnionTree(result, nextPos);
	}

	public static Tree<SourceRef> insert(Tree t1, Tree t2, Tree t3, Tree t4,
			Tree t5, Tree t6, Tree t7, Tree t8, Tree t9, Tree t10, Tree t11,
			Tree t12) {
		Tree<SourceRef>[] result = new Tree[11];
		int nextPos = 0;
		if (t1 != EMPTY_TREE) {
			result[nextPos++] = t1;
		}
		if (t2 != EMPTY_TREE) {
			if (t2 != result[0]) {
				result[nextPos++] = t2;
			}
		}
		if (t3 != EMPTY_TREE) {
			if (t3 != result[0] && t3 != result[1]) {
				result[nextPos++] = t3;
			}
		}
		if (t4 != EMPTY_TREE) {
			if (t4 != result[0] && t4 != result[1] && t4 != result[2]) {
				result[nextPos++] = t4;
			}
		}
		if (t5 != EMPTY_TREE) {
			if (t5 != result[0] && t5 != result[1] && t5 != result[2]
					&& t5 != result[3]) {
				result[nextPos++] = t5;
			}
		}
		if (t6 != EMPTY_TREE) {
			if (t6 != result[0] && t6 != result[1] && t6 != result[2]
					&& t6 != result[3] && t6 != result[4]) {
				result[nextPos++] = t5;
			}
		}
		if (t7 != EMPTY_TREE) {
			if (t7 != result[0] && t7 != result[1] && t7 != result[2]
					&& t7 != result[3] && t7 != result[4] && t7 != result[5]) {
				result[nextPos++] = t7;
			}
		}
		if (t8 != EMPTY_TREE) {
			if (t8 != result[0] && t8 != result[1] && t8 != result[2]
					&& t8 != result[3] && t8 != result[4] && t8 != result[5]
					&& t8 != result[6]) {
				result[nextPos++] = t8;
			}
		}
		if (t9 != EMPTY_TREE) {
			if (t9 != result[0] && t9 != result[1] && t9 != result[2]
					&& t9 != result[3] && t9 != result[4] && t9 != result[5]
					&& t9 != result[6] && t9 != result[7]) {
				result[nextPos++] = t8;
			}
		}
		if (t10 != EMPTY_TREE) {
			if (t10 != result[0] && t10 != result[1] && t10 != result[2]
					&& t10 != result[3] && t10 != result[4] && t10 != result[5]
					&& t10 != result[6] && t10 != result[7] && t10 != result[8]) {
				result[nextPos++] = t10;
			}
		}
		if (t11 != EMPTY_TREE) {
			if (t11 != result[0] && t11 != result[1] && t11 != result[2]
					&& t11 != result[3] && t11 != result[4] && t11 != result[5]
					&& t11 != result[6] && t11 != result[7] && t11 != result[8]
					&& t11 != result[9]) {
				result[nextPos++] = t11;
			}
		}
		if (t12 != EMPTY_TREE) {
			if (t12 != result[0] && t12 != result[1] && t12 != result[2]
					&& t12 != result[3] && t12 != result[4] && t12 != result[5]
					&& t12 != result[6] && t12 != result[7] && t12 != result[8]
					&& t12 != result[9] && t12 != result[10]) {
				result[nextPos++] = t12;
			}
		}
		return makeUnionTree(result, nextPos);

	}

	public static Tree<SourceRef> EMPTY_TREE = new Tree<SourceRef>() {
		@Override
		public boolean isInteresting() {
			return false;
		}

		@Override
		public Tree getParent() {
			return null;
		}

		@Override
		public Set<Tree> getChildren() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return "EMPTY";
		}

		@Override
		public void print() {
			System.out.println("EMPTY TREE.");
		}

		@Override
		public int getID() {
			return -1;
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

		@Override
		public boolean equals(Object o) {
			return false;
		}

		@Override
		public boolean hasChildren() {
			return false;
		}

		@Override
		public Iterator<SourceRef> iterator() {
			throw new UnsupportedOperationException();
		}
	};

	private static Tree<SourceRef> makeUnionTree(Tree<SourceRef>[] result,
			int nextPos) {
		Tree<SourceRef> resultTree;
		switch (nextPos) {
		case 0:
			resultTree = EMPTY_TREE;
			break;
		case 1:
			resultTree = result[0];
			break;
		case 2:
			resultTree = new CompositeTree2(result[0], result[1]);
			break;
		case 3:
			resultTree = new CompositeTree3<SourceRef>(result[0], result[1],
					result[2]);
			break;
		case 4:
			resultTree = new CompositeTree4<SourceRef>(result[0], result[1],
					result[2], result[3]);
			break;
		case 5:
			resultTree = new CompositeTree5<SourceRef>(result[0], result[1],
					result[2], result[3], result[4]);
			break;
		case 6:
			resultTree = new CompositeTree6<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5]);
			break;
		case 7:
			resultTree = new CompositeTree7<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5], result[6]);
			break;
		case 8:
			resultTree = new CompositeTree8<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5], result[6],
					result[7]);
			break;
		case 9:
			resultTree = new CompositeTree9<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5], result[6],
					result[7], result[8]);
			break;
		case 10:
			resultTree = new CompositeTree10<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5], result[6],
					result[7], result[8], result[9]);
			break;
		case 11:
			resultTree = new CompositeTree11<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5], result[6],
					result[7], result[8], result[9], result[10]);
			break;
		case 12:
			resultTree = new CompositeTree12<SourceRef>(result[0], result[1],
					result[2], result[3], result[4], result[5], result[6],
					result[7], result[8], result[9], result[10], result[11]);
			break;
		default:
			throw new UnsupportedOperationException("unexpected value!");
		}
		// resultTree.print();
		return resultTree;
	}

	public static void serializeSliceId(String filename) {
		try {
			File ser = new File(filename);
			System.out.println("=================SER===============\n"
					+ ser.toString() + "\n\n\n");
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(ser));
			oos.writeObject(new Integer(TREE_ID));
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

	// public static void serlializeSlices(DependencyMap dp, String filename){
	public static void serlializeSlices(Set<Pair<Color, Color>> interactions,
			String filename) {
		try {
			// if(!interactions.isEmpty()){
			File ser = new File(filename);
			// ObjectOutputStream oos = new ObjectOutputStream(new
			// GZIPOutputStream(new FileOutputStream(ser)));
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(ser));
			oos.writeObject(interactions);
			oos.flush();
			oos.close();
			// }
		} catch (Exception e) {
			throw new RuntimeException("ERROR: Could not serialize the slices",
					e);
		}
	}

}
