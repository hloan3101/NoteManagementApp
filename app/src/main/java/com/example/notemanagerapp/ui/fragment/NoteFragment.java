package com.example.notemanagerapp.ui.fragment;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.adapter.NoteAdapter;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.DialogAddNoteBinding;
import com.example.notemanagerapp.databinding.FragmentNoteBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.Note;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;
import com.example.notemanagerapp.ui.dialog.AddNoteDialog;
import com.example.notemanagerapp.ui.viewmodel.NoteViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteFragment extends Fragment {

    private NoteAdapter adapter;
    private NoteViewModel viewModel;
    private FragmentNoteBinding binding;
    private Note note;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.fragmentNoteRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(this.getContext());
        binding.fragmentNoteRv.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(NoteViewModel.class);
        viewModel.getNoteList().observe(getViewLifecycleOwner(), notes -> {
            adapter.setNoteList(notes);
        });

        note = new Note();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<Note> notes = adapter.getNoteList();
                note = notes.get(position);

                if (direction == ItemTouchHelper.RIGHT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle(getString(R.string.title_dialog));
                    builder.setMessage(getString(R.string.massage_dialog));
                    builder.setCancelable(false);

                    builder.setPositiveButton(getString(R.string.yes_dialog),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModel.deleteNote(note.getName()).enqueue(
                                    new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call,
                                                       Response<BaseResponse> response) {
                                    if(response.body() != null){
                                        deleteNote(response.body());
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    Toast.makeText(requireContext(), t.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                    builder.setNegativeButton(getString(R.string.no_dialog),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModel.refreshData();
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    callAddNoteDialog(getString(R.string.edit));
                    viewModel.refreshData();
                }
            }
        }).attachToRecyclerView(binding.fragmentNoteRv);

        binding.fragmentNoteBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddNoteDialog(getString(R.string.add));
                viewModel.refreshData();
            }
        });

        return view;
    }

    private void deleteNote(BaseResponse response){
        if(response.getStatus() == Constants.SUCCESSFUL){
            Toast.makeText(getContext(), getString(R.string.delete_successfully),
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), getString(R.string.delete_error), Toast.LENGTH_LONG).show();
        }
    }

    private void callAddNoteDialog (String str){
        DialogFragment dialogFragment = AddNoteDialog.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.check_edit), str);
        bundle.putString(getString(R.string.name_note), note.getName());
        bundle.putString(getString(R.string.plan_date), note.getPlanDate());

        dialogFragment.setArguments(bundle);

        dialogFragment.show(getActivity().getSupportFragmentManager(), "AddNoteDialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshData();
    }
}