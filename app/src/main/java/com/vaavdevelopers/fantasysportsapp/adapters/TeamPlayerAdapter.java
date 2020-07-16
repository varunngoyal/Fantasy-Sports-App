package com.vaavdevelopers.fantasysportsapp.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vaavdevelopers.fantasysportsapp.R;
import com.vaavdevelopers.fantasysportsapp.models.TeamPlayer;


import java.util.ArrayList;

public class TeamPlayerAdapter extends RecyclerView.Adapter<TeamPlayerAdapter.TeamPlayerViewHolder> {

    private ArrayList<TeamPlayer> playerArrayList;
    private Drawable typeIcon;
    private onTeamPlayerClickListener teamPlayerClickListener;

    public TeamPlayerAdapter(ArrayList<TeamPlayer> playerArrayList,
                             Drawable typeIcon, onTeamPlayerClickListener listener) {
        this.playerArrayList = playerArrayList;
        this.typeIcon = typeIcon;
        this.teamPlayerClickListener = listener;
    }

    @NonNull
    @Override
    public TeamPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.team_player_item,
                parent, false);
        return new TeamPlayerViewHolder(view, teamPlayerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamPlayerViewHolder holder, int position) {

        String name = playerArrayList.get(position).getName();
        String team = playerArrayList.get(position).getTeam();
        String credits = playerArrayList.get(position).getCredits();

        holder.name.setText(name);
        holder.team.setText(team);
        holder.credits.setText(credits);
        holder.icon.setImageDrawable(typeIcon);
        //holder.icon.setImageResource(R.drawable.bat_icon);

    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static class TeamPlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, team, credits;
        ImageButton addPlayer;
        ImageView icon;
        onTeamPlayerClickListener listener;

        private TeamPlayerViewHolder(@NonNull View itemView, onTeamPlayerClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            team = itemView.findViewById(R.id.team);
            credits = itemView.findViewById(R.id.credits);
            addPlayer = itemView.findViewById(R.id.btnAddPlayer);
            //set resource for image drawable
            icon = itemView.findViewById(R.id.playerTypeIcon);
            this.listener = listener;
            addPlayer.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            listener.onTeamPlayerClick(getAdapterPosition());
        }
    }

    public interface onTeamPlayerClickListener {
        void onTeamPlayerClick(int position);
    }

}
