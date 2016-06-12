package com.healify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.healify.R;

public class CheckOutPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_out_popup);

        Button confirmButton = (Button) findViewById(R.id.check_out_yes);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToDo: check out via REST
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
