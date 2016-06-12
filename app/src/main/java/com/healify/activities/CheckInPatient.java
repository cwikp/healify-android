package com.healify.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.web.api.PatientAPI;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInPatient extends AppCompatActivity {

    private ProximityManagerContract proximityManager;
    private Set<PatientsList.Beacon> beacons = Collections.synchronizedSet(new HashSet<PatientsList.Beacon>());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientDTO patient = getPatientData(getWindow().getDecorView().getRootView());
                saveData(patient);
            }
        });

        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);

        KontaktSDK.initialize("ICoaWqUPuyRrbSeeTCOZHfbtjtwrzFqn");

        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.configuration()
                .scanPeriod(ScanPeriod.RANGING);

    }

    private void saveData(PatientDTO patientDTO){

        PatientAPI service = ServiceGenerator.createService(PatientAPI.class, null);
        Call<PatientDTO> call = service.checkInPatient(patientDTO);
        call.enqueue(new Callback<PatientDTO>() {

            @Override
            public void onResponse(Call<PatientDTO> call, Response<PatientDTO> response) {
                response.code();

                if(response.isSuccessful()){
                    Toast.makeText(CheckInPatient.this, "Patient saved successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(CheckInPatient.this, "Patient not saved", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PatientDTO> call, Throwable t) {
                Log.e("MainActivity", t.getMessage(), t);
            }

        });

    }

    private PatientDTO getPatientData(View view) {

        TextView name = (TextView) view.findViewById(R.id.singlePatientName);
        TextView surname = (TextView) view.findViewById(R.id.singlePatientSurname);
        TextView birthDate = (TextView) view.findViewById(R.id.singlePatientBirthDate);
        TextView datein = (TextView) view.findViewById(R.id.singlePatientDatein);
        TextView diagnosis = (TextView) view.findViewById(R.id.singlePatientDiagnosis);
        TextView doctor = (TextView) view.findViewById(R.id.singlePatientDoctor);

        PatientDTO patientDTO = PatientDTO.builder()
                .firstName(name.getText().toString())
                .lastName(surname.getText().toString())
                .birthDate(birthDate.getText().toString())
                .in(datein.getText().toString())
                .disease(diagnosis.getText().toString())
                .doctor(doctor.getText().toString())
                .build();

        return patientDTO;
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                Log.i("Sample", "updated: " + iBeacons.size());

                for(IBeaconDevice ibeacon : iBeacons) {
                    PatientsList.Beacon beacon = new PatientsList.Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());
//                Log.i("Sample", getBeacons().toString());

                    if(beacons.contains(beacon)) {
                        beacons.remove(beacon);
                    }

                    beacons.add(beacon);
                }

//                Log.i("Sample", beacons.toString());

            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                PatientsList.Beacon beacon = new PatientsList.Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());

                if(beacons.contains(beacon)) {
                    beacons.remove(beacon);
                    Log.i("Sample", "beacon removed: " + beacon.getId());
                }

            }

        };
    }


    public List<PatientsList.Beacon> getSortedBeacons() {
        List<PatientsList.Beacon> sortedBeacons = new ArrayList<>(beacons);
        Collections.sort(sortedBeacons);
        return sortedBeacons;
    }

}
