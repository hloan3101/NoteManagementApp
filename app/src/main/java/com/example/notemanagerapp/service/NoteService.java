package com.example.notemanagerapp.service;

import com.example.notemanagerapp.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NoteService {

    @GET("get")
    Call<BaseResponse> getAllNote (@Query("tab") String tab, @Query("email") String email);

    @GET("add")
    Call<BaseResponse> addNote (@Query("tab") String tab, @Query("email") String email,
                                @Query("name") String name, @Query("Priority") String priority,
                                @Query("Category") String category, @Query("Status") String status,
                                @Query("PlanDate") String planDate);

    @GET("update")
    Call<BaseResponse> updateNote(@Query("tab") String tab, @Query("email") String email,
                                  @Query("name") String name, @Query("nname") String newName,
                                  @Query("Priority") String priority,
                                  @Query("Category") String category, @Query("Status") String status,
                                  @Query("PlanDate") String planDate);

    @GET("del")
    Call<BaseResponse> deleteNote(@Query("tab") String tab, @Query("email") String email,
                                  @Query("name") String name);
}
