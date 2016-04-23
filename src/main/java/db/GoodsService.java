package db;

import java.util.List;

public interface GoodsService {

    Goods get(Integer id);

    List<Goods> list();

    void save(Goods goods);

    boolean delete(Goods goods);
}
