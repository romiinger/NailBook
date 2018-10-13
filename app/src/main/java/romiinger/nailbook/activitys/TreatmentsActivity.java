package romiinger.nailbook.activitys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.adapter.TreatmentsAdapter;

public class TreatmentsActivity extends AppCompatActivity {

    private List<Treatments> mtreatmentsList;
    private RecyclerView recyclerView;
    private TreatmentsAdapter treatmentsAdapter;
    private Toolbar toolbar;
    private SearchView searchView;

    private static final String TAG = "TreatmentsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatments_list);
        Log.d(TAG,"in TreatmentsActivity.onCreate()");
        createToolBar();
    }

    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Treatments ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_treatments, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_treatments) .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                treatmentsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                treatmentsAdapter.getFilter().filter(query);
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
        Intent intent = new Intent(TreatmentsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.rvTreatments);

        TreatmentsAdapterFirebase treatmentsAdapterFirebase = new TreatmentsAdapterFirebase();
        treatmentsAdapterFirebase.getTreatmentsList(new TreatmentsAdapterFirebase.GetTreatmentListListener() {
            @Override
            public void onComplete(final List<Treatments> treatmentsList)
            {
                Log.d(TAG,"list users is update");
                mtreatmentsList = treatmentsList;
                for(int i=0; i<treatmentsList.size();i++)
                {
                    Log.d(TAG,"Contract user i:" + i +"name:" + treatmentsList.get(i).getName());
                }
                treatmentsAdapter = new TreatmentsAdapter(getApplicationContext(), treatmentsList, new TreatmentsAdapter.TreatmentsAdapterListener() {
                    @Override
                    public void onClientSelected(Treatments treatments) {
                        //Intent intent = new Intent(TreatmentsActivity.this, TreatmentActivity.class);
                       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       // Bundle b = new Bundle();
                      //  b.putString("userId",user.getStId());
                      //  intent.putExtras(b);
                      //  startActivity(intent);
                        // Toast.makeText(getApplicationContext() ,"Selected: " + user.getName() + ", " + user.getPhone(), Toast.LENGTH_LONG).show();
                       // finish();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(TreatmentsActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(treatmentsAdapter);
            }
        });
    }
}
