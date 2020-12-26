package com.example.yatnotes.adapter;


import com.example.yatnotes.model.Note;

public interface NoteClickListener {
    void onUpdate(int Id);

    void onDelete(Note note);
}
