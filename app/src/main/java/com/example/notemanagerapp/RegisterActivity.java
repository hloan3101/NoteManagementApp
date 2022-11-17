package com.example.notemanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.ActivityRegisterBinding;
import com.example.notemanagerapp.model.Account;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;
import com.example.notemanagerapp.ui.viewmodel.AccountViewModel;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding.activityRegisterBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        binding.activityRegisterBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoginActivity();
            }
        });
    }

    private boolean checkInput (){
        resetError();

        boolean checkData = true;

        if (binding.activityRegisterEtFirstName.getText().toString().trim().isEmpty()){
            binding.activityRegisterTilFirstName.setError(getResources().getString(R.string.require));
            checkData = false;
        }

        if (binding.activityRegisterEtLastName.getText().toString().trim().isEmpty()){
            binding.activityRegisterTilLastName.setError(getResources().getString(R.string.require));
            checkData = false;
        }

        if (binding.activityRegisterEtEmail.getText().toString().trim().isEmpty()){
            binding.activityRegisterTilEmail.setError(getResources().getString(R.string.require));
            checkData = false;
        } else if (!Pattern.matches(getResources().getString(R.string.email_pattern),
                binding.activityRegisterEtEmail.getText().toString().trim())){
            binding.activityRegisterTilEmail.setError(getResources().getString(R.string.email_format));
            checkData = false;
        }

        if (binding.activityRegisterEtPassword.getText().toString().trim().isEmpty()){
            binding.activityRegisterTilPassword.setError(getResources().getString(R.string.require));
            checkData = false;
        }

        if (binding.activityRegisterEtConfirmPassword.getText().toString().trim().isEmpty()){
            binding.activityRegisterTilConfirmPassword.setError(getResources()
                    .getString(R.string.require));
            checkData = false;
        } else if (!binding.activityRegisterEtPassword.getText().toString().trim()
                .equals(binding.activityRegisterEtConfirmPassword.getText().toString().trim())){
            binding.activityRegisterTilConfirmPassword.setError(getResources()
                    .getString(R.string.password_error));
            checkData = false;
        }

        return checkData;
    }

    private void signUp (){
        if (!checkInput()){
            return;
        }

        final Account account = new Account(
                binding.activityRegisterEtEmail.getText().toString().trim(),
                binding.activityRegisterEtConfirmPassword.getText().toString().trim(),
                binding.activityRegisterEtFirstName.getText().toString().trim(),
                binding.activityRegisterEtLastName.getText().toString().trim());

        viewModel.signUp(account).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.ERROR){
                        binding.activityRegisterTilEmail.setError(getResources()
                                .getString(R.string.email_exists));
                    } else{
                        DataLocalManager.setEmail(account.getEmail());
                        DataLocalManager.setPassword(account.getPassword());

                        callLoginActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetError (){
        binding.activityRegisterTilFirstName.setError(null);
        binding.activityRegisterTilLastName.setError(null);
        binding.activityRegisterTilEmail.setError(null);
        binding.activityRegisterTilPassword.setError(null);
        binding.activityRegisterTilConfirmPassword.setError(null);

    }

    private void callLoginActivity (){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}