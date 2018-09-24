package romiinger.nailbook;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class ProfileUserActivity extends  AppCompatActivity {

    private static MyUser user;
    private static final String TAG = "ProfileUserActivity";
    private DrawerLayout mdrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Log.d(TAG,"profile activity start");
        UserAdapterFirebase userAdapterFirebase= new UserAdapterFirebase();
        userAdapterFirebase.getUserById(new UserAdapterFirebase.GetUserByIdListener() {
            @Override
            public void onComplete(MyUser user) {
                Log.d("TAG", "got new student name:" + user.getName());
                Log.d(TAG,"user=" + user);
                Log.d(TAG,"user.getName()=" + user.getName() );
                TextView nameLayout = (TextView)findViewById(R.id.fullNameLayout);
                String name = user.getName();
                nameLayout.setText(name);
                TextView emailLayout = (TextView)findViewById(R.id.emailLayout);
                String email = user.getEmail();
                emailLayout.setText(email);
                TextView phoneLayout = (TextView)findViewById(R.id.phoneLayout);
                String phone = user.getPhone();
                phoneLayout.setText(phone);
            }
        });






    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ProfileUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    }

