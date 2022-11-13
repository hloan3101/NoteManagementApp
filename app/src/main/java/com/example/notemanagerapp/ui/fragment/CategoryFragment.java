package com.example.notemanagerapp.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

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

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.adapter.DetailItemNoteAdapter;
import com.example.notemanagerapp.databinding.FragmentCategoryBinding;
import com.example.notemanagerapp.model.DetailItemNote;
import com.example.notemanagerapp.ui.viewmodel.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {

    private CategoryViewModel viewModel;
    private DetailItemNoteAdapter adapter;
    private FragmentCategoryBinding binding;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.fragmentCategoryRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DetailItemNoteAdapter(this.getContext());
         binding.fragmentCategoryRv.setAdapter(adapter);

         viewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
         viewModel.getListDetailItemNote().observe(getViewLifecycleOwner(), categories -> {
             adapter.setDetailItemNoteList(categories);
         });

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
                 List<DetailItemNote> detailItemNoteList = adapter.getDetailItemNoteList();
                 DetailItemNote category = detailItemNoteList.get(position);

                 if (direction == ItemTouchHelper.RIGHT){

                 }else {

                 }
             }
         }).attachToRecyclerView(binding.fragmentCategoryRv);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshLiveData();
    }
}