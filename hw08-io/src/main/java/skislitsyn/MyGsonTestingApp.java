package skislitsyn;

import java.util.Arrays;

import com.google.gson.Gson;

public class MyGsonTestingApp {

    public static void main(String[] args) {
	TestObject obj = new TestObject((byte) 127, (short) 1123, 64536, 2147483648L, 3.14f, 1.7e-300, true, 'a',
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
	System.out.println("Test object:" + obj);

	// Gson library
	System.out.println("Testing Gson library:");
	Gson gson = new Gson();
	String json = gson.toJson(obj);
	System.out.println(json);

	TestObject obj2 = gson.fromJson(json, TestObject.class);
	System.out.println(obj.equals(obj2));

	// MyGson library
	System.out.println("Testing MyGson library:");
	MyGson myGson = new MyGson();
	String json2 = myGson.toJson(obj);
	System.out.println(json2);

	TestObject obj3 = gson.fromJson(json2, TestObject.class);
	System.out.println(obj.equals(obj3));
    }

}
