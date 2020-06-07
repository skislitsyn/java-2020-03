package skislitsyn.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import skislitsyn.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final MetaData<T> metaData = new MetaData<T>();

    public EntityClassMetaDataImpl(Class<T> clazz) {
	metaData.name = clazz.getSimpleName();
	metaData.constructor = extractConstructor(clazz);

	List<Field> fields = getAllFields(new ArrayList<Field>(), clazz);
	for (Field field : fields) {
	    metaData.allFields.add(field);
	    if (field.isAnnotationPresent(Id.class)) {
		metaData.idField = field;
	    } else {
		metaData.fieldsWithoutId.add(field);
	    }
	}
    }

    @Override
    public String getName() {
	return metaData.name;
    }

    @Override
    public Constructor<T> getConstructor() {
	return metaData.constructor;
    }

    @Override
    public Field getIdField() {
	return metaData.idField;
    }

    @Override
    public List<Field> getAllFields() {
	return metaData.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
	return metaData.fieldsWithoutId;
    }

    @SuppressWarnings("unchecked")
    private Constructor<T> extractConstructor(Class<T> clazz) {
	Constructor<?>[] constructors = clazz.getConstructors();
	if (constructors.length == 1) {
	    return (Constructor<T>) constructors[0];
	} else {
	    throw new UnsupportedOperationException("Entity class must have one public constructor");
	}
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
	fields.addAll(Arrays.asList(type.getDeclaredFields()));

	if (type.getSuperclass() != null) {
	    getAllFields(fields, type.getSuperclass());
	}

	return fields;
    }

    private class MetaData<T> {
	private String name;
	private Constructor<T> constructor;
	private Field idField;
	private List<Field> allFields = new ArrayList<>();
	private List<Field> fieldsWithoutId = new ArrayList<>();
    }
}
