package com.tej.note_winmachines_android.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.developer.kalert.KAlertDialog;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import io.realm.Realm;

public class NoteAddFragment extends Fragment{
    TextView txtTitle;
    ImageView rightBarButton;
    ImageView leftBarButton;
    EditText noteTitle,noteDesc;
    String imageURL,audioURL;
    Double latitude,longitude;
    Realm realm;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        rightBarButton = rootView.findViewById(R.id.rightBarButton);
        leftBarButton = rootView.findViewById(R.id.leftBarButton);
        txtTitle = rootView.findViewById(R.id.toolTitle);
        txtTitle.setText(R.string.addNote);
        leftBarButton.setImageResource(R.mipmap.back);
        rightBarButton.setImageResource(R.mipmap.save);
        noteTitle = rootView.findViewById(R.id.txtNoteTitle);
        noteDesc = rootView.findViewById(R.id.txtNoteDesc);
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ImageCross Action
        leftBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { handleBack();}
        });
        rightBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSave();
            }
        });
        if(getArguments().getString("item") != null){
            int item = Integer.parseInt(getArguments().getString("item"));
            Note note = DBAccess.fetchNotes().get(item);
            noteTitle.setText(note.getNote_title());
            noteDesc.setText(note.getNote_desc());

        }

    }
    void handleBack(){
        NavHostFragment.findNavController(NoteAddFragment.this)
                .navigate(R.id.toHome);
    }
    void handleSave(){
        audioURL = "XX";
        imageURL = "xx";
        latitude = 0.0;
        longitude = 0.0;
        DBAccess.saveNote(noteTitle.getText().toString(),noteDesc.getText().toString(),audioURL,imageURL,latitude,longitude);
        clearAll();
        new KAlertDialog(this.getContext(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!!")
                .setContentText("Note Added Successfully.!")
                .show();
    }
    void clearAll(){
        noteTitle.getText().clear();
        noteDesc.getText().clear();
        audioURL = "";
        imageURL = "";
    }
}