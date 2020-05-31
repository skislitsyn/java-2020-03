package skislitsyn;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.apache.commons.lang3.ArrayUtils;

public class MyGson {
    private final Map<Class<?>, List<Field>> fieldsForSerializationMap = new HashMap<>();

    public String toJson(Object src) {
	return toJsonValue(src).toString();
    }

    public JsonValue toJsonValue(Object src) {
	if (src == null) {
	    return JsonObject.NULL;
	}

	List<Field> fieldsForSerialization = getFieldsForSerialization(src.getClass());

	JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
	setJsonObjectBuilder(jsonObjectBuilder, fieldsForSerialization, src);
	JsonObject jsonObject = jsonObjectBuilder.build();

	return jsonObject;
    }

    private List<Field> getFieldsForSerialization(Class<?> clazz) {
	if (fieldsForSerializationMap.containsKey(clazz)) {
	    return fieldsForSerializationMap.get(clazz);
	} else {
	    List<Field> fieldsForSerialization = new ArrayList<>();
	    Field[] flields = clazz.getDeclaredFields();
	    for (Field field : flields) {
		int mod = field.getModifiers();
		if (!Modifier.isTransient(mod)) {
		    fieldsForSerialization.add(field);
		}
	    }
	    fieldsForSerializationMap.put(clazz, fieldsForSerialization);
	    return fieldsForSerialization;
	}
    }

    @SuppressWarnings("unchecked")
    private void setJsonObjectBuilder(JsonObjectBuilder jsonObjectBuilder, List<Field> fields, Object obj) {
	for (Field field : fields) {
	    field.setAccessible(true);
	    try {
		if (field.getType().isPrimitive() || isBoxedPrimitive(field.getType())) {
		    addPrimitive(jsonObjectBuilder, field.getName(), field.get(obj));
		} else if (isString(field.getType())) {
		    addString(jsonObjectBuilder, field.getName(), (String) field.get(obj));
		} else if (field.getType().isArray()) {
		    if (isPrimitiveArray(field.get(obj))) {
			addPrimitiveArray(jsonObjectBuilder, field.getName(),
				getObjectArrayFromPrimitiveArray(field.get(obj)));
		    } else {
			addObjectArray(jsonObjectBuilder, field.getName(), (Object[]) field.get(obj));
		    }
		} else if (isCollection(field.getType())) {
		    addCollection(jsonObjectBuilder, field.getName(), (Collection<Object>) field.get(obj));
		} else if (isMap(field.getType())) {
		    throw new UnsupportedOperationException("Maps are not supported");
		} else {
		    addObject(jsonObjectBuilder, field.getName(), field.get(obj));
		}
	    } catch (ReflectiveOperationException e) {
		throw new RuntimeException(e);
	    }
	}
    }

    private void addPrimitive(JsonObjectBuilder jsonObjectBuilder, String key, Object value) {
	// TODO Can be refactored using Visitor pattern
	if (value instanceof Byte) {
	    jsonObjectBuilder.add(key, (byte) value);
	} else if (value instanceof Short) {
	    jsonObjectBuilder.add(key, (short) value);
	} else if (value instanceof Integer) {
	    jsonObjectBuilder.add(key, (int) value);
	} else if (value instanceof Long) {
	    jsonObjectBuilder.add(key, (long) value);
	} else if (value instanceof Float) {
	    jsonObjectBuilder.add(key, Double.valueOf(((Float) value).toString()));
	} else if (value instanceof Double) {
	    jsonObjectBuilder.add(key, (double) value);
	} else if (value instanceof Boolean) {
	    jsonObjectBuilder.add(key, (boolean) value);
	} else if (value instanceof Character) {
	    jsonObjectBuilder.add(key, String.valueOf((char) value));
	}
    }

    private void addPrimitiveArray(JsonObjectBuilder jsonObjectBuilder, String key, Object[] values) {
	// TODO Can be refactored using Visitor pattern
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	if (values instanceof Byte[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add((byte) value);
	    }
	} else if (values instanceof Short[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add((short) value);
	    }
	} else if (values instanceof Integer[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add((int) value);
	    }
	} else if (values instanceof Long[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add((long) value);
	    }
	} else if (values instanceof Float[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add(Double.valueOf(((Float) value).toString()));
	    }
	} else if (values instanceof Double[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add((double) value);
	    }
	} else if (values instanceof Boolean[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add((boolean) value);
	    }
	} else if (values instanceof Character[]) {
	    for (Object value : values) {
		jsonArrayBuilder.add(String.valueOf((char) value));
	    }
	}
	jsonObjectBuilder.add(key, jsonArrayBuilder);
    }

    private <T> void addCollection(JsonObjectBuilder jsonObjectBuilder, String key, Collection<T> values) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	for (Object value : values) {
	    jsonArrayBuilder.add(toJsonValue(value));
	}
	jsonObjectBuilder.add(key, jsonArrayBuilder);
    }

    private <T> void addString(JsonObjectBuilder jsonObjectBuilder, String key, String value) {
	jsonObjectBuilder.add(key, value);
    }

    private <T> void addObject(JsonObjectBuilder jsonObjectBuilder, String key, Object value) {
	jsonObjectBuilder.add(key, toJsonValue(value));
    }

    private void addObjectArray(JsonObjectBuilder jsonObjectBuilder, String key, Object[] values) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	for (Object value : values) {
	    jsonArrayBuilder.add(toJsonValue(value));
	}
	jsonObjectBuilder.add(key, jsonArrayBuilder);
    }

    private boolean isCollection(Class<?> clazz) {
	return Collection.class.isAssignableFrom(clazz);
    }

    private boolean isMap(Class<?> clazz) {
	return Map.class.isAssignableFrom(clazz);
    }

    private boolean isBoxedPrimitive(Class<?> clazz) {
	return Byte.class.equals(clazz) || Short.class.equals(clazz) || Integer.class.equals(clazz)
		|| Long.class.equals(clazz) || Float.class.equals(clazz) || Double.class.equals(clazz)
		|| Boolean.class.equals(clazz) || Character.class.equals(clazz);
    }

    private boolean isString(Class<?> clazz) {
	return String.class.equals(clazz);
    }

    private boolean isPrimitiveArray(Object obj) {
	if (obj instanceof byte[] || obj instanceof short[] || obj instanceof int[] || obj instanceof long[]
		|| obj instanceof float[] || obj instanceof double[] || obj instanceof boolean[]
		|| obj instanceof char[]) {
	    return true;
	} else {
	    return false;
	}
    }

    private Object[] getObjectArrayFromPrimitiveArray(Object obj) {
	if (obj instanceof byte[]) {
	    return ArrayUtils.toObject((byte[]) obj);
	} else if (obj instanceof short[]) {
	    return ArrayUtils.toObject((short[]) obj);
	} else if (obj instanceof int[]) {
	    return ArrayUtils.toObject((int[]) obj);
	} else if (obj instanceof long[]) {
	    return ArrayUtils.toObject((long[]) obj);
	} else if (obj instanceof float[]) {
	    return ArrayUtils.toObject((float[]) obj);
	} else if (obj instanceof double[]) {
	    return ArrayUtils.toObject((double[]) obj);
	} else if (obj instanceof boolean[]) {
	    return ArrayUtils.toObject((boolean[]) obj);
	} else if (obj instanceof char[]) {
	    return ArrayUtils.toObject((char[]) obj);
	} else {
	    return null;
	}
    }
}
