package romiinger.nailbook.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.R;
import romiinger.nailbook.Firebase.UserAdapterFirebase;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout inputPassword,inputCurrentPassword,inputEmail;
    private Button btnReset, btnBack;
   //private FirebaseUser user;
    private ProgressBar progressBar;
    private UserAdapterFirebase user;
    private static final String TAG = "ResetPasswordActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Log.d(TAG,"Create activity reset password");
        inputCurrentPassword = (TextInputLayout) findViewById(R.id.inputCurrentPassword);
        inputPassword = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.profile_activity);
                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button Reset Passwor is clicked");

                String newPassword = inputPassword.getEditText().getText().toString();
                String currentPassword = inputCurrentPassword.getEditText().getText().toString();
                Log.d(TAG,"the password input is: "+ newPassword);
                if (TextUtils.isEmpty(newPassword)|| TextUtils.isEmpty((currentPassword))) {
                    Toast.makeText(getApplication(), "Enter your curr password and new Password ", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUtil firebaseUtil = new FirebaseUtil();
                firebaseUtil.resetPassword(currentPassword,newPassword, new FirebaseUtil.FirebaseListener(){
                    @Override
                    public void onComplete(String message) {
                        Log.d(TAG,"onComplete()");
                        Log.d(TAG,"message= "+ message);
                        if (message.equals("password is update"))
                        {
                            Intent intent = new Intent(ResetPasswordActivity.this, ProfileUserActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }});
        }});
    }
    @Override
    public void onBackPressed()
    {
        // setContentView(R.layout.activity_main);

        Intent intent = new Intent(ResetPasswordActivity.this, ProfileUserActivity.class);
        startActivity(intent);
        finish();
    }

}