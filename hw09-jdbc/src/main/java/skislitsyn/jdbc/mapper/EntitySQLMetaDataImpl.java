package skislitsyn.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
	this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
	return "select " + getFieldsList(entityClassMetaData.getAllFields()) + " from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
	return "select " + getFieldsList(entityClassMetaData.getAllFields()) + " from " + entityClassMetaData.getName()
		+ " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
	return "insert into " + entityClassMetaData.getName() + "("
		+ getFieldsList(entityClassMetaData.getFieldsWithoutId()) + ") values ("
		+ getSqlParamMarkerList(entityClassMetaData.getFieldsWithoutId().size()) + ")";
    }

    @Override
    public String getUpdateSql() {
	return "update " + entityClassMetaData.getName() + " set "
		+ getFieldsKeyValueList(entityClassMetaData.getFieldsWithoutId()) + " where "
		+ entityClassMetaData.getIdField().getName() + " = ?";
    }

    private String getFieldsList(List<Field> fields) {
	return fields.stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getFieldsKeyValueList(List<Field> fields) {
	return fields.stream().map(f -> f.getName() + " = ?").collect(Collectors.joining(", "));
    }

    private static String getSqlParamMarkerList(int count) {
	String result = "";
	for (int i = 0; i < count; i++) {
	    result += "?, ";
	}
	return result.replaceAll(", $", "");
    }
}
