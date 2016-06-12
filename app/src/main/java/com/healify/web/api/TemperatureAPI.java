package com.healify.web.api;

import com.healify.web.dto.TemperatureDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TemperatureAPI {

    @GET("api/patients/{id}/temperatures")
    Call<List<TemperatureDTO>> getPatientsTemperatures(@Path("id") String id);
}
