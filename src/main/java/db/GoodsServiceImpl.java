package db;

import java.sql.Connection;
import java.util.List;

public class GoodsServiceImpl implements GoodsService {

    private Class<Goods> clazz = Goods.class;

    @Override
    public Goods get(Integer id) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            EntityDao dao = new EntityDao(connection);
            return dao.get(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Goods> list() {
        try (Connection connection = ConnectionUtil.createConnection()) {
            EntityDao dao = new EntityDao(connection);
            return dao.list(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Goods goods) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            EntityDao dao = new EntityDao(connection);
            dao.save(goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Goods goods) {
        try (Connection connection = ConnectionUtil.createConnection()) {
            EntityDao dao = new EntityDao(connection);
            return dao.delete(goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
