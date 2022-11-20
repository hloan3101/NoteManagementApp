package com.example.notemanagerapp.repository;

import com.example.notemanagerapp.api.API;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.Note;
import com.example.notemanagerapp.service.NoteService;
import com.example.notemanagerapp.sharedpreferences.DataLocalManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {
    private NoteService noteService;

    public NoteRepository() {
        this.noteService = getNoteService();
    }

    public NoteService getNoteService() {
        return API.getAccount().create(NoteService.class);
    }

    public RefreshLiveData<List<Note>> getAllNote(){
        final RefreshLiveData<List<Note>> liveData = new RefreshLiveData<>(callback -> {
            noteService.getAllNote(Constants.TAB_NOTE, DataLocalManager.getEmail()).enqueue(
                    new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            BaseResponse baseResponse = response.body();

                            if(baseResponse != null){
                                List<Note> notes = new ArrayList<>();

                                for (List<String> note: baseResponse.getData()){
                                    notes.add(new Note(note.get(0), note.get(1), note.get(2),
                                            note.get(3), note.get(4), note.get(6)));
                                }

                                callback.onDataLoaded(notes);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                        }
                    }
            );
        });
        return liveData;
    }

    public Call<BaseResponse> addNote(Note note){
        return noteService.addNote(Constants.TAB_NOTE, DataLocalManager.getEmail(), note.getName(),
                note.getPriority(), note.getCategory(), note.getStatus(), note.getPlanDate());
    }

    public Call<BaseResponse> updateNote(String nameNote, Note note) {
        return noteService.updateNote(Constants.TAB_NOTE, DataLocalManager.getEmail(), nameNote,
                note.getName(), note.getPriority(), note.getCategory(), note.getStatus(),
                note.getPlanDate());
    }

    public Call<BaseResponse> deleteNote(String name) {
        return noteService.deleteNote(Constants.TAB_NOTE, DataLocalManager.getEmail(), name);
    }
}
