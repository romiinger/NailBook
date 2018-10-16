package romiinger.nailbook.activitys.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import romiinger.nailbook.Class.LimitationEvent;
import romiinger.nailbook.Firebase.LimitationAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.activitys.Utils.MyPickerDateAndTime;
import romiinger.nailbook.adapter.LimitationAdapter;

public class LimitationActivity extends AppCompatActivity {

    private static final String TAG = "LimitationActivity";
    private Toolbar toolbar;
    private TextInputLayout inputName;
    private EditText inputStartHour,inputEndHour, inputDate;
    private FloatingActionButton btSave;
    private Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_limitation);
        createToolBar();
    }
    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("New limitation work day ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        inputName= (TextInputLayout) findViewById(R.id.inputNameLimitation);
        inputDate= (EditText) findViewById(R.id.inputDateLayout);
        inputStartHour= (EditText) findViewById(R.id.inputOpenHour);
        inputEndHour = (EditText) findViewById(R.id.inputClouseHour);
        getDatePicker(inputDate);
        getHourPicker(inputStartHour);
        getHourPicker(inputEndHour);
        final LimitationAdapterFirebase limitationAdapterFirebase = new LimitationAdapterFirebase();
        final String id = limitationAdapterFirebase.getNewLimitationId();
        btSave = (FloatingActionButton)findViewById(R.id.saveLimitation);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = inputDate.getText().toString();
                String startHour = inputStartHour.getText().toString();
                String endHour = inputEndHour.getText().toString();
                String name = inputName.getEditText().getText().toString();
                if (date != null && name != null && startHour!= null && endHour != null ){
                    btSave.setVisibility(View.INVISIBLE);
                    final LimitationEvent workDay = new LimitationEvent(id,date,startHour,endHour,name);
                    limitationAdapterFirebase.addLimitationEven(workDay, new LimitationAdapterFirebase.GetAddLimitationListener() {
                        @Override
                        public void onComplete(boolean onSucess) {
                            if(onSucess)
                            {
                               //toDo back to WorkDay
                                Toast.makeText(LimitationActivity.this,"save new limitation success ",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LimitationActivity.this, CustomCalendarActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(LimitationActivity.this,"failed to create new limitation ",Toast.LENGTH_SHORT).show();
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
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                editText.setText(sdf.format(myCalendar.getTime()));
                Log.d(TAG, "hour= " + myCalendar.get(Calendar.HOUR_OF_DAY) +":" + myCalendar.get(Calendar.MINUTE));
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(LimitationActivity.this, time, myCalendar
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
                new DatePickerDialog(LimitationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(LimitationActivity.this, CustomCalendarActivity.class);
        startActivity(intent);
        finish();
    }
}
