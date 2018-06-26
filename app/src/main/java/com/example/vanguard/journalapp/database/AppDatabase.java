package com.example.vanguard.journalapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {Journal.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "journals_app";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null) {
            synchronized (LOCK){
                Log.d(TAG, "Creating new Database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        // Queries should not be run on the main thread
                        // Remove this when done...
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(TAG, "Getting Database instance");
        return sInstance;
    }

    public abstract JournalDao journalDao();
}
