package romiinger.nailbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import romiinger.nailbook.activitys.CalendarCustomView;
import romiinger.nailbook.activitys.MainActivity;

public class CustomCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calendar);
        CalendarCustomView customView = (CalendarCustomView)findViewById(R.id.custom_calendar);
    }
    @Override
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(CustomCalendarActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
