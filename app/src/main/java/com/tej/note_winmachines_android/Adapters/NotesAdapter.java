package com.tej.note_winmachines_android.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tej.note_winmachines_android.Activities.HomeActivity;
import com.tej.note_winmachines_android.Fragments.HomeFragment;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;
import io.realm.RealmResults;
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{

    Context context;
    private onNoteClicked mCallback;
    private Boolean selectedMode = false;
    RealmResults<Note> data;
    List<Note> filteredNotes;
    NotesAdapter adapter;

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
//        holder.itemView.setOnLongClickListener(view -> {
//
//            mCallback.onLongClickItem(view,position);
//            return true;
//        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

               // mCallback.onLongClickItem(view, position);
                ((HomeActivity)context).mapdialog(data.get(position), false);

//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                String opt[] = {"Map","Cancel"};
//
//                builder.setTitle("Navigate");
//                builder.setItems(opt, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                            switch (item)
//                            {
//                                case  R.id.opt[0];
//
//                            }
//                    }
//                });
//                builder.show();
               return false;
            }
        });


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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Any One");
            menu.add(getAdapterPosition(),101,0,"Get Navigation");
            menu.add(getAdapterPosition(),102,1,"Cancel");

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
