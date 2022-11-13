package com.example.notemanagerapp.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.notemanagerapp.databinding.DialogAddCategoryBinding;
import com.example.notemanagerapp.ui.viewmodel.CategoryViewModel;


public class AddCategoryDialog extends DialogFragment {
    private final String EDIT = "Edit";
    private final String ADD = "Add";
    private DialogAddCategoryBinding binding;
    private CategoryViewModel model;

    public static AddCategoryDialog newInstance (){
        return new AddCategoryDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setCancelable(false);

        return view;
    }

    private void initView(){

    }
}
