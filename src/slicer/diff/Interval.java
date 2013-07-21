package slicer.diff;

import slicer.SetupSlicer;
import slicer.SetupSlicer.DiffType;

public class Interval {
	public enum IntervalType {
		a, c, d
	};

	private int v0Lo;
	private int v0Hi;

	private IntervalType type;
	private int v1Lo;
	private int v1Hi;

	// Only deleted intervals need this field;
	private Integer changeRange = null;

	public Interval(Integer v0Lo, Integer v0Hi, Integer v1Lo, Integer v1Hi,
			IntervalType type) {
		this.v0Lo = v0Lo;
		if (v0Hi == null) {
			this.v0Hi = v0Lo;
		} else {
			this.v0Hi = v0Hi;
		}

		this.v1Lo = v1Lo;
		if (v1Hi == null) {
			this.v1Hi = v1Lo;
		} else {
			this.v1Hi = v1Hi;
		}

		this.type = type;

		if (type == IntervalType.d) {
			int tmp = this.v0Hi - this.v0Lo;
			if (tmp > 0) {
				this.changeRange = tmp;
			} else {
				this.changeRange = 1;
			}
		}

	}

	public IntervalType getType() {
		return type;
	}

	public void setType(IntervalType type) {
		this.type = type;
	}

	private boolean containsToV1(int num) {
		boolean contains;
		if (SetupSlicer.diffType == DiffType.JAVA_DIFF) {
			if (this.type != IntervalType.d) {
				contains = num >= v1Lo && num <= v1Hi;
			} else {
				contains = (num - changeRange) == v1Lo;
			}

		} else {// DiffJ

			if (this.type == IntervalType.d && v1Lo != v1Hi) {
				contains = false;// the method was deleted, interval like
									// 1,5dstarClassLine,endClasseLine
			} else {
				contains = num >= v1Lo && num <= v1Hi;
			}
		}

		return contains;

	}

	private boolean containsToV0(int num) {
		if (this.type != IntervalType.d) {
			return num >= this.v0Lo && num <= this.v0Hi;
		}
		return false;
	}

	public boolean contains(int num) {
		if (!SetupSlicer.considerSourceInDiffForClassification) {
			return containsToV1(num);
		} else {
			return containsToV0(num);
		}
	}

	public int getNumberOfLines() {
		if (!SetupSlicer.considerSourceInDiffForClassification) {
			return this.v1Hi - this.v1Lo + 1;
		} else {
			return this.v0Hi - this.v0Lo + 1;
		}

	}

	public boolean isPast(int num) {
		if (!SetupSlicer.considerSourceInDiffForClassification) {
			return num < v1Lo;
		} else {
			return num < v0Lo;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((changeRange == null) ? 0 : changeRange.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + v0Hi;
		result = prime * result + v0Lo;
		result = prime * result + v1Hi;
		result = prime * result + v1Lo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval other = (Interval) obj;
		if (changeRange == null) {
			if (other.changeRange != null)
				return false;
		} else if (!changeRange.equals(other.changeRange))
			return false;
		if (type != other.type)
			return false;
		if (v0Hi != other.v0Hi)
			return false;
		if (v0Lo != other.v0Lo)
			return false;
		if (v1Hi != other.v1Hi)
			return false;
		if (v1Lo != other.v1Lo)
			return false;
		if (this == obj)
			return true;
		return true;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		if (this.v0Lo != this.v0Hi) {
			result.append(this.v0Lo);
			result.append(',');
			result.append(this.v0Hi);
		} else {
			result.append(this.v0Lo);
		}

		result.append(type.name());

		if (this.v1Lo != this.v1Hi) {
			result.append(this.v1Lo);
			result.append(',');
			result.append(this.v1Hi);
		} else {
			result.append(this.v1Lo);
		}
		if (this.changeRange != null) {
			result.append('(');
			result.append(this.changeRange);
			result.append(')');
		} else {
			result.append('(');
			result.append("-1");
			result.append(')');
		}
		return result.toString();
	}

	public int getV1Lo() {
		return this.v1Lo;
	}

	public int getV0Lo() {
		return this.v0Lo;
	}

	public void setV0Lo(int v0Lo) {
		this.v0Lo = v0Lo;
	}

	public int getV0Hi() {
		return v0Hi;
	}

	public void setV0Hi(int v0Hi) {
		this.v0Hi = v0Hi;
	}

	public void setV1Lo(int v1Lo) {
		this.v1Lo = v1Lo;
	}

	public int getV1Hi() {
		return v1Hi;
	}

	public void setV1Hi(int v1Hi) {
		this.v1Hi = v1Hi;
	}

}
