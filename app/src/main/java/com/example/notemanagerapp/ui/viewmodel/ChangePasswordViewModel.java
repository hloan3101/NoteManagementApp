package com.example.notemanagerapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.notemanagerapp.model.Account;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.repository.AccountRepository;

import retrofit2.Call;

public class ChangePasswordViewModel extends AndroidViewModel {

    private AccountRepository repository;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);

        repository = new AccountRepository();
    }

    public Call<BaseResponse> changePassword(String newPassword){
        return repository.changePassword(newPassword);
    }
}