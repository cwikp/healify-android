package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.web.api.PatientAPI;
import com.healify.web.dto.CheckUpDTO;
import com.healify.web.dto.DrugDTO;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kacper on 11.06.2016.
 */
public class AddCheckPopup extends Activity {

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        Button okButton = (Button) findViewById(R.id.check_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = patientDTO.getBeaconId();
                String name = ((TextView) findViewById(R.id.checkup_name_input)).getText().toString();
                String result = ((TextView) findViewById(R.id.checkup_result_input)).getText().toString();
                Log.e("MISIU!!!", result);

                CheckUpDTO checkUpDTO = CheckUpDTO.builder().name(name).result(result).build();

                PatientAPI service = ServiceGenerator.createService(PatientAPI.class, "application/json");
                Call<Void> call = service.sendCheckUp(id, checkUpDTO);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(AddCheckPopup.this,
                                Integer.toString(response.code()),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AddCheckPopup.this,
                                "API failed",
                                Toast.LENGTH_LONG).show();
                    }
                });

                AddCheckPopup.this.finish();
            }
        });
    }
}

