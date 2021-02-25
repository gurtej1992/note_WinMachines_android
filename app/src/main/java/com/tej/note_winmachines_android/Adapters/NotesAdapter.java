package com.tej.note_winmachines_android.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    private final onNoteClicked mCallback;
    private final Boolean selectedMode = false;
    public RealmResults<Note> data;
    List<Note> filteredNotes;

    public RealmResults<Note> getData() {
        return data;
    }

    public NotesAdapter(Context ct, RealmResults<Note> s1, onNoteClicked listener) {
        context = ct;
        data = s1;
        mCallback = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_cell, parent, false);
        return new NotesViewHolder(view, mCallback);
    }


    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.txtTitle.setText(data.get(position).getNote_title());
        holder.txtDesc.setText(data.get(position).getNote_desc());
//        holder.tvLat.setText("Latitude: " + data.get(position).getLatitude());
//        holder.tvLong.setText("Longitude: " + data.get(position).getLongitude());
        holder.noteCardView.setOnLongClickListener(v -> {
            mCallback.onLongClickItem(v, position);
            // mCallback.onLongClickItem(view, position);
//            ((HomeActivity) context).mapdialog(data.get(position), false);

            return false;
        });
        holder.noteCardView.setOnClickListener(view -> mCallback.onClickItem(view, position));


    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredNotes = data;
                } else {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note n : data) {
                        if (n.getNote_title().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(n);
                        }
                        filteredNotes = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredNotes;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredNotes = (List<Note>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView txtTitle, txtDesc, tvLat, tvLong;
        ImageView noteImg, selectImg;
        onNoteClicked mCallback;
        CardView noteCardView;

        public NotesViewHolder(@NonNull View itemView, onNoteClicked mCallback) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.toolTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            tvLat = itemView.findViewById(R.id.tvLat);
            tvLong = itemView.findViewById(R.id.tvLong);
            selectImg = itemView.findViewById(R.id.imgNoteSelect);
            noteCardView = itemView.findViewById(R.id.noteCardView);
            this.mCallback = mCallback;

        }


        void bind(final Note note) {
            selectImg.setVisibility(note.isSelected() ? View.VISIBLE : View.GONE);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Any One");
            menu.add(getAdapterPosition(), 101, 0, "Get Navigation");
            menu.add(getAdapterPosition(), 102, 1, "Cancel");

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
