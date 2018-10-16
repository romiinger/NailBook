package romiinger.nailbook.activitys.Calendar;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.R;
import romiinger.nailbook.activitys.MainActivity;
import romiinger.nailbook.activitys.Utils.MyDatePickerFragment;

public class CustomCalendarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calendar);
        CalendarCustomView customView = (CalendarCustomView)findViewById(R.id.custom_calendar);
        createToolBar();
        FloatingActionButton newWorkDay = (FloatingActionButton)findViewById(R.id.addLimitation);
        newWorkDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newWorkDayActivity();

            }
        });
    }

    private void newWorkDayActivity()
    {
        Intent intent = new Intent(CustomCalendarActivity.this, NewWorkDayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(CustomCalendarActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        menu.add("").setIcon(romiinger.nailbook.R.drawable.ic_launcher_background);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        return true;
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            //getUserInstance();
            switch (item.getItemId()) {
                case romiinger.nailbook.R.id.limitationMenu: {
                    Intent intent = new Intent(CustomCalendarActivity.this, LimitationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                case romiinger.nailbook.R.id.appointmensts_menu: {
                    //Log.d(TAG, "Start profile_activity");
                    Intent intent = new Intent(CustomCalendarActivity.this, NewAppointment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }

        return true;
    }
    private void createToolBar() {
        toolbar = (Toolbar) findViewById(romiinger.nailbook.R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }
    public void showMenu() {
        invalidateOptionsMenu();
        //createNavigationView();
    }
}
