package com.example.notemanagerapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.notemanagerapp.model.Account;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.repository.AccountRepository;

import retrofit2.Call;

public class EditProfileViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);

        accountRepository = new AccountRepository();
    }

    public Call<BaseResponse> editProfile (Account account){
        return accountRepository.editProfile(account);
    }

}