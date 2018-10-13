package romiinger.nailbook.activitys.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.Class.Wallet;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Firebase.UserAdapterFirebase;
import romiinger.nailbook.Firebase.WalletAdapterFirebase;
import romiinger.nailbook.R;
import romiinger.nailbook.activitys.User.ProfileUserActivity;

public class WalletActivity extends AppCompatActivity {

    private static final String TAG = "WalletActivity";
    private Toolbar toolbar;
    private TextView ammountLayout, nameUserLayout;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private String userIdBundle;
    private LinearLayout credentionalLayout;
    private Button accreditButton;
    private Wallet mWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        userIdBundle = null;
        bundle = getIntent().getExtras();
        if (bundle != null) {
            userIdBundle = bundle.getString("userId");
            createToolBar();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        setViewWalletData();
        //recyclerView = (RecyclerView) findViewById(R.id.rvWalletUpdates);//todo
    }

    private void setViewWalletData()
    {
        WalletAdapterFirebase walletAdapterFirebase = new WalletAdapterFirebase();
        walletAdapterFirebase.getWalletByUserId(userIdBundle, new WalletAdapterFirebase.GetWalletByClientIdListener() {
            @Override
            public void onComplete(Wallet wallet) {
                mWallet = wallet;
                ammountLayout = (TextView) findViewById(R.id.ammountLayout);
                String ammount = mWallet.getAmmount();
                ammountLayout.setText(ammount);
                String usId = mWallet.getUserId();
                UserAdapterFirebase.getUserById(usId, new UserAdapterFirebase.GetUserByIdListener() {
                    @Override
                    public void onComplete(MyUser user) {
                        nameUserLayout = (TextView)findViewById(R.id.fullNameLayout);
                        String name = user.getName();
                        Log.d(TAG,"user name= "+ name );
                        nameUserLayout.setText(name);
                    }
                });
            }
        });
        if(FirebaseUtil.isIsAdmin())
        {
            credentionalLayout = (LinearLayout) findViewById(R.id.credentional_layout);
            credentionalLayout.setVisibility(View.VISIBLE);
            accreditButton = (Button) findViewById(R.id.saveCredit);
            accreditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextInputLayout newCreditLayout = (TextInputLayout)findViewById(R.id.inputaddNewCreditLayout);
                    String newCredit = newCreditLayout.getEditText().getText().toString();
                    mWallet.setAmmount(newCredit);
                }
            });
        }
        else{
            Log.d(TAG,"set buttons to administrator view");

        }
    }

    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Wallet: ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    @Override
    public void onBackPressed()
    {

        // setContentView(R.layout.activity_main);
        Intent intent = new Intent(WalletActivity.this, ProfileUserActivity.class);
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

}
