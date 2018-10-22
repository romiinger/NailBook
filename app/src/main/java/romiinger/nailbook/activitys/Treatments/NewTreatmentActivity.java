package romiinger.nailbook.activitys.Treatments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.R;

public class NewTreatmentActivity extends AppCompatActivity {

    private static final String TAG = "NewTreatmentActivity";
    private TextInputLayout nameTL , descriptionTL , durationTL , priceTL;
    private FloatingActionButton btSave;
    private TreatmentsAdapterFirebase adapterFirebase;
    private Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teatment);
        createToolBar();
        Log.d(TAG, "Create activity_new_teatment)");
         nameTL = (TextInputLayout) findViewById(R.id.inputNameTreatment);
         descriptionTL =(TextInputLayout) findViewById(R.id.inputDescription);
         durationTL =(TextInputLayout) findViewById(R.id.inputDuration);
         priceTL = (TextInputLayout) findViewById(R.id.inputPrice);

         btSave = (FloatingActionButton) findViewById(R.id.saveTreatments);
         btSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 adapterFirebase = new TreatmentsAdapterFirebase();
                 String id =adapterFirebase.getNewTreatmentId();
                 String name = nameTL.getEditText().getText().toString();
                 String description = descriptionTL.getEditText().getText().toString();
                 String duration = durationTL.getEditText().getText().toString();
                 String price = priceTL.getEditText().getText().toString();
                 Log.d(TAG,"new Treatment id = "+ name+ " description= " +description+ "duration= " +duration+ " price="+price);

                 Treatments treatment = new Treatments(id,name,description,price,duration);
                 adapterFirebase.addTreatment(treatment, new TreatmentsAdapterFirebase.GetAddTreatmentListener() {
                     @Override
                     public void onComplete(boolean onSucess) {
                         if(!onSucess){
                             Toast.makeText(getApplicationContext() ,"failed to save new Treatment", Toast.LENGTH_LONG).show();
                         }
                         Intent intent = new Intent(NewTreatmentActivity.this, TreatmentsActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         finish();
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
        toolbar.setSubtitle("New Treatment ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(NewTreatmentActivity.this, TreatmentsActivity.class);
        startActivity(intent);
        finish();
    }
}
