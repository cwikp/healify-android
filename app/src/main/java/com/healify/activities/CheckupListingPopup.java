package com.healify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healify.R;
import com.healify.entities.CheckupEntity;
import com.healify.entities.DrugEntity;

public class CheckupListingPopup extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_popup);

        Button okButton = (Button) findViewById(R.id.listing_ok_button);
        okButton.setOnClickListener(this);

        //ToDo: remove this mock
        CheckupEntity[] drugEntities = {
                new CheckupEntity("OB", 20),
                new CheckupEntity("Sugar", 250),
                new CheckupEntity("Calcium", 35)};

        TextView checkupList = (TextView) findViewById(R.id.drugs);

        String drugs = "";
        for(int i=0; i<drugEntities.length; i++) {
            String drug = drugEntities[i].getName() + " " +
                    Integer.toString(drugEntities[i].getResult()) + "\n";
            drugs = drugs.concat(drug);
        }
        checkupList.setText(drugs);

    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
