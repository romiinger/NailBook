package romiinger.nailbook.Class;

public class Treatments {

    private String name;
    private String id;
    private String price;
    private String duration;
    public Treatments(String name , String id , String price, String duration)
    {
        this.name = name;
        this.id = id;
        this.price = price;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
