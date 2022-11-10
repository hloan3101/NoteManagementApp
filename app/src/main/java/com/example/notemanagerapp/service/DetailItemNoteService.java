package com.example.notemanagerapp.service;

import com.example.notemanagerapp.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DetailItemNoteService {

    @GET("get")
    Call<BaseResponse> getDetailItemNote(@Query("tab") String tab, @Query("email") String email);

    @GET("add")
    Call<BaseResponse> addDetailItemNote(@Query("tab") String tab, @Query("email") String email,
                                         @Query("name") String name);

    @GET("update")
    Call<BaseResponse> updateDetailItemNote(@Query("tab") String tab, @Query("email") String email,
                                            @Query("name") String name, @Query("nname") String nName);

    @GET("del")
    Call<BaseResponse> deleteDetailItemNote(@Query("tab") String tab, @Query("email") String email,
                                            @Query("name") String name);
}
