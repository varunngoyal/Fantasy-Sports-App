package com.vaavdevelopers.fantasysportsapp.adapters;

import android.graphics.Color;
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
    private selectionListener listener;

    public TeamPlayerAdapter(ArrayList<TeamPlayer> playerArrayList,
                             Drawable typeIcon) {
        this.playerArrayList = playerArrayList;
        this.typeIcon = typeIcon;
    }

    public void setSelectionListener(selectionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.team_player_item,
                parent, false);
        return new TeamPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamPlayerViewHolder holder, int position) {

        String name = playerArrayList.get(position).getName();
        String team = playerArrayList.get(position).getTeam();
        String credits = playerArrayList.get(position).getCredits();

        holder.name.setText(name);
        holder.team.setText(team);
        holder.credits.setText(credits);
        holder.icon.setImageDrawable(typeIcon);

        boolean checkStatus = playerArrayList.get(position).isChecked();
        if(!checkStatus) {
            holder.itemView.setBackgroundColor(Color.WHITE);

        } else {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }

        holder.setOnTeamPlayerClickListener(new TeamPlayerAdapter.onTeamPlayerClickListener() {

            @Override
            public void onTeamPlayerClick(int position) {
                if(playerArrayList.get(position).isChecked()) {
                    holder.itemView.setBackgroundColor(Color.WHITE);

                } else {
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                boolean previousState = playerArrayList.get(position).isChecked();
                playerArrayList.get(position).setChecked(!previousState);

                listener.reflectChanges(position, previousState);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(playerArrayList == null)
            return 0;
        return playerArrayList.size();
    }

    public static class TeamPlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, team, credits;
        ImageButton addPlayer;
        ImageView icon;
        onTeamPlayerClickListener listener;

        private TeamPlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            team = itemView.findViewById(R.id.team);
            credits = itemView.findViewById(R.id.credits);
            addPlayer = itemView.findViewById(R.id.btnAddPlayer);
            icon = itemView.findViewById(R.id.playerTypeIcon);
            addPlayer.setOnClickListener(this);

        }

        private void setOnTeamPlayerClickListener(TeamPlayerAdapter.onTeamPlayerClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            this.listener.onTeamPlayerClick(getAdapterPosition());
        }
    }
    public interface onTeamPlayerClickListener {
        void onTeamPlayerClick(int position);
    }

    public interface selectionListener {
        void reflectChanges(int position, boolean selected);
    }

}
