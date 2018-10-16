package romiinger.nailbook.activitys.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.AttributeSet;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import romiinger.nailbook.Class.WorkDay;
import romiinger.nailbook.Firebase.WorkDayAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.adapter.GridAdapter;

public class CalendarCustomView extends LinearLayout{

    private static final String TAG = "CalendarCustomView";
    private static final int MAX_CALENDAR_COLUMN=42;
    private ImageView btPrevious,btNext;
    private TextView currenDate;
    private GridView calendarGridView;
    private int month,year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private GridAdapter mAdapter;
    private Context mContext;

    public CalendarCustomView(Context context)
    {
        super(context);
        this.context = context;
    }
    public CalendarCustomView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAtapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }
    /*
    public void CalendarCustomView(Context context,AttributeSet attrs, int defSyleAttr)    {
        super(context,attrs,defSyleAttr);
    }
    */
    private void initializeUILayout()
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar, this);
        btPrevious = (ImageView)view.findViewById(R.id.previous_month);
        btNext = (ImageView)view.findViewById(R.id.next_month);
        currenDate = (TextView)view.findViewById(R.id.display_current_date);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
    }
    private void setPreviousButtonClickEvent()
    {
        btPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,-1);
                setUpCalendarAtapter();
            }
        });
    }
    private void setNextButtonClickEvent()
    {
        btNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,1);
                setUpCalendarAtapter();
            }
        });
    }

    private void setGridCellClickEvents()
    {
        calendarGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String day =  Integer.toString(i);
                newWorkDayActivity(day);
               Log.d("TAG","cliked in" + i);
            }
        });
    }

    private void newWorkDayActivity(String day)
    {
        Intent intent = new Intent(getContext(), NewWorkDayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        //context.finish();
    }
    private void setUpCalendarAtapter()
    {
        final List<Date> dayValuesInCells = new ArrayList<>();
        WorkDayAdapterFirebase workDayAdapterFirebase = new WorkDayAdapterFirebase();
        workDayAdapterFirebase.getWorkDayList(new WorkDayAdapterFirebase.GetWorkDayListListener() {
            @Override
            public void onComplete(List<WorkDay> workDayList) {
                Calendar mCal =(Calendar)calendar.clone();
                mCal.set(Calendar.DAY_OF_MONTH,1);
                int fisrtDayOdTheMonth =mCal.get(Calendar.DAY_OF_WEEK) -1;
                mCal.add(Calendar.DAY_OF_MONTH,-fisrtDayOdTheMonth);
                while(dayValuesInCells.size()< MAX_CALENDAR_COLUMN){
                    dayValuesInCells.add(mCal.getTime());
                    mCal.add(Calendar.DAY_OF_MONTH,1);
                }
                Log.d(TAG, "Number of date" + dayValuesInCells.size());
                String sDate =formatter.format(calendar.getTime());
                currenDate.setText(sDate);
                mAdapter = new GridAdapter(context,dayValuesInCells,calendar,workDayList);
                calendarGridView.setAdapter(mAdapter);
            }
        });


    }

}
