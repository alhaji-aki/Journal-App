package com.example.vanguard.journalapp.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vanguard.journalapp.R;
import com.example.vanguard.journalapp.database.AppDatabase;
import com.example.vanguard.journalapp.database.Journal;
import com.example.vanguard.journalapp.database.JournalViewModel;
import com.example.vanguard.journalapp.database.JournalViewModelFactory;
import com.example.vanguard.journalapp.executors.AppExecutors;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReadJournalActivity extends AppCompatActivity {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private static final String TAG = ReadJournalActivity.class.getSimpleName();

    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    private static final int DEFAULT_JOURNAL_ID = -1;
    private static final String INSTANCE_JOURNAL_ID = "instanceJournalId";

    private int mJournalId = DEFAULT_JOURNAL_ID;

    private TextView mTitleTextView, mDateTextView, mContentTextView;
    private Button mUpdateButton;

    private AppDatabase mDb;

    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_journal);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)){
            if (mJournalId == DEFAULT_JOURNAL_ID){
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);

                JournalViewModelFactory factory = new JournalViewModelFactory(mDb, mJournalId);
                final JournalViewModel viewModel = ViewModelProviders.of(this, factory).get(JournalViewModel.class);

                viewModel.getJournal().observe(this, new Observer<Journal>() {
                    @Override
                    public void onChanged(@Nullable Journal journal) {
                        viewModel.getJournal().removeObserver(this);
                        populateUI(journal);
                    }
                });
            }
        }
    }

    private void initViews() {
        mTitleTextView = findViewById(R.id.title_textview);
        mDateTextView = findViewById(R.id.date_textview);
        mContentTextView = findViewById(R.id.content_textview);
        mUpdateButton = findViewById(R.id.btn_update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateButtonClicked();
            }
        });
    }

    private void onUpdateButtonClicked() {
        Intent intent = new Intent(ReadJournalActivity.this, AddJournalActivity.class);
        intent.putExtra(ReadJournalActivity.EXTRA_JOURNAL_ID, mJournalId);
        startActivity(intent);
    }

    private void populateUI(Journal journal) {
        if (journal != null) {
            mTitleTextView.setText(journal.getTitle());
            mDateTextView.setText(dateFormat.format(journal.getCreatedAt()));
            mContentTextView.setText(journal.getContent());
        }else {
            Intent intent = new Intent(ReadJournalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
