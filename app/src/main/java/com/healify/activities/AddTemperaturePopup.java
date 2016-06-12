package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.web.api.PatientAPI;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kacper on 11.06.2016.
 */
public class AddTemperaturePopup extends Activity {

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.temperature_popup);

        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        Button okButton = (Button) findViewById(R.id.temp_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = patientDTO.getBeaconId();
                int temperature = Integer.parseInt(((TextView) findViewById(R.id.temperature_input)).getText().toString());

                PatientAPI service = ServiceGenerator.createService(PatientAPI.class, null);
                Call<Void> call = service.sendTemperature(id, temperature);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(AddTemperaturePopup.this,
                                Integer.toString(response.code()),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AddTemperaturePopup.this,
                                "API failed",
                                Toast.LENGTH_LONG).show();
                    }
                });

                AddTemperaturePopup.this.finish();
            }
        });
    }
}
