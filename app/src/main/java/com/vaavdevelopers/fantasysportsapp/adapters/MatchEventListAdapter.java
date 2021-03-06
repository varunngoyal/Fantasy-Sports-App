package com.vaavdevelopers.fantasysportsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.vaavdevelopers.fantasysportsapp.R;
import com.vaavdevelopers.fantasysportsapp.models.MatchEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MatchEventListAdapter extends RecyclerView.Adapter<MatchEventListAdapter.MatchEventListViewHolder> {

    ArrayList<MatchEvent> matchesList;
    private OnMatchClickListener matchClickListener;

    public MatchEventListAdapter(ArrayList<MatchEvent> matchesList, OnMatchClickListener matchClickListener) {
        this.matchesList = matchesList;
        this.matchClickListener = matchClickListener;
    }

    @NonNull
    @Override
    public MatchEventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_matchevent,parent, false);
        return new MatchEventListViewHolder(view, matchClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchEventListViewHolder holder, int position) {

        String team1 = matchesList.get(position).getTeam1();
        String team2 = matchesList.get(position).getTeam2();
        Timestamp timeStamp = matchesList.get(position).getTime_of_match();

        Date date = timeStamp.toDate();
        SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        String timestampStr = sf.format(date);

        holder.txtTeam1.setText(team1);
        holder.txtTeam2.setText(team2);
        holder.txtMatchTimestamp.setText(timestampStr);
    }


    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public class MatchEventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTeam1, txtTeam2, txtMatchTimestamp;
        OnMatchClickListener onMatchClickListener;

        public MatchEventListViewHolder(@NonNull View itemView, OnMatchClickListener listener) {
            super(itemView);
            txtTeam1 = itemView.findViewById(R.id.team1);
            txtTeam2 = itemView.findViewById(R.id.team2);
            txtMatchTimestamp = itemView.findViewById(R.id.match_timestamp);

            this.onMatchClickListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMatchClickListener.onMatchClick(getAdapterPosition());
        }
    }

    public interface OnMatchClickListener {
        void onMatchClick(int position);

    }

}
