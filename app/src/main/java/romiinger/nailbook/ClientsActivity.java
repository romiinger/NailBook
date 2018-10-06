package romiinger.nailbook;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;
import android.widget.Toast;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

public class ClientsActivity extends AppCompatActivity  {

    private List<MyUser> myUserList;
    private RecyclerView recyclerView;
    private ClientsAdapter mAdapter;
    private Toolbar toolbar;

    private static final String TAG = "ClientsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        createToolBar();

       // recyclerView = (RecyclerView) findViewById(R.id.rvClients);
//
       // UserAdapterFirebase userAdapterFirebase  = new UserAdapterFirebase();
       // userAdapterFirebase.getUsers(new UserAdapterFirebase.GetUsersListener() {
       //     @Override
       //     public void onComplete(final List<MyUser> userList)
       //     {
       //         Log.d(TAG,"list users is update");
       //         myUserList = userList;
       //         mAdapter = new ClientsAdapter(myUserList);
       //         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
       //         recyclerView.setLayoutManager(mLayoutManager);
       //         //ecyclerView.addItemDecoration(new DividerItemDecoration(ClientsActivity.this, LinearLayoutManager.VERTICAL));
       //         recyclerView.setItemAnimator(new DefaultItemAnimator());
       //         recyclerView.setAdapter(mAdapter);
       //         recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
       //             @Override
       //             public void onClick(View view, int position) {
       //                 MyUser user = userList.get(position);
       //                 Toast.makeText(getApplicationContext(), user.getName() + " is selected!", Toast.LENGTH_SHORT).show();
       //                 //toDo over to client layour
       //             }
//
       //             @Override
       //             public void onLongClick(View view, int position) {
//
       //             }
       //         }));
//
       //     }
       // });
    }
    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Clients ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clients, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
       // searchView.setOnQueryTextListener(ClientsActivity.class);

        return true;
    }
    @Override
    public void onResume()
    {
        super.onResume();
         recyclerView = (RecyclerView) findViewById(R.id.rvClients);

         UserAdapterFirebase userAdapterFirebase  = new UserAdapterFirebase();
         userAdapterFirebase.getUsers(new UserAdapterFirebase.GetUsersListener() {
             @Override
             public void onComplete(final List<MyUser> userList)
             {
                 Log.d(TAG,"list users is update");
                 myUserList = userList;
                 mAdapter = new ClientsAdapter(myUserList);
                 RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                 recyclerView.setLayoutManager(mLayoutManager);
                 //ecyclerView.addItemDecoration(new DividerItemDecoration(ClientsActivity.this, LinearLayoutManager.VERTICAL));
                 recyclerView.setItemAnimator(new DefaultItemAnimator());
                 recyclerView.setAdapter(mAdapter);
                 recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                     @Override
                     public void onClick(View view, int position) {
                         MyUser user = userList.get(position);
                         Toast.makeText(getApplicationContext(), user.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                         //toDo over to client layour
                     }
             @Override
                     public void onLongClick(View view, int position) {
             }
                 }));
     }
         });
    }
}