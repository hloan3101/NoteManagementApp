package com.example.notemanagerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view =layoutInflater.inflate(R.layout.item_note, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        if (note == null){
            return;
        }

        holder.tvName.setText(holder.tvName.getText().toString() + ": " + note.getName());
        holder.tvCategory.setText(holder.tvCategory.getText().toString() + ": "+ note.getCategory());
        holder.tvPriority.setText(holder.tvPriority.getText().toString() + ": "+ note.getPriority());
        holder.tvStatus.setText(holder.tvStatus.getText().toString() + ": " + note.getStatus());
        holder.tvPlanDate.setText(holder.tvPlanDate.getText().toString() + ": " + note.getPlanDate());
        holder.tvCreatedDate.setText(holder.tvCreatedDate.getText().toString()
                + ": "+ note.getCreatedDate());
    }

    @Override
    public int getItemCount() {
        if (noteList != null){
            return noteList.size();
        }

        return 0;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvCategory;
        private TextView tvPriority;
        private TextView tvStatus;
        private TextView tvPlanDate;
        private TextView tvCreatedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.item_note_tv_name);
            tvCategory = itemView.findViewById(R.id.item_note_tv_category);
            tvPriority = itemView.findViewById(R.id.item_note_tv_priority);
            tvStatus = itemView.findViewById(R.id.item_note_tv_status);
            tvPlanDate = itemView.findViewById(R.id.item_note_tv_plane_date);
            tvCreatedDate = itemView.findViewById(R.id.item_note_tv_created_date);
        }
    }
}
