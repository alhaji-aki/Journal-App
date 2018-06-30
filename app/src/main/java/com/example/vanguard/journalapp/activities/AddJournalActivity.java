package com.example.vanguard.journalapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vanguard.journalapp.R;
import com.example.vanguard.journalapp.database.AppDatabase;
import com.example.vanguard.journalapp.database.Journal;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private EditText mTitleEditText, mContentEditText;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        //use butter knife to do initialization
        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    private void initViews() {
        mTitleEditText = findViewById(R.id.title_edit_text);
        mContentEditText = findViewById(R.id.content_edit_text);
        mSaveButton = findViewById(R.id.btn_save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButton();
            }
        });
    }

    private void onSaveButton() {
        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();
        Date created_at = new Date();

        Journal journal = new Journal(title, content, created_at);
        mDb.journalDao().insertJournal(journal);
        finish();
    }
}
