package romiinger.nailbook.Class;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Time;
import java.util.List;

public class WorkDay {
    private Date date;
    private Time openHour;
    private Time closeHour;
    private List<LimitationEvent> limitationEventList;


    public WorkDay(Date date, Time openHour , Time closeHour)
    {
        this.date = date;
        this.openHour = openHour;
        this.closeHour = closeHour;
        limitationEventList=new ArrayList<>();
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getOpenHour() {
        return openHour;
    }

    public void setOpenHour(Time openHour) {
        this.openHour = openHour;
    }

    public Time getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(Time closeHour) {
        this.closeHour = closeHour;
    }

    public List<LimitationEvent> getLimitationEventList() {
        return limitationEventList;
    }

    public void setLimitationEventList(List<LimitationEvent> limitationEventList) {
        this.limitationEventList = limitationEventList;
    }
}
