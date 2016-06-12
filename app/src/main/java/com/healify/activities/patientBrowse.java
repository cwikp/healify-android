package com.healify.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.healify.R;

public class PatientBrowse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_browse);

        // temperature popup
        Button temperatureButton = (Button) findViewById(R.id.temp_button);
        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientBrowse.this, BrowseTemperaturePopup.class));
            }
        });

        // drugs popup
        Button drugsButton = (Button) findViewById(R.id.drug_button);
        drugsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientBrowse.this, DrugsListingPopup.class));
            }
        });

        // checkups popup
        Button checkupsButton = (Button) findViewById(R.id.check_button);
        checkupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientBrowse.this, CheckupListingPopup.class));
            }
        });
    }
}
