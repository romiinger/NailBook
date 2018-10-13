package romiinger.nailbook.activitys.User;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import romiinger.nailbook.Class.Wallet;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.Firebase.WalletAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.Firebase.UserAdapterFirebase;
import romiinger.nailbook.activitys.MainActivity;
import romiinger.nailbook.activitys.Wallet.WalletActivity;

public class ProfileUserActivity extends  AppCompatActivity {

    private MyUser myUser;
    private static final String TAG = "ProfileUserActivity";
    private DrawerLayout mdrawerLayout;
    private Button btResetPassword ,btEditProfile;
    private LinearLayout walletLayout;
    private Bundle bundle;
    private String userIdBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Log.d(TAG, "profile activity start");
        UserAdapterFirebase userAdapterFirebase = new UserAdapterFirebase();
        userIdBundle = null;
        bundle = getIntent().getExtras();
        if(bundle != null) {
            userIdBundle = bundle.getString("userId");
            Log.d(TAG,"after get extra, userUid = " + userIdBundle );
        }
        userAdapterFirebase.getUserById(userIdBundle,new UserAdapterFirebase.GetUserByIdListener() {
            @Override
            public void onComplete(MyUser user) {
                myUser=user;
                Log.d("TAG", "got new student name:" + user.getName());
                Log.d(TAG, "user=" + user);
                Log.d(TAG, "user.getName()=" + user.getName());
                TextView nameLayout = (TextView) findViewById(R.id.fullNameLayout);
                String name = user.getName();
                nameLayout.setText(name);
                TextView emailLayout = (TextView) findViewById(R.id.emailLayout);
                String email = user.getEmail();
                emailLayout.setText(email);
                TextView phoneLayout = (TextView) findViewById(R.id.phoneLayout);
                String phone = user.getPhone();
                phoneLayout.setText(phone);
                final TextView walletLayout = (TextView)findViewById(R.id.walletLayout);
                //String wallet = user.getWallet();
                //walletLayout.setText(wallet);
                WalletAdapterFirebase walletAdapterFirebase = new WalletAdapterFirebase();
                walletAdapterFirebase.getWalletByUserId(user.getStId(), new WalletAdapterFirebase.GetWalletByClientIdListener() {
                    @Override
                    public void onComplete(Wallet wallet) {
                        walletLayout.setText(wallet.getAmmount());
                    }
                });
            }

        });
        walletLayout = (LinearLayout)findViewById(R.id.wallet_Layout);
        walletLayout.setOnClickListener(new OnWalletClick());
        btResetPassword = (Button) findViewById(R.id.btnResetPassword);
        btEditProfile = (Button) findViewById(R.id.update_profil);
        if(FirebaseUtil.isIsAdmin())
        {
            Log.d(TAG,"set buttons to administrator view");

        }
        else{
            Log.d(TAG,"no administrator view");
            btResetPassword.setVisibility(View.VISIBLE);
            btEditProfile.setVisibility(View.VISIBLE);
            btResetPassword.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d(TAG,"setContentView to resetPasswordActivity");
                    //setContentView(R.layout.activity_reset_password);
                    Intent  intent=new Intent(ProfileUserActivity.this,ResetPasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
    @Override
    public void onBackPressed()
    {
       // setContentView(R.layout.activity_main);

        Intent intent = new Intent(ProfileUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onWalletClicked()
    {
        Log.d(TAG,"onWalletClicked");
        Intent  intent=new Intent(ProfileUserActivity.this,WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if(userIdBundle!=null)
        {
            Bundle b = new Bundle();
            b.putString("userId",userIdBundle);
            intent.putExtras(b);
        }
        startActivity(intent);
        finish();
    }

    class OnWalletClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            onWalletClicked();
        }
    }

}

