package romiinger.nailbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.MyViewHolder>
        implements Filterable {

    private static final String TAG = "ClientsAdapter";

    private Context context;
    private List<MyUser> clientList;
    private List<MyUser> clientListFiltered;
    private ClientsAdapterListener listener;


    public ClientsAdapter(Context context, List<MyUser> clientList, ClientsAdapterListener listener) {
        this.clientList = clientList;
        for(int i=0; i<clientList.size();i++)
        {
            Log.d(TAG,"Contract user i:" + i +"name:" + clientList.get(i).getName());
        }
        this.context = context;
        this.listener =listener;
        this.clientListFiltered = clientList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MyUser user = clientListFiltered.get(position);
        Log.d("onBindViewHolder:","user.getName()"+ user.getName());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return clientListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    clientListFiltered = clientList;
                } else {
                    List<MyUser> filteredList = new ArrayList<>();
                    for (MyUser row : clientList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    clientListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clientListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clientListFiltered = (ArrayList<MyUser>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface ClientsAdapterListener {
        void onClientSelected(MyUser user);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email, phone;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.fullNameLayout);
            email = (TextView) view.findViewById(R.id.emailLayout);
            phone = (TextView) view.findViewById(R.id.phoneNumber);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onClientSelected(clientListFiltered.get(getAdapterPosition())); }
            });
        }
    }
}
