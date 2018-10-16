package romiinger.nailbook.Class;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Date;

public class Treatments implements Comparable<Treatments>{

    private String name;
    private String id;
    private String price;
    private String duration;
    private String description;
    public Treatments(String id, String name , String description, String price, String duration)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public int compareTo( @NonNull Treatments treatments) {
        if (treatments.getDuration() == null || this.getDuration()==null){
            return 0;
        }
        return this.getDuration().compareTo(treatments.getDuration());

    }
}
