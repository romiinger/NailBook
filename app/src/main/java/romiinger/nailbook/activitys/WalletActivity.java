package romiinger.nailbook.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.Class.Wallet;
import romiinger.nailbook.Firebase.UserAdapterFirebase;
import romiinger.nailbook.Firebase.WalletAdapterFirebase;
import romiinger.nailbook.R;

public class WalletActivity extends AppCompatActivity {

    private static final String TAG = "ClientsActivity";
    private Toolbar toolbar;
    private TextView ammountLayout, nameUserLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        createToolBar();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        WalletAdapterFirebase walletAdapterFirebase = new WalletAdapterFirebase();
        walletAdapterFirebase.getWalletByUserId(null, new WalletAdapterFirebase.GetWalletByClientIdListener() {
            @Override
            public void onComplete(Wallet wallet) {
                ammountLayout = (TextView) findViewById(R.id.ammountLayout);
                String ammount = wallet.getAmmount();
                ammountLayout.setText(ammount);
                String usId = wallet.getUserId();
                UserAdapterFirebase.getUserById(usId, new UserAdapterFirebase.GetUserByIdListener() {
                    @Override
                    public void onComplete(MyUser user) {
                        nameUserLayout = (TextView)findViewById(R.id.fullNameLayout);
                        String name = user.getName();
                        nameUserLayout.setText(name);
                    }
                });
            }
        });
    }
    private void setViewWalletData()
    {
        WalletAdapterFirebase walletAdapterFirebase = new WalletAdapterFirebase();
        walletAdapterFirebase.getWalletByUserId(null, new WalletAdapterFirebase.GetWalletByClientIdListener() {
            @Override
            public void onComplete(Wallet wallet) {
                ammountLayout = (TextView) findViewById(R.id.ammountLayout);
                String ammount = wallet.getAmmount();
                ammountLayout.setText(ammount);
                String usId = wallet.getUserId();
                UserAdapterFirebase.getUserById(usId, new UserAdapterFirebase.GetUserByIdListener() {
                    @Override
                    public void onComplete(MyUser user) {
                        nameUserLayout = (TextView)findViewById(R.id.fullNameLayout);
                        String name = user.getName();
                        nameUserLayout.setText(name);
                    }
                });
            }
        });
    }
    private void createToolBar()
    {
        Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Wallet ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

}
