package slicer;

public class Slicer {

	public static native void report(int i);

	public static native void reportLong(long l);

	public static native void reportBoolean(boolean b);

	public static native void reportObject(Object o);

	public static native void reportFloat(float f);

	public static native void reportDouble(double d);

	public static native void reportByte(byte b);

	public static native void reportShort(short s);

	public static native void reportChar(char c);

	public static native void stop();

	public static native void print(String s);

	public static native void reportFinal(Object o);

}
