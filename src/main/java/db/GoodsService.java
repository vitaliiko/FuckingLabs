package db;

import java.util.List;

public interface GoodsService {

    Goods get(Class<Goods> clazz, Integer id) throws Exception;

    List<Goods> list(Class<Goods> clazz) throws Exception;

    void save(Goods goods) throws Exception;

    boolean delete(Goods goods) throws Exception;
}
