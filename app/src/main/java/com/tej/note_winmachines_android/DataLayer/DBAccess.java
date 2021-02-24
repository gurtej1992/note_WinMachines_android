package com.tej.note_winmachines_android.DataLayer;
import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.tej.note_winmachines_android.Fragments.NoteAddFragment;
import com.tej.note_winmachines_android.Model.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBAccess {

    //private static Realm realm;
    static Realm realm = Realm.getDefaultInstance();
    static public RealmResults<Note> fetchNotes(){

        return realm.where(Note.class).findAll();
    }

    static public void saveNote(String noteTitle , String noteDesc, String noteAudio, String noteImage,Double latitude,Double longitude){
       realm.executeTransaction(realm -> {
           Number maxId = realm.where(Note.class).max("note_id");
           int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
           Note note = realm.createObject(Note.class,nextId);
           note.setNote_title(noteTitle);
           note.setNote_desc(noteDesc);
           note.setDate_created(new Date());
           note.setDate_modified(new Date());
           note.setNote_audio(noteAudio);
           note.setNote_image(noteImage);
           note.setLatitude(latitude);
           note.setLongitude(longitude);
       });
    }
    static public void deleteNote(Note note, Context context) {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }
}
