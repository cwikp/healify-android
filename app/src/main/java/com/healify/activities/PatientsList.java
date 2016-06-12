package com.healify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.utils.NavigationDrawer;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsList extends AppCompatActivity {

    private ProximityManagerContract proximityManager;
    private Set<Beacon> beacons = Collections.synchronizedSet(new HashSet<Beacon>());
    private List<PatientDTO> patientsData = new ArrayList<>();
    private PatientDTO PatientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Patients nearby");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientsList.this, CheckInPatient.class);
                PatientsList.this.startActivityForResult(intent, 1);
            }

        });

        getPatientsFromServer();

//        Toast.makeText(PatientsList.this, "Started", Toast.LENGTH_LONG).show();
        onPatientsListItemClick();

        NavigationDrawer navigationDrawer = new NavigationDrawer(this, toolbar);
        navigationDrawer.setupDrawer();

        KontaktSDK.initialize("ICoaWqUPuyRrbSeeTCOZHfbtjtwrzFqn");

        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.configuration()
                .scanPeriod(ScanPeriod.RANGING);

    }

    private void getPatientsFromServer() {
        List<String> beaconsIds = new ArrayList<>();

        for (Beacon beacon : beacons) {
            beaconsIds.add(beacon.getId());
        }

        PatientAPI service = ServiceGenerator.createService(PatientAPI.class, null);
        Call<List<PatientDTO>> call = service.getPatientsWithIds(beaconsIds);
        call.enqueue(new Callback<List<PatientDTO>>() {

            @Override
            public void onResponse(Call<List<PatientDTO>> call, Response<List<PatientDTO>> response) {
                response.code();

                if(response.isSuccessful()){
                    List<PatientDTO> body = response.body();

                    List<PatientDTO> matchedPatients = sortPatients(body);

                    Log.i("Sample", matchedPatients.toString());

                    setPatientsData(matchedPatients);
                    fillpatientsListView();
//                    Toast.makeText(PatientsList.this, "Patients downloaded successfully", Toast.LENGTH_LONG).show();
                }
                else {
//                    Toast.makeText(PatientsList.this, "Patients not downloaded", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PatientDTO>> call, Throwable t) {
                Log.e("MainActivity", t.getMessage(), t);
            }

        });
    }

    public void setPatientsData(List<PatientDTO> patientsData) {
        this.patientsData = patientsData;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
//                Toast.makeText(this, data.getStringExtra("createPatientResult"), Toast.LENGTH_LONG).show();
                getPatientsFromServer();
            }
            else{
//                Toast.makeText(this, "Patient might not have been created", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void fillpatientsListView() {
        ArrayAdapter<PatientDTO> adapter = new PatientsListAdapter();
        ListView list = (ListView) findViewById(R.id.patientsListView);
        list.setAdapter(adapter);
    }

    public void onPatientsListItemClick() {
        ListView list = (ListView) findViewById(R.id.patientsListView);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientDTO clickedPatient = patientsData.get(position);
//                Toast.makeText(PatientsList.this, clickedPatient.getFirstName(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PatientsList.this, SinglePatient.class);
                intent.putExtra("patient", clickedPatient);
                PatientsList.this.startActivity(intent);
            }
        });
    }

    public List<Beacon> getSortedBeacons() {
        List<Beacon> sortedBeacons = new ArrayList<>(beacons);
        Collections.sort(sortedBeacons);
        return sortedBeacons;
    }

    private class PatientsListAdapter extends ArrayAdapter<PatientDTO>{

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE d MMM ''yy");

        public PatientsListAdapter() {
            super(PatientsList.this, R.layout.item_patients_list, patientsData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_patients_list, parent, false);
            }

            PatientDTO currentPatient =  patientsData.get(position);

            TextView patientName = (TextView) itemView.findViewById(R.id.text_patientName);


            if(currentPatient != null) {
                patientName.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());
            }
            else {
                patientName.setText("Patient Name");
            }

            return itemView;
        }
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
                    Beacon beacon = new Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());
//                Log.i("Sample", getBeacons().toString());

                    if(beacons.contains(beacon)) {
                        beacons.remove(beacon);
                    }

                    beacons.add(beacon);
                }

//                Log.i("Sample", beacons.toString());
                getPatientsFromServer();

            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                Beacon beacon = new Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());

                if(beacons.contains(beacon)) {
                    beacons.remove(beacon);
                    Log.i("Sample", "beacon removed: " + beacon.getId());
                }

            }

        };
    }

    private List<PatientDTO> sortPatients(List<PatientDTO> patients) {
        Map<String, PatientDTO> mappedPatients = new HashMap<>();

        for(PatientDTO patient : patients) {
            mappedPatients.put(patient.getBeaconId(), patient);
        }

        List<Beacon> sortedBeacons = getSortedBeacons();

        List<PatientDTO> matchedPatients = new ArrayList<>(patients.size());

        for (Beacon beacon : sortedBeacons) {
            if(mappedPatients.containsKey(beacon.getId())) {
                matchedPatients.add(mappedPatients.get(beacon.getId()));
            }
        }

        return matchedPatients;
    }

    @Data
    @RequiredArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(exclude = {"rssi"})
    public static class Beacon implements Comparable<Beacon> {

        private final String id;
        private final double rssi;

        @Override
        public int compareTo(Beacon other) {
            return Double.compare(other.rssi, this.rssi);
        }
    }

}
