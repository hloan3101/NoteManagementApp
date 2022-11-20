package com.example.notemanagerapp.ui.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.DialogAddNoteBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.Note;
import com.example.notemanagerapp.ui.viewmodel.CategoryViewModel;
import com.example.notemanagerapp.ui.viewmodel.NoteViewModel;
import com.example.notemanagerapp.ui.viewmodel.PriorityViewModel;
import com.example.notemanagerapp.ui.viewmodel.StatusViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteDialog extends DialogFragment {

    private final String EDIT = "Edit";
    private final String ADD = "Add";
    private DialogAddNoteBinding binding;
    private CategoryViewModel categoryViewModel;
    private PriorityViewModel priorityViewModel;
    private StatusViewModel statusViewModel;
    private NoteViewModel noteViewModel;

    public static AddNoteDialog newInstance() {
        return new AddNoteDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogAddNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        noteViewModel = new ViewModelProvider(getActivity()).get(NoteViewModel.class);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        priorityViewModel = new ViewModelProvider(getActivity()).get(PriorityViewModel.class);
        statusViewModel = new ViewModelProvider(getActivity()).get(StatusViewModel.class);

        categoryViewModel.refreshLiveData();
        priorityViewModel.refreshLiveData();
        statusViewModel.refreshLiveData();
        noteViewModel.refreshData();

        initView();


        binding.dialogAddNoteBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.dialogAddNoteBtnAdd.getText().toString().equals(EDIT)){
                    editNote();
                }else {
                    addNote();
                }
            }
        });

        binding.dialogAddNoteBtnChoosePlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePlanDate();
            }
        });
        binding.dialogAddNoteBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    private void initView() {
        binding.dialogAddNoteBtnAdd.setText(getArguments().getString(getString(R.string.check_edit)));

        if (binding.dialogAddNoteBtnAdd.getText().toString().equals(EDIT)) {
            binding.dialogAddNoteEtName.setText(getArguments().
                    getString(getString(R.string.name_note)));
            binding.dialogAddNoteTvPlaneDate.setText(getArguments().
                    getString(getString(R.string.plan_date)));
        }
        setSpinner();
    }

    private void setSpinner() {

        final List<String> nameCategoryList = new ArrayList<>();
        final List<String> namePriorityList = new ArrayList<>();
        final List<String> nameStatusList = new ArrayList<>();

        categoryViewModel.getListCategoryItem().observe(getViewLifecycleOwner(), categoryList -> {
            for (int i = 0; i < categoryList.size(); i++) {
                nameCategoryList.add(categoryList.get(i).getName());
                binding.dialogAddAutoCompleteCategory.setText(nameCategoryList.get(0), false);
            }
        });

        priorityViewModel.getListPriorityItem().observe(getViewLifecycleOwner(), priorityList -> {
            for (int i = 0; i < priorityList.size(); i++) {
                namePriorityList.add(priorityList.get(i).getName());
                binding.dialogAddAutoCompletePriority.setText(namePriorityList.get(0), false);
            }
        });

        statusViewModel.getListPriorityItem().observe(getViewLifecycleOwner(), statusList -> {
            for (int i = 0; i < statusList.size(); i++) {
                nameStatusList.add(statusList.get(i).getName());
                binding.dialogAddAutoCompleteStatus.setText(nameStatusList.get(0), false);
            }
        });

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item, nameCategoryList);
        binding.dialogAddAutoCompleteCategory.setAdapter(adapterCategory);

        ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item, namePriorityList);
        binding.dialogAddAutoCompletePriority.setAdapter(adapterPriority);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getContext(),
                R.layout.dropdown_item, nameStatusList);
        binding.dialogAddAutoCompleteStatus.setAdapter(adapterStatus);
    }

    private void choosePlanDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                binding.dialogAddNoteTvPlaneDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day).show();
    }

    private boolean checkInput() {
        binding.dialogAddNoteTilName.setError(null);
        binding.dialogAddNoteTvPlaneDate.setError(null);

        if (TextUtils.isEmpty(binding.dialogAddNoteEtName.getText().toString().trim())) {
            binding.dialogAddNoteTilName.setError(getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.dialogAddNoteTvPlaneDate.getText().toString().trim())) {
            binding.dialogAddNoteTvPlaneDate.setError(getString(R.string.require));
            return false;
        }

        return true;
    }

    private Note initNote() {
        return new Note(binding.dialogAddNoteEtName.getText().toString(),
                binding.dialogAddAutoCompleteCategory.getText().toString(),
                binding.dialogAddAutoCompletePriority.getText().toString(),
                binding.dialogAddAutoCompleteStatus.getText().toString(),
                binding.dialogAddNoteTvPlaneDate.getText().toString());
    }

    private void addNote (){
        if (!checkInput()){
            return;
        }

        Note note = initNote();

        noteViewModel.addNote(note).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus() == Constants.SUCCESSFUL){
                        Toast.makeText(getContext(), getString(R.string.add_successfully),
                                Toast.LENGTH_LONG).show();
                        noteViewModel.refreshData();
                        dismiss();
                    }else{
                        binding.dialogAddNoteEtName.setError(getString(R.string.name_exists));
                        Toast.makeText(getContext(), getString(R.string.add_error),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editNote (){
        if (!checkInput()){
            return;
        }

        Note note = initNote();

        noteViewModel.updateNote(getArguments().getString(getString(R.string.name_note)), note)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (response.body().getStatus() == Constants.SUCCESSFUL){
                                Toast.makeText(getContext(), getString(R.string.edit_successfully),
                                        Toast.LENGTH_SHORT).show();
                                noteViewModel.refreshData();
                                dismiss();
                            }else{
                                Toast.makeText(getContext(), getString(R.string.edit_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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