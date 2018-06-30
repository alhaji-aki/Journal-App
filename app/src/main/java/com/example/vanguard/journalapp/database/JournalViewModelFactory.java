package com.example.vanguard.journalapp.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class JournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mJournalId;


    public JournalViewModelFactory(AppDatabase database, int journalId) {
        this.mDb = database;
        this.mJournalId = journalId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new JournalViewModel(mDb, mJournalId);
    }
}
