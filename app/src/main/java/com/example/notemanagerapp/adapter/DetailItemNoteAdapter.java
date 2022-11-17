package com.example.notemanagerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.model.DetailItemNote;

import java.util.List;


public class DetailItemNoteAdapter extends RecyclerView.Adapter<DetailItemNoteAdapter.ViewHolder> {
    private Context context;
    private List<DetailItemNote> detailItemNoteList;

    public DetailItemNoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_detail_note, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailItemNote detailItemNote = detailItemNoteList.get(position);
        if (detailItemNote == null){
            return;
        }

        holder.tvName.setText("Name: " + detailItemNote.getName());
        holder.tvCreatedDate.setText("Created date: "+detailItemNote.getDateCreate());
    }

    @Override
    public int getItemCount() {
        if (detailItemNoteList != null){
            return detailItemNoteList.size();
        }
        return 0;
    }

    public List<DetailItemNote> getDetailItemNoteList() {
        return detailItemNoteList;
    }

    public void setDetailItemNoteList(List<DetailItemNote> detailItemNoteList) {
        this.detailItemNoteList = detailItemNoteList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvCreatedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.item_detail_note_tv_name);
            tvCreatedDate = itemView.findViewById(R.id.item_detail_note_tv_created_date);
        }
    }
}
