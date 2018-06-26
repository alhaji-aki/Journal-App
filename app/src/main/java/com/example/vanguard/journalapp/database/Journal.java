package com.example.vanguard.journalapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "journals")
public class Journal {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private Date created_at;
    private Date updated_at;

    @Ignore
    private String summary;

    @Ignore
    public Journal(String title, String content, Date updated_at) {
        this.title = title;
        this.content = content;
        this.updated_at = updated_at;
    }

    public Journal(int id, String title, String content, Date created_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
