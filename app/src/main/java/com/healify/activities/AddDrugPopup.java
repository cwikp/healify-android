package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.healify.R;
import com.healify.web.api.PatientAPI;
import com.healify.web.dto.DrugDTO;
import com.healify.web.dto.PatientDTO;
import com.healify.web.services.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kacper on 11.06.2016.
 */
public class AddDrugPopup extends Activity implements AdapterView.OnItemSelectedListener {

    private String drugUnit;
    private ArrayAdapter<CharSequence> adapter;

    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");


        //lista lekow

        final Spinner drugSpinner = (Spinner) findViewById(R.id.drug_list);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.units, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drugSpinner.setAdapter(adapter);

        drugSpinner.setOnItemSelectedListener(this);

        Button okButton = (Button) findViewById(R.id.drug_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = patientDTO.getBeaconId();
                String name = ((TextView) findViewById(R.id.drug_name_input)).getText().toString();
                String dose = ((TextView) findViewById(R.id.drug_dose_input)).getText().toString();

                DrugDTO drugDTO = DrugDTO.builder().name(name).quantity(dose).unit(drugUnit).build();
                Log.e("MISIU!!!", drugUnit);

                PatientAPI service = ServiceGenerator.createService(PatientAPI.class, "application/json");
                Call<Void> call = service.sendDrug(id, drugDTO);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(AddDrugPopup.this,
                                Integer.toString(response.code()),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AddDrugPopup.this,
                                "API failed",
                                Toast.LENGTH_LONG).show();
                    }
                });

                AddDrugPopup.this.finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.drugUnit = this.adapter.getItem(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}