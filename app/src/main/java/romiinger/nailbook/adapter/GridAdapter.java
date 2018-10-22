package romiinger.nailbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import romiinger.nailbook.Class.WorkDay;
import romiinger.nailbook.R;

public class GridAdapter extends ArrayAdapter {

    private static final String TAG = "GridAdapter";
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<WorkDay> allEvents;
    public GridAdapter(Context context,List<Date> monthlyDates,Calendar currentDate,List<WorkDay> allEvents)
    {
     super(context, R.layout.single_cell_layout);
     this.monthlyDates = monthlyDates;
     this.currentDate = currentDate;
     this.allEvents = allEvents;
     mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH)+1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view =convertView;
        if(view == null)
        {
            view = mInflater.inflate(R.layout.single_cell_layout,parent,false);
        }
        if(displayMonth == currentMonth && displayYear == currentYear)
        {
            view.setBackgroundColor(Color.parseColor( "#CE93D8"));
        }
        else {
            view.setBackgroundColor(Color.parseColor( "#cccccc"));
        }
        //Add day to calendar
        TextView cellNumber =(TextView)view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));
        //Add workDay to calendar
        TextView eventIndicator =(TextView)view.findViewById(R.id.event_id);
        Calendar eventCalendar = Calendar.getInstance();
        for (int i=0; i<allEvents.size();i++){
            WorkDay event = allEvents.get(i);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate;
            try {
                startDate = df.parse(event.getDate().toString());
                eventCalendar.setTime(startDate);
            } catch (Exception e) {
               Log.e(TAG,"exeption "+e);
            }

            if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) &&
                    displayMonth == eventCalendar.get(Calendar.MONTH)+1 &&
                    displayYear == eventCalendar.get(Calendar.YEAR))
            {
                eventIndicator.setBackgroundColor(Color.parseColor("#FF4081"));
            }
        }
        return view;
    }
    @Override
    public int getCount()
    {
        return monthlyDates.size();
    }
    @NonNull
    @Override
    public Object getItem(int position)
    {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item)
    {
        return monthlyDates.indexOf(item);
    }


}
