package com.healify.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.healify.R;

public class PatientEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_browse);

        // popup temperatury
        Button tempButton = (Button) findViewById(R.id.temp_button);

        assert tempButton != null;
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientEntry.this, AddTemperaturePopup.class));

            }
        });
        // popup leków
        Button drugButton = (Button) findViewById(R.id.drug_button);

        assert drugButton != null;
        drugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientEntry.this, AddDrugPopup.class));

            }
        });

        // popup badania

        Button checkButton = (Button) findViewById(R.id.check_button);

        assert checkButton != null;
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientEntry.this, AddCheckPopup.class));

            }
        });


    }
}
