package com.healify.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.healify.R;
import com.healify.web.dto.PatientDTO;

import java.util.Arrays;

public class BrowseTemperaturePopup extends AppCompatActivity implements View.OnClickListener {

    private XYPlot temperaturePlot;
    private PatientDTO patientDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_temperature_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        // ToDo: remove this mock
        this.temperaturePlot = (XYPlot) findViewById(R.id.temperature_plot);
        Number[] data = {37.1, 36.6, 37.5, 38.2, 38.8, 37.3};
        Number[] x = {1, 2, 3, 4, 5, 6};
        XYSeries series = new SimpleXYSeries(
                Arrays.asList(x),
                Arrays.asList(data),
                "Temperature");
        temperaturePlot.addSeries(series, new LineAndPointFormatter());

        Button okButton = (Button) findViewById(R.id.temperature_bowse_ok_button);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
