package db;

public class Goods extends Entity {

    private String type;
    private String producer;
    private String model;
    private int price;
    private String description;

    public Goods() {}

    public Goods(String type, String producer, String model, int price, String description) {
        this.type = type;
        this.producer = producer;
        this.model = model;
        this.price = price;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
