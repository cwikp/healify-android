package com.healify.web.api;

import com.healify.web.dto.PatientDTO;

import retrofit2.Call;
import retrofit2.http.GET;


public interface PatientAPI {

    @GET("api/users")
    Call<PatientDTO> createUser();

}
