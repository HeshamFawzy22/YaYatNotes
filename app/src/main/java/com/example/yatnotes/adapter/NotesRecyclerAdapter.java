package com.example.yatnotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.yatnotes.R;
import com.example.yatnotes.model.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private List<Note> notes;
    private final OnItemClickListener onItemClickListener;

    public NotesRecyclerAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.setData(note,onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotesList(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title , time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
        }

        public void setData(Note note , OnItemClickListener onItemClickListener){
            String newTitle = note.getTitle();
            String newTime = note.getTime();
            title.setText(newTitle);
            time.setText(newTime);
            itemView.setOnClickListener(view -> {
                onItemClickListener.onItemClick(note.getId() , note.getTitle() , note.getContent() , note.getTime());
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int id, String title, String content, String time);
    }
}
