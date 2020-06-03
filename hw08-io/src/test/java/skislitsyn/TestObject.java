package skislitsyn;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TestObject {
    private byte b;
    private short s;
    private int i;
    private long l;
    private float f;
    private double d;
    private boolean bool;
    private char c;
    private byte[] bArray;
    private short[] sArray;
    private int[] iArray;
    private long[] lArray;
    private float[] fArray;
    private double[] dArray;
    private boolean[] boolArray;
    private char[] cArray;
    private String str;
    private TestObjectDependency o;
    private TestObjectDependency[] oArray;
    private Collection<TestObjectDependency> collection;

    public TestObject(byte b, short s, int i, long l, float f, double d, boolean bool, char c, byte[] bArray,
	    short[] sArray, int[] iArray, long[] lArray, float[] fArray, double[] dArray, boolean[] boolArray,
	    char[] cArray, String str, TestObjectDependency o, TestObjectDependency[] oArray,
	    Collection<TestObjectDependency> collection) {
	this.b = b;
	this.s = s;
	this.i = i;
	this.l = l;
	this.f = f;
	this.d = d;
	this.bool = bool;
	this.c = c;
	this.bArray = bArray;
	this.sArray = sArray;
	this.iArray = iArray;
	this.lArray = lArray;
	this.fArray = fArray;
	this.dArray = dArray;
	this.boolArray = boolArray;
	this.cArray = cArray;
	this.str = str;
	this.o = o;
	this.oArray = oArray;
	this.collection = collection;
    }

    @Override
    public String toString() {
	return "TestObject{" + "b=" + b + ", s=" + s + ", i=" + i + ", l=" + l + ", f=" + f + ", d=" + d + ", bool="
		+ bool + ", c=" + c + ", bArray=" + Arrays.toString(bArray) + ", sArray=" + Arrays.toString(sArray)
		+ ", iArray=" + Arrays.toString(iArray) + ", lArray=" + Arrays.toString(lArray) + ", fArray="
		+ Arrays.toString(fArray) + ", dArray=" + Arrays.toString(dArray) + ", boolArray="
		+ Arrays.toString(boolArray) + ", cArray=" + Arrays.toString(cArray) + ", str=" + str + ", o="
		+ o.toString() + ", oArray=" + Arrays.toString(oArray) + ", collection=" + "["
		+ collection.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]" + "}";
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == this) {
	    return true;
	}

	if (!(obj instanceof TestObject)) {
	    return false;
	}

	TestObject testObject = (TestObject) obj;

	return Byte.compare(b, testObject.b) == 0 && Short.compare(s, testObject.s) == 0
		&& Integer.compare(i, testObject.i) == 0 && Long.compare(l, testObject.l) == 0
		&& Float.compare(f, testObject.f) == 0 && Double.compare(d, testObject.d) == 0
		&& Boolean.compare(bool, testObject.bool) == 0 && Character.compare(c, testObject.c) == 0
		&& Arrays.equals(bArray, testObject.bArray) && Arrays.equals(sArray, testObject.sArray)
		&& Arrays.equals(iArray, testObject.iArray) && Arrays.equals(lArray, testObject.lArray)
		&& Arrays.equals(fArray, testObject.fArray) && Arrays.equals(dArray, testObject.dArray)
		&& Arrays.equals(boolArray, testObject.boolArray) && Arrays.equals(cArray, testObject.cArray)
		&& str.equals(testObject.str) && o.equals(testObject.o) && Arrays.equals(oArray, testObject.oArray)
		&& ((collection.isEmpty() && testObject.collection.isEmpty()) ? true
			: ((collection.isEmpty() || testObject.collection.isEmpty()) ? false
				: (collection.stream().filter(s -> !testObject.collection.contains(s))
					.collect(Collectors.toList())).isEmpty()));
    }
}
