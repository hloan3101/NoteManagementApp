package com.example.notemanagerapp.service;

import com.example.notemanagerapp.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccountService {

    @GET("login")
    Call<BaseResponse> signIn(@Query("email") String email, @Query("pass") String password);

    @GET("signup")
    Call<BaseResponse> signUp(@Query("email") String email, @Query("pass") String password,
                              @Query("firstname") String firstname,
                              @Query("lastname") String lastname);

    @GET("update")
    Call<BaseResponse> editProfile (@Query("tab") String tab, @Query("email") String email,
                                    @Query("firstname") String firstname,
                                    @Query("lastname") String lastname);

    @GET("update")
    Call<BaseResponse> changePassword (@Query("tab") String tab, @Query("email") String email,
                                       @Query("pass") String password,
                                       @Query("npass") String newPassword);
}
