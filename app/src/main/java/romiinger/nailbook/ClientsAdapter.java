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

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.MyViewHolder>  {

    private List<MyUser> userList;
    private List<MyUser> contactListFiltered;
    private ClientsAdapterListener listener;


    public ClientsAdapter(List<MyUser> userList) {
     this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.client_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyUser user = userList.get(position);
        Log.d("onBindViewHolder:","user.getName()"+ user.getName());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
                    listener.onClientSelected(contactListFiltered.get(getAdapterPosition())); }
            });
        }
    }
}

