package com.example.yatnotes.data.dao;

import com.example.yatnotes.model.Course;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CoursesDao {

    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses WHERE id = :id")
    Course findById(int id);

    @Query("SELECT * FROM COURSES WHERE 1")
    List<Course> getAllCourses();
}
