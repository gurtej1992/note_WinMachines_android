package com.tej.note_winmachines_android.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.tej.note_winmachines_android.Activities.HomeActivity;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Helper.AudioRecorder;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.IOException;

public class NoteAddFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    TextView txtTitle;
    ImageView rightBarButton,leftBarButton,rightBarButton2,addImageView;
    EditText noteTitle,noteDesc;
    String imageURL,audioURL;
    //final int SELECT_PICTURE = 100;
    Double latitude,longitude;
    Button btnDelete;
    ImageButton btnAudio;
    Note note;
    CardView cardImage;
    AudioRecorder r;
    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
        btnAudio = (ImageButton) rootView.findViewById(R.id.btnAudio);
        btnAudio.setTag("Record");
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ImageCross Action
        leftBarButton.setOnClickListener(view1 -> handleBack());
        rightBarButton.setOnClickListener(v -> handleSave());
        btnDelete.setOnClickListener(View -> handleDelete());
        rightBarButton2.setOnClickListener(this::handleAttachment);
        btnAudio.setOnClickListener(this::handleAudio);
        if(getArguments() != null){
            int item = Integer.parseInt(getArguments().getString("item"));
             note = DBAccess.fetchNotes().get(item);
            noteTitle.setText(note.getNote_title());
            noteDesc.setText(note.getNote_desc());
        }
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
         r = new AudioRecorder("abc");
    }

    private void handleAudio(View v) {
        ImageButton i = (ImageButton) v;
        //RECORD AUDIO START
        if(v.getTag() == "Record"){
            i.setBackgroundResource(R.mipmap.stop_btn);
            Toast.makeText(getContext(),"Start Recording",Toast.LENGTH_SHORT).show();
            try {
                r.start();
                i.setTag("StopRecord");
            } catch (IOException e) {
                Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        //RECORD AUDIO STOP
        else if(i.getTag() == "StopRecord"){
            Toast.makeText(getContext(),"Stop Recording",Toast.LENGTH_SHORT).show();
            btnAudio.setTag("Play");
            i.setBackgroundResource(R.mipmap.play_btn);
            r.stop();
        }
        else if(i.getTag() == "Play"){
            Toast.makeText(getContext(),"Start Playing",Toast.LENGTH_SHORT).show();
            try {
                btnAudio.setTag("PlayStop");
                r.playOrStopRecording(r.path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i.setBackgroundResource(R.mipmap.stop_btn);
        }
        else{
            try {
                r.playOrStopRecording(r.path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(),"Stop Playing",Toast.LENGTH_SHORT).show();
            i.setTag("Record");
        }
//
//        if (r.isRecording){
////            btnAudio.setTag("Play");
////            v.setBackgroundResource(R.mipmap.play_btn);
////            //Its Recording
////                r.stop();
//        }
//        else {
//            //Not Recording
//            if(r.isRecordingDone){
//                if(i.getTag() == "PlayStop"){
//                    try {
//                        r.playOrStopRecording(r.path);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(getContext(),"Stop Playing",Toast.LENGTH_SHORT).show();
//                    v.setTag("Record");
//                }
//                else{
//                    Toast.makeText(getContext(),"Playing",Toast.LENGTH_SHORT).show();
//                    btnAudio.setTag("PlayStop");
//                    try {
//                        r.playOrStopRecording(r.path);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    v.setBackgroundResource(R.mipmap.stop_btn);
//                }
//            }

    //    }

    }

    void handleDelete(){
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
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
            PickImageDialog dialog = PickImageDialog.build(new PickSetup()).show(this.getActivity());
            PickImageDialog.build(new PickSetup())
                    .setOnPickResult(r -> {
                        addImageView.setImageURI(null);
                        cardImage.setVisibility(View.VISIBLE);
                        //Setting the real returned image.
                        addImageView.setImageURI(r.getUri());
                        dialog.dismiss();
                    })
                    .setOnPickCancel(() -> {
                        //TODO: do what you have to if user clicked cancel
                    }).show(this.getParentFragmentManager());
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted ){
            Toast.makeText(getContext(),"Go to setting and give permission to use mic",Toast.LENGTH_SHORT).show();
        }

    }
}