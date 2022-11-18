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
import com.example.notemanagerapp.databinding.DialogAddPriorityItemBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.ui.viewmodel.PriorityViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddPriorityItemDialog extends DialogFragment {

    private final String EDIT = "Edit";
    private final String ADD = "Add";
    private DialogAddPriorityItemBinding binding;
    private PriorityViewModel model;

    public static AddPriorityItemDialog newInstance (){
        return new AddPriorityItemDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogAddPriorityItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setCancelable(false);
        initView();

        binding.dialogAddPriorityItemBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.dialogAddPriorityItemBtnAdd.getText().toString().trim().equals(EDIT)){
                    editCategoryItem();
                } else {
                    addPriorityItem();
                }
            }
        });

        binding.dialogAddPriorityItemBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void initView(){
        String str = getArguments().getString(getString(R.string.check_edit));
        binding.dialogAddPriorityItemBtnAdd.setText(str);

        if (str.equals(EDIT)){
            binding.dialogAddPriorityItemNameDialog.setText(getString(R.string.edit_priority_dialog));
        }else {
            binding.dialogAddPriorityItemNameDialog.setText(getString(R.string.add_priority_dialog));
        }

        model = new ViewModelProvider(getActivity()).get(PriorityViewModel.class);
    }

    private boolean checkInput (){
        if (binding.dialogAddPriorityItemEtName.getText().toString().trim().isEmpty()){
            binding.dialogAddPriorityItemTilName.setError(getString(R.string.require));
            return false;
        }

        return true;
    }

    private void addPriorityItem(){
         if (!checkInput()){
             return;
         }

         model.addPriorityItem(binding.dialogAddPriorityItemEtName.getText().toString().trim())
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
                         binding.dialogAddPriorityItemEtName.setError(getString(R.string.name_exists));
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

    private void editCategoryItem(){
        if(!checkInput()){
            return;
        }

        model.updatePriorityItem(getArguments().getString(getString(R.string.name_priority)),
                binding.dialogAddPriorityItemEtName.getText().toString().trim()).enqueue(
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
