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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        void onComplete(boolean onSucess);
    }

    public void addWorkDay(WorkDay workDay, final GetAddWorkDayListener listener) {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE).child(workDay.getId());
        Map<String, Object> value = new HashMap<>();
        value.put("date", workDay.getDate());
        value.put("openHour" , workDay.getOpenHour());
        value.put("id", workDay.getId());
        value.put("closeHour", workDay.getCloseHour());
        myRef.setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(true);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Exception to push data " + e);
                        listener.onComplete(false);
                    }
                });

    }

    public interface GetWorkDayListListener{
        void onComplete(List<WorkDay> workDayList);
    }

    public void getWorkDayList( final GetWorkDayListListener listener) {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE);
        myRef.addValueEventListener(new ValueEventListener()
        {
            final List<WorkDay> workDayList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    Date dateFromDatabase= (Date) value.get("date");
                     Time openHour = (Time) value.get("openHour");
                     Time closeHour =(Time) value.get("closeHour");
                     String id =(String) value.get("id");
                     WorkDay workDay = new WorkDay(id,dateFromDatabase,openHour,closeHour );
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
    public void getWorkDayByDate(final Date date, final GetWorkDayByDateListener listener) {
        getWorkDayList(new GetWorkDayListListener() {
            @Override
            public void onComplete(List<WorkDay> workDayList) {
                for(int i=0;i<workDayList.size();i++)
                {
                    if(date.compareTo(workDayList.get(i).getDate()) == 0)
                    {
                        listener.onComplete(workDayList.get(i));
                    }
                }
            }
        });
    }
}
