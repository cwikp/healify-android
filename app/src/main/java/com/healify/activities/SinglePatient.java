package com.healify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healify.R;
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

        Button updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SinglePatient.this, PatientEntry.class));
            }
        });

        Button healthstateButton = (Button) findViewById(R.id.buttonHealthState);
        healthstateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SinglePatient.this, PatientBrowse.class));
            }
        });

//        Button checkoutButton = (Button) findViewById(R.id.buttonCheckOut);
//        checkoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SinglePatient.this, CheckOutPopup.class));
//            }
//        });



        Intent intent = getIntent();
        patientDTO = (PatientDTO) intent.getSerializableExtra("patient");
        showPatient(view, patientDTO);
    }

    private void showPatient(View view, PatientDTO event) {


//        TextView eventId = (TextView) view.findViewById(R.id.id_value);
//        TextView description = (TextView) view.findViewById(R.id.description);
//        TextView name = (TextView) view.findViewById(R.id.name);
//        TextView ownerName = (TextView) view.findViewById(R.id.ownerName);
//        TextView place = (TextView) view.findViewById(R.id.place);
//        TextView date = (TextView) view.findViewById(R.id.event_date_value);
//        TextView isPublic = (TextView) view.findViewById(R.id.event_ispublic_value);
//        TextView participants = (TextView) view.findViewById(R.id.event_particpants_value);
//
//        eventId.setText(event.getId());
//        description.setText(event.getDescription());
//        name.setText(event.getName());
//        ownerName.setText(event.getOwnerName());
//        place.setText(event.getPlace());
//        date.setText(dateFormatter.format(new Date(event.getDate() * 1000)));
//        isPublic.setText(event.isPublicEvent() + "");
//        participants.setText((event.getParticipants()).toString());
//
//        setTitle(event.getName());
    }

}
