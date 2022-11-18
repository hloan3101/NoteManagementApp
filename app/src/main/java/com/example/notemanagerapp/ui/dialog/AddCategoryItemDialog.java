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
import com.example.notemanagerapp.databinding.DialogAddCategoryItemBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.ui.viewmodel.CategoryViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCategoryItemDialog extends DialogFragment {

    private final String EDIT = "Edit";
    private final String ADD = "Add";
    private DialogAddCategoryItemBinding binding;
    private CategoryViewModel model;

    public static AddCategoryItemDialog newInstance (){
        return new AddCategoryItemDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogAddCategoryItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setCancelable(false);
        initView();

        binding.dialogAddCategoryItemBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.dialogAddCategoryItemBtnAdd.getText().toString().trim().equals(EDIT)){
                    editCategoryItem();
                } else {
                    addCategoryItem();
                }
            }
        });

        binding.dialogAddCategoryItemBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void initView(){
        String str = getArguments().getString(getString(R.string.check_edit));
        binding.dialogAddCategoryItemBtnAdd.setText(str);

        if (str.equals(EDIT)){
            binding.dialogAddCategoryItemNameDialog.setText(getString(R.string.edit_category_dialog));
            binding.dialogAddCategoryItemEtName.setText(getArguments().getString(
                    getString(R.string.name_category)));
        }else {
            binding.dialogAddCategoryItemNameDialog.setText(getString(R.string.add_category_dialog));
        }

        model = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
    }

    private boolean checkInput (){
        if (binding.dialogAddCategoryItemEtName.getText().toString().trim().isEmpty()){
            binding.dialogAddCategoryItemTilName.setError(getString(R.string.require));
            return false;
        }

        return true;
    }

    private void addCategoryItem(){
         if (!checkInput()){
             return;
         }

         model.addCategoryItem(binding.dialogAddCategoryItemEtName.getText().toString().trim())
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
                         binding.dialogAddCategoryItemTilName.setError(getString(R.string.name_exists));
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

        model.updateCategoryItem(getArguments().getString(getString(R.string.name_category)),
                binding.dialogAddCategoryItemEtName.getText().toString().trim()).enqueue(
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
