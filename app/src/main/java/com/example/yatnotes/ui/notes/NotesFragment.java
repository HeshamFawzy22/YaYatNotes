package com.example.yatnotes.ui.notes;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yatnotes.R;
import com.example.yatnotes.adapter.NotesAdapter;
import com.example.yatnotes.databinding.FragmentNotesBinding;
import com.example.yatnotes.model.Note;
import com.example.yatnotes.adapter.NoteClickListener;

import java.util.List;

public class NotesFragment extends Fragment implements NoteClickListener {
    private FragmentNotesBinding binding;
    private NotesViewModel notesViewModel;
    private int courseId = 0;
    Bundle bundle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);

        notesViewModel = new ViewModelProvider(this,
                        new NotesViewModelFactory(requireActivity().getApplication()))
                        .get(NotesViewModel.class);
        if (getArguments() != null) {
            courseId = requireArguments().getInt(getString(R.string.course_id), 0);
//            requireActivity().setTitle(getString(R.string.course_notes));
            loadCourseNotes(courseId);
        } else {
//            requireActivity().setTitle(getString(R.string.all_notes));
            loadAllNotes();
        }

        binding.fabNotes.setOnClickListener(v ->
                Navigation.findNavController(binding.getRoot()).
                        navigate(R.id.action_nav_notes_to_addEditNoteFragment));

        return binding.getRoot();
    }

    private void loadCourseNotes(int courseId) {
        notesViewModel.getCourseNotes(courseId).observe(getViewLifecycleOwner(), notes -> {
            if (notes.size() > 0) {
                initRecyclerView(notes);
            } else {
                binding.notesRecyclerView.setVisibility(View.GONE);
                binding.noNotesTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadAllNotes() {
        try {
            notesViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
                if (notes.size() > 0) {
                    initRecyclerView(notes);
                } else {
                    binding.notesRecyclerView.setVisibility(View.GONE);
                    binding.noNotesTextView.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView(List<Note> notes) {
        try {
            RecyclerView recyclerView = binding.notesRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setHasFixedSize(true);
            NotesAdapter adapter = new NotesAdapter(this);
            adapter.setNotesList(notes);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            binding.noNotesTextView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(int id) {
        bundle = new Bundle();
        bundle.putInt(getString(R.string.note_id), id);
        bundle.putBoolean(getString(R.string.is_edit), true);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_nav_notes_to_addEditNoteFragment, bundle);
    }

    @Override
    public void onDelete(Note note) {
        try {
            showMessage(R.string.delete_note_confirmation ,
                    R.string.yes , R.string.No ,
                    (dialogInterface, i) -> deleteNote(note), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteNote(Note note) {
        try {
            notesViewModel.deleteNote(note)
                    .observe(getViewLifecycleOwner(), isDeleted -> {
                        if (isDeleted) {
                            if (courseId == 0) {
                                loadAllNotes();
                            }else {
                                loadCourseNotes(courseId);
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    getString(R.string.failed_to_delete_note) + note.getTitle(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
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