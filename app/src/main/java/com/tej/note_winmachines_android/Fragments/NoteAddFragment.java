package com.tej.note_winmachines_android.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.gms.location.LocationServices;
import com.tej.note_winmachines_android.Activities.HomeActivity;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class NoteAddFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, IPickResult {
    TextView txtTitle;
    ImageView rightBarButton,leftBarButton,rightBarButton2,addImageView;
    EditText noteTitle,noteDesc;
    String imageURL,audioURL;
    //final int SELECT_PICTURE = 100;
    Double latitude,longitude;
    Button btnDelete;
    Note note;
    CardView cardImage;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        addImageView = rootView.findViewById(R.id.addImage);
        rightBarButton2  = rootView.findViewById(R.id.rightBarButton2);
        rightBarButton = rootView.findViewById(R.id.rightBarButton);
        leftBarButton = rootView.findViewById(R.id.leftBarButton);
        txtTitle = rootView.findViewById(R.id.toolTitle);
        txtTitle.setText(R.string.addNote);
        leftBarButton.setImageResource(R.mipmap.back);
        rightBarButton.setImageResource(R.mipmap.save);
        rightBarButton2.setVisibility(View.VISIBLE);
        noteTitle = rootView.findViewById(R.id.txtNoteTitle);
        noteDesc = rootView.findViewById(R.id.txtNoteDesc);
        btnDelete = rootView.findViewById(R.id.buttonDeleteNote);
        cardImage = rootView.findViewById(R.id.cardImage);
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ImageCross Action
        leftBarButton.setOnClickListener(view1 -> handleBack());
        rightBarButton.setOnClickListener(v -> handleSave());
        btnDelete.setOnClickListener(View -> handleDelete());
        rightBarButton2.setOnClickListener(v -> handleAttachment(v));
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
    void handleAttachment(View v){
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.attachment_menu);
        popup.show();
    }
    void handleBack(){
        NavHostFragment.findNavController(NoteAddFragment.this)
                .navigate(R.id.toHome);
    }
    void handleSave(){
        HomeActivity home = (HomeActivity) getActivity();
        audioURL = "XX";
        imageURL = "xx";
        longitude = home.userLocation.getLongitude();
        latitude = home.userLocation.getLatitude();
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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.image_item){
            PickImageDialog.build(new PickSetup()).show(this.getActivity());
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            return true;
        }
        else if(menuItem.getItemId() == R.id.voice_item){
            Toast.makeText(getContext(),"Voice",Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(getContext(),"No",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SELECT_PICTURE) {
//            Uri selectedImageURI = data.getData();
//            cardImage.setVisibility(View.VISIBLE);
//            addImageView.setImageURI(selectedImageURI);
//        }
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            addImageView.setImageURI(null);

            //Setting the real returned image.
            addImageView.setImageURI(r.getUri());

            //If you want the Bitmap.
            //getImageView().setImageBitmap(r.getBitmap());

            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this.getContext(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}