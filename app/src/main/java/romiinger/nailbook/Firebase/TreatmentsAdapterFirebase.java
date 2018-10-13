package romiinger.nailbook.Firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import romiinger.nailbook.Class.Treatments;

public class TreatmentsAdapterFirebase  {

    private static final String TAG = "TreatmentsFirebase";
    private FirebaseDatabase mdatabase;


    public TreatmentsAdapterFirebase()
    {
        mdatabase = FirebaseDatabase.getInstance();
    }


    public String getNewTreatmentId() {
        DatabaseReference myRef = mdatabase.getReference("treatments");
        String treatmentId = myRef.push().getKey();
        Log.d(TAG, "treatmentd= " + treatmentId);
        return treatmentId;
    }
    public void addTreatment(Treatments treatment) {
        DatabaseReference myRef = mdatabase.getReference("treatments").child(treatment.getId());
        Map<String, Object> value = new HashMap<>();
        value.put("name", treatment.getName());
        value.put("id", treatment.getId());
        value.put("price", treatment.getPrice());
        value.put("duration" , treatment.getDuration());
        myRef.setValue(value);
    }

    public interface GetTreatmentByIdListener {
        void onComplete(Treatments treatment);
    }

    public void getTreatmentById(final String id, final GetTreatmentByIdListener listener) {
        getTreatmentsList(new GetTreatmentListListener() {
            public void onComplete(final List<Treatments> treatmentsList) {
                Log.d(TAG, "after get wallet list, wallet size=" + treatmentsList.size());
                for (int i = 0; i < treatmentsList.size(); i++) {
                    Treatments treatment = treatmentsList.get(i);
                    Log.d(TAG,"treatment i: "+i+ " id= " + treatment.getId());
                    if (treatment.getId().equals(id) ) {
                        Log.d(TAG,"Wallet by userId found ! " );
                        listener.onComplete(treatment);
                    }
                }
            }
        });
    }
    public interface GetTreatmentListListener {
        void onComplete(List<Treatments> treatmentsList);
    }

    public void getTreatmentsList(final GetTreatmentListListener listener) {
        DatabaseReference myRef = mdatabase.getReference("treatments");
        final List<Treatments> treatmentsList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                treatmentsList.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    String name = (String) value.get("name");
                    String id = (String) value.get("id");
                    String price = (String) value.get("price");
                    String duration = (String) value.get("duration");
                    Treatments newTreatament = new Treatments(name,id,price,duration);
                    treatmentsList.add(newTreatament);
                }
                listener.onComplete(treatmentsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }
}
