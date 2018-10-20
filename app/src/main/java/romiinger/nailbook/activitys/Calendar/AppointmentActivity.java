package romiinger.nailbook.activitys.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Class.ScheduleAppointment;
import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.R;

public class AppointmentActivity extends AppCompatActivity {

    private static final String TAG = "AppointmentActivity";
    private Toolbar toolbar;
    private static Context context;
    private Bundle bundle;
    private String appointmnetId;
    private Appointment mAppointment;
    private FloatingActionButton btCancel;
    private TextView name,date,hour,duration,price;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"in onCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointmnet);
        AppointmentActivity.context=getApplicationContext();
        createToolBar();
        bundle = getIntent().getExtras();
        appointmnetId = bundle.getString("id");
        Log.d(TAG,"appointmentID= "+appointmnetId);
        name = (TextView) findViewById(R.id.nameTreatment);
        date = (TextView) findViewById(R.id.layoutDate);
        hour = (TextView) findViewById(R.id.hourAppointment);
        duration = (TextView) findViewById(R.id.duratioTreatment);
        price = (TextView) findViewById(R.id.priceTreatment);
        btCancel = (FloatingActionButton)findViewById(R.id.removeAppointment);
        AppointmentAdapterFirebase appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        appointmentAdapterFirebase.getAppointmentById(appointmnetId, new AppointmentAdapterFirebase.GetAppointmentsByIdListener() {
            @Override
            public void onComplete(Appointment appointment) {

                mAppointment = appointment;
                TreatmentsAdapterFirebase treatmentsAdapterFirebase = new TreatmentsAdapterFirebase();
                treatmentsAdapterFirebase.getTreatmentById(mAppointment.getTreatmentId(), new TreatmentsAdapterFirebase.GetTreatmentByIdListener() {
                    @Override
                    public void onComplete(Treatments treatment) {
                        Log.d(TAG,"treatments name = "+treatment.getName());
                        name.setText(treatment.getName());
                        date.setText(mAppointment.getDate());
                        hour.setText(mAppointment.getStartHour());
                        duration.setText(treatment.getDuration());
                        price.setText(treatment.getPrice());
                        btCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ScheduleAppointment scheduleAppointment = new ScheduleAppointment();
                                scheduleAppointment.removeClientAppointment(mAppointment, new ScheduleAppointment.GetSchechuleListener() {
                                    @Override
                                    public void onComplete(boolean isSuccess) {
                                        String messege = "removed appointment failed";
                                        if (isSuccess) {
                                            messege = "removed appointment successes";
                                            onBackPressed();
                                        }
                                        Toast.makeText(AppointmentActivity.this,messege,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    private void createToolBar() {
        //Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Calendar - Appointment ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);

    }
    public static Context getAppContext() {
        return AppointmentActivity.context;
    }
    @Override
    public void onResume(){
        super.onResume();

    }
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(AppointmentActivity.this, CustomCalendarActivity.class);
        startActivity(intent);
        finish();
    }
}