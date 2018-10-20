package romiinger.nailbook.activitys.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Class.ScheduleAppointment;
import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.R;

public class NewAppointmentActivity extends AppCompatActivity {

    private static final String TAG = "NewAppointment";
    private static Context context;
    private Toolbar toolbar;
    private FloatingActionButton btsave, btSearch;
    private RecyclerView recyclerView;
    private ListView mListView;
    private SimpleCursorAdapter mSCA;
    private EditText inputStartHour,inputEndHour, inputDate;
    private Calendar myCalendar = Calendar.getInstance();
    private List<Appointment> mAppointmentList;
    private List<Treatments> mTreatmentsList;
    private Appointment mAppointment;
    private Treatments mTreatment;
    private CheckBox mcheckBoxT;
    private TextView mTreatmentView, mAppointmentView;
    private String mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        createToolBar();
        NewAppointmentActivity.context=getApplicationContext();
        Bundle bundle =getIntent().getExtras();
        inputDate= (EditText) findViewById(R.id.inputDateLayout);
        if(bundle!=null){
            mDate = bundle.getString("date");
            inputDate.setText(mDate);
        }
        else{
            getDatePicker(inputDate);
        }

        mListView = (ListView) findViewById(R.id.lvTreatments);
        btsave = (FloatingActionButton) findViewById(R.id.saveAppointment);
        btSearch =(FloatingActionButton)findViewById(R.id.search_appointments);
        mcheckBoxT = (CheckBox)findViewById(R.id.record_checkbox_treatmet);
        mTreatmentView = (TextView) findViewById(R.id.tile_treatments);
        mAppointmentView = (TextView) findViewById(R.id.textChoiceAppointment);

        getTreatmentsView();
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppointmentView();
                TextView text = (TextView)findViewById(R.id.textChoiceAppointment);
                text.setVisibility(View.VISIBLE);

            }
        });
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btsave.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.INVISIBLE);
                saveNewAppointment();

            }
        });

    }

    @Override
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(NewAppointmentActivity.this, CustomCalendarActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveNewAppointment()
    {

        ScheduleAppointment scheduleAppointment = new ScheduleAppointment();
        scheduleAppointment.appendAppointmnetToClient(mAppointment, mTreatment, new ScheduleAppointment.GetSchechuleListener() {
            @Override
            public void onComplete(boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(NewAppointmentActivity.this, "save appointmnets sucess", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewAppointmentActivity.this, AppointmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle b = new Bundle();
                    b.putString("id",mAppointment.getId());
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }

                else {
                    Toast.makeText(NewAppointmentActivity.this, "save appointmnets failed", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });


    }
    private void createToolBar() {
        //Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Calendar - New Appointment ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);

    }
    private void getAppointmentView()
    {
        AppointmentAdapterFirebase appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        appointmentAdapterFirebase.getEmptyAppointmentsByDate(inputDate.getText().toString(), new AppointmentAdapterFirebase.GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                mAppointmentList = appointmentList;
                final ArrayList<String> emptysHour = new ArrayList<>();

                for(int i=0 ;i< appointmentList.size();i++)
                {
                    if(!appointmentList.get(i).getTreatmentId().equals(mTreatment.getId() ))
                    {

                        int thisDuration = Integer.parseInt(mTreatment.getDuration().toString());
                        String appointmentTreatment = null;
                        for (int j=0;j<mTreatmentsList.size();j++)
                        {
                            String aId=appointmentList.get(i).getTreatmentId();
                            String trId =mTreatmentsList.get(j).getId();
                            if(aId.equals(trId))
                            {
                                appointmentTreatment=mTreatmentsList.get(j).getDuration();
                                break;
                            }
                        }
                        int appointmentsDuration = Integer.parseInt(appointmentTreatment);
                        if(!(thisDuration > appointmentsDuration))
                        {
                            emptysHour.add(appointmentList.get(i).getStartHour());
                        }
                    }
                    else
                    {
                        emptysHour.add(appointmentList.get(i).getStartHour());
                    }
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(NewAppointmentActivity.this,
                        android.R.layout.simple_expandable_list_item_1, emptysHour);
                mListView.setAdapter(adapter);
                mListView.setVisibility(View.VISIBLE);
                btSearch.setVisibility(View.INVISIBLE);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {

                        btsave.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        mAppointment = mAppointmentList.get(position);
                        mAppointmentView.setText("Hour: "+ mAppointment.getStartHour());
                        Log.d(TAG,"appointmnet starthour= "+ mAppointment.getStartHour()+" is clicked");
                    }
                });
            }
        });
    }
    private  void getTreatmentsView()
    {
        TreatmentsAdapterFirebase treatmentsAdapterFirebase = new TreatmentsAdapterFirebase();
        treatmentsAdapterFirebase.getTreatmentsList(new TreatmentsAdapterFirebase.GetTreatmentListListener() {
            @Override
            public void onComplete(List<Treatments> treatmentsList) {

                mTreatmentsList = treatmentsList;
                final ArrayList<String> treatmnetsNameList = new ArrayList<>();
                for (int i = 0; i < treatmentsList.size(); i++) {
                    treatmnetsNameList.add(treatmentsList.get(i).getName());
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(NewAppointmentActivity.this,
                        android.R.layout.simple_expandable_list_item_1, treatmnetsNameList);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {



                        mTreatment = mTreatmentsList.get(position);
                        mTreatmentView.setText("Treatment Name: "+mTreatment.getName());
                        mAppointmentView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        btSearch.setVisibility(View.VISIBLE);
                        Log.d(TAG,"treatemnt name= "+ mTreatment.getName()+" is clicked");

                    }
                });
            }

        });
    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private void  getDatePicker(final EditText editText)
    {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH  , monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                editText.setText(sdf.format(myCalendar.getTime()));
                Log.d(TAG, "day= " + editText.getText().toString());
            }

        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewAppointmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    public static Context getAppContext() {
        return NewAppointmentActivity.context;
    }
}
