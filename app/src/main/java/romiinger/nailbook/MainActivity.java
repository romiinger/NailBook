package romiinger.nailbook;

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
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myFirebase = new FirebaseUtil();

        createToolBar();
        createNavigationView();

        //myFirebase
        //myFirebase.openFbReference("user",this);
/*
        //get firebase auth instance
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG,"User is null?" + (user==null));
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "in onAuthStateChanged()");
                if (user == null) {
                    Log.d(TAG,"User no logIn, start new Intent: activity_login");
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(MainActivity.this, activity_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        };*/

        //next Activity
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_signup);
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
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.logout_menu: {
                FirebaseUtil.logOut();
                return true;
            }
            case R.id.user_profile_menu:
            {
             User thisUser =FirebaseUtil.getUserProfile();
             //Todo showProfile();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public  boolean onNavigationItemSelected(MenuItem menuItem)
    {
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        switch (menuItem.getItemId())
        {
            case R.id.workDiary: {
                     //toDo new activity
                       Toast.makeText(this,"coming soon ",Toast.LENGTH_LONG).show();
                       break;
                  }
            case R.id.treatments: {
                      Toast.makeText(this, "coming soon ", Toast.LENGTH_LONG).show();
                      //toDo new activity
                     break;
            }

        }
        return true;
    }
    private void showDrawer()
    {
        mdrawerLayout.openDrawer(GravityCompat.START);
    }
    private void hideDrawer()
    {
        mdrawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public void onBackPressed()
    {
        if(mdrawerLayout.isDrawerOpen(GravityCompat.START))
           hideDrawer();
        else
            super.onBackPressed();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        FirebaseUtil.detachListener();
    }
    protected void onResume()
    {
        super.onResume();
        FirebaseUtil.openFbReference("user",this);
        FirebaseUtil.attachListener();
        getUserInstance();
    }

    private void createNavigationView()
    {
        Log.d(TAG," In Create NavigationView");
        mdrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        if(FirebaseUtil.isIsAdmin() ==true)
        {
            navigationView.getMenu().setGroupVisible(R.id.administrator_menu,true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle =  new ActionBarDrawerToggle(this,mdrawerLayout,toolbar,
                R.string.drawer_open,R.string.drawer_close);
        mdrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }
    private void getUserInstance()
    {
        DatabaseReference ref=FirebaseUtil.getmDatabaseReference();
        if(ref.child("name") == null)
        {
            String userId = ref.push().getKey();
            User user = new User(userId);
            setContentView(R.layout.user_activity);
            ref.child(userId).setValue(user);        }
    }
}