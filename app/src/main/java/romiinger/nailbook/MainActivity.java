package romiinger.nailbook;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.*;
import android.support.annotation.NonNull;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.firebase.ui.auth.*;

import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.io.Console;

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
        setContentView(R.layout.activity_main);
        myFirebase = new FirebaseUtil();

        createToolBar();
        //createNavigationView();

        //next Activity
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_login);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        menu.add("").setIcon(R.drawable.ic_launcher_background);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getUserInstance();
        switch (item.getItemId()) {
            case R.id.logout_menu: {
                FirebaseUtil.logOut();
                return true;
            }
            case R.id.user_profile_menu: {
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
        getUserInstance();
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.workDiary: {
                //toDo new activity
                Toast.makeText(this, "coming soon ", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.treatments: {
                Toast.makeText(this, "coming soon ", Toast.LENGTH_LONG).show();
                //toDo new activity
                break;
            }
            case R.id.Client: {
                Log.d(TAG, "Start ClientsActivity");
                Intent intent = new Intent(MainActivity.this, ClientsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
        FirebaseUtil.openFbReference("users", this);
        FirebaseUtil.attachListener();
        Log.d(TAG, "Before get User Instance");
        createNavigationView();


    }

    private void createNavigationView() {
        Log.d(TAG, " In Create NavigationView");
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (FirebaseUtil.isIsAdmin() == true) {
            Log.d(TAG, "is administrator' show menu");
            navigationView.getMenu().setGroupVisible(R.id.administrator_menu, true);
        } else {
            Log.d(TAG, "the user is not administrator");
            navigationView.getMenu().setGroupVisible(R.id.administrator_menu, false);

        }

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mdrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void createToolBar() {
        Log.d(TAG, " In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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