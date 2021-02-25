package com.tej.note_winmachines_android.Model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Note extends RealmObject implements Serializable {
    @PrimaryKey
    @Required
    private Long note_id;
    private String note_title;
    private String note_desc;
    private Long subId;
    private String note_audio;
    private String note_image;
    private Date date_created;

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    private Date date_modified;
    private double latitude;
    private double longitude;
    private Date date;

    public Long getNote_id() {
        return note_id;
    }

    public void setNote_id(Long note_id) {
        this.note_id = note_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = false;
    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_desc() {
        return note_desc;
    }


    public void setNote_desc(String note_desc) {
        this.note_desc = note_desc;
    }

    public String getNote_audio() {
        return note_audio;
    }

    public void setNote_audio(String note_audio) {
        this.note_audio = note_audio;
    }

    public String getNote_image() {
        return note_image;
    }

    public void setNote_image(String note_image) {
        this.note_image = note_image;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
