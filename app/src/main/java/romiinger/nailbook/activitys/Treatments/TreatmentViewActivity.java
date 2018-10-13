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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        menu.add("").setIcon(romiinger.nailbook.R.drawable.ic_launcher_background);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_treatment_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //getUserInstance();
        switch (item.getItemId()) {
            case romiinger.nailbook.R.id.editTreatment: {
                editTreatment();
                break;
            }
            case romiinger.nailbook.R.id.user_profile_menu: {
                //Log.d(TAG, "Start profile_activity");
                // Intent intent = new Intent(TreatmentViewActivity.this, ProfileUserActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(intent);
                //  finish();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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

                Treatments treatment = new Treatments(treatmentIdBundle, name, description, price, duration);
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
