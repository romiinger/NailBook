package romiinger.nailbook;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_user extends AppCompatActivity {

    private TextInputLayout inputEmail, inputName,inputPhone;
    private Button btnRegister;
    private static final String TAG = "activityuser";
    private static MyUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"In activity_user");
        setContentView(R.layout.user_activity);

        inputName =  (TextInputLayout)findViewById(R.id.text_input_layout_fullName);

        inputEmail =  (TextInputLayout)findViewById(R.id.text_input_layout_email);
        inputPhone = (TextInputLayout)findViewById(R.id.text_input_layout_phone);
        btnRegister = (Button) findViewById(R.id.register);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = inputName.getEditText().getText().toString();
                String phone = inputPhone.getEditText().getText().toString();
                String email = inputEmail.getEditText().getText().toString();
                Log.d(TAG,"inputName = "+ name );
                if(!name.isEmpty() && !phone.isEmpty()&& !email.isEmpty())
                {
                    Log.d(TAG,"Input is correct create new User ");
                    user = new MyUser(name,phone,email);
                    Log.d(TAG,"mUser.getStId() " +user.getStId());
                    Toast.makeText(getApplicationContext(), "Register Sucess", Toast.LENGTH_SHORT).show();
                    UserAdapter.setUserProfile(user);
                    Intent intent = new Intent(activity_user.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Context context = getApplicationContext();

                    Toast.makeText(context, "Registration failed.Please check input data", Toast.LENGTH_LONG).show();

                }
               // setContentView(R.layout.activity_main);
                //finish();
            }
        });
    }
}
