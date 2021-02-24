package com.tej.note_winmachines_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.Model.SubjectModel;
import com.tej.note_winmachines_android.R;

import io.realm.RealmResults;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.VH> {

    Context context;
    public RealmResults<SubjectModel> data;
    com.tej.note_winmachines_android.Adapters.onNoteClicked onNoteClicked;

    public RealmResults<SubjectModel> getData() {
        return data;
    }

    public SubjectsAdapter(Context ct, RealmResults<SubjectModel> s1, com.tej.note_winmachines_android.Adapters.onNoteClicked onNoteClicked) {
        context = ct;
        data = s1;
        this.onNoteClicked = onNoteClicked;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cateogy_item, parent, false);
        return new VH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvSubject.setText("Name: " + data.get(position).getSubjectName());
        holder.tvDate.setText("Date: " + data.get(position).getDate().toString());
        RealmResults<Note> notes = DBAccess.fetchNotesWhereSubId(data.get(position).getSubId());
        holder.tvTotalNotes.setText("Total Notes: " + notes.size());
        holder.cvSubject.setOnClickListener(view -> {
            onNoteClicked.onClickItem(view, position);
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VH extends RecyclerView.ViewHolder {

        TextView tvSubject, tvDate, tvTotalNotes;
        CardView cvSubject;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalNotes = itemView.findViewById(R.id.tvTotalNotes);
            cvSubject = itemView.findViewById(R.id.cvSubject);


        }

    }


}
