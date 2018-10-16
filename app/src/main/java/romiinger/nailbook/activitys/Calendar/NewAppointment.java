package romiinger.nailbook.activitys.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.R;

public class NewAppointment extends AppCompatActivity {

    private static final String TAG = "NewAppointment";

    private FloatingActionButton btsave;
    private ListView mListView;
    private SimpleCursorAdapter mSCA;
    private EditText inputStartHour,inputEndHour, inputDate;
    private Calendar myCalendar = Calendar.getInstance();
    private List<Treatments> mTreatmentsList;
    private Treatments mTreatment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        inputDate= (EditText) findViewById(R.id.inputDateLayout);
        inputStartHour= (EditText) findViewById(R.id.inputOpenHour);
        mListView = (ListView) findViewById(R.id.lvTreatments);
        btsave = (FloatingActionButton) findViewById(R.id.saveAppointment);

        getDatePicker(inputDate);
        getHourPicker(inputStartHour);
        getTreatmentsView();
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewAppointment();
            }
        });

    }

    private void saveNewAppointment()
    {
        String date = inputDate.getText().toString();
        String startHour = inputStartHour.getText().toString();
        String treatmentId = mTreatment.getId();
        String clienId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    }
    private void getTreatmentsView()
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
                final StableArrayAdapter adapter = new StableArrayAdapter(NewAppointment.this,
                        android.R.layout.simple_expandable_list_item_1, treatmnetsNameList);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        btsave.setVisibility(View.VISIBLE);
                        mTreatment = mTreatmentsList.get(position);
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
                new TimePickerDialog(NewAppointment.this, time, myCalendar
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
                new DatePickerDialog(NewAppointment.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
