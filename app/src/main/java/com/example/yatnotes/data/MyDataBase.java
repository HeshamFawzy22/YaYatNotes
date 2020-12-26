package com.example.yatnotes.data;

import android.content.Context;

import com.example.yatnotes.data.dao.CoursesDao;
import com.example.yatnotes.data.dao.NotesDao;
import com.example.yatnotes.model.Course;
import com.example.yatnotes.model.Note;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class, Note.class}, version = 1, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    private final static String DATABASE_NAME = "notesDatabase";

    private static MyDataBase myDataBase;
    public abstract CoursesDao coursesDao();

    public abstract NotesDao notesDao();

    public static MyDataBase getInstance(Context context){
        if (myDataBase == null){
            myDataBase = Room.databaseBuilder(context , MyDataBase.class , DATABASE_NAME)
                    .build();
        }
        return myDataBase;
    }
}
