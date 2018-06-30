package com.example.vanguard.journalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class JournalViewModel extends ViewModel {

    private LiveData<Journal> journal;
    JournalViewModel(AppDatabase database, int journalId) {
        journal = database.journalDao().getJournal(journalId);
    }

    public LiveData<Journal> getJournal() {
        return journal;
    }
}
