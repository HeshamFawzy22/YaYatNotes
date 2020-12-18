package com.example.yatnotes.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yatnotes.R;
import com.example.yatnotes.adapter.NotesRecyclerAdapter;
import com.example.yatnotes.model.Note;
import com.example.yatnotes.viewmodel.NotesViewModel;
import com.example.yatnotes.viewmodel.NotesViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListFragment extends Fragment implements NotesRecyclerAdapter.OnItemClickListener {
    //ui
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ConstraintLayout constraintLayout;

    //declare
    public static List<Note> notesList;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private NotesViewModel notesViewModel;


    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        initRecyclerView();

        loadNotes();

        swipeToDeleteAndUndo();

        fab.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_notesListFragment_to_addNoteFragment);
        });

    }


    private void loadNotes() {
        notesViewModel = new ViewModelProvider(getActivity(),
                new NotesViewModelFactory(getActivity().getApplication()))
                .get(NotesViewModel.class);
        notesViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            setRecyclerViewAdapter(notes);
            notesList = notes;
        });
    }

    private void initRecyclerView() {
        notesRecyclerAdapter = new NotesRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void setRecyclerViewAdapter(List<Note> notes) {
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        notesRecyclerAdapter.setNotesList(notes);
        recyclerView.setAdapter(notesRecyclerAdapter);
    }

    private void initView(@NonNull View view) {
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);
        constraintLayout = view.findViewById(R.id.constraint_layout);
    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private void swipeToDeleteAndUndo() {
        // swipe to remove item from recyclerView
        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteNoteAndUndoDelete(viewHolder);
            }
        };
    }

    private void deleteNoteAndUndoDelete(@NonNull RecyclerView.ViewHolder viewHolder) {
        Note note = notesList.remove(viewHolder.getAdapterPosition());
        notesViewModel.deleteNote(note).observe(getViewLifecycleOwner() , isDeleted -> {
            if (isDeleted){
                notesRecyclerAdapter.notifyDataSetChanged();
                // coordinatorLayout (root)
                Snackbar.make(constraintLayout,"Item was removed from the list.",Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notesList.add(note);
                                notesViewModel.addNote(note).observe(getViewLifecycleOwner() , isAdded -> {
                                    if (isAdded){
                                        notesRecyclerAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).show();
            }
        });
    }


    @Override
    public void onItemClick(int id, String title, String content, String time) {
        NotesListFragmentDirections.ActionNotesListFragmentToAddNoteFragment action =
                NotesListFragmentDirections.actionNotesListFragmentToAddNoteFragment();
        action.setId(id);
        action.setTitle(title);
        action.setContent(content);
        action.setTime(time);
        action.setIsEdit(true);
        navController.navigate(action);
    }
}