package com.example.notemanagerapp.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.DialogAddStatusItemBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.ui.viewmodel.StatusViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddStatusItemDialog extends DialogFragment {

    private final String EDIT = "Edit";
    private final String ADD = "Add";
    private DialogAddStatusItemBinding binding;
    private StatusViewModel model;

    public static AddStatusItemDialog newInstance (){
        return new AddStatusItemDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogAddStatusItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setCancelable(false);
        initView();

        binding.dialogAddStatusItemBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.dialogAddStatusItemBtnAdd.getText().toString().trim().equals(EDIT)){
                    editStatusItem();
                } else {
                    addStatusItem();
                }
            }
        });

        binding.dialogAddStatusItemBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void initView(){
        String str = getArguments().getString(getString(R.string.check_edit));
        binding.dialogAddStatusItemBtnAdd.setText(str);

        if (str.equals(EDIT)){
            binding.dialogAddStatusItemNameDialog.setText(getString(R.string.edit_status_dialog));
            binding.dialogAddStatusItemEtName.setText(getArguments().getString(
                    getString(R.string.name_status)));
        }else {
            binding.dialogAddStatusItemNameDialog.setText(getString(R.string.add_status_dialog));
        }

        model = new ViewModelProvider(getActivity()).get(StatusViewModel.class);
    }

    private boolean checkInput (){
        if (binding.dialogAddStatusItemEtName.getText().toString().trim().isEmpty()){
            binding.dialogAddStatusItemTilName.setError(getString(R.string.require));
            return false;
        }

        return true;
    }

    private void addStatusItem(){
         if (!checkInput()){
             return;
         }

         model.addStatusItem(binding.dialogAddStatusItemEtName.getText().toString().trim())
                 .enqueue(new Callback<BaseResponse>() {
             @Override
             public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                 if(response.body() != null){
                     if (response.body().getStatus() == Constants.SUCCESSFUL){
                         Toast.makeText(getContext(), getString(R.string.add_successfully),
                                 Toast.LENGTH_LONG).show();
                         model.refreshLiveData();
                         dismiss();

                     }else {
                         binding.dialogAddStatusItemEtName.setError(getString(R.string.name_exists));
                         Toast.makeText(getContext(), getString(R.string.add_error),
                                 Toast.LENGTH_LONG).show();
                     }
                 }
             }

             @Override
             public void onFailure(Call<BaseResponse> call, Throwable t) {
                 Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
             }
         });
    }

    private void editStatusItem(){
        if(!checkInput()){
            return;
        }

        model.updateStatusItem(getArguments().getString(getString(R.string.name_status)),
                binding.dialogAddStatusItemEtName.getText().toString().trim()).enqueue(
                new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (response.body().getStatus() == Constants.SUCCESSFUL){
                                Toast.makeText(getContext(), getString(R.string.edit_successfully),
                                        Toast.LENGTH_LONG).show();
                                model.refreshLiveData();
                                dismiss();
                            }
                            else {
                                Toast.makeText(getContext(), getString(R.string.edit_error),
                                        Toast.LENGTH_LONG).show();
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

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) layoutParams);
    }
}
