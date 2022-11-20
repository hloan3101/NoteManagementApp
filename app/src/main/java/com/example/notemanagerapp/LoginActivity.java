package com.example.notemanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.ActivityLoginBinding;
import com.example.notemanagerapp.model.Account;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;
import com.example.notemanagerapp.ui.viewmodel.AccountViewModel;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (!DataLocalManager.getFirstInstalled()){
            DataLocalManager.setCheckLogout(true);
            DataLocalManager.setFirstInstalled(true);
        }

        if (!DataLocalManager.getCheckLogout()){
            callActivityHome();
        }
        else {
            if (DataLocalManager.getCheckRememberMe()){
                binding.activityLoginEtEmail.setText(DataLocalManager.getEmail());
                binding.activityLoginEtPassword.setText(DataLocalManager.getPassword());
            }
        }
        binding.activityLoginBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        binding.activityLoginBtnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callActivityRegister();
            }
        });

        binding.activityLoginBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

    }

    private void callActivityRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void callActivityHome (){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkInput() {
        resetError();

        boolean checkData = true;

        if (binding.activityLoginEtEmail.getText().toString().trim().isEmpty()) {
            binding.activityLoginTilEmail.setError(getResources().getString(R.string.require));
            checkData = false;
        } else if (!Pattern.matches(getResources().getString(R.string.email_pattern),
                binding.activityLoginEtEmail.getText().toString().trim())) {
            binding.activityLoginTilEmail.setError(getResources().getString(R.string.email_format));
            checkData = false;
        }

        if (binding.activityLoginEtPassword.getText().toString().trim().isEmpty()) {
            binding.activityLoginTilPassword.setError(getResources().getString(R.string.require));
            checkData = false;
        }

        return checkData;
    }

    private void signIn (){
        if (!checkInput()){
            return;
        }

        final Account account = new Account(binding.activityLoginEtEmail.getText().toString().trim(),
                binding.activityLoginEtPassword.getText().toString().trim());
        viewModel.logIn(account).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.ERROR){
                        if (baseResponse.getError() == Constants.PASSWORD_ERROR){
                            binding.activityLoginTilPassword.setError(getResources().
                                    getString(R.string.password_error));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, getResources().getString
                                    (R.string.account_not_found), Toast.LENGTH_LONG).show();

                            binding.activityLoginTilEmail.setError(getResources().getString(R.string.require));
                            binding.activityLoginTilPassword.setError(getResources().getString(R.string.require));
                        }
                    }
                    else {
                        DataLocalManager.setEmail(account.getEmail());
                        DataLocalManager.setPassword(account.getPassword());
                        DataLocalManager.setCheckRememberMe(isCheckedRemember());
                        DataLocalManager.setCheckLogout(false);
                        DataLocalManager.setFirstname(baseResponse.getInfo().getFirstName());
                        DataLocalManager.setLastname(baseResponse.getInfo().getLastName());

                        callActivityHome();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isCheckedRemember (){
        if (binding.activityLoginChkRememberMe.isChecked()){
            return true;
        }

        return false;
    }

    private void resetError() {
        binding.activityLoginTilEmail.setError(null);
        binding.activityLoginTilPassword.setError(null);
    }
}