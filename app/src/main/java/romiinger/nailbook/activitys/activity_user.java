package romiinger.nailbook.activitys;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.R;
import romiinger.nailbook.Firebase.UserAdapterFirebase;

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
                    UserAdapterFirebase userAdapterFirebase = new UserAdapterFirebase();
                    userAdapterFirebase.addUser(user, new UserAdapterFirebase.AddUserListener() {
                        @Override
                        public void onComplete() {
                            Intent intent = new Intent(activity_user.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

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
