package db;

import java.sql.Connection;
import java.util.List;

public class GoodsServiceImpl implements GoodsService {

    private EntityDao dao;

    public GoodsServiceImpl(Connection connection) {
        dao = new EntityDao(connection);
    }

    @Override
    public Goods get(Class<Goods> clazz, Integer id) throws Exception {
        return dao.get(clazz, id);
    }

    @Override
    public List<Goods> list(Class<Goods> clazz) throws Exception {
        return dao.list(clazz);
    }

    @Override
    public void save(Goods goods) throws Exception {
        dao.save(goods);
    }

    @Override
    public boolean delete(Goods goods) throws Exception {
        return dao.delete(goods);
    }
}
