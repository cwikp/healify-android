package com.healify.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInPatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ProximityManagerContract proximityManager;
    private Set<PatientsList.Beacon> beacons = Collections.synchronizedSet(new HashSet<PatientsList.Beacon>());
    private TextView birthDate;
    private TextView datein;
    private Spinner beaconSpinner;
    private ArrayAdapter<String> dataAdapter;
    private String beaconId;


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

        birthDate = (TextView) findViewById(R.id.singlePatientBirthDate);
        datein = (TextView) findViewById(R.id.singlePatientDatein);

        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);

        KontaktSDK.initialize("ICoaWqUPuyRrbSeeTCOZHfbtjtwrzFqn");

        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.configuration()
                .scanPeriod(ScanPeriod.RANGING);

        beaconSpinner = (Spinner) findViewById(R.id.spinnerBeacons);
//        adapter = ArrayAdapter.createFromResource(this, R.array.beacons, R.layout.spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



    }

    private void saveData(PatientDTO patientDTO){

        PatientAPI service = ServiceGenerator.createService(PatientAPI.class, null);
        Call<PatientDTO> call = service.checkInPatient(patientDTO);
        call.enqueue(new Callback<PatientDTO>() {

            @Override
            public void onResponse(Call<PatientDTO> call, Response<PatientDTO> response) {
                response.code();

                if(response.isSuccessful()){
//                    Toast.makeText(CheckInPatient.this, "Patient saved successfully " + response.code(), Toast.LENGTH_LONG).show();
                }
                else {
//                    Toast.makeText(CheckInPatient.this, "Patient not saved", Toast.LENGTH_LONG).show();
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
        TextView diagnosis = (TextView) view.findViewById(R.id.singlePatientDiagnosis);
        TextView doctor = (TextView) view.findViewById(R.id.singlePatientDoctor);

        PatientDTO patientDTO = PatientDTO.builder()
                .firstName(name.getText().toString())
                .lastName(surname.getText().toString())
                .birthDate(birthDate.getText().toString())
                .in(datein.getText().toString())
                .disease(diagnosis.getText().toString())
                .doctor(doctor.getText().toString())
                .beaconId(beaconId)
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

                List<PatientsList.Beacon> beaconsList = getSortedBeacons();
                List<String> list = new ArrayList<>();
                for (PatientsList.Beacon beacon : beaconsList) {
                    list.add(beacon.getId().toString());
                }
                dataAdapter = new ArrayAdapter<>(CheckInPatient.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                beaconSpinner.setAdapter(dataAdapter);
                beaconSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

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

    public void onBirthDateButtonClicked(View view) {
      datePicker(1);
    }

    public void onDateInButtonClicked(View view) {
        datePicker(2);
    }

    private void datePicker(final int field){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String y = Integer.toString(year);
                        String m = Integer.toString(monthOfYear + 1);
                        String d = Integer.toString(dayOfMonth);
                        if (monthOfYear < 10) {
                            m = "0" + m;
                        }
                        if (dayOfMonth < 10) {
                            d = "0" + d;
                        }
                        if (field == 1)
                            birthDate.setText(y + "/" + m + "/" + d);
                        else datein.setText(y + "/" + m + "/" + d);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            beaconId = parent.getItemAtPosition(pos).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        beaconId = this.dataAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //dadad
    }
}
