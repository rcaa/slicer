package slicer.diff;

import java.util.ArrayList;
import java.util.List;

import slicer.SetupSlicer;
import slicer.diff.Interval.IntervalType;

public class IntervalMembership {

	private List<Interval> intervals;

	public IntervalMembership(List<Interval> intervals) {
		this.intervals = intervals;
	}

	/**
	 * select from numbers the ones that belong to the interval.
	 * 
	 * this can be significantly optimized!
	 **/
	public List<Integer> filter(List<Integer> numbers) {
		List<Integer> result = new ArrayList<Integer>();
		a: for (Integer num : numbers) {
			for (Interval interval : intervals) {
				if (interval.contains(num)) {
					result.add(num);
				}
				if (interval.isPast(num)) {
					continue a;
				}
			}
		}
		return result;
	}

	public boolean contains(int number) {
		if (intervals.isEmpty())
			return true;
		for (Interval i : intervals) {
			if (i.contains(number))
				return true;
		}
		return false;
	}

	public boolean contains(int lineNumber, int lastIntructionLine) {
		if (intervals.isEmpty())
			return true;
		for (Interval i : intervals) {
			if (i.contains(lineNumber)) {
				return true;
			} else if (i.getType() == IntervalType.d) {
				if (!SetupSlicer.considerSourceInDiffForClassification) {
					if (i.getV1Lo() == lastIntructionLine + 1)
						return true;
				} else {
					if (i.getV0Lo() == lastIntructionLine + 1)
						return true;
				}

			}
		}
		return false;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();

		for (Interval i : intervals) {
			result.append(i.toString());
			result.append('-');
		}
		return result.toString();
	}

	// public static void main(String[] args) {
	// Interval int1 = new Interval(2, 5);
	// Interval int2 = new Interval(10, 13);
	// Interval int3 = new Interval(17);
	// Interval int4 = new Interval(19, 20);
	// System.out.println("int1:" + int1);
	// List<Interval> list = new ArrayList<Interval>();
	// list.add(int1);
	// list.add(int2);
	// list.add(int3);
	// list.add(int4);
	// IntervalMembership im = new IntervalMembership(list);
	//
	// List<Integer> nums = Arrays.asList(new
	// Integer[]{2,3,4,6,7,8,14,17,19,22,23});
	// System.out.println(im.filter(nums));
	// }

}