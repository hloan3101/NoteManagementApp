package com.example.notemanagerapp.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagerapp.HomeActivity;
import com.example.notemanagerapp.LoginActivity;
import com.example.notemanagerapp.R;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.FragmentChangePasswordBinding;
import com.example.notemanagerapp.databinding.FragmentEditProfileBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;
import com.example.notemanagerapp.ui.viewmodel.ChangePasswordViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel viewModel;
    private FragmentChangePasswordBinding binding;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentChangePasswordBinding.inflate(inflater, container,false);

        initView();

        return binding.getRoot();
    }

    private void initView(){
        viewModel = new ViewModelProvider(getActivity()).get(ChangePasswordViewModel.class);

        binding.fragmentChangePasswordEtCurrentPassword.setText(DataLocalManager.getPassword());

        binding.fragmentChangePasswordBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        binding.fragmentChangePasswordBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callHomeActivity();
            }
        });
    }

    private void resetInput (){
        binding.fragmentChangePasswordTilCurrentPassword.setError(null);
        binding.fragmentChangePasswordTilNewPassword.setError(null);
        binding.fragmentChangePasswordTilConfirmNewPassword.setError(null);
    }

    private boolean checkInput(){
        resetInput();

        boolean checkData = true;

        if (binding.fragmentChangePasswordEtCurrentPassword.getText().toString().trim().isEmpty()){
            binding.fragmentChangePasswordTilCurrentPassword.setError(getString(R.string.require));
            checkData = false;
        }

        if (binding.fragmentChangePasswordEtNewPassword.getText().toString().trim().isEmpty()){
            binding.fragmentChangePasswordTilNewPassword.setError(getString(R.string.require));
            checkData = false;
        }

        if (binding.fragmentChangePasswordEtConfirmNewPassword.getText().toString().trim().isEmpty()){
            binding.fragmentChangePasswordTilConfirmNewPassword.setError(getString(R.string.require));
            checkData = false;
        }else if (!binding.fragmentChangePasswordEtNewPassword.getText().toString().trim().equals(
                binding.fragmentChangePasswordEtConfirmNewPassword.getText().toString().trim())){
            binding.fragmentChangePasswordTilConfirmNewPassword.setError(getString(R.string.password_error));
            checkData = false;
        }

        return checkData;
    }

    private void callHomeActivity (){
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }

    private void callLoginActivity (){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        DataLocalManager.setCheckLogout(true);
        startActivity(intent);
    }

    private void changePassword(){
        if (!checkInput()){
            return;
        }

        viewModel.changePassword(binding.fragmentChangePasswordEtConfirmNewPassword
                .getText().toString().trim()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus() == Constants.SUCCESSFUL){
                        Toast.makeText(getContext(), getString(
                                R.string.change_password_successfully), Toast.LENGTH_LONG).show();

                        DataLocalManager.setPassword(
                                binding.fragmentChangePasswordEtConfirmNewPassword.getText()
                                        .toString().trim());
                        callLoginActivity();
                    }else{
                        Toast.makeText(getContext(), getString(R.string.change_password_error),
                                Toast.LENGTH_LONG).show();

                        binding.fragmentChangePasswordTilCurrentPassword.setError(
                                getString(R.string.password_error));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}