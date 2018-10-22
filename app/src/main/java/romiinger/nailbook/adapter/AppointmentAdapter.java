package romiinger.nailbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.Firebase.UserAdapterFirebase;
import romiinger.nailbook.R;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolderAppointment>
{
    private static final String TAG = "LimitationAdapter";
    private Context context;
    private List<Appointment> appointmentList;
    private AppointmentAdapterListener listener;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList, AppointmentAdapterListener listener) {
        this.appointmentList = appointmentList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolderAppointment onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_row, parent, false);

        return new MyViewHolderAppointment(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolderAppointment holder, int position) {
        final Appointment appointment = appointmentList.get(position);
        Log.d("onBindViewHolder:", "treatment.getName()" + appointment.getDate());
        holder.date.setText("Date: " + appointment.getDate());
        holder.startHour.setText("Hour:" +appointment.getStartHour());
        TreatmentsAdapterFirebase treatmentsAdapterFirebase = new TreatmentsAdapterFirebase();
        treatmentsAdapterFirebase.getTreatmentById(appointment.getTreatmentId(), new TreatmentsAdapterFirebase.GetTreatmentByIdListener() {
            @Override
            public void onComplete(Treatments treatment) {
                holder.treatmentId.setText("Treatment: " + treatment.getName());
            }
        });
        if(FirebaseUtil.isIsAdmin()){
            UserAdapterFirebase userAdapterFirebase = new UserAdapterFirebase();
            userAdapterFirebase.getUserById(appointment.getClientId(), new UserAdapterFirebase.GetUserByIdListener() {
                @Override
                public void onComplete(MyUser user) {
                    holder.clientId.setVisibility(View.VISIBLE);
                    holder.clientId.setText("Client Name: "+ user.getName());
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public interface AppointmentAdapterListener {
        void onAppointmetSelected(Appointment appointment);
    }

    public class MyViewHolderAppointment extends RecyclerView.ViewHolder {
        public TextView date, startHour, treatmentId ,clientId;

        public MyViewHolderAppointment(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.appointmentDate);
            startHour = (TextView) view.findViewById(R.id.appintmentHour);
            treatmentId = (TextView) view.findViewById(R.id.appointmentTreatment);
            clientId = (TextView) view.findViewById(R.id.appointmentCleantName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onAppointmetSelected(appointmentList.get(getAdapterPosition()));
                }
            });
        }
    }
}
