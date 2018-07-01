package com.example.vanguard.journalapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vanguard.journalapp.R;
import com.example.vanguard.journalapp.database.AppDatabase;
import com.example.vanguard.journalapp.database.Journal;
import com.example.vanguard.journalapp.database.JournalViewModel;
import com.example.vanguard.journalapp.database.JournalViewModelFactory;
import com.example.vanguard.journalapp.utilities.JournalAppUtilities;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReadJournalActivity extends AppCompatActivity {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private static final String TAG = ReadJournalActivity.class.getSimpleName();

    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    private static final int DEFAULT_JOURNAL_ID = -1;

    private int mJournalId = DEFAULT_JOURNAL_ID;

    private TextView mTitleTextView, mDateTextView, mContentTextView;

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
                        populateUI(journal);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_journal_layout_menu, menu);
        if (mJournalId != DEFAULT_JOURNAL_ID){
            menu.findItem(R.id.add_journal_action).setTitle(R.string.edit);
        }
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
            onUpdateMenuItemClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        mTitleTextView = findViewById(R.id.title_textview);
        mDateTextView = findViewById(R.id.date_textview);
        mContentTextView = findViewById(R.id.content_textview);
    }

    private void onUpdateMenuItemClicked() {
        Intent intent = new Intent(ReadJournalActivity.this, AddJournalActivity.class);
        intent.putExtra(ReadJournalActivity.EXTRA_JOURNAL_ID, mJournalId);
        startActivity(intent);
    }

    private void populateUI(Journal journal) {
        if (journal != null) {
            if (journal.getTitle().equals("")){
                mTitleTextView.setHint("No Title");
            }else {
                mTitleTextView.setText(journal.getTitle());
            }
            String createdAtString = JournalAppUtilities.getFriendlyDateString(this,
                    journal.getCreatedAt().getTime(), true);

            String dateText = this.getString(R.string.today, createdAtString);
            mDateTextView.setText(dateText);
            mContentTextView.setText(journal.getContent());
        }else {
            Intent intent = new Intent(ReadJournalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
