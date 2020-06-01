package skislitsyn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

public class MyGsonTestingApp {

    public static void main(String[] args) {
	Gson gson = new Gson();
	MyGson myGson = new MyGson();

	if (gson.toJson(null).equals(myGson.toJson(null))) {
	    System.out.println("Ok");
	}
	if (gson.toJson((byte) 1).equals(myGson.toJson((byte) 1))) {
	    System.out.println("Ok");
	}
	if (gson.toJson((short) 1f).equals(myGson.toJson((short) 1f))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(1).equals(myGson.toJson(1))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(1L).equals(myGson.toJson(1L))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(1f).equals(myGson.toJson(1f))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(1d).equals(myGson.toJson(1d))) {
	    System.out.println("Ok");
	}
	if (gson.toJson("aaa").equals(myGson.toJson("aaa"))) {
	    System.out.println("Ok");
	}
	if (gson.toJson('a').equals(myGson.toJson('a'))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(new int[] { 1, 2, 3 }).equals(myGson.toJson(new int[] { 1, 2, 3 }))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(List.of(1, 2, 3)).equals(myGson.toJson(List.of(1, 2, 3)))) {
	    System.out.println("Ok");
	}
	if (gson.toJson(Collections.singletonList(1)).equals(myGson.toJson(Collections.singletonList(1)))) {
	    System.out.println("Ok");
	}

	if (gson.toJson(getComplexObject()).equals(getComplexObject())) {
	    System.out.println("Ok");
	}
    }

    private static TestObject getComplexObject() {
	return new TestObject((byte) 127, (short) 1123, 64536, 2147483648L, 3.14f, 1.7e-300, true, 'a',
		new byte[] { (byte) 125, (byte) 126, (byte) 127 },
		new short[] { (short) 1121, (short) 1122, (short) 1123 }, new int[] { 64534, 64535, 64536 },
		new long[] { 2147483646L, 2147483647L, 2147483648L }, new float[] { 3.12f, 3.13f, 3.14f },
		new double[] { 1.5e-300, 1.6e-300, 1.7e-300 }, new boolean[] { true, true, false },
		new char[] { 'a', 'b', 'c' }, "test", new TestObjectDependency(5, "test dependency"),
		new TestObjectDependency[] { new TestObjectDependency(1, "test dependency 1"),
			new TestObjectDependency(2, "test dependency 2"),
			new TestObjectDependency(3, "test dependency 3") },
		Arrays.asList(new TestObjectDependency(4, "test dependency 4"),
			new TestObjectDependency(5, "test dependency 5"),
			new TestObjectDependency(6, "test dependency 6")));
    }
}
