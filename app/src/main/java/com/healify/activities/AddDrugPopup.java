package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.healify.R;

/**
 * Created by Kacper on 11.06.2016.
 */
public class AddDrugPopup extends Activity implements AdapterView.OnItemSelectedListener {

    private String drug_unit;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drug_popup);

        //lista lekow

        Spinner drugSpinner = (Spinner) findViewById(R.id.drug_list);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.units, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drugSpinner.setAdapter(adapter);

        Button okButton = (Button) findViewById(R.id.drug_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDrugPopup.this.finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.drug_unit = this.adapter.getItem(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}