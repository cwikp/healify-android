package com.healify.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.healify.R;
import com.healify.web.api.TemperatureAPI;
import com.healify.web.dto.PatientDTO;
import com.healify.web.dto.TemperatureDTO;
import com.healify.web.services.ServiceGenerator;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowseTemperaturePopup extends AppCompatActivity implements View.OnClickListener {

    private XYPlot temperaturePlot;
    private PatientDTO patientDTO
;
    private TemperatureAPI temperatureAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_temperature_popup);
        this.patientDTO = (PatientDTO) getIntent().getSerializableExtra("patientDTO");

        temperatureAPI = ServiceGenerator.createService(TemperatureAPI.class, null);
        this.temperaturePlot = (XYPlot) findViewById(R.id.temperature_plot);

        Button okButton = (Button) findViewById(R.id.temperature_bowse_ok_button);
        okButton.setOnClickListener(this);

        Call<List<TemperatureDTO>> call = temperatureAPI.getPatientsTemperatures(patientDTO.getBeaconId());
        call.enqueue(new Callback<List<TemperatureDTO>>() {
            @Override
            public void onResponse(Call<List<TemperatureDTO>> call, Response<List<TemperatureDTO>> response) {
                if (response.isSuccessful()) {
                    List<TemperatureDTO> body = response.body();
                    Toast.makeText(BrowseTemperaturePopup.this, "temperatures downloaded successfully", Toast.LENGTH_LONG);
                    temperaturesDownloaded(body);
                } else {
                    Toast.makeText(BrowseTemperaturePopup.this, "temperatures not downloaded", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<TemperatureDTO>> call, Throwable t) {

            }
        });

        // ToDo: remove this mock
//        this.temperaturePlot = (XYPlot) findViewById(R.id.temperature_plot);
//        Number[] data = {37.1, 36.6, 37.5, 38.2, 38.8, 37.3};
//        Number[] x = {1, 2, 3, 4, 5, 6};
//        XYSeries series = new SimpleXYSeries(
//                Arrays.asList(x),
//                Arrays.asList(data),
//                "Temperature");
//        temperaturePlot.addSeries(series, new LineAndPointFormatter());
//
//        Button okButton = (Button) findViewById(R.id.temperature_bowse_ok_button);
//        okButton.setOnClickListener(this);
    }

    private void temperaturesDownloaded(List<TemperatureDTO> temperatures) {
        Log.i("BrowseTemperaturePopup", temperatures.toString());

        Number[] x = new Number[temperatures.size() + 1];
        Number[] data = new Number[temperatures.size() + 1];

//        List<Number> x = new ArrayList<>();
//        List<Number> data = new ArrayList<>();
//
        for (int i = 0; i < temperatures.size(); i++) {
            x[i] = i;
            data[i] = Double.valueOf(temperatures.get(i).getValue());
        }

//        Number[] data = {37.1, 36.6, 37.5, 38.2, 38.8, 37.3};
//        Number[] x = {1, 2, 3, 4, 5, 6};

        Log.i("X values", x.toString());
        Log.i("Temperature values", data.toString());
        XYSeries series = new SimpleXYSeries(
                Arrays.asList(x),
                Arrays.asList(data),
                "Temperature");
        temperaturePlot.addSeries(series, new LineAndPointFormatter());

        temperaturePlot.redraw();

    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
