package com.healify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healify.R;
import com.healify.entities.DrugEntity;

public class DrugsListingPopup extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_popup);

        Button okButton = (Button) findViewById(R.id.drugs_listing_ok_button);
        okButton.setOnClickListener(this);

        //ToDo: remove this mock
        DrugEntity[] drugEntities = {new DrugEntity("Panadol", 500, "mg"),
                                     new DrugEntity("Wit. C", 2500, "ml"),
                                     new DrugEntity("Aspirin", 3, "g")};

        TextView drugList = (TextView) findViewById(R.id.drugs);

        String drugs = "";
        for(int i=0; i<drugEntities.length; i++) {
            String drug = drugEntities[i].getName() + " " +
                    Integer.toString(drugEntities[i].getDose()) + " " +
                    drugEntities[i].getUnit() + "\n";
            drugs = drugs.concat(drug);
        }
        drugList.setText(drugs);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
