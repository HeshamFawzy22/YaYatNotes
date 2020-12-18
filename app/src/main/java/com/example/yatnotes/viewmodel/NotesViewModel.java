package com.example.yatnotes.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import com.example.yatnotes.database.MyDataBase;
import com.example.yatnotes.model.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NotesViewModel extends AndroidViewModel {
    private static MyDataBase dataBase;
    private static Note currentNote;
    private static MutableLiveData<Boolean> isAddedLiveData = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isUpdatedLiveData = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isDeletedLiveData = new MutableLiveData<>();
    private static MutableLiveData<List<Note>> noteListLiveData = new MutableLiveData<>();
    private static MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
    public NotesViewModel(@NonNull Application application) {
        super(application);
        dataBase = MyDataBase.getInstance(application.getApplicationContext());
    }

    public LiveData<Boolean> addNote(Note note){
        new AddNote().execute(note.getTitle() , note.getContent() , note.getTime());
        return isAddedLiveData;
    }

    public LiveData<Boolean> updateNote(Note note){
        currentNote = note;
        new UpdateNote().execute();
        return isUpdatedLiveData;
    }

    public LiveData<Boolean> deleteNote(Note note){
        new DeleteNote().execute(note);
        return isDeletedLiveData;
    }

    public LiveData<List<Note>> getAllNotes(){
        new GetAllNotes().execute();
        return noteListLiveData;
    }

    public LiveData<Note> findNote(int id){
        new FindNote().execute(id);
        return noteLiveData;
    }

    private static class AddNote extends AsyncTask<String , Void , Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isAddedLiveData.setValue(false);
        }

        @Override
        protected Void doInBackground(String... strings) {
            Note note = new Note(strings[0] , strings [1] , strings[2]);
            dataBase.notesDao().addNote(note);
            isAddedLiveData.postValue(true);
            return null;
        }
    }

    private static class UpdateNote extends AsyncTask<Void , Void , Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isUpdatedLiveData.setValue(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataBase.notesDao().updateNote(currentNote);
            isUpdatedLiveData.postValue(true);
            return null;
        }
    }

    private static class DeleteNote extends AsyncTask<Note , Void , Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDeletedLiveData.setValue(false);
        }

        @Override
        protected Void doInBackground(Note... notes) {
            dataBase.notesDao().deleteNote(notes[0]);
            isDeletedLiveData.postValue(true);
            return null;
        }
    }

    private static class GetAllNotes extends AsyncTask<Void , Void , Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            noteListLiveData.postValue(dataBase.notesDao().getAllNotes());
            return null;
        }
    }

    private static class FindNote extends AsyncTask<Integer , Void , Void>{

        @Override
        protected Void doInBackground(Integer... integers) {
            noteLiveData.postValue(dataBase.notesDao().searchNoteById(integers[0]));
            return null;
        }
    }

}
