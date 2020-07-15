package com.vaavdevelopers.fantasysportsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public MatchEventListAdapter(ArrayList<MatchEvent> matchesList) {
        this.matchesList = matchesList;
    }

    @NonNull
    @Override
    public MatchEventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_matchevent,parent, false);
        return new MatchEventListViewHolder(view);
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

    public class MatchEventListViewHolder extends RecyclerView.ViewHolder {

        TextView txtTeam1, txtTeam2, txtMatchTimestamp;

        public MatchEventListViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTeam1 = itemView.findViewById(R.id.team1);
            txtTeam2 = itemView.findViewById(R.id.team2);
            txtMatchTimestamp = itemView.findViewById(R.id.match_timestamp);
        }
    }

}
