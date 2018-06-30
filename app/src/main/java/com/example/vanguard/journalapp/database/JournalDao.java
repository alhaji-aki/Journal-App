package com.example.vanguard.journalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journals ORDER BY created_at;")
    LiveData<List<Journal>> loadAllJournals();

    @Query("SELECT * FROM journals WHERE id == :id")
    LiveData<Journal> getJournal(int id);

    @Insert
    void insertJournal(Journal journal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);
}
