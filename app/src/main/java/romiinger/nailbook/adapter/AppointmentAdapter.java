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
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.R;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolderAppointment>
      implements Filterable {
    private static final String TAG = "LimitationAdapter";
    private Context context;
    private List<Appointment> appointmentList;
    private List<Appointment> appointmentListFiltered;
    private AppointmentAdapterListener listener;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList, AppointmentAdapterListener listener) {
        this.appointmentList = appointmentList;
        this.context = context;
        this.listener = listener;
        this.appointmentListFiltered = appointmentList;
    }

    @Override
    public MyViewHolderAppointment onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_row, parent, false);

        return new MyViewHolderAppointment(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolderAppointment holder, int position) {
        final Appointment appointment = appointmentListFiltered.get(position);
        Log.d("onBindViewHolder:", "treatment.getName()" + appointment.getDate());
        holder.date.setText(appointment.getDate());
        holder.startHour.setText(appointment.getStartHour());
        holder.treatmentId.setText(appointment.getTreatmentId());
        holder.clientId.setText(appointment.getClientId());
        //toDo approve appointment
    }
    @Override
    public int getItemCount() {
        return appointmentListFiltered.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    appointmentListFiltered = appointmentList;
                } else {
                    List<Appointment> filteredList = new ArrayList<>();
                    for (Appointment row : appointmentList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getClientId().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    appointmentListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = appointmentListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                appointmentListFiltered = (ArrayList<Appointment>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
                    listener.onAppointmetSelected(appointmentListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
