package com.example.yatnotes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yatnotes.R;
import com.example.yatnotes.model.Note;
import com.example.yatnotes.viewmodel.NotesViewModel;
import com.example.yatnotes.viewmodel.NotesViewModelFactory;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initView();
        loadData();
    }


    private void loadData() {
        oldId = getIntent().getIntExtra("oldId" , -1);
        oldTitle = getIntent().getStringExtra("oldTitle");
        oldContent = getIntent().getStringExtra("oldContent");
        oldTime = getIntent().getStringExtra("oldTime");
        isEdit = getIntent().getBooleanExtra("isEdit" , false);

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

        if (validatedForm()){
            Note note = new Note(titleS,contentS,choosedTime);
            notesViewModel.addNote(note).observe(this, isAdded -> {
                if(isAdded) {
                    showMessage(R.string.note_added_successfully, R.string.ok, (dialog, i) -> {
                        dialog.dismiss();
                        finish();
                    }, false);

                }
            });

        }
    }

    private void updateNote() {
        String newTitle , newContent;
        newTitle = etTitle.getText().toString();
        newContent = etContent.getText().toString();
        if (validatedForm()) {
            Note currentNote = new Note(oldId, newTitle, newContent, choosedTime);
//            currentNote.setId(oldId);
//            currentNote.setTitle(etTitle.getText().toString());
//            currentNote.setContent(etContent.getText().toString());
//            currentNote.setTime(tvTime.getText().toString());
            notesViewModel.updateNote(currentNote).observe(this, isUpdated -> {
                if (isUpdated) {
                    showMessage(R.string.updated, R.string.ok, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    },false);

                }
            });
            isEdit = false;
            addNoteBtn.setText("Add");
        }
    }

    private void addNoteDate() {
        // current hour and minute
        Calendar calendar = Calendar.getInstance();
        // TimePickerDialog to choose Note Date
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            // after time choose
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay + ":" + minute);
                choosedTime = hourOfDay + ":" + minute;
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void initView() {
        etTitle = findViewById(R.id.title);
        etContent = findViewById(R.id.content);
        tvTime = findViewById(R.id.time);
        addNoteBtn = findViewById(R.id.addNote);
        tvTime.setOnClickListener(this);
        addNoteBtn.setOnClickListener(this);

        notesViewModel = new ViewModelProvider(this ,
                new NotesViewModelFactory(getApplication()))
                .get(NotesViewModel.class);
    }

    boolean full;
    public boolean validatedForm(){
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
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, onClickListener);
        builder.setCancelable(isCancelable);
        builder.show();
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