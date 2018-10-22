package romiinger.nailbook.activitys.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.R;
import romiinger.nailbook.activitys.MainActivity;

public class CustomCalendarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calendar);
        CalendarCustomView customView = (CalendarCustomView)findViewById(R.id.custom_calendar);
        createToolBar();
        if (FirebaseUtil.isIsAdmin()) {
            FloatingActionButton newWorkDay = (FloatingActionButton)findViewById(R.id.addNewWorkday);
            newWorkDay.setVisibility(View.VISIBLE);
            newWorkDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newWorkDayActivity();

                }
            });
        }

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
        if (FirebaseUtil.isIsAdmin()) {
            //this feature entry in the next version
           // MenuItem limitationMenu =menu.findItem(R.id.limitationMenu);
           // limitationMenu.setVisible(true);
            MenuItem workDayMenu =menu.findItem(R.id.workDayMenu);
            workDayMenu.setVisible(true);
            MenuItem myAppointments = menu.findItem(R.id.myAppointments);
            myAppointments.setVisible(false);
        }



        return true;
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            //getUserInstance();
            switch (item.getItemId()) {
                //this feature  entry in the next version
                /*
                case romiinger.nailbook.R.id.limitationMenu: {

                    Intent intent = new Intent(CustomCalendarActivity.this, LimitationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }*/
                case romiinger.nailbook.R.id.workDayMenu: {
                    newWorkDayActivity();
                    return true;
                }
                case romiinger.nailbook.R.id.appointmensts_menu: {
                    //Log.d(TAG, "Start profile_activity");
                    Intent intent = new Intent(CustomCalendarActivity.this, NewAppointmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                case romiinger.nailbook.R.id.myAppointments: {
                    //Log.d(TAG, "Start profile_activity");
                    Intent intent = new Intent(CustomCalendarActivity.this, ApointmentListViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
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
