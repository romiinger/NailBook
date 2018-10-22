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

import romiinger.nailbook.Class.LimitationEvent;
import romiinger.nailbook.Firebase.LimitationAdapterFirebase;
import romiinger.nailbook.R;

public class LimitationAdapter extends RecyclerView.Adapter<LimitationAdapter.MyViewHolderLimitation>

        implements Filterable {
    private static final String TAG = "LimitationAdapter";
    private Context context;
    private List<LimitationEvent> limitationList;
    private List<LimitationEvent> limitationListFiltered;
    private LimitationAdapterListener listener;

    public LimitationAdapter(Context context, List<LimitationEvent> limitationList, LimitationAdapterListener listener) {
        this.limitationList = limitationList;
        this.context = context;
        this.listener = listener;
        this.limitationListFiltered = limitationList;
    }

    @Override
    public MyViewHolderLimitation onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.limitation_row, parent, false);

        return new MyViewHolderLimitation(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolderLimitation holder, int position) {
        final LimitationEvent limitation = limitationListFiltered.get(position);
        Log.d("onBindViewHolder:", "treatment.getName()" + limitation.getName());
        holder.name.setText(limitation.getName());
        holder.startHour.setText(limitation.getStartHour().toString());
        holder.endHour.setText(limitation.getEndHour().toString());
    }

    @Override
    public int getItemCount() {
        return limitationListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    limitationListFiltered = limitationList;
                } else {
                    List<LimitationEvent> filteredList = new ArrayList<>();
                    for (LimitationEvent row : limitationList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    limitationListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = limitationListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                limitationListFiltered = (ArrayList<LimitationEvent>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface LimitationAdapterListener {
        void onLimitationSelected(LimitationEvent limitation);
    }

    public class MyViewHolderLimitation extends RecyclerView.ViewHolder {
        public TextView name, startHour, endHour;

        public MyViewHolderLimitation(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.limitationName);
            startHour = (TextView) view.findViewById(R.id.limitationStartHour);
            endHour = (TextView) view.findViewById(R.id.limitationEnd);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onLimitationSelected(limitationListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}