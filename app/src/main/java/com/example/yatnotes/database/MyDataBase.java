package com.example.yatnotes.database;

import android.content.Context;

import com.example.yatnotes.model.Note;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class} , version = 1 , exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {

    public abstract NotesDao notesDao();
    private static MyDataBase myDataBase;
    private final static String DATABASE_NAME = "notesDatabase";

    public static MyDataBase getInstance(Context context){
        if (myDataBase == null){
            myDataBase = Room.databaseBuilder(context , MyDataBase.class , DATABASE_NAME)
                    .build();
        }
        return myDataBase;
    }
}
