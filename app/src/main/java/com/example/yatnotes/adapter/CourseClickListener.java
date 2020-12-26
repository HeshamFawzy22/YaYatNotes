package com.example.yatnotes.adapter;

import com.example.yatnotes.model.Course;

public interface CourseClickListener {

    void onClick(int id);

    void onUpdate(int id);

    void onDelete(Course course);
}
