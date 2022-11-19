package com.example.notemanagerapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.Note;
import com.example.notemanagerapp.repository.NoteRepository;

import java.util.List;

import retrofit2.Call;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private RefreshLiveData<List<Note>> noteList;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        noteRepository = new NoteRepository();
        noteList = noteRepository.getAllNote();
    }

    public LiveData<List<Note>> getNoteList() {
        return noteList;
    }

    public Call<BaseResponse> addNote(Note note){
        return noteRepository.addNote(note);
    }

    public Call<BaseResponse> updateNote(String nameNote, Note note){
        return noteRepository.updateNote(nameNote, note);
    }

    public Call<BaseResponse> deleteNote(String name){
        return noteRepository.deleteNote(name);
    }

    public  void refreshData (){
        noteList.refresh();
    }
}