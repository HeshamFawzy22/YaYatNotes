package com.example.yatnotes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.example.yatnotes.databinding.NotesListItemBinding;
import com.example.yatnotes.model.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> notes;
    private final NoteClickListener onNoteClickListener;

    public NotesAdapter(NoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NotesListItemBinding binding = NotesListItemBinding.inflate(inflater , parent , false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotesList(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private final NotesListItemBinding binding;

        public ViewHolder(@NonNull NotesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Note note) {
            binding.titleTextView.setText(note.getTitle());
            binding.contentTextView.setText(note.getContent());
            binding.updateButton.setOnClickListener(v -> onNoteClickListener.onUpdate(note.getId()));
            binding.deleteButton.setOnClickListener(v -> onNoteClickListener.onDelete(note));
        }

    }

}
