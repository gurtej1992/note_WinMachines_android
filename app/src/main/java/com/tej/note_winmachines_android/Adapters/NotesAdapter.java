package com.tej.note_winmachines_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tej.note_winmachines_android.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    String[] data;

    public  NotesAdapter(Context ct, String[] s1) {
        context = ct;
        data = s1;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_cell,parent,false);

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.txtTitle.setText(data[position]);
        holder.txtDesc.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDesc;
        ImageView noteImg;
         public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
             txtTitle = itemView.findViewById(R.id.txtTitle);
             txtDesc = itemView.findViewById(R.id.txtDesc);
        }
    }
}
