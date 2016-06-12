package com.healify.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.entities.DrugEntity;
import com.healify.web.api.DrugAPI;
import com.healify.web.dto.DrugDTO;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrugsListingPopup extends AppCompatActivity implements View.OnClickListener {

    private DrugAPI drugAPI = ServiceGenerator.createService(DrugAPI.class, null);

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        Button okButton = (Button) findViewById(R.id.listing_ok_button);
        okButton.setOnClickListener(this);

        Call<List<DrugDTO>> call = drugAPI.getPatientsDrugs(patientDTO.getBeaconId());
        call.enqueue(new Callback<List<DrugDTO>>() {
            @Override
            public void onResponse(Call<List<DrugDTO>> call, Response<List<DrugDTO>> response) {
                if (response.isSuccessful()) {
                    drugsDownloaded(response.body());
//                    Toast.makeText(DrugsListingPopup.this, "Drugs downloaded successfully", Toast.LENGTH_LONG);
                } else {
//                    Toast.makeText(DrugsListingPopup.this, "Drugs not downloaded", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DrugDTO>> call, Throwable t) {

            }
        });
    }

    private void drugsDownloaded(List<DrugDTO> drugDTOs) {
        Log.i("DrugListingPopup", drugDTOs.toString());

        TextView drugList = (TextView) findViewById(R.id.drugs);
        String drugsString = "";
        for(int i=0; i<drugDTOs.size(); i++) {
            String drug = drugDTOs.get(i).getName() + " " +
                    drugDTOs.get(i).getQuantity() + " " +
                    drugDTOs.get(i).getUnit() + "\n";
            drugsString = drugsString.concat(drug);
        }
        drugList.setText(drugsString);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
