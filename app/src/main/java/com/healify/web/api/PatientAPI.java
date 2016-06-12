package com.healify.web.api;

import com.healify.web.dto.PatientDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface PatientAPI {

    @POST("api/patients/with-ids")
    Call<List<PatientDTO>> getPatientsWithIds(@Body List<String> beaconIds);

    @POST("api/patients")
    Call<PatientDTO> checkInPatient(@Body PatientDTO patientDTO);

    @PATCH("api/patients/{id}")
    Call<Void> sendTemperature(@Path("id") String id, @Query("temperature") String temperature);

}
