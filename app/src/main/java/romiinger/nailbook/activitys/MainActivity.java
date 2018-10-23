package romiinger.nailbook.activitys;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.*;
import android.widget.Button;

import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import romiinger.nailbook.Class.Appointment;
import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.activitys.Calendar.CustomCalendarActivity;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.R;
import romiinger.nailbook.Firebase.UserAdapterFirebase;
import romiinger.nailbook.activitys.Treatments.TreatmentsActivity;
import romiinger.nailbook.activitys.User.ClientsActivity;
import romiinger.nailbook.activitys.User.ProfileUserActivity;
import romiinger.nailbook.activitys.User.activity_user;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button next;
    private FirebaseUtil myFirebase;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private DrawerLayout mdrawerLayout;
    private MyUser myUser;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(romiinger.nailbook.R.layout.activity_main);

        myFirebase = new FirebaseUtil();


        createToolBar();
        //createNavigationView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        menu.add("").setIcon(romiinger.nailbook.R.drawable.ic_launcher_background);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(romiinger.nailbook.R.menu.menu_settings, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //getUserInstance();
        switch (item.getItemId()) {
            case romiinger.nailbook.R.id.logout_menu: {
                FirebaseUtil.logOut();
                return true;
            }
            case romiinger.nailbook.R.id.user_profile_menu: {
                Log.d(TAG, "Start profile_activity");
                Intent intent = new Intent(MainActivity.this, ProfileUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //getUserInstance();
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case romiinger.nailbook.R.id.workDiary: {
                //toDo new activity
                Intent intent = new Intent(MainActivity.this, CustomCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //Toast.makeText(this, "coming soon ", Toast.LENGTH_LONG).show();
                break;
            }
            case romiinger.nailbook.R.id.treatments: {
                Log.d(TAG, "Start TreatmentsActivity");
                Intent intent = new Intent(MainActivity.this, TreatmentsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            }
            case romiinger.nailbook.R.id.Client: {
                Log.d(TAG, "Start ClientsActivity");
                Intent intent = new Intent(MainActivity.this, ClientsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            }

        }
        return true;
    }

    private void showDrawer() {
        mdrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mdrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mdrawerLayout.isDrawerOpen(GravityCompat.START))
            hideDrawer();
        else
            super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("users", this,new FirebaseUtil.FirebaseListener() {

                    @Override
                    public void onComplete(String message) {
                       // Toast.makeText(getBaseContext(), "Welcome back!", Toast.LENGTH_LONG).show();
                        FirebaseUtil.attachListener();
                        Log.d(TAG, "Before get User Instance");
                        createNavigationView();
                    }
                }
        );



    }

    private void createNavigationView() {
        Log.d(TAG, " In Create NavigationView");
        mdrawerLayout = (DrawerLayout) findViewById(romiinger.nailbook.R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(romiinger.nailbook.R.id.navigation_view);

        if (FirebaseUtil.isIsAdmin() == true) {
            Log.d(TAG, "is administrator' show menu");
            navigationView.getMenu().setGroupVisible(romiinger.nailbook.R.id.administrator_menu, true);
        } else {
            Log.d(TAG, "the user is not administrator");
            navigationView.getMenu().setGroupVisible(romiinger.nailbook.R.id.administrator_menu, false);

        }

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar,
                romiinger.nailbook.R.string.drawer_open, romiinger.nailbook.R.string.drawer_close);
        mdrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void createToolBar() {
        Log.d(TAG, " In Create toolbar");
        toolbar = (Toolbar) findViewById(romiinger.nailbook.R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    private void getUserInstance() {
        UserAdapterFirebase userAdapterFirebase = new UserAdapterFirebase();
        userAdapterFirebase.getUserById(null,new UserAdapterFirebase.GetUserByIdListener() {
            @Override
            public void onComplete(MyUser user) {
                if (user.getName() != null) {
                    Log.d(TAG, "User is Register");
                } else {
                    Log.d(TAG, "User not Register, over tu user_activity");
                    Intent intent = new Intent(MainActivity.this, activity_user.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    public void showMenu() {
        invalidateOptionsMenu();
        createNavigationView();
    }
}
