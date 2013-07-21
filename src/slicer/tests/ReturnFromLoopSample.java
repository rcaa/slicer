package slicer.tests;

public class ReturnFromLoopSample {


	public static void main(String[] args) { }

	private int _currentWritePos;
	private int _currentReadPos;
	private int comparePrefix2;
	private int comparePrefix;
	private int len;
	private Object esc;

	protected int returnFromInsideLoop(int offset) {
		while (true) {
			if (esc != null) {
				switch (comparePrefix) {
				case 0:
					continue;
				case -1:
					return _currentWritePos - _currentReadPos;
				}
			}
			switch (comparePrefix2) {
			case 0:
				len += 1;
				break;
			case -1:
				return 10;
			}
		}
	}
	
	protected int returnFromOutsideLoop(int offset) {
		while (true) {
			if (esc != null) {
				switch (comparePrefix) {
				case 0:
					continue;
				case -1:
					return _currentWritePos - _currentReadPos;
				}
			}
			switch (comparePrefix2) {
			case 0:
				len += 1;
				break;
			case -1:
				return 10;
			}
			if (len > 10) break;
		}
		return len;
	}



}
