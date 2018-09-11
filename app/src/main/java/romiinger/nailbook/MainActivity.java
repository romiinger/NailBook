package romiinger.nailbook;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.*;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button next;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private DrawerLayout mdrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        //toolbar.setLogo(R.drawable.wallpaper);
        //toolbar.setSubtitle("Welcome");
        setSupportActionBar(toolbar);

        mdrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle =  new ActionBarDrawerToggle(this,mdrawerLayout,toolbar,
                R.string.drawer_open,R.string.drawer_close);
        mdrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //get firebase auth instance
        /*auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, activity_login.class));
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

        /*mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users");*/


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        //menu.add("").setIcon(R.drawable.baseline_list_black_18dp);
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_nb, menu);
        return true;
    }

    @Override
    public  boolean onNavigationItemSelected(MenuItem menuItem)
    {
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        switch (menuItem.getItemId())
        {
            case R.id.workDiary:
                    //toDo new activity
                     break;
            case R.id.treatments:
                //toDo new activity
                     break;
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
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
          /*  case R.id.newClient: new User();*/
        /*}
        return super.onOptionsItemSelected(item);
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
        FirebaseUtil.openFbReference("newClient",this);
        FirebaseUtil.attachListener();

    }*/
}