package com.tej.note_winmachines_android.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.tej.note_winmachines_android.Activities.MapsActivity;
import com.tej.note_winmachines_android.Activities.Category;
import com.tej.note_winmachines_android.Adapters.NotesAdapter;
import com.tej.note_winmachines_android.Adapters.onNoteClicked;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import java.util.ArrayList;
import java.util.List;


import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements onNoteClicked {
    Button btnAdd, btnMap, btnSubjects;
    TextView txtTitle;
    ImageView imgSearch;
    ImageView imgCross;
    RecyclerView notesRecycler;
    NotesAdapter adapter;
    EditText etsearch;
    LinearLayout searchLayout;
    Dialog dialog;
    List<Note> listNotes;

    ArrayList<Note> notesObj = new ArrayList<Note>();

    private static final int noteid = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        btnSubjects = rootView.findViewById(R.id.btnSubjects);
        btnMap = rootView.findViewById(R.id.btnmap);
        imgSearch = rootView.findViewById(R.id.rightBarButton);
        imgCross = rootView.findViewById(R.id.leftBarButton);
        txtTitle = rootView.findViewById(R.id.toolTitle);
        notesRecycler = rootView.findViewById(R.id.notesRecycler);
        etsearch = rootView.findViewById(R.id.search_txt);
        searchLayout = rootView.findViewById(R.id.search_layout);
        txtTitle.setText(R.string.notes);

        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });


        imgSearch.setOnClickListener(v -> {
            if(searchLayout.getVisibility() == View.VISIBLE){
                searchLayout.setVisibility(View.GONE);
            }
            else{
                searchLayout.setVisibility(View.VISIBLE);
            }

        });
        btnSubjects.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Category.class);
            startActivity(intent);
        });

        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("allNotes", "yes");
            startActivity(intent);
        });

        adapter = new NotesAdapter(getContext(), DBAccess.fetchNotes(), this);

        notesRecycler.setAdapter(adapter);
        notesRecycler.setLayoutManager((new LinearLayoutManager(this.getContext())));
        // Inflate the layout for this fragment
        return rootView;
    }

   


/*
    private void filter(String text) {

        //RealmResults<Note> filterdata = new ArrayList<Note>();

        for (int i = 0; i < notesList.size(); i++) {

            if (notesList.get(i).getNote_title().toLowerCase().contains(text.toLowerCase()) || notesList.get(i).getNote_title().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdata.add(notesList.get(i));
            }
        }

        notesAdapter = new NotesAdapter(getContext(), filterdata,this);
        notesRecycler.setAdapter(notesAdapter);

    }
*/


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.toNoteDetail));
    }

    @Override
    public void onClickItem(View view, int item) {

        Bundle bundle = new Bundle();
        bundle.putString("item", "" + item);
        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.toNoteDetail, bundle);

    }

    @Override
    public void onLongClickItem(View view, int item) {
        mapDialog(adapter.getData().get(item), item);
    }

    Note note;

    public void mapDialog(Note note, int pos) {
        note = adapter.data.get(pos);
        dialog = new Dialog((requireContext()));
        dialog.setContentView(R.layout.maponlong_layout);
        Button btnmap = dialog.findViewById(R.id.btn_navigate);
        Button btnMove = dialog.findViewById(R.id.btnMove);
        ImageView img_move_delete = dialog.findViewById(R.id.img_move_delete);

        btnmap.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapsActivity.class);
//            intent.putExtra("note", note);
            intent.putExtra("pos", pos);
            startActivity(intent);
            dialog.dismiss();
        });
        btnMove.setOnClickListener(v -> {
            startActivityForResult(new Intent(requireContext(), Category.class).putExtra("from", "addNote"), 200);

            dialog.dismiss();
        });
        img_move_delete.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
//            note.setSubId(data.getLongExtra("selectedSubjectId", -1L));
            DBAccess.updateNote(note.getNote_id(),data.getLongExtra("selectedSubjectId", -1L));
            new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!!")
                    .setContentText("You successfully moved a note.")
                    .showCancelButton(true)
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .show();
        }
    }
}