package com.example.notemanagerapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.DetailItemNote;
import com.example.notemanagerapp.repository.DetailItemNoteRepository;

import java.util.List;

import retrofit2.Call;

public class CategoryViewModel extends AndroidViewModel {
    private DetailItemNoteRepository repository;
    private RefreshLiveData<List<DetailItemNote>> liveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new DetailItemNoteRepository();
        liveData = repository.getListDetailItemNote(Constants.TAB_CATEGORY);
    }

    public LiveData<List<DetailItemNote>> getListCategoryItem (){
        return liveData;
    }

    public Call<BaseResponse> addCategoryItem(String name){
        return repository.addDetailItemNote(Constants.TAB_CATEGORY, name);
    }

    public Call<BaseResponse> updateCategoryItem (String name, String newName){
        return repository.updateDetailItemNote(Constants.TAB_CATEGORY, name, newName);
    }

    public Call<BaseResponse> deleteCategoryItem (DetailItemNote detailItemNote){
        return repository.deleteDetailItemNote(Constants.TAB_CATEGORY, detailItemNote);
    }

    public void refreshLiveData (){
        liveData.refresh();
    }
}