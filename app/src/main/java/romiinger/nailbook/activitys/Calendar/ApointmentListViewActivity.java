package romiinger.nailbook.activitys.Calendar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.R;
import romiinger.nailbook.adapter.AppointmentAdapter;

public class ApointmentListViewActivity extends AppCompatActivity {

    private static final String TAG = "ApointmentUserActivity";
    private Toolbar toolbar;
    private static Context context;
    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> mAppointmentList;
    private String userIdBundle;
    private String mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_list);
        ApointmentListViewActivity.context = getApplicationContext();
        Log.d(TAG, "in ApointmentListViewActivity.onCreate()");
        createToolBar();
        Bundle bundle =getIntent().getExtras();
        if(bundle!=null){
            userIdBundle = bundle.getString("userId");
            mDate = bundle.getString("date");
        }
    }

    public static Context getAppContext() {
        return ApointmentListViewActivity.context;
    }



    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ApointmentListViewActivity.this, CustomCalendarActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.rvTreatments);
        if (FirebaseUtil.isIsAdmin()) {
            if (userIdBundle != null)
                updateRecicleViewClient(userIdBundle);
            else updateRecicleViewAdministartor();
        }
        else {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            updateRecicleViewClient(userId);
        }

    }

    private void updateRecicleViewAdministartor() {
        String date;
        if(mDate==null){
            Calendar calendar = Calendar.getInstance();
            DateFormat formatDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
            date = formatDate.format(calendar.getTime());
        }
        else
            date =mDate;
        Log.d(TAG, "today is: " + date);
        AppointmentAdapterFirebase appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        appointmentAdapterFirebase.getClientAppointmnetByDate(date, new AppointmentAdapterFirebase.GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                updateAdapter(appointmentList);
            }
        });
    }
    private void updateRecicleViewClient(final String userId) {

        AppointmentAdapterFirebase appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        appointmentAdapterFirebase.getAppointmentsByUser(userId, new AppointmentAdapterFirebase.GetAppointmentsListListener() {
            @Override
            public void onComplete(final List<Appointment> appointmentList) {
                updateAdapter(appointmentList);

            }
        });
    }
    private void updateAdapter(List<Appointment> appointmentList) {

        Log.d(TAG, "list appointments is update");
        mAppointmentList = appointmentList;

        appointmentAdapter = new AppointmentAdapter(getAppContext(), appointmentList, new AppointmentAdapter.AppointmentAdapterListener() {
            @Override
            public void onAppointmetSelected(Appointment appointment) {
                Intent intent = new Intent(ApointmentListViewActivity.this, AppointmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle b = new Bundle();
                b.putString("id", appointment.getId());
                intent.putExtras(b);
                startActivity(intent);
                // Toast.makeText(getApplicationContext() ,"Selected: " + user.getName() + ", " + user.getPhone(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(ApointmentListViewActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(appointmentAdapter);
    }
    private void createToolBar() {
        Log.d(TAG, " In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        if(FirebaseUtil.isIsAdmin())
            toolbar.setSubtitle("Appointments ");
        else
            toolbar.setSubtitle("My Appointments ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

}
