package romiinger.nailbook;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

public class ClientsActivity extends AppCompatActivity
       {

    private List<MyUser> myUserList;
    private RecyclerView recyclerView;
    private ClientsAdapter mAdapter;
    private Toolbar toolbar;
    private SearchView searchView;

    private static final String TAG = "ClientsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        createToolBar();
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

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search) .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
           @Override
           public boolean onOptionsItemSelected(MenuItem item) {
               int id = item.getItemId();
               if (id == R.id.action_search) {
                   return true;
               }
               return super.onOptionsItemSelected(item);
           }

           @Override
           public void onBackPressed() {
               // close search view on back button pressed
               if (!searchView.isIconified()) {
                   searchView.setIconified(true);
                   return;
               }
               Intent intent = new Intent(ClientsActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
           }

           private void whiteNotificationBar(View view) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   int flags = view.getSystemUiVisibility();
                   flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                   view.setSystemUiVisibility(flags);
                   getWindow().setStatusBarColor(Color.WHITE);
               }
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
                for(int i=0; i<myUserList.size();i++)
                {
                    Log.d(TAG,"Contract user i:" + i +"name:" + myUserList.get(i).getName());
                }
                mAdapter = new ClientsAdapter(getApplicationContext(), myUserList, new ClientsAdapter.ClientsAdapterListener() {
                    @Override
                    public void onClientSelected(MyUser user) {
                        Toast.makeText(getApplicationContext() ,"Selected: " + user.getName() + ", " + user.getPhone(), Toast.LENGTH_LONG).show();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(ClientsActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
}