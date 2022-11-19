package com.example.notemanagerapp.service;


import com.example.notemanagerapp.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardService {

    @GET("get")
    Call<BaseResponse> getDashboard(@Query("tab") String tab, @Query("email") String email);

}
