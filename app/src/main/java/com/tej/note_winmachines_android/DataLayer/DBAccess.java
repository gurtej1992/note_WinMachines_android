package com.tej.note_winmachines_android.DataLayer;

import com.developer.kalert.KAlertDialog;
import com.tej.note_winmachines_android.Fragments.NoteAddFragment;
import com.tej.note_winmachines_android.Model.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class DBAccess {
    private static Realm realm = Realm.getDefaultInstance();

    static public RealmResults<Note> fetchNotes(){
        return realm.where(Note.class).findAll();
    }

    static public void saveNote(String noteTitle , String noteDesc, String noteAudio, String noteImage,Double latitude,Double longitude){
       realm.executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               Number maxId = realm.where(Note.class).max("note_id");
               int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
               realm.beginTransaction();
               Note note = realm.createObject(Note.class,nextId);
               note.setNote_title(noteTitle);
               note.setNote_desc(noteDesc);
               note.setDate_created(new Date());
               note.setDate_modified(new Date());
               note.setNote_audio(noteAudio);
               note.setNote_image(noteImage);
               note.setLatitude(latitude);
               note.setLongitude(longitude);
               realm.commitTransaction();
           }
       });
    }
}
