package com.example.vanguard.journalapp.database;

import android.arch.persistence.room.ColumnInfo;
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

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updatedAt")
    private Date updatedAt;

    @Ignore
    public Journal(String title, String content, Date createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    Journal(int id, String title, String content, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
