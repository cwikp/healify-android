package com.healify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.healify.R;
import com.healify.web.dto.PatientDTO;

public class PatientBrowse extends AppCompatActivity {

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_browse);

        patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        // temperature popup
        Button temperatureButton = (Button) findViewById(R.id.temp_button);
        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientBrowse.this, BrowseTemperaturePopup.class);
                intent.putExtra("patientDTO", patientDTO);
                PatientBrowse.this.startActivity(intent);
            }
        });

        // drugs popup
        Button drugsButton = (Button) findViewById(R.id.drug_button);
        drugsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientBrowse.this, DrugsListingPopup.class);
                intent.putExtra("patientDTO", patientDTO);
                PatientBrowse.this.startActivity(intent);
            }
        });

        // checkups popup
        Button checkupsButton = (Button) findViewById(R.id.check_button);
        checkupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientBrowse.this, CheckupListingPopup.class);
                intent.putExtra("patientDTO", patientDTO);
                PatientBrowse.this.startActivity(intent);
            }
        });
    }
}
