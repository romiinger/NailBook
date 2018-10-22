package romiinger.nailbook.activitys.Treatments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import romiinger.nailbook.Class.Treatments;
import romiinger.nailbook.Firebase.FirebaseUtil;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.R;

public class TreatmentViewActivity extends AppCompatActivity {

    private static final String TAG = "TreatmentViewActivity";
    private Toolbar toolbar;
    private Bundle bundle;
    private String treatmentIdBundle;
    private Treatments mTreatemt;
    private FloatingActionButton btEdit, btSave;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_treatment);
        createToolBar();
        bundle = getIntent().getExtras();
        treatmentIdBundle = bundle.getString("id");

        TreatmentsAdapterFirebase adapterFirebase = new TreatmentsAdapterFirebase();
        adapterFirebase.getTreatmentById(treatmentIdBundle, new TreatmentsAdapterFirebase.GetTreatmentByIdListener() {
            @Override
            public void onComplete(Treatments treatment) {
                mTreatemt=treatment;
                TextView nameLT = (TextView) findViewById(R.id.nameTreatment);
                TextView descriptionLT = (TextView) findViewById(R.id.descriptionTreatment);
                TextView durationLT = (TextView) findViewById(R.id.duratioTreatment);
                TextView priceLT = (TextView) findViewById(R.id.priceTreatment);
                nameLT.setText(treatment.getName());
                descriptionLT.setText(treatment.getDescription());
                Log.d(TAG, "description: " + descriptionLT);
                durationLT.setText(treatment.getDuration() + " minutes");
                priceLT.setText(treatment.getPrice() + " ₪");
            }
        });
        btEdit = (FloatingActionButton) findViewById(R.id.editTreatment);
        if (FirebaseUtil.isIsAdmin()) {
            btEdit.setVisibility(View.VISIBLE);
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTreatment();
                }
            });
        }
        else
        {
            btEdit.setVisibility(View.INVISIBLE);
        }
    }

    private void createToolBar() {
        //Log.d(TAG," In Create toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Treatment ");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);

    }

    private void editTreatment() {
        btEdit.setVisibility(View.INVISIBLE);
        final LinearLayout editL = (LinearLayout) findViewById(R.id.editLinear);
        editL.setVisibility(View.VISIBLE);
        btSave = (FloatingActionButton) findViewById(R.id.saveTreatments);
        btSave.setVisibility(View.VISIBLE);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TreatmentsAdapterFirebase adapterFirebase = new TreatmentsAdapterFirebase();
                TextInputLayout nameTL = (TextInputLayout) findViewById(R.id.inputNameTreatment);
                TextInputLayout descriptionTL = (TextInputLayout) findViewById(R.id.inputDescription);
                TextInputLayout durationTL = (TextInputLayout) findViewById(R.id.inputDuration);
                TextInputLayout priceTL = (TextInputLayout) findViewById(R.id.inputPrice);
                String name = nameTL.getEditText().getText().toString();
                String description = descriptionTL.getEditText().getText().toString();
                String duration = durationTL.getEditText().getText().toString();
                String price = priceTL.getEditText().getText().toString();
                Log.d(TAG, "new Treatment id = " + name + " description= " + description + "duration= " + duration + " price=" + price);
                Treatments treatment = mTreatemt;
                if(name != null)
                    treatment.setName(name);
                if(description != null)
                    treatment.setDescription(description);
                if(duration != null)
                    treatment.setDuration(duration);
                if(price!= null)
                    treatment.setPrice(price);
                adapterFirebase.addTreatment(treatment, new TreatmentsAdapterFirebase.GetAddTreatmentListener() {
                    @Override
                    public void onComplete(boolean onSucess) {
                        if (!onSucess) {
                            Toast.makeText(getApplicationContext(), "failed to save new Treatment", Toast.LENGTH_LONG).show();
                        }
                        btSave.setVisibility(View.INVISIBLE);
                        btEdit.setVisibility(View.VISIBLE);
                        editL.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TreatmentViewActivity.this, TreatmentsActivity.class);
        startActivity(intent);
        finish();
    }
}
