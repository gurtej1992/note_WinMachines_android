package com.tej.note_winmachines_android.DataLayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.tej.note_winmachines_android.Helper.Helper;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.Model.SubjectModel;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DBAccess {

    //private static Realm realm;
    static Realm realm = Realm.getDefaultInstance();

    static public RealmResults<Note> fetchNotes() {
        return realm.where(Note.class).findAll();
    }

    static public RealmResults<Note> fetchNotesWhereSubId(Long subId) {
        RealmQuery<Note> query = realm.where(Note.class);
        query = query.equalTo("subId", subId);
        return query.findAll();
    }

    static public RealmResults<SubjectModel> fetchSubjects() {

        return realm.where(SubjectModel.class).findAll();
    }
    static public SubjectModel fetchSubjectWhereSubjectID(Long subjectID) {
        RealmQuery<SubjectModel> query = realm.where(SubjectModel.class);
        query = query.equalTo("subId", subjectID);
        return query.findFirst();
    }
    static public void saveNote(Note existingNote,String noteTitle, String noteDesc, String noteAudio, Bitmap noteImage, Double latitude, Double longitude, Long selectedSubjectId) {
        if(existingNote != null){
            //Update
            realm.executeTransaction(realm -> {
                existingNote.setNote_title(noteTitle);
                existingNote.setNote_desc(noteDesc);
                existingNote.setDate_modified(new Date());
                existingNote.setNote_audio(noteAudio);
                if(noteImage != null){
                    existingNote.setNote_image(Helper.ImageToByte(noteImage));
                }
                existingNote.setSubId(selectedSubjectId);
            });
        }
        else{
            //New
            realm.executeTransaction(realm -> {
                Number maxId = realm.where(Note.class).max("note_id");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                Note note = realm.createObject(Note.class, nextId);
                note.setNote_title(noteTitle);
                note.setNote_desc(noteDesc);
                note.setDate_created(new Date());
                note.setDate_modified(new Date());
                note.setNote_audio(noteAudio);
                if(noteImage != null){
                    note.setNote_image(Helper.ImageToByte(noteImage));
                }
                note.setLatitude(latitude);
                note.setLongitude(longitude);
                note.setSubId(selectedSubjectId);
            });
        }

    }

    static public boolean saveSubject(String subjectName, Context context) {
        boolean isSubjectExist = realm.where(SubjectModel.class).equalTo("subjectName", subjectName.toLowerCase()).findAll().size() != 0;
        if (isSubjectExist) {
            Toast.makeText(context, "Subject already exist", Toast.LENGTH_SHORT).show();
            return false;
        }
        realm.executeTransaction(realm -> {
            Number maxId = realm.where(SubjectModel.class).max("subId");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            SubjectModel subModel = realm.createObject(SubjectModel.class, nextId);
            subModel.setSubjectName(subjectName);
            subModel.setDate(new Date());
        });
        return true;
    }

    static public void updateNote(Long noteId, Long subId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final Note realmDB_class = realm.where(Note.class).equalTo("note_id", noteId).findFirst();
                realmDB_class.setSubId(subId);

            }
        });
    }

    static public void deleteNote(Note note, Context context) {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }
}
