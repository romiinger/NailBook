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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import romiinger.nailbook.Class.Appointment;

public class AppointmentAdapterFirebase {
    private static final String TAG = "AppointmentAdapterF";
    private FirebaseDatabase mdatabase;
    private final String TABLE_DATABASE = "appointment";
    private DateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

    public AppointmentAdapterFirebase()
    {
        mdatabase = FirebaseDatabase.getInstance();
    }
    public String getNewAppointmentId() {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE);
        String appointmentId = myRef.push().getKey();
        Log.d(TAG, "appointmentId= " + appointmentId);
        return appointmentId;
    }



    public void getEmptyAppointmentsByDate(final String date,final GetAppointmentsListListener listener ){
        getAppointmentsByDate(date, new GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                List<Appointment> newList = new ArrayList<>();
                for(int i=0;i< appointmentList.size();i++)
                {
                    Appointment appointment = appointmentList.get(i);
                    if(appointment.getClientId().isEmpty()|| appointment.getClientId() == null)
                    {
                        newList.add(appointment);
                    }
                }
                Collections.sort(appointmentList);
                listener.onComplete(appointmentList);
            }
        });
    }
    public void getClientAppointmnetByDate(final String date,final GetAppointmentsListListener listener)
    {
        getAppointmentsByDate(date, new GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                List<Appointment> newList = new ArrayList<>();
                for(int i=0;i< appointmentList.size();i++)
                {
                    Appointment appointment = appointmentList.get(i);
                    if(!appointment.getClientId().isEmpty()&& appointment.getClientId() != null)
                    {
                        newList.add(appointment);
                    }
                }
                Collections.sort(appointmentList);
                listener.onComplete(appointmentList);
            }
        });
    }
    public void getAppointmentsByTreatments(final String treatmentId, final GetAppointmentsListListener listener)
    {
        getAppointmentsList(new GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                List<Appointment> newList = new ArrayList<>();
                for(int i=0;i< appointmentList.size();i++)
                {
                    Appointment appointment = appointmentList.get(i);
                    if(appointment.getTreatmentId() == treatmentId)
                    {
                        newList.add(appointment);
                    }
                }
                Collections.sort(appointmentList);
                listener.onComplete(appointmentList);
            }
        });

    }
    public void getAppointmentsByUser(final String clientId, final GetAppointmentsListListener listener)
    {
        getAppointmentsList(new GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                List<Appointment> newList = new ArrayList<>();
                for(int i=0;i< appointmentList.size();i++)
                {
                    Appointment appointment = appointmentList.get(i);
                    if(appointment.getClientId() == clientId)
                    {
                        newList.add(appointment);
                    }
                }
                Collections.sort(appointmentList);
                listener.onComplete(appointmentList);
            }
        });
    }
    public void addAppointment(Appointment appointment, final GetAddAppointmentListener listener) {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE).child(appointment.getId());
        Map<String, Object> value = toMap(appointment);
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


    public void removeApoointmentById(Appointment appointment)
    {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE).child(appointment.getId());
        myRef.removeValue();

    }
    public void getAppointmentsByDate(final String date, final GetAppointmentsListListener listener) {
        getAppointmentsByWeeks(date,date, listener );
    }

    public void getAppointmentsByWeeks(final String startDate, final String endDate, final GetAppointmentsListListener listener) {
        getAppointmentsList(new GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                List<Appointment> newList = new ArrayList<>();
                for(int i=0;i< appointmentList.size();i++)
                {
                    Appointment appointment = appointmentList.get(i);
                    Date dStartdate = new Date();
                    Date dAppointment = new Date();
                    Date dEndDate = new Date();
                    try{
                        dStartdate = format.parse(startDate);
                        dAppointment =format.parse(appointment.getDate());
                        dEndDate = format.parse(endDate);
                    }
                    catch(Exception e)
                    {
                        Log.e(TAG,"Exeption "+ e);
                    }


                    if(!dStartdate.after(dAppointment) && !dEndDate.before(dAppointment))  /* startDate <= dateFromDatabase <= endDate */
                    {
                        newList.add(appointment);
                    }
                }
                Collections.sort(appointmentList);
                listener.onComplete(appointmentList);
            }
        });
    }

    public void getAppointmentById(final String id , final GetAppointmentsByIdListener listener)
    {
        getAppointmentsList(new GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                List<Appointment> newList = new ArrayList<>();
                for(int i=0;i< appointmentList.size();i++)
                {
                    Appointment appointment = appointmentList.get(i);
                    if(appointment.getId() == id)
                    {
                        listener.onComplete(appointment);
                    }
                }
            }
        });
    }


    public void getAppointmentsList( final GetAppointmentsListListener listener) {
        DatabaseReference myRef = mdatabase.getReference(TABLE_DATABASE);
        final List<Appointment> appointmentList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appointmentList.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    Appointment appointment = toAppointment(value);
                        appointmentList.add(appointment);
                }
                Collections.sort(appointmentList);
                listener.onComplete(appointmentList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }

    private Appointment toAppointment(Map<String, Object> value)
    {
        String dateFromDatabase= (String) value.get("date");
        String startHour = (String) value.get("startHour");
        String endHour = (String) value.get("endHour");
        String treatmentId =(String) value.get("treatmentId");
        String clientId =(String) value.get("clientId");
        boolean approved =(boolean) value.get("approved");
        String id =(String) value.get("id");
        Appointment appointment = new Appointment(id,dateFromDatabase,startHour,endHour,treatmentId,clientId);
        return appointment;
    }
    private Map<String, Object> toMap(Appointment appointment)
    {
        Map<String, Object> value = new HashMap<>();
        value.put("id", appointment.getId());
        value.put("date", appointment.getDate());
        value.put("startHour" , appointment.getStartHour());
        value.put("endHour" , appointment.getEndHour());
        value.put("treatmentId", appointment.getTreatmentId());
        value.put("clientId", appointment.getClientId());
        return value;
    }


    public interface GetAppointmentsListListener {
        void onComplete(List<Appointment> appointmentList);
    }
    public interface GetAddAppointmentListener{
        void onComplete(boolean onSucess);
    }
    public interface GetAppointmentsByIdListener {
        void onComplete(Appointment appointment);
    }
}