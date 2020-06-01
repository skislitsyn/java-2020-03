package skislitsyn;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
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

	Class<?> clazz = src.getClass();

	if (isJsonLiteral(clazz)) {
	    return createJsonLiteral(clazz, src);
	}

	if (isJsonArray(clazz)) {
	    return createJsonArray(clazz, src);
	}

	if (isJsonObject(clazz)) {
	    return createJsonObject(clazz, src);
	}

	return null;
    }

    private JsonValue createJsonLiteral(Class<?> clazz, Object src) {
	JsonValue jsonValue;

	if (isNumber(clazz)) {
	    jsonValue = getJsonNumber((Number) src);
	} else if (isBoolean(clazz)) {
	    jsonValue = getJsonBoolean((Boolean) src);
	} else if (isCharacter(clazz)) {
	    jsonValue = getJsonString(String.valueOf((char) src));
	} else if (isString(clazz)) {
	    jsonValue = getJsonString((String) src);
	} else {
	    throw new UnsupportedOperationException("Can not convert object to Json literal");
	}

	return jsonValue;
    }

    private JsonArray createJsonArray(Class<?> clazz, Object src) {
	JsonArray jsonArray;

	if (isArray(clazz)) {
	    if (isPrimitiveArray(src)) {
		jsonArray = getJsonArray(clazz, getObjectArrayFromPrimitiveArray(src));
	    } else {
		jsonArray = getJsonArray(clazz, (Object[]) src);
	    }
	} else if (isCollection(clazz)) {
	    jsonArray = getJsonArray((Collection<?>) src);
	} else {
	    throw new UnsupportedOperationException("Can not convert object to Json array");
	}

	return jsonArray;
    }

    private JsonObject createJsonObject(Class<?> clazz, Object src) {
	JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

	if (isMap(clazz)) {
	    // Out of HW scope
	    throw new UnsupportedOperationException("Can not convert object to Json object");
	} else {
	    List<Field> fields = getFieldsForSerialization(clazz);

	    for (Field field : fields) {
		field.setAccessible(true);
		try {
		    jsonObjectBuilder.add(field.getName(), toJsonValue(field.get(src)));
		} catch (ReflectiveOperationException e) {
		    throw new RuntimeException(e);
		}
	    }
	}

	return jsonObjectBuilder.build();
    }

    private JsonNumber getJsonNumber(Number src) {
	JsonNumber jsonNumber;

	if (src instanceof Byte) {
	    jsonNumber = Json.createValue((byte) src);
	} else if (src instanceof Short) {
	    jsonNumber = Json.createValue((short) src);
	} else if (src instanceof Integer) {
	    jsonNumber = Json.createValue((int) src);
	} else if (src instanceof Long) {
	    jsonNumber = Json.createValue((long) src);
	} else if (src instanceof Float) {
	    jsonNumber = Json.createValue(Double.valueOf(((Float) src).toString()));
	} else if (src instanceof Double) {
	    jsonNumber = Json.createValue((double) src);
	} else if (src instanceof BigInteger) {
	    jsonNumber = Json.createValue((BigInteger) src);
	} else if (src instanceof BigDecimal) {
	    jsonNumber = Json.createValue((BigDecimal) src);
	} else {
	    throw new UnsupportedOperationException("Can not convert object to Json number");
	}

	return jsonNumber;
    }

    private JsonValue getJsonBoolean(Boolean src) {
	if (src.booleanValue() == true) {
	    return JsonValue.TRUE;
	} else {
	    return JsonValue.FALSE;
	}
    }

    private JsonString getJsonString(String src) {
	return Json.createValue(src);
    }

    private JsonArray getJsonArray(Class<?> clazz, Object[] src) {
	JsonArray jsonArray;

	if (isNumberArray(clazz)) {
	    jsonArray = getJsonArray((Number[]) src);
	} else if (isBooleanArray(clazz)) {
	    jsonArray = getJsonArray((Boolean[]) src);
	} else if (isCharacterArray(clazz)) {
	    jsonArray = getJsonArray((Character[]) src);
	} else if (isStringArray(clazz)) {
	    jsonArray = getJsonArray((String[]) src);
	} else if (isObjectArray(clazz)) {
	    jsonArray = getJsonArray(src);
	} else {
	    throw new UnsupportedOperationException("Can not convert object to Json array");
	}

	return jsonArray;
    }

    private JsonArray getJsonArray(Number[] src) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

	if (src instanceof Byte[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((byte) n);
	    }
	} else if (src instanceof Short[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((short) n);
	    }
	} else if (src instanceof Integer[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((int) n);
	    }
	} else if (src instanceof Long[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((long) n);
	    }
	} else if (src instanceof Float[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add(Double.valueOf(((Float) n).toString()));
	    }
	} else if (src instanceof Double[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((double) n);
	    }
	} else if (src instanceof BigInteger[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((BigInteger) n);
	    }
	} else if (src instanceof BigDecimal[]) {
	    for (Number n : src) {
		jsonArrayBuilder.add((BigDecimal) n);
	    }
	} else {
	    throw new UnsupportedOperationException("Can not convert object to Json array");
	}

	return jsonArrayBuilder.build();
    }

    private JsonArray getJsonArray(Boolean[] src) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

	for (Boolean b : src) {
	    jsonArrayBuilder.add(b);
	}

	return jsonArrayBuilder.build();
    }

    private JsonArray getJsonArray(Character[] src) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

	for (Character c : src) {
	    jsonArrayBuilder.add(String.valueOf((char) c));
	}

	return jsonArrayBuilder.build();
    }

    private JsonArray getJsonArray(String[] src) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

	for (String s : src) {
	    jsonArrayBuilder.add(s);
	}

	return jsonArrayBuilder.build();
    }

    private JsonArray getJsonArray(Object[] src) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

	for (Object o : src) {
	    jsonArrayBuilder.add(toJsonValue(o));
	}

	return jsonArrayBuilder.build();
    }

    private JsonArray getJsonArray(Collection<?> src) {
	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

	for (Object o : src) {
	    jsonArrayBuilder.add(toJsonValue(o));
	}

	return jsonArrayBuilder.build();
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

    private boolean isMap(Class<?> clazz) {
	return Map.class.isAssignableFrom(clazz);
    }

    private boolean isJsonLiteral(Class<?> clazz) {
	return isNumber(clazz) || isBoolean(clazz) || isCharacter(clazz) || isString(clazz);
    }

    private boolean isJsonArray(Class<?> clazz) {
	return isArray(clazz) || isCollection(clazz);
    }

    private boolean isJsonObject(Class<?> clazz) {
	return !(isJsonLiteral(clazz) || isJsonArray(clazz));
    }

    private boolean isNumber(Class<?> clazz) {
	return Number.class.isAssignableFrom(clazz);
    }

    private boolean isBoolean(Class<?> clazz) {
	return Boolean.class.equals(clazz);
    }

    private boolean isCharacter(Class<?> clazz) {
	return Character.class.equals(clazz);
    }

    private boolean isString(Class<?> clazz) {
	return String.class.equals(clazz);
    }

    private boolean isArray(Class<?> clazz) {
	return clazz.isArray();
    }

    private boolean isNumberArray(Class<?> clazz) {
	return Number.class.isAssignableFrom(clazz.componentType());
    }

    private boolean isBooleanArray(Class<?> clazz) {
	return Boolean.class.equals(clazz.componentType());
    }

    private boolean isCharacterArray(Class<?> clazz) {
	return Character.class.equals(clazz.componentType());
    }

    private boolean isStringArray(Class<?> clazz) {
	return String.class.equals(clazz.componentType());
    }

    private boolean isObjectArray(Class<?> clazz) {
	return !(isNumberArray(clazz) || isBooleanArray(clazz) || isCharacterArray(clazz) || isStringArray(clazz));
    }

    private boolean isCollection(Class<?> clazz) {
	return Collection.class.isAssignableFrom(clazz);
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

    private Object[] getObjectArrayFromPrimitiveArray(Object src) {
	if (src instanceof byte[]) {
	    return ArrayUtils.toObject((byte[]) src);
	} else if (src instanceof short[]) {
	    return ArrayUtils.toObject((short[]) src);
	} else if (src instanceof int[]) {
	    return ArrayUtils.toObject((int[]) src);
	} else if (src instanceof long[]) {
	    return ArrayUtils.toObject((long[]) src);
	} else if (src instanceof float[]) {
	    return ArrayUtils.toObject((float[]) src);
	} else if (src instanceof double[]) {
	    return ArrayUtils.toObject((double[]) src);
	} else if (src instanceof boolean[]) {
	    return ArrayUtils.toObject((boolean[]) src);
	} else if (src instanceof char[]) {
	    return ArrayUtils.toObject((char[]) src);
	} else {
	    return null;
	}
    }
}
