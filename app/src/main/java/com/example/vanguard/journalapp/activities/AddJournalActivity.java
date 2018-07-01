package com.example.vanguard.journalapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.vanguard.journalapp.R;
import com.example.vanguard.journalapp.database.AppDatabase;
import com.example.vanguard.journalapp.database.Journal;
import com.example.vanguard.journalapp.database.JournalViewModel;
import com.example.vanguard.journalapp.database.JournalViewModelFactory;
import com.example.vanguard.journalapp.executors.AppExecutors;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    private static final String TAG = AddJournalActivity.class.getSimpleName();
    private static final int DEFAULT_JOURNAL_ID = -1;
    private AppDatabase mDb;
    private EditText mTitleEditText, mContentEditText;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_journal_layout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.logout_item) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else if (i == R.id.add_journal_action){
            onSaveMenuItemClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
    }

    private void onSaveMenuItemClicked() {
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
