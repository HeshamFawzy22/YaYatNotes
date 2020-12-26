package com.example.yatnotes.ui.courses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yatnotes.R;
import com.example.yatnotes.adapter.CoursesAdapter;
import com.example.yatnotes.databinding.FragmentCoursesBinding;
import com.example.yatnotes.model.Course;
import com.example.yatnotes.adapter.CourseClickListener;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesFragment extends Fragment implements CourseClickListener {
    //declare
    private FragmentCoursesBinding binding;
    private CoursesViewModel coursesViewModel;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoursesBinding.inflate(inflater , container , false);
        coursesViewModel = new ViewModelProvider(this,
                new CoursesViewModelFactory(requireActivity().getApplication()))
                .get(CoursesViewModel.class);
        binding.fabCourses.setOnClickListener(view -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_nav_courses_to_addEditCourseFragment);
        });

        loadCourses();

        return binding.getRoot();
    }
    private void loadCourses() {
        try {
            coursesViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
                if (courses.size() > 0) {
                    initRecyclerView(courses);
                } else {
                    binding.coursesRecyclerView.setVisibility(View.GONE);
                    binding.noCoursesTextView.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView(List<Course> courses) {
        try {
            RecyclerView recyclerView = binding.coursesRecyclerView;
            CoursesAdapter coursesAdapter = new CoursesAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setHasFixedSize(true);
            coursesAdapter.setCoursesList(courses);
            recyclerView.setAdapter(coursesAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            binding.noCoursesTextView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(int id) {
        try {
            bundle = new Bundle();
            bundle.putInt(getString(R.string.course_id), id);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_nav_courses_to_nav_notes, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(int id) {
        try {
            bundle = new Bundle();
            bundle.putInt(getString(R.string.course_id), id);
            bundle.putBoolean(getString(R.string.is_edit), true);
            Navigation.findNavController(binding.getRoot()).
                    navigate(R.id.action_nav_courses_to_addEditCourseFragment , bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDelete(Course course) {
        try {
            showMessage(R.string.delete_course_confirmation ,
                    R.string.yes , R.string.No ,
                    (dialogInterface, i) -> deleteCourse(course), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse(Course course) {
        coursesViewModel.deleteCourse(course).observe(getViewLifecycleOwner() , isDeleted -> {
            if (isDeleted){
                loadCourses();
            }else {
                Toast.makeText(requireContext(),
                        getString(R.string.failed_to_delete_course), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showMessage(int message ,
                            int positiveMsg , int negativeMsg ,
                            DialogInterface.OnClickListener onClickListenerPositive,
                            DialogInterface.OnClickListener onClickListenerNegative){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton(negativeMsg,onClickListenerNegative)
                .setNegativeButton(positiveMsg,onClickListenerPositive)
                .setTitle("Warning!")
                .setCancelable(false);
        builder.show();
    }
}