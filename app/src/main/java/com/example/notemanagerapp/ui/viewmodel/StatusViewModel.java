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

public class StatusViewModel extends AndroidViewModel {
    private DetailItemNoteRepository repository;
    private RefreshLiveData<List<DetailItemNote>> liveData;

    public StatusViewModel(@NonNull Application application) {
        super(application);
        repository = new DetailItemNoteRepository();
        liveData = repository.getListDetailItemNote(Constants.TAB_STATUS);
    }

    public LiveData<List<DetailItemNote>> getListPriorityItem (){
        return liveData;
    }

    public Call<BaseResponse> addStatusItem(String name){
        return repository.addDetailItemNote(Constants.TAB_STATUS, name);
    }

    public Call<BaseResponse> updateStatusItem (String name, String newName){
        return repository.updateDetailItemNote(Constants.TAB_STATUS, name, newName);
    }

    public Call<BaseResponse> deleteStatusItem (DetailItemNote detailItemNote){
        return repository.deleteDetailItemNote(Constants.TAB_STATUS, detailItemNote);
    }

    public void refreshLiveData (){
        liveData.refresh();
    }
}