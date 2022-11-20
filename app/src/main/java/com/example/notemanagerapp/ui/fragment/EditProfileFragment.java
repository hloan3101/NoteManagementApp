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
import com.example.notemanagerapp.databinding.FragmentEditProfileBinding;
import com.example.notemanagerapp.model.Account;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;
import com.example.notemanagerapp.ui.viewmodel.EditProfileViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private EditProfileViewModel viewModel;
    private FragmentEditProfileBinding binding;
    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        initView();

        return binding.getRoot();
    }

    private void initView (){
        viewModel = new ViewModelProvider(getActivity()).get(EditProfileViewModel.class);

        binding.fragmentEditProfileEtFirstName.setText(DataLocalManager.getFirstName());
        binding.fragmentEditProfileEtLastName.setText(DataLocalManager.getLastname());
        binding.fragmentEditProfileEtEmail.setText(DataLocalManager.getEmail());

        binding.fragmentEditProfileBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callHomeActivity();
            }
        });

        binding.fragmentEditProfileBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
    }

    private boolean checkInput (){
        resetInput();

        boolean checkData = true;

        if(binding.fragmentEditProfileEtFirstName.getText().toString().trim().isEmpty()){
            binding.fragmentEditProfileTilFirstName.setError(getString(R.string.require));
            checkData = false;
        }

        if (binding.fragmentEditProfileEtLastName.getText().toString().trim().isEmpty()){
            binding.fragmentEditProfileTilLastName.setError(getString(R.string.require));
            checkData = false;
        }

        if (binding.fragmentEditProfileEtEmail.getText().toString().trim().isEmpty()){
            binding.fragmentEditProfileTilEmail.setError(getString(R.string.require));
            checkData = false;
        }else if (binding.fragmentEditProfileEtEmail.getText().toString().trim().equals(
                getString(R.string.email_pattern))){
            binding.fragmentEditProfileTilEmail.setError(getString(R.string.email_format));
            checkData = false;
        }

        if (binding.fragmentEditProfileEtNewEmail.getText().toString().trim().isEmpty()){
            binding.fragmentEditProfileTilNewEmail.setError(getString(R.string.require));
            checkData = false;
        }else if (binding.fragmentEditProfileEtNewEmail.getText().toString().trim().equals(
                getString(R.string.email_pattern))){
            binding.fragmentEditProfileTilNewEmail.setError(getString(R.string.email_format));
            checkData = false;
        }

        return checkData;
    }

    private void resetInput(){
        binding.fragmentEditProfileTilFirstName.setError(null);
        binding.fragmentEditProfileTilLastName.setError(null);
        binding.fragmentEditProfileTilEmail.setError(null);
        binding.fragmentEditProfileTilNewEmail.setError(null);
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

    private void editProfile (){
        if (!checkInput()){
            return;
        }

        viewModel.editProfile(new Account(
                binding.fragmentEditProfileEtNewEmail.getText().toString().trim(),
                binding.fragmentEditProfileEtFirstName.getText().toString().trim(),
                binding.fragmentEditProfileEtLastName.getText().toString().trim())).enqueue(
                new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (response.body().getStatus() == Constants.SUCCESSFUL){
                                Toast.makeText(getContext(), getString(R.string.edit_profile_successfully),
                                        Toast.LENGTH_LONG).show();

                                DataLocalManager.setEmail(binding.fragmentEditProfileEtNewEmail
                                        .getText().toString().trim());
                                DataLocalManager.setFirstname(binding.fragmentEditProfileEtFirstName
                                        .getText().toString().trim());
                                DataLocalManager.setLastname(binding.fragmentEditProfileEtLastName
                                        .getText().toString().trim());

                                callLoginActivity();
                            }else {
                                Toast.makeText(getContext(), getString(R.string.edit_profile_error),
                                        Toast.LENGTH_LONG).show();

                                binding.fragmentEditProfileTilEmail.setError(getString
                                        (R.string.email_error));
                                binding.fragmentEditProfileTilNewEmail.setError(getString
                                        (R.string.email_error));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}