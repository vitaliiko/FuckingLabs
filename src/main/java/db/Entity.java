package db;

public abstract class Entity {

    private Integer id;

    public boolean isNew() {
        return getId() == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
