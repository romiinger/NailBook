package romiinger.nailbook.activitys.Calendar;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Class.WorkDay;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Firebase.WorkDayAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.activitys.Utils.MyDatePickerFragment;
import romiinger.nailbook.activitys.Utils.MyPickerDateAndTime;

public class NewWorkDayActivity extends AppCompatActivity {

    private Bundle bundle;
    private String dateBundle;
    private static final String TAG = "NewWorkDayActivity";
    private Toolbar toolbar;
    private WorkDay mWorkDay;
    private EditText inputOpenHour,inputClouseHour, inputDate;
    private FloatingActionButton btSave,btNewLimitation;
    private Date mDate;
    private  DateFormat formatDate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private DateFormat formatHour = new SimpleDateFormat("HH:mm");
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workday);
        createToolBar();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        setViewInput();
        //recyclerView = (RecyclerView) findViewById(R.id.rvWalletUpdates);//todo
    }
    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("New work Day ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    private void setViewInput() {
        inputDate= (EditText) findViewById(R.id.inputDateLayout);
        inputOpenHour = (EditText) findViewById(R.id.inputOpenHour);
        inputClouseHour = (EditText) findViewById(R.id.inputClouseHour);
        getDatePicker(inputDate);
        getHourPicker(inputOpenHour);
        getHourPicker(inputClouseHour);


        final WorkDayAdapterFirebase workDayAdapterFirebase = new WorkDayAdapterFirebase();
        final String id =workDayAdapterFirebase.getNewWorkDayId();
        btSave = (FloatingActionButton)findViewById(R.id.saveWorkDay);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = inputDate.getText().toString();
                String openHour = inputOpenHour.getText().toString();
                String clouseHour = inputClouseHour.getText().toString();
                if (date != null && openHour != null && clouseHour!= null  ){
                    btSave.setVisibility(View.INVISIBLE);
                    final WorkDay workDay = new WorkDay(id,date,openHour,clouseHour);
                    workDayAdapterFirebase.addWorkDay(workDay, new WorkDayAdapterFirebase.GetAddWorkDayListener() {
                        @Override
                        public void onComplete(List<Appointment> emptyAppointments) {
                            if(emptyAppointments!=null)
                            {
                                btNewLimitation = (FloatingActionButton)findViewById(R.id.addLimitation);
                                btNewLimitation.setVisibility(View.VISIBLE);
                                btNewLimitation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(NewWorkDayActivity.this, LimitationActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        NewWorkDayActivity.this.startActivity(intent);
                                        //context.finish();
                                }});
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(NewWorkDayActivity.this,"failed to create new work date",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void getHourPicker(final EditText editText)
    {
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay );
                myCalendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                editText.setText(sdf.format(myCalendar.getTime()));
                Log.d(TAG, "hour= " + myCalendar.get(Calendar.HOUR_OF_DAY) +":" + myCalendar.get(Calendar.MINUTE));
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(NewWorkDayActivity.this, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });
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
                new DatePickerDialog(NewWorkDayActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(NewWorkDayActivity.this, CustomCalendarActivity.class);
        startActivity(intent);
        finish();
    }
}
