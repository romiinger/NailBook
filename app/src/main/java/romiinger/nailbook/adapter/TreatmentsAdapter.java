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

import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.R;

public class TreatmentsAdapter extends RecyclerView.Adapter<TreatmentsAdapter.MyViewHolderTreatment>
        implements Filterable {
    private static final String TAG = "TreatmentsAdapter";
    private Context context;
    private List<Treatments> treatmentsList;
    private List<Treatments> treatmentsListFiltered;
    private TreatmentsAdapterListener listener;

    public TreatmentsAdapter(Context context, List<Treatments> treatmentsList, TreatmentsAdapterListener listener) {
        this.treatmentsList = treatmentsList;
        for(int i=0; i<treatmentsList.size();i++)
        {
            Log.d(TAG,"Contract user i:" + i +"name:" + treatmentsList.get(i).getName());
        }
        this.context = context;
        this.listener =listener;
        this.treatmentsListFiltered = treatmentsList;
    }

    @Override
    public MyViewHolderTreatment onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treatment_row, parent, false);

        return new MyViewHolderTreatment(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolderTreatment holder, int position) {
        final Treatments treatment = treatmentsListFiltered.get(position);
        Log.d("onBindViewHolder:","treatment.getName()"+ treatment.getName());
        holder.name.setText(treatment.getName());
        holder.duration.setText(treatment.getDuration()+ " minutes");
        holder.price.setText(treatment.getPrice() + " ₪");
    }

    @Override
    public int getItemCount() {
        return treatmentsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    treatmentsListFiltered = treatmentsList;
                } else {
                    List<Treatments> filteredList = new ArrayList<>();
                    for (Treatments row : treatmentsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()))  {
                            filteredList.add(row);
                        }
                    }

                    treatmentsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = treatmentsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                treatmentsListFiltered = (ArrayList<Treatments>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface TreatmentsAdapterListener {
        void onClientSelected(Treatments user);
    }

    public class MyViewHolderTreatment extends RecyclerView.ViewHolder {
        public TextView name, duration, price;

        public MyViewHolderTreatment(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameTreatment);
            duration = (TextView) view.findViewById(R.id.durationLayout);
            price = (TextView) view.findViewById(R.id.priceLayout);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onClientSelected(treatmentsListFiltered.get(getAdapterPosition())); }
            });
        }
    }
}
