package com.example.yatnotes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.example.yatnotes.databinding.CoursesListItemBinding;
import com.example.yatnotes.model.Course;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private List<Course> courses;
    private final CourseClickListener onCourseClickListener;

    public CoursesAdapter(CourseClickListener onCourseClickListener) {
        this.onCourseClickListener = onCourseClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CoursesListItemBinding binding = CoursesListItemBinding.inflate(inflater , parent , false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCoursesList(List<Course> courses){
        this.courses = courses;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private final CoursesListItemBinding binding;

        public ViewHolder(@NonNull CoursesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Course course) {
            binding.nameTextView.setText(course.getName());
            binding.descriptionTextView.setText(course.getDescription());
            binding.getRoot().setOnClickListener(v -> onCourseClickListener.onClick(course.getId()));
            binding.updateButton.setOnClickListener(v -> onCourseClickListener.onUpdate(course.getId()));
            binding.deleteButton.setOnClickListener(v -> onCourseClickListener.onDelete(course));
        }

    }

}
