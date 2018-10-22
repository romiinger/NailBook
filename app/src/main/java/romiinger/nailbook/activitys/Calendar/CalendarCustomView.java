package romiinger.nailbook.activitys.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Class.WorkDay;
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Firebase.WorkDayAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.adapter.GridAdapter;

public class CalendarCustomView extends LinearLayout{

    private static final String TAG = "CalendarCustomView";
    private static final int MAX_CALENDAR_COLUMN=42;
    private ImageView btPrevious,btNext;
    private TextView currenDate;
    private GridView calendarGridView;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private DateFormat formatDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private GridAdapter mAdapter;
    private Context mContext;
    //private int month = calendar.get(Calendar.MONTH);
    //private int year = calendar.get(Calendar.YEAR);
    private int month = -1;
    private int year = -1;

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
                month =calendar.get(Calendar.MONTH)+1;
                year =calendar.get(Calendar.YEAR);

                //calendar.set(Calendar.MONTH,month);
                //calendar.set(Calendar.YEAR,year);
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
                month =calendar.get(Calendar.MONTH)-1;
                year =calendar.get(Calendar.YEAR);
                setUpCalendarAtapter();
            }
        });
    }

    private void setGridCellClickEvents()
    {
        calendarGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //calendar.set(year,month,i);
                if(month == -1 || year == -1){
                    month = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);
                }
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,i);
                String date =  formatDate.format(calendar.getTime());
                Log.d("TAG","cliked in date =" + date);
                if(FirebaseUtil.isIsAdmin())
                 updateViewDateToAdmin(date);
                else
                    updateViewDateToUser(date);

            }
        });
    }
    private void updateViewDateToUser (final String date){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        AppointmentAdapterFirebase appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        appointmentAdapterFirebase.getAppointmentsByUser(userId, new AppointmentAdapterFirebase.GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                boolean found=false;
                String appointmentsId=null;
                for(int i=0;i<appointmentList.size();i++){
                    if(appointmentList.get(i).getDate().equals(date)){
                        found=true;
                        appointmentsId=appointmentList.get(i).getId();
                        break;
                    }
                }
                if(found){
                    Intent intent = new Intent(getContext(), AppointmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle b = new Bundle();
                    b.putString("id",appointmentsId);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getContext(), NewAppointmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle b = new Bundle();
                    b.putString("date", date);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
            }
        });
    }
    private void updateViewDateToAdmin(final String date){
        WorkDayAdapterFirebase workDayAdapterFirebase = new WorkDayAdapterFirebase();
        workDayAdapterFirebase.getWorkDayByDate(date, new WorkDayAdapterFirebase.GetWorkDayByDateListener() {
            @Override
            public void onComplete(WorkDay workDay) {
                if(workDay.getId()!=null){
                    Intent intent = new Intent(getContext(), ApointmentListViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle b = new Bundle();
                    b.putString("date",date);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
                else
                    newWorkDayActivity(date);
            }
        });

    }

    private void newWorkDayActivity(String date)
    {
        Intent intent = new Intent(getContext(), NewWorkDayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle b = new Bundle();
        b.putString("date",date);
        intent.putExtras(b);
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
                int fisrtDayOdTheMonth = mCal.get(Calendar.DAY_OF_WEEK) -1;
                mCal.add(Calendar.DAY_OF_MONTH,-fisrtDayOdTheMonth);
                while(dayValuesInCells.size()< MAX_CALENDAR_COLUMN){
                    dayValuesInCells.add(mCal.getTime());
                    mCal.add(Calendar.DAY_OF_MONTH,1);
                }
                String sDate =formatter.format(calendar.getTime());
                currenDate.setText(sDate);
                mAdapter = new GridAdapter(context,dayValuesInCells,calendar,workDayList);
                calendarGridView.setAdapter(mAdapter);
            }
        });


    }

}
