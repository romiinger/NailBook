package romiinger.nailbook.Class;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyEventCalendar  implements Comparable<MyEventCalendar>{
    private String date;
    private String startHour;
    private String endHour;
    private String id;
    private DateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
    private static final String TAG = "Appointment";

    public MyEventCalendar(String id ,String date, String startHour , String endHour)
    {
        this.id = id;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo( @NonNull MyEventCalendar event) {
        if (event.getDate() == null || this.getDate()==null){
            return 0;
        }
        Date thisDate = new Date();
        Date appointmentDate =  new Date();
        try{
            thisDate = format.parse(this.getDate());
            appointmentDate =format.parse(event.getDate());
        }
        catch(Exception e)
        {
            Log.e(TAG,"Exeption "+ e);
        }
        return thisDate.compareTo(appointmentDate);

    }
}
