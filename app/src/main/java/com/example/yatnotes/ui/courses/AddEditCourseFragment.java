package com.example.yatnotes.ui.courses;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yatnotes.R;
import com.example.yatnotes.databinding.FragmentAddEditCourseBinding;
import com.example.yatnotes.model.Course;

public class AddEditCourseFragment extends Fragment {

    private FragmentAddEditCourseBinding binding;
    private CoursesViewModel coursesViewModel;
    private int id;
    private boolean isEdit;
    private Course currentCourse;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEditCourseBinding.inflate(inflater, container, false);
        try {
            coursesViewModel = new ViewModelProvider(this,
                            new CoursesViewModelFactory(requireActivity().getApplication()))
                            .get(CoursesViewModel.class);


            requireActivity().setTitle(getString(R.string.new_course));

            if (getArguments() != null) {
                id = requireArguments().getInt(getString(R.string.course_id), 0);
                isEdit = requireArguments().getBoolean(getString(R.string.is_edit), false);
            }

            if (isEdit && id > 0) {
                requireActivity().setTitle(getString(R.string.update_course));
                loadCurrentCourse(id);
            }

            binding.btnAddCourse.setOnClickListener(v -> {
                if (isEdit) updateCourse();
                else addCourse();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    private void addCourse() {
        try {
            Course course = new Course(binding.nameEditText.getText().toString(),
                    binding.descriptionEditText.getText().toString());
            coursesViewModel.addCourse(course).observe(getViewLifecycleOwner(), isAdded -> {
                if (isAdded) {
                    showMessage(R.string.course_added_successfully, R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_addEditCourseFragment_to_nav_courses);
                    },false);
                } else {
                    Toast.makeText(requireContext(),
                            getString(R.string.failed_to_add_course),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCourse() {
        try {
            currentCourse.setName(binding.nameEditText.getText().toString());
            currentCourse.setDescription(binding.descriptionEditText.getText().toString());
            coursesViewModel.updateCourse(currentCourse).observe(getViewLifecycleOwner(), isUpdated -> {
                if (isUpdated) {
                    showMessage(R.string.course_updated_successfully, R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_addEditCourseFragment_to_nav_courses);
                    },false);
                    binding.btnAddCourse.setText(R.string.add);
                } else {
                    Toast.makeText(requireContext(),
                            getString(R.string.failed_to_update_course),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCurrentCourse(int id) {
        try {
            binding.btnAddCourse.setText(R.string.update);
            coursesViewModel.findCourseById(id).observe(getViewLifecycleOwner(), course -> {
                currentCourse = course;
                binding.nameEditText.setText(course.getName());
                binding.descriptionEditText.setText(course.getDescription());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMessage(int message, int posActionName,
                            DialogInterface.OnClickListener onClickListener,
                            boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(requireContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener);
        builder.setCancelable(isCancelable);
        builder.show();
    }
}