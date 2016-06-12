package com.healify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.healify.R;
import com.healify.utils.NavigationDrawer;
import com.healify.web.dto.PatientDTO;

public class SinglePatient extends AppCompatActivity {

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View view = findViewById(R.id.single_patient_id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        NavigationDrawer navigationDrawer = new NavigationDrawer(this, toolbar);
        navigationDrawer.setupDrawer();

        Intent intent = getIntent();
        patientDTO = (PatientDTO) intent.getSerializableExtra("patient");
        showPatient(view, patientDTO);
    }

    private void showPatient(View view, PatientDTO patient) {

        TextView name = (TextView) view.findViewById(R.id.singlePatientName);
        TextView surname = (TextView) view.findViewById(R.id.singlePatientSurname);
        TextView birthDate = (TextView) view.findViewById(R.id.singlePatientBirthDate);
        TextView datein = (TextView) view.findViewById(R.id.singlePatientDatein);
        TextView diagnosis = (TextView) view.findViewById(R.id.singlePatientDiagnosis);
        TextView doctor = (TextView) view.findViewById(R.id.singlePatientDoctor);

        name.setText(patient.getFirstName());
        surname.setText(patient.getLastName());
        birthDate.setText(patient.getBirthDate());
        datein.setText(patient.getIn());
        diagnosis.setText(patient.getDisease());
        doctor.setText(patient.getDoctor());

        setTitle(patient.getFirstName() + " " + patient.getLastName());
    }

}
