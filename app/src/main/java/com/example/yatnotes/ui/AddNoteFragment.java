package com.example.yatnotes.ui;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yatnotes.R;
import com.example.yatnotes.model.Note;
import com.example.yatnotes.viewmodel.NotesViewModel;
import com.example.yatnotes.viewmodel.NotesViewModelFactory;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class AddNoteFragment extends Fragment implements View.OnClickListener {
    //ui
    private EditText etTitle;
    private EditText etContent;
    private TextView tvTime;
    private Button addNoteBtn;

    //Declare
    private String choosedTime = "";
    private String titleS;
    private String contentS;
    private NavController navController;
    private int oldId;
    private String oldTitle;
    private String oldContent;
    private String oldTime;


    private boolean isEdit;


    private NotesViewModel notesViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        loadData();


    }

    private void loadData() {
        oldId = AddNoteFragmentArgs.fromBundle(getArguments()).getId();
        oldTitle = AddNoteFragmentArgs.fromBundle(getArguments()).getTitle();
        oldContent = AddNoteFragmentArgs.fromBundle(getArguments()).getContent();
        oldTime = AddNoteFragmentArgs.fromBundle(getArguments()).getTime();
        isEdit = AddNoteFragmentArgs.fromBundle(getArguments()).getIsEdit();

        if (isEdit){
//            notesViewModel.findNote(oldId).observe(getViewLifecycleOwner(), new Observer<Note>() {
//                @Override
//                public void onChanged(Note note) {
//                    currentNote = note;
//                    tvTime.setText(currentNote.getTime());
//                    etTitle.setText(currentNote.getTitle());
//                    etContent.setText(currentNote.getContent());
//                    addNoteBtn.setText("Update");
//                }
//            });
            tvTime.setText(oldTime);
            etTitle.setText(oldTitle);
            etContent.setText(oldContent);
            addNoteBtn.setText("Update");
        }
    }

    private void addNote() {
        titleS = etTitle.getText().toString();
        contentS = etContent.getText().toString();

        if (ValidatedForm()){
            Note note = new Note(titleS,contentS,choosedTime);
            notesViewModel.addNote(note).observe(getViewLifecycleOwner(), isAdded -> {
                if(isAdded) {
                    showMessage(R.string.note_added_successfully, R.string.ok, (dialog, i) -> {
                        if (i == R.string.ok){
                            dialog.dismiss();
                        }
                    }, false);

                }
            });

        }
    }

    private void updateNote() {
        String newTitle , newContent;
        newTitle = etTitle.getText().toString();
        newContent = etContent.getText().toString();
        Note currentNote = new Note(oldId ,  newTitle , newContent, choosedTime);
//            currentNote.setId(oldId);
//            currentNote.setTitle(etTitle.getText().toString());
//            currentNote.setContent(etContent.getText().toString());
//            currentNote.setTime(tvTime.getText().toString());
        notesViewModel.updateNote(currentNote).observe(getViewLifecycleOwner(), isUpdated -> {
            if (isUpdated){
                showMessage(R.string.updated, R.string.ok, (dialogInterface, i) ->
                        dialogInterface.dismiss(), false);
            }
        });
        isEdit = false;
        addNoteBtn.setText("Add");
    }

    private void addNoteDate() {
        // current hour and minute
        Calendar calendar = Calendar.getInstance();
        // TimePickerDialog to choose Note Date
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            // after time choose
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay + ":" + minute);
                choosedTime = hourOfDay + ":" + minute;
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void initView(@NonNull View view) {
        navController = Navigation.findNavController(view);
        etTitle = view.findViewById(R.id.title);
        etContent = view.findViewById(R.id.content);
        tvTime = view.findViewById(R.id.time);
        addNoteBtn = view.findViewById(R.id.addNote);
        tvTime.setOnClickListener(this);
        addNoteBtn.setOnClickListener(this);

        notesViewModel = new ViewModelProvider(this ,
                new NotesViewModelFactory(getActivity().getApplication()))
                .get(NotesViewModel.class);
    }

    boolean full;
    public boolean ValidatedForm(){
        String titleS = etTitle.getText().toString();
        String contentS = etContent.getText().toString();
        full = false;

        if (titleS.isEmpty()){
            etTitle.setError("required");
            full = false;
        }else {
            etTitle.setError(null);
            full=true;
        }
        if (contentS.isEmpty()){
            etContent.setError("required");
            full = false;
        }else {
            etContent.setError(null);
            full=true;
        }
        if (choosedTime.isEmpty()){
            tvTime.setError("required");
            full = false;
        }else {
            tvTime.setError(null);
            full=true;
        }

        return full;
    }
    public void showMessage(int message, int posActionName,
                            DialogInterface.OnClickListener onClickListener,
                            boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        if (getActivity() != null) {
            builder.setMessage(message);
            builder.setPositiveButton(posActionName, onClickListener);
            builder.setCancelable(isCancelable);
            builder.show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == tvTime){
            addNoteDate();
        }else if (view == addNoteBtn){
            if (isEdit) updateNote();
            else addNote();
        }
    }


}