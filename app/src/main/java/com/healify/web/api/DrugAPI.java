package com.healify.web.api;

import com.healify.web.dto.DrugDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DrugAPI {

    @GET("api/patients/{id}/drugs")
    Call<List<DrugDTO>> getPatientsDrugs(@Path("id") String id);

}
