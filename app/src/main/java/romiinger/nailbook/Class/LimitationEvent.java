package romiinger.nailbook.Class;

import java.sql.Time;
import java.util.Date;


public class LimitationEvent {
    private Date date;
    private Time startHour;
    private Time endHour;
    private String name;
    private String id;


    public LimitationEvent(String id ,Date date, Time startHour , Time endHour , String nameEvent)
    {
        this.id = id;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.name = nameEvent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartHour() {
        return startHour;
    }

    public void setStartHour(Time startHour) {
        this.startHour = startHour;
    }

    public Time getEndHour() {
        return endHour;
    }

    public void setEndHour(Time endHour) {
        this.endHour = endHour;
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
}
