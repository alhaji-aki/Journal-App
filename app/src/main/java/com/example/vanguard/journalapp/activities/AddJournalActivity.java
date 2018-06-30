package com.example.vanguard.journalapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vanguard.journalapp.R;
import com.example.vanguard.journalapp.database.AppDatabase;
import com.example.vanguard.journalapp.database.Journal;
import com.example.vanguard.journalapp.executors.AppExecutors;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    private static final int DEFAULT_JOURNAL_ID = -1;
    private AppDatabase mDb;
    private EditText mTitleEditText, mContentEditText;
    private Button mSaveButton;

    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    private static final String INSTANCE_JOURNAL_ID = "instanceJournalId";

    private int mJournalId = DEFAULT_JOURNAL_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        //use butter knife to do initialization
        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)){
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)){
            mSaveButton.setText(R.string.update);
            if (mJournalId == DEFAULT_JOURNAL_ID){
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);

                AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Journal journal = mDb.journalDao().getJournal(mJournalId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(journal);
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }

    private void populateUI(Journal journal) {
        if (journal == null){
            return;
        }

        mTitleEditText.setText(journal.getTitle());
        mContentEditText.setText(journal.getContent());
    }

    private void initViews() {
        mTitleEditText = findViewById(R.id.title_edit_text);
        mContentEditText = findViewById(R.id.content_edit_text);
        mSaveButton = findViewById(R.id.btn_save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void onSaveButtonClicked() {
        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();
        Date created_at = new Date();

        final Journal journal = new Journal(title, content, created_at);

        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    mDb.journalDao().insertJournal(journal);
                }else {
                    journal.setId(mJournalId);
                    journal.setUpdatedAt(new Date());

                    mDb.journalDao().updateJournal(journal);
                }
                finish();
            }
        });
    }
}
