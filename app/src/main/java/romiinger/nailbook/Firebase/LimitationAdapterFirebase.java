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
    public void addLimitationEven(LimitationEvent limitationEvent, final GetAddLimitationListener listener) {
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

    public interface GetLimitationByDateListener {
        void onComplete(LimitationEvent limitationEvent);
    }

    public void getLimitationByDate(final Date date, final GetLimitationByDateListener listener) {
        getLimitationsList(new GetLimitationsListListener() {
            public void onComplete(final List<LimitationEvent> limitationEventList) {
                Log.d(TAG, "after get wallet list, wallet size=" + limitationEventList.size());
                for (int i = 0; i < limitationEventList.size(); i++) {
                    LimitationEvent limitationEvent = limitationEventList.get(i);
                    Log.d(TAG,"treatment i: "+i+ " id= " + limitationEvent.getDate());
                    if (limitationEvent.getDate().equals(date) ) {
                        Log.d(TAG,"Wallet by userId found ! " );
                        listener.onComplete(limitationEvent);
                    }
                }
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
                    Date date = (Date) value.get("date");
                    String id = (String) value.get("id");
                    Time startHour = (Time) value.get("price");
                    Time endHour = (Time) value.get("duration");
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
}
