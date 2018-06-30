package com.example.vanguard.journalapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vanguard.journalapp.R;
import com.example.vanguard.journalapp.database.Journal;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.JournalViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<Journal> mJournals;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public JournalsAdapter(Context context, ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public JournalsAdapter.JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.journal_layout, parent, false);

        return new JournalsAdapter.JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalsAdapter.JournalViewHolder holder, int position) {
        Journal journal = mJournals.get(position);
        String title = journal.getTitle();
        String summary = journal.getContent();
        String created_at = dateFormat.format(journal.getCreatedAt());

        //Set values
        holder.titleView.setText(title);
        holder.createdAtView.setText(created_at);
        holder.summaryView.setText(summary);
    }

    @Override
    public int getItemCount() {
        if (mJournals == null){
            return 0;
        }

        return mJournals.size();
    }

    public void setJournals(List<Journal> journals) {
        mJournals = journals;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleView;
        TextView createdAtView;
        TextView summaryView;

        JournalViewHolder(View itemView) {
            super(itemView);
            this.titleView = itemView.findViewById(R.id.title_textview);
            this.createdAtView = itemView.findViewById(R.id.date_textview);
            this.summaryView = itemView.findViewById(R.id.summary_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mJournals.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
