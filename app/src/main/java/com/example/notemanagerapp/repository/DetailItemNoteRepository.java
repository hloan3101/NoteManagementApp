package com.example.notemanagerapp.repository;

import com.example.notemanagerapp.api.API;
import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.DetailItemNote;
import com.example.notemanagerapp.service.DetailItemNoteService;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailItemNoteRepository {
    private DetailItemNoteService detailItemNoteService;

    public DetailItemNoteRepository() {
        this.detailItemNoteService = getDetailItemNoteService();
    }

    private DetailItemNoteService getDetailItemNoteService() {
        return API.getAccount().create(DetailItemNoteService.class);
    }

    public RefreshLiveData<List<DetailItemNote>> getListDetailItemNote (String tab){
        RefreshLiveData<List<DetailItemNote>> data = new RefreshLiveData<>((callback -> {
            detailItemNoteService.getDetailItemNote(tab, DataLocalManager.getEmail()).enqueue(
                    new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            BaseResponse baseResponse = response.body();

                            if (baseResponse != null){
                                List<DetailItemNote> detailItemNoteList = new ArrayList<>();

                                for (List<String> detailItemNote : baseResponse.getData()){
                                    detailItemNoteList.add(new DetailItemNote(detailItemNote.get(0),
                                            detailItemNote.get(2),detailItemNote.get(1)));

                                }
                                callback.onDataLoaded(detailItemNoteList);

                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    }
            );
        }));
        return data;
    }

    public Call<BaseResponse> addDetailItemNote (String tab, DetailItemNote detailItemNote){
        return detailItemNoteService.addDetailItemNote(tab, detailItemNote.getEmail(),
                detailItemNote.getName());
    }

    public Call<BaseResponse> updateDetailItemNote(String tab, DetailItemNote detailItemNote, String newName){
        return detailItemNoteService.updateDetailItemNote(tab, detailItemNote.getEmail(),
                detailItemNote.getName(), newName);
    }

    public Call<BaseResponse> deleteDetailItemNote(String tab, DetailItemNote detailItemNote){
        return detailItemNoteService.deleteDetailItemNote(tab, detailItemNote.getEmail(),
                detailItemNote.getName());
    }
}
