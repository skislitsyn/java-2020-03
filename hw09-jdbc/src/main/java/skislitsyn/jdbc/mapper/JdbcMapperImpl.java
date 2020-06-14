package skislitsyn.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDaoException;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<T> dbExecutor;
    private final Class<T> clazz;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<T> dbExecutor, Class<T> clazz,
	    EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData) {
	this.sessionManager = sessionManager;
	this.dbExecutor = dbExecutor;
	this.clazz = clazz;
	this.entityClassMetaData = entityClassMetaData;
	this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public void insert(T objectData) {
	try {
	    long id = dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
		    getParamsList(objectData, entityClassMetaData.getFieldsWithoutId()));
	    setId(objectData, entityClassMetaData.getIdField(), id);
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    throw new UserDaoException(e);
	}
    }

    @Override
    public void update(T objectData) {
	try {
	    dbExecutor.executeUpdate(getConnection(), entitySQLMetaData.getUpdateSql(),
		    getParamsList(objectData, entityClassMetaData.getFieldsWithoutId()),
		    getId(objectData, entityClassMetaData.getIdField()));
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    throw new UserDaoException(e);
	}
    }

    @Override
    public void insertOrUpdate(T objectData) {
	if (findById(getId(objectData, entityClassMetaData.getIdField()), clazz) != null) {
	    update(objectData);
	} else {
	    insert(objectData);
	}
    }

    @Override
    public T findById(long id, Class<T> clazz) {
	try {
	    return dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(), id, rs -> {
		try {
		    if (rs.next()) {
			return getEntity(rs);
		    }
		} catch (SQLException | ReflectiveOperationException e) {
		    logger.error(e.getMessage(), e);
		}
		return null;
	    }).orElse(null);
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	}
	return null;
    }

    private Connection getConnection() {
	return sessionManager.getCurrentSession().getConnection();
    }

    private List<Object> getParamsList(Object obj, List<Field> fields) {
	List<Object> result = new ArrayList<>();
	for (Field field : fields) {
	    field.setAccessible(true);
	    try {
		result.add(field.get(obj));
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
	return result;
    }

    private long getId(Object obj, Field field) {
	try {
	    field.setAccessible(true);
	    return (long) field.get(obj);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private void setId(Object obj, Field field, long id) {
	try {
	    field.setAccessible(true);
	    field.set(obj, id);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private T getEntity(ResultSet rs) throws SQLException, ReflectiveOperationException {
	List<Field> fields = entityClassMetaData.getAllFields();
	List<Object> args = new ArrayList<>(fields.size());
	for (Field field : fields) {
	    args.add(rs.getObject(field.getName(), getFieldType(field)));
	}
	return entityClassMetaData.getConstructor().newInstance(args.toArray());
    }

    private Class<?> getFieldType(Field field) {
	Class<?> type = field.getType();
	if (type.isPrimitive()) {
	    if (byte.class.equals(type)) {
		return Byte.class;
	    } else if (short.class.equals(type)) {
		return Short.class;
	    } else if (int.class.equals(type)) {
		return Integer.class;
	    } else if (long.class.equals(type)) {
		return Long.class;
	    } else if (float.class.equals(type)) {
		return Float.class;
	    } else if (double.class.equals(type)) {
		return Double.class;
	    } else if (boolean.class.equals(type)) {
		return Boolean.class;
	    } else if (char.class.equals(type)) {
		return Character.class;
	    }
	    return null;
	} else {
	    return type;
	}
    }
}
