package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.healify.R;
import com.healify.web.api.PatientAPI;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutPopup extends Activity {

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        setContentView(R.layout.check_out_popup);

        Button confirmButton = (Button) findViewById(R.id.check_out_yes);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = patientDTO.getBeaconId();
                PatientAPI service = ServiceGenerator.createService(PatientAPI.class, "application/json");
                Call<Void> call = service.sendCheckOut(id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        Toast.makeText(CheckOutPopup.this,
//                                Integer.toString(response.code()),
//                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(CheckOutPopup.this,
//                                "API failed",
//                                Toast.LENGTH_LONG).show();
                    }
                });

                CheckOutPopup.this.finish();
            }
        });

        Button declineButton = (Button) findViewById(R.id.check_out_no);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutPopup.this.finish();
            }
        });


    }
}
