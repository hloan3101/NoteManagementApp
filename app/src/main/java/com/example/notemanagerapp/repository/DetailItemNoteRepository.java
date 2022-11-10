package com.example.notemanagerapp.repository;

import androidx.lifecycle.LiveData;

import com.example.notemanagerapp.api.API;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.DetailItemNote;
import com.example.notemanagerapp.service.DetailItemNoteService;

import retrofit2.Call;

public class DetailItemNoteRepository {
    private DetailItemNoteService detailItemNoteService;

    public DetailItemNoteRepository() {
        this.detailItemNoteService = getDetailItemNoteService();
    }

    public DetailItemNoteService getDetailItemNoteService() {
        return API.getAccount().create(DetailItemNoteService.class);
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
