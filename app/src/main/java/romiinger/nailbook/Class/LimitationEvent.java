package romiinger.nailbook.Class;



public class LimitationEvent extends MyEventCalendar
{
       private String name;

    public LimitationEvent(String id ,String date, String startHour , String endHour , String nameEvent)
    {
        super(id,date,startHour,endHour);
        this.name = nameEvent;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
