package com.healify.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.entities.CheckupEntity;
import com.healify.web.api.CheckupAPI;
import com.healify.web.dto.CheckUpDTO;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckupListingPopup extends AppCompatActivity implements View.OnClickListener {

    private PatientDTO patientDTO;
    private CheckupAPI checkupAPI;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        Button okButton = (Button) findViewById(R.id.listing_ok_button);
        okButton.setOnClickListener(this);


        checkupAPI = ServiceGenerator.createService(CheckupAPI.class, null);
        Call<List<CheckUpDTO>> call = checkupAPI.getPatientsCheckups(patientDTO.getBeaconId());
        call.enqueue(new Callback<List<CheckUpDTO>>() {
            @Override
            public void onResponse(Call<List<CheckUpDTO>> call, Response<List<CheckUpDTO>> response) {
                if(response.isSuccessful()){
                    List<CheckUpDTO> body = response.body();

                    checkupsDownloaded(body);

                    Toast.makeText(CheckupListingPopup.this, "Patients downloaded successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(CheckupListingPopup.this, "Patients not downloaded", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CheckUpDTO>> call, Throwable t) {

            }
        });

        //ToDo: remove this mock
        CheckupEntity[] drugEntities = {
                new CheckupEntity("OB", 20),
                new CheckupEntity("Sugar", 250),
                new CheckupEntity("Calcium", 35)};

        TextView checkupList = (TextView) findViewById(R.id.drugs);

        String drugs = "";
        for (int i = 0; i < drugEntities.length; i++) {
            String drug = drugEntities[i].getName() + " " +
                    Integer.toString(drugEntities[i].getResult()) + "\n";
            drugs = drugs.concat(drug);
        }
        checkupList.setText(drugs);

    }

    private void checkupsDownloaded(List<CheckUpDTO> checkups) {
        Log.i("Sample", checkups.toString());

    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
