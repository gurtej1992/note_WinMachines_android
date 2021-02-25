package com.tej.note_winmachines_android.DataLayer;

import android.content.Context;

import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.Activities.CategoryNotes;
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

    static public void saveNote(String noteTitle, String noteDesc, String noteAudio, String noteImage, Double latitude, Double longitude, Long selectedSubjectId) {
        realm.executeTransaction(realm -> {
            Number maxId = realm.where(Note.class).max("note_id");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            Note note = realm.createObject(Note.class, nextId);
            note.setNote_title(noteTitle);
            note.setNote_desc(noteDesc);
            note.setDate_created(new Date());
            note.setDate_modified(new Date());
            note.setNote_audio(noteAudio);
            note.setNote_image(noteImage);
            note.setLatitude(latitude);
            note.setLongitude(longitude);
            note.setSubId(selectedSubjectId);
        });
    }

    static public void saveSubject(String subjectName) {
        realm.executeTransaction(realm -> {
            Number maxId = realm.where(SubjectModel.class).max("subId");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            SubjectModel subModel = realm.createObject(SubjectModel.class, nextId);
            subModel.setSubjectName(subjectName);
            subModel.setDate(new Date());
        });
    }

    static public void updateNote(Long noteId, Long subId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final Note realmDB_class = realm.where(Note.class).equalTo("note_id", noteId).findFirst();
                realmDB_class.setSubId(subId);
            }
        });

      /*  realm.executeTransaction(realm -> {
            realm.insertOrUpdate(note);
        });*/
    }

    static public void deleteNote(Note note, Context context) {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }
}
