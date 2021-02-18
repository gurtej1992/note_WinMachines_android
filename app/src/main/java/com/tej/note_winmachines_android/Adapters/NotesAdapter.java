package com.tej.note_winmachines_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import io.realm.RealmResults;
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{

    Context context;
    private onNoteClicked mCallback;
    RealmResults<Note> data;
    public  NotesAdapter(Context ct, RealmResults<Note> s1,onNoteClicked listener) {
        context = ct;
        data = s1;
        mCallback = listener;
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
        holder.txtTitle.setText(data.get(position).getNote_title());
        holder.txtDesc.setText(data.get(position).getNote_desc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDesc;
        ImageView noteImg;
         public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
             txtTitle = itemView.findViewById(R.id.toolTitle);
             txtDesc = itemView.findViewById(R.id.txtDesc);
        }
    }

}
