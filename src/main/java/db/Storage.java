package db;

import java.util.List;

public interface Storage {

    <T extends Entity> T get(Class<T> clazz, Integer id) throws Exception;

    <T extends Entity> List<T> list(Class<T> clazz) throws Exception;

    <T extends Entity> boolean delete(T entity) throws Exception;

    <T extends Entity> void save(T entity) throws Exception;
}
