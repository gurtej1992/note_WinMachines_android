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

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{

    Context context;
    private onNoteClicked mCallback;
    private Boolean selectedMode = false;
    RealmResults<Note> data;
    static  Realm realm = Realm.getDefaultInstance();
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
        return new NotesViewHolder(view,mCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.txtTitle.setText(data.get(position).getNote_title());
        holder.txtDesc.setText(data.get(position).getNote_desc());
       holder.itemView.setOnClickListener(view -> mCallback.onClickItem(view,position));
        holder.itemView.setOnLongClickListener(view -> {
            mCallback.onLongClickItem(view,position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDesc;
        ImageView noteImg,selectImg;
        onNoteClicked mCallback;
         public NotesViewHolder(@NonNull View itemView , onNoteClicked mCallback) {
            super(itemView);
             txtTitle = itemView.findViewById(R.id.toolTitle);
             txtDesc = itemView.findViewById(R.id.txtDesc);
             selectImg = itemView.findViewById(R.id.imgNoteSelect);
             this.mCallback = mCallback;

        }
        void bind(final Note note) {
            selectImg.setVisibility(note.isSelected() ? View.VISIBLE : View.GONE);
           // textView.setText(note.getName());
//
//            itemView.setOnClickListener(view -> {
//                realm.beginTransaction();
//                note.setSelected(!note.isSelected());
//                realm.commitTransaction();
//                selectImg.setVisibility(note.isSelected() ? View.VISIBLE : View.GONE);
//                mCallback.onLongClickItem(view,position);
//            });
//            itemView.setOnLongClickListener(view -> {
//
//                return true;
//            });
        }
    }

    public ArrayList<Note> getSelected() {
        ArrayList<Note> selected = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelected()) {
                selected.add(data.get(i));
            }
        }
        return selected;
    }

}
