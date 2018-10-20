package romiinger.nailbook.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Class.ScheduleAppointment;
import romiinger.nailbook.Class.WorkDay;

public class WorkDayAdapterFirebase {

    private static final String TAG = "TreatmentsFirebase";
    private FirebaseDatabase mdatabase;
    private final String TABLE_DATABASE = "workDay";

    public WorkDayAdapterFirebase()
    {
        mdatabase = FirebaseDatabase.getInstance();
    }
    public String getNewWorkDayId() {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE);
        String workDayId = myRef.push().getKey();
        Log.d(TAG, "workDayId= " + workDayId);
        return workDayId;
    }
    public interface GetAddWorkDayListener{
        void onComplete(List<Appointment> emptyAppointments);
    }

    public void addWorkDay(final WorkDay workDay, final GetAddWorkDayListener listener) {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE).child(workDay.getId());
        Map<String, Object> value = new HashMap<>();
        value.put("date", workDay.getDate());
        value.put("openHour" , workDay.getOpenHour());
        value.put("id", workDay.getId());
        value.put("closeHour", workDay.getCloseHour());
        myRef.setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"new workdate is pushed to database");
                Log.d(TAG,"Before to create new emptys appointments");
                ScheduleAppointment scheduleAppointment = new ScheduleAppointment();
                scheduleAppointment.getEmptyAppointments(workDay , new ScheduleAppointment.GetDatesToAppointmentListener() {
                    @Override
                    public void onComplete(List<Appointment> emptyAppointments) {
                        listener.onComplete(emptyAppointments);                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Exception to push data " + e);
                        listener.onComplete(null);
                    }
                });
    }

    public interface GetWorkDayListListener{
        void onComplete(List<WorkDay> workDayList);
    }

    public void getWorkDayList( final GetWorkDayListListener listener) {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            final List<WorkDay> workDayList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    String date = value.get("date").toString();
                    String openHour = value.get("openHour").toString();
                    String closeHour = value.get("closeHour").toString();
                     String id = value.get("id").toString();
                     WorkDay workDay = new WorkDay(id,date,openHour,closeHour );
                    workDayList.add(workDay);
                    }
                listener.onComplete(workDayList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }

    public interface GetWorkDayByDateListener {
        void onComplete(WorkDay workDay);
    }
    public void getWorkDayByDate(final String date, final GetWorkDayByDateListener listener) {
        getWorkDayList(new GetWorkDayListListener() {
            @Override
            public void onComplete(List<WorkDay> workDayList) {
                WorkDay worday=null;
                for(int i=0;i<workDayList.size();i++)
                {
                    Calendar cal = Calendar.getInstance();
                    DateFormat formatDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                    Date dateD= new Date(date);
                    Date date2 = new Date(workDayList.get(i).getDate());
                    //cal.setTime(formatDate.format(dateD));
                    if(dateD.compareTo(date2)==0)
                    //if(date == workDayList.get(i).getDate())
                    {
                        worday=workDayList.get(i);
                        break;
                    }
                }
                if(worday!=null){
                    listener.onComplete(worday);
                }
                else{
                    listener.onComplete(new WorkDay());

                }
            }
        });
    }

}
