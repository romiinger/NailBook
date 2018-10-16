package romiinger.nailbook.activitys.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MyPickerDateAndTime {

    public void getHourPicker(final EditText editText, final Calendar calendar, final Context context)
    {
        final Calendar mycalendar = Calendar.getInstance();
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                mycalendar.set(Calendar.HOUR_OF_DAY,hourOfDay );
                mycalendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                editText.setText(sdf.format(mycalendar.getTime()));
                //Log.d(TAG, "hour= " + calendar.get(Calendar.HOUR_OF_DAY) +":" + calendar.get(Calendar.MINUTE));
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(context, time, mycalendar
                        .get(Calendar.HOUR_OF_DAY), mycalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });
    }
    public void  getDatePicker(final EditText editText, final Calendar calendar, final Context context)
    {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH  , monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                editText.setText(sdf.format(calendar.getTime()));
                //Log.d(TAG, "day= " + editText.getText().toString());
            }

        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
