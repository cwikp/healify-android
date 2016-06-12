package com.healify.web.api;

import com.healify.web.dto.CheckUpDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CheckUpAPI {

    @GET("api/patients/{id}/check-ups")
    Call<List<CheckUpDTO>> getPatientCheckups(@Path("id") String id);

}
