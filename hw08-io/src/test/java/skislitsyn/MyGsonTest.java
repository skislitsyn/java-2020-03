package skislitsyn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

@DisplayName("Класс MyGson должен ")
class MyGsonTest {
    private Gson gson;
    private MyGson myGson;

    @BeforeEach
    public void setUp() {
	gson = new Gson();
	myGson = new MyGson();
    }

    @DisplayName("уметь работать с null ")
    @Test
    public void shouldComputeNull() {
	assertThat(myGson.toJson(null)).isEqualTo(gson.toJson(null));
    }

    @DisplayName("уметь работать с примитивами ")
    @Test
    public void shouldComputePrimitives() {
	assertThat(myGson.toJson((byte) 1)).isEqualTo(gson.toJson((byte) 1));
	assertThat(myGson.toJson((short) 1)).isEqualTo(gson.toJson((short) 1));
	assertThat(myGson.toJson(1)).isEqualTo(gson.toJson(1));
	assertThat(myGson.toJson(1L)).isEqualTo(gson.toJson(1L));
	assertThat(myGson.toJson(1f)).isEqualTo(gson.toJson(1f));
	assertThat(myGson.toJson(1d)).isEqualTo(gson.toJson(1d));
	assertThat(myGson.toJson('a')).isEqualTo(gson.toJson('a'));
    }

    @DisplayName("уметь работать со строками ")
    @Test
    public void shouldComputeStrings() {
	assertThat(myGson.toJson("aaa")).isEqualTo(gson.toJson("aaa"));
    }

    @DisplayName("уметь работать с массивами примитивов ")
    @Test
    public void shouldComputePrivitiveArrays() {
	assertThat(myGson.toJson(new byte[] { (byte) 1, (byte) 2, (byte) 3 }))
		.isEqualTo(gson.toJson(new byte[] { (byte) 1, (byte) 2, (byte) 3 }));
	assertThat(myGson.toJson(new short[] { (short) 1, (short) 2, (short) 3 }))
		.isEqualTo(gson.toJson(new short[] { (short) 1, (short) 2, (short) 3 }));
	assertThat(myGson.toJson(new int[] { 1, 2, 3 })).isEqualTo(gson.toJson(new int[] { 1, 2, 3 }));
	assertThat(myGson.toJson(new long[] { 1L, 2L, 3L })).isEqualTo(gson.toJson(new long[] { 1L, 2L, 3L }));
	assertThat(myGson.toJson(new float[] { 1f, 2f, 3f })).isEqualTo(gson.toJson(new float[] { 1f, 2f, 3f }));
	assertThat(myGson.toJson(new double[] { 1d, 2d, 3d })).isEqualTo(gson.toJson(new double[] { 1d, 2d, 3d }));
	assertThat(myGson.toJson(new char[] { 'a', 'b', 'c' })).isEqualTo(gson.toJson(new char[] { 'a', 'b', 'c' }));
    }

    @DisplayName("уметь работать с коллекциями ")
    @Test
    public void shouldComputeCollections() {
	assertThat(myGson.toJson(List.of(1, 2, 3))).isEqualTo(gson.toJson(List.of(1, 2, 3)));
	assertThat(myGson.toJson(Collections.singletonList(1))).isEqualTo(gson.toJson(Collections.singletonList(1)));
    }

    @DisplayName("уметь работать с объектами ")
    @Test
    public void shouldComputeObjects() {
	assertThat(myGson.toJson(getTestObject())).isEqualTo(gson.toJson(getTestObject()));
    }

    private static TestObject getTestObject() {
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
