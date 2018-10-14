package romiinger.nailbook.Class;

import java.sql.Time;
import java.util.Date;

public class Appointment {
    private Date date;
    private Time startHour;
    private String treatmentId;
    private String clientId;
    private boolean approved;
    private String id;

    public Appointment(String id,Date date, Time startHour, String treatmentId, String clientId ,boolean approved)
    {
        this.id = id;
        this.date = date;
        this.startHour = startHour;
        this.treatmentId =treatmentId;
        this.clientId = clientId;
        this.approved = approved;
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

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
