package romiinger.nailbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout inputPassword;
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

                String password = inputPassword.getEditText().getText().toString();
                Log.d(TAG,"the password input is: "+ password);
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUtil firebaseUtil = new FirebaseUtil();
                firebaseUtil.resetPassword(password);
            }
        });
    }

}