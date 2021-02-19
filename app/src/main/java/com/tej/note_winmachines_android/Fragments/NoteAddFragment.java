package com.tej.note_winmachines_android.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

public class NoteAddFragment extends Fragment{
    TextView txtTitle;
    ImageView rightBarButton,leftBarButton;
    EditText noteTitle,noteDesc;
    String imageURL,audioURL;
    Double latitude,longitude;
    Button btnDelete;
    Note note;
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
        btnDelete = rootView.findViewById(R.id.buttonDeleteNote);
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ImageCross Action
        leftBarButton.setOnClickListener(view1 -> handleBack());
        rightBarButton.setOnClickListener(v -> handleSave());
        btnDelete.setOnClickListener(View -> { handleDelete(); });
        if(getArguments() != null){
            int item = Integer.parseInt(getArguments().getString("item"));
             note = DBAccess.fetchNotes().get(item);
            noteTitle.setText(note.getNote_title());
            noteDesc.setText(note.getNote_desc());

        }

    }
    void handleDelete(){
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this note!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(sDialog -> {
                            DBAccess.deleteNote(note,getContext());
                            sDialog.dismiss();
                            new SweetAlertDialog(this.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Deleted!")
                                    .setContentText("Note has been deleted!")
                                    .setCancelText("Okay")
                                    .setCancelClickListener(Dialog ->{Dialog.dismiss(); handleBack();})
                                    .show();
                        }
                       )
                .setCancelText("No")
                .show();


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
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!!")
                .setContentText("You successfully added a note.")
                .showCancelButton(true)
                .setConfirmText("Yes")
                .show();
    }
    void clearAll(){
        noteTitle.getText().clear();
        noteDesc.getText().clear();
        audioURL = "";
        imageURL = "";
    }
}