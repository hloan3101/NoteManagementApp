package com.example.notemanagerapp.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.DialogAddDetailItemNoteBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.ui.viewmodel.CategoryViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCategoryNoteDialog extends DialogFragment {

    private final String EDIT = "Edit";
    private final String ADD = "Add";
    private DialogAddDetailItemNoteBinding binding;
    private CategoryViewModel model;

    public static AddCategoryNoteDialog newInstance (){
        return new AddCategoryNoteDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddDetailItemNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

    //    setCancelable(false);
        initView();
        return view;
    }

    private void initView(){
        binding.dialogAddCategoryBtnAdd.setText(getArguments().getString(getString(R.string.checkEdit)));
        String typeDetailItemNote = getArguments().getString(getString(R.string.typeDetailItemNote));
        if (typeDetailItemNote.equals(Constants.TAB_CATEGORY)){
            model = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        }
    }

    private boolean checkInput (){
        if (binding.dialogAddCategoryEtName.getText().toString().trim().isEmpty()){
            binding.dialogAddCategoryTilName.setError(getString(R.string.require));
            return false;
        }

        return true;
    }

    private void addDetailItemNote(){
         if (!checkInput()){
             return;
         }

         model.addDetailItemNote(binding.dialogAddCategoryEtName.getText().toString().trim())
                 .enqueue(new Callback<BaseResponse>() {
             @Override
             public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                 if(response.body() != null){
                     if (response.body().getStatus() == Constants.ERROR){
                         binding.dialogAddCategoryTilName.setError(getString(R.string.name_exists));
                          Toast.makeText(getContext(), getString(R.string.add_error),
                                  Toast.LENGTH_LONG).show();
                     }else {
                        Toast.makeText(getContext(), getString(R.string.add_successfully),
                                Toast.LENGTH_LONG).show();
                        model.refreshLiveData();
                        dismiss();
                     }
                 }
             }

             @Override
             public void onFailure(Call<BaseResponse> call, Throwable t) {

             }
         });
    }

    private void editDetailItemNote(){
        if(!checkInput()){
            return;
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        model.refreshLiveData();
    }
}
