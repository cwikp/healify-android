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
import com.healify.entities.DrugEntity;

import com.healify.web.api.DrugAPI;
import com.healify.web.dto.CheckUpDTO;
import com.healify.web.dto.DrugDTO;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckupListingPopup extends AppCompatActivity implements View.OnClickListener {

    private CheckupAPI checkUpAPI = ServiceGenerator.createService(CheckupAPI.class, null);

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        Button okButton = (Button) findViewById(R.id.listing_ok_button);
        okButton.setOnClickListener(this);

        Call<List<CheckUpDTO>> call = checkUpAPI.getPatientsCheckups(patientDTO.getBeaconId());
        call.enqueue(new Callback<List<CheckUpDTO>>() {
            @Override
            public void onResponse(Call<List<CheckUpDTO>> call, Response<List<CheckUpDTO>> response) {
                if(response.isSuccessful()){
                    checkupsDownloaded(response.body());
//                    Toast.makeText(CheckupListingPopup.this, "Patients downloaded successfully", Toast.LENGTH_LONG).show();
                }
                else {
//                    Toast.makeText(CheckupListingPopup.this, "Patients not downloaded", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CheckUpDTO>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }


    private void checkupsDownloaded(List<CheckUpDTO> body) {
        TextView checkupList = (TextView) findViewById(R.id.drugs);

        String checkups = "";
        for (int i = 0; i < body.size(); i++) {
            String drug = body.get(i).getName() + " " +
                    body.get(i).getResult()+ "\n";
            checkups = checkups.concat(drug);
        }
        checkupList.setText(checkups);
    }
}