package com.healify.web.api;

import com.healify.web.dto.PatientDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface PatientAPI {

    @POST("api/patients/with-ids")
    Call<List<PatientDTO>> getPatientsWithIds(@Body List<String> beaconIds);
}
