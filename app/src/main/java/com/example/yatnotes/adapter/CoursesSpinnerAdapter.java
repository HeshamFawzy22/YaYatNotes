package com.example.yatnotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yatnotes.model.Course;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoursesSpinnerAdapter extends ArrayAdapter<Course> {
    private List<Course> courses;

    public CoursesSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Course> courses) {
        super(context, resource, courses);
        this.courses = courses;
    }


    @Override
    public int getCount() {
        return courses.size();
    }

    @Nullable
    @Override
    public Course getItem(int position) {
        return courses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setTextSize(25);
        label.setText(getItem(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setTextSize(25);
        label.setText(getItem(position).getName());
        return label;
    }
}
