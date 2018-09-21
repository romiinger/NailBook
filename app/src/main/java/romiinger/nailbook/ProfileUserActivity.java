package romiinger.nailbook;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
public class ProfileUserActivity extends  AppCompatActivity {

    //private static MyUser user;
    private static final String TAG = "ProfileUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Log.d(TAG,"profile activity start");
        UserAdapter userAdapter = new UserAdapter();

        MyUser user = userAdapter.getmUser();
        Log.d(TAG,"user=" + user);
        Log.d(TAG,"user.getName()=" + user.getName() );



        TextView nameLayout = (TextView)findViewById(R.id.fullNameLayout);
        String name = user.getName();
        nameLayout.setText(name);
    }
}
