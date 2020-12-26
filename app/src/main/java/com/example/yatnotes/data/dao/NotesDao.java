package com.example.yatnotes.data.dao;

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
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes WHERE id = :id")
    Note findNoteById(int id);

    @Query("SELECT * FROM notes WHERE 1")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE courseId = :courseId")
    List<Note> getCourseNotes(int courseId);
}
