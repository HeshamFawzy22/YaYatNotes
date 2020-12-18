package com.example.yatnotes.database;

import com.example.yatnotes.model.Note;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotesDao {

    @Insert
    void addNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("select * from Note where id=:id")
    Note searchNoteById(int id);

    @Query("select * from Note")
    List<Note> getAllNotes();
}
