package db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EntityDao implements Storage {

    private Connection connection;
    private Map<String, Object> dataMap;

    public EntityDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T extends Entity> T get(Class<T> clazz, Integer id) throws Exception {
        String sql = "SELECT * FROM " + clazz.getSimpleName() + " WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            List<T> result = extractResult(clazz, statement.executeQuery(sql));
            return result.isEmpty() ? null : result.get(0);
        }
    }

    @Override
    public <T extends Entity> List<T> list(Class<T> clazz) throws Exception {
        String sql = "SELECT * FROM " + clazz.getSimpleName();
        try (Statement statement = connection.createStatement()) {
            return extractResult(clazz, statement.executeQuery(sql));
        }
    }

    @Override
    public <T extends Entity> boolean delete(T entity) throws Exception {
        String sql = "DELETE FROM " + entity.getClass().getSimpleName() + " WHERE id = " + entity.getId();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            return true;
        }
    }

    @Override
    public <T extends Entity> void save(T entity) throws Exception {
        dataMap = prepareEntity(entity);

        String table = entity.getClass().getSimpleName();

        String parameters = "";
        for (int i = 0; i < dataMap.size(); i++) {
            parameters += "?,";
        }
        parameters = parameters.substring(0, parameters.lastIndexOf(','));

        String sql;
        if (entity.isNew()) {
            sql = "INSERT INTO " + table + "(" + prepareParameters(",") + ") VALUES(" + parameters + ")";
        } else {
            sql = "UPDATE " + table + " SET " + prepareParameters("=?,") + " WHERE id = ?";
            dataMap.put("id", entity.getId());
        }

        int id = sendRequest(sql);
        if (entity.isNew()) {
            entity.setId(id);
        }
    }

    private int sendRequest(String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            for (String s : dataMap.keySet()) {
                statement.setObject(i++, dataMap.get(s));
            }
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Result set is empty");
        }
    }

    private String prepareParameters(String addition) {
        String parameters = "";
        for (String s : dataMap.keySet()) {
            parameters += s + addition;
        }
        return parameters.substring(0, parameters.lastIndexOf(','));
    }

    private <T extends Entity> Map<String, Object> prepareEntity(T entity) throws Exception {
        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Ignore.class)) {
                objectMap.put(field.getName(), field.get(entity));
            }
        }
        return objectMap;
    }

    private <T extends Entity> List<T> extractResult(Class<T> clazz, ResultSet resultSet) throws Exception {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            List<Field> fields = new ArrayList<>();
            Collections.addAll(fields, clazz.getDeclaredFields());
            Collections.addAll(fields, clazz.getSuperclass().getDeclaredFields());

            T entity = clazz.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Ignore.class)) {
                    field.set(entity, resultSet.getObject(field.getName()));
                }
            }
            result.add(entity);
        }
        return result;
    }
}
