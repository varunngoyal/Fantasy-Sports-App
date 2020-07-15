package com.vaavdevelopers.fantasysportsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vaavdevelopers.fantasysportsapp.R;
import com.vaavdevelopers.fantasysportsapp.models.TeamPlayer;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TeamPlayerAdapter extends RecyclerView.Adapter<TeamPlayerAdapter.TeamPlayerViewHolder> {

    ArrayList<TeamPlayer> playerArrayList;

    public TeamPlayerAdapter(ArrayList<TeamPlayer> playerArrayList) {
        this.playerArrayList = playerArrayList;
    }

    @NonNull
    @Override
    public TeamPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.team_player_item, parent, false);
        return new TeamPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamPlayerViewHolder holder, int position) {

        String name = playerArrayList.get(position).getName();
        String team = playerArrayList.get(position).getTeam();
        String credits = playerArrayList.get(position).getCredits();

        holder.name.setText(name);
        holder.team.setText(team);
        holder.credits.setText(credits);
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public class TeamPlayerViewHolder extends RecyclerView.ViewHolder {

        TextView name, team, credits;

        public TeamPlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            team = itemView.findViewById(R.id.team);
            credits = itemView.findViewById(R.id.credits);
        }
    }
}
