package com.example.notemanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.ActivityLoginBinding;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;
import com.example.notemanagerapp.viewmodel.AccountViewModel;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

    }

    public boolean checkInput() {
        resetError();

        if (binding.activityLoginEtEmail.getText().toString().trim().isEmpty() &&
                binding.activityLoginEtPassword.getText().toString().trim().isEmpty()) {
            binding.activityLoginTilEmail.setError(getResources().getString(R.string.require));
            binding.activityLoginTilPassword.setError(getResources().getString(R.string.require));
            return false;
        }

        if (binding.activityLoginEtEmail.getText().toString().trim().isEmpty()) {
            binding.activityLoginTilEmail.setError(getResources().getString(R.string.require));
            return false;
        }

        if (binding.activityLoginEtPassword.getText().toString().trim().isEmpty()) {
            binding.activityLoginTilPassword.setError(getResources().getString(R.string.require));
            return false;
        }

        if (!Pattern.matches(Constants.email_pattern,
                binding.activityLoginEtEmail.getText().toString().trim())) {
            binding.activityLoginTilEmail.setError(getResources().getString(R.string.email_format));
            return false;
        }

        return true;
    }

    public void resetError() {
        binding.activityLoginTilEmail.setError(null);
        binding.activityLoginTilPassword.setError(null);
    }
}