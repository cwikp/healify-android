package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.healify.R;

/**
 * Created by Kacper on 11.06.2016.
 */
public class AddCheckPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_popup);

        Button okButton = (Button) findViewById(R.id.check_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCheckPopup.this.finish();
            }
        });
    }
}

