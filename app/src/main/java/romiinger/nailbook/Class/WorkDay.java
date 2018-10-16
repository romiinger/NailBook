package romiinger.nailbook.Class;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Time;
import java.util.List;

public class WorkDay {
    private String date;
    private String openHour;
    private String closeHour;
    private List<LimitationEvent> limitationEventList;
    private String id;


    public WorkDay(String id, String date, String openHour , String closeHour)
    {
        this.id = id;
        this.date = date;
        this.openHour = openHour;
        this.closeHour = closeHour;
        limitationEventList=new ArrayList<>();
    }

    public WorkDay() {

    }

    public List<LimitationEvent> getLimitationEventList() {
        return limitationEventList;
    }

    public void setLimitationEventList(List<LimitationEvent> limitationEventList) {
        this.limitationEventList = limitationEventList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }
}
