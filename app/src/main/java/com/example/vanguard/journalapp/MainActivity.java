package com.example.vanguard.journalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vanguard.journalapp.database.AppDatabase;
import com.example.vanguard.journalapp.database.Journal;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());

        //saving dummy data
        String title = "My first journal";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sed eleifend justo."
                + " Vestibulum semper ac neque eu egestas. Nam nulla est, finibus nec facilisis sit amet,"
                + " commodo quis odio. Sed ligula nibh, pretium eu cursus non, viverra quis nulla. Aliquam"
                + " iaculis dignissim metus, eu pretium tortor efficitur at.";
        Date created_at = new Date();

        Journal journal = new Journal(title, content, created_at);
        mDb.journalDao().insertJournal(journal);

        System.out.println("done");

    }
}
