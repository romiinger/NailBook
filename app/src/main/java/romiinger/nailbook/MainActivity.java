package romiinger.nailbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.*;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {

    Button next;
    //private FirebaseDatabase mFirebaseDatabase;
    // private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //next Activity
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_login);

            }
        });

        /*mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users");*/


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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nb,menu);
        return true;
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