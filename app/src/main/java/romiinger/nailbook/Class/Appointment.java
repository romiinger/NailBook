package romiinger.nailbook.Class;


public  class Appointment extends MyEventCalendar {

    private String treatmentId;
    private String clientId;


    public Appointment(String id,String date, String startHour, String endHour, String treatmentId, String clientId )
    {
        super(id,date,startHour,endHour);
        this.treatmentId =treatmentId;
        this.clientId = clientId;

    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getClientId() {
        return clientId;
    }
}
