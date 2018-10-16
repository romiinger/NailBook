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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.sql.Time;


import romiinger.nailbook.Class.LimitationEvent;
import romiinger.nailbook.Class.ScheduleAppointment;

public class LimitationAdapterFirebase {
    private static final String TAG = "LimitationAdapterF";
    private FirebaseDatabase mdatabase;
    public LimitationAdapterFirebase()
    {
        mdatabase = FirebaseDatabase.getInstance();
    }

    public String getNewLimitationId() {
        DatabaseReference myRef = mdatabase.getReference("limitationEvent");
        String limitationEventId = myRef.push().getKey();
        Log.d(TAG, "limitationEventId= " + limitationEventId);
        return limitationEventId;
    }

    public interface GetAddLimitationListener{
        void onComplete(boolean onSucess);
    }
    public void addLimitationEven(final LimitationEvent limitationEvent, final GetAddLimitationListener listener) {
        ScheduleAppointment scheduleAppointment = new ScheduleAppointment();
        scheduleAppointment.veridicationAppointments(limitationEvent,new ScheduleAppointment.GetVeridicationLimitation() {
            @Override
            public void onComplete(boolean isVerificaded) {
                if (isVerificaded)
                {
                    DatabaseReference myRef = mdatabase.getReference("limitationEvent").child(limitationEvent.getId());
                    Map<String, Object> value = new HashMap<>();
                    value.put("date", limitationEvent.getDate());
                    value.put("startHour", limitationEvent.getStartHour());
                    value.put("endHour", limitationEvent.getEndHour());
                    value.put("name", limitationEvent.getName());
                    value.put("id", limitationEvent.getId());
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
                Log.d(TAG,"Limitation no cand be added because client appointment exixst in the same hours");
                listener.onComplete(false);
            }
        });
    }

    public interface GetLimitationByDateListener {
        void onComplete(List<LimitationEvent> limitationEventList);
    }

    public void getLimitationByDate(final String date, final GetLimitationByDateListener listener) {
        getLimitationsList(new GetLimitationsListListener() {
            public void onComplete(final List<LimitationEvent> limitationEventList) {
                List<LimitationEvent> newList = new ArrayList<>();
                Log.d(TAG, "after get wallet list, wallet size=" + limitationEventList.size());
                for (int i = 0; i < limitationEventList.size(); i++) {
                    LimitationEvent limitationEvent = limitationEventList.get(i);
                    Log.d(TAG,"treatment i: "+i+ " id= " + limitationEvent.getDate());
                    if (limitationEvent.getDate() == date ) {
                        newList.add(limitationEvent);
                        Log.d(TAG,"Wallet by userId found ! " );

                    }
                }
                listener.onComplete(newList);
            }
        });
    }
    public interface GetLimitationsListListener {
        void onComplete(List<LimitationEvent> limitationEventList);
    }

    public void getLimitationsList(final GetLimitationsListListener listener) {
        DatabaseReference myRef = mdatabase.getReference("limitationEvent");
        final List<LimitationEvent> limitationEventList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                limitationEventList.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    String name = (String) value.get("name");
                    String date = (String) value.get("date");
                    String id = (String) value.get("id");
                    String startHour = (String) value.get("startHour");
                    String endHour = (String) value.get("endHour");
                    LimitationEvent newTreatament = new LimitationEvent(id,date,startHour,endHour,name);
                    limitationEventList.add(newTreatament);
                }
                listener.onComplete(limitationEventList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }
    public void removeLimitation(final LimitationEvent limitationEvent, final GetAddLimitationListener listener)
    {
        ScheduleAppointment scheduleAppointment = new ScheduleAppointment();
        scheduleAppointment.updateEmptyAppointments(limitationEvent, new ScheduleAppointment.GetVeridicationLimitation() {
            @Override
            public void onComplete(boolean isVerificaded) {
                listener.onComplete(true);
            }
        });
    }
}
