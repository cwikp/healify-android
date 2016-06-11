package com.healify.activities;

import android.app.Activity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsList extends AppCompatActivity {

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
            }

        });

        getPatientsFromServer();

        Toast.makeText(PatientsList.this, "Started", Toast.LENGTH_LONG).show();
        onPatientsListItemClick();

        NavigationDrawer navigationDrawer = new NavigationDrawer(this, toolbar);
        navigationDrawer.setupDrawer();

    }

    private void getPatientsFromServer() {
        List<String> beaconsIds = new ArrayList<>();
        beaconsIds.add("Xt9P");
        beaconsIds.add("PUyp");
        beaconsIds.add("kBZ8");
        PatientAPI service = ServiceGenerator.createService(PatientAPI.class, null);
        Call<List<PatientDTO>> call = service.getPatientsWithIds(beaconsIds);
        call.enqueue(new Callback<List<PatientDTO>>() {

            @Override
            public void onResponse(Call<List<PatientDTO>> call, Response<List<PatientDTO>> response) {
                response.code();
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);

                if(response.isSuccessful()){
                    setPatientsData(response.body());
                    fillpatientsListView();
                    Toast.makeText(PatientsList.this, "Patients downloaded successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(PatientsList.this, "Patients not downloaded", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, data.getStringExtra("createPatientResult"), Toast.LENGTH_LONG).show();
                getPatientsFromServer();
            }
            else{
                Toast.makeText(this, "Patient might not have been created", Toast.LENGTH_LONG).show();

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
                Toast.makeText(PatientsList.this, clickedPatient.getFirstName(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PatientsList.this, SinglePatient.class);
                intent.putExtra("patient", clickedPatient);
                PatientsList.this.startActivity(intent);
            }
        });
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

}
