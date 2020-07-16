package com.vaavdevelopers.fantasysportsapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vaavdevelopers.fantasysportsapp.adapters.TeamPlayerAdapter;
import com.vaavdevelopers.fantasysportsapp.models.TeamPlayer;

import java.util.ArrayList;

import static com.vaavdevelopers.fantasysportsapp.TeamActivity.matchId;


/**
 * A simple {@link Fragment} subclass.
 */
public class WkFragment extends Fragment implements TeamPlayerAdapter.onTeamPlayerClickListener {

    RecyclerView recyclerView;
    TeamPlayerAdapter teamPlayerAdapter;
    FirebaseFirestore db;
    Drawable typeIcon;

    public WkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wk, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));

        typeIcon = getResources().getDrawable(R.drawable.wk_icon, null);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        db = FirebaseFirestore.getInstance();

        db.collection("matches")
                .document(matchId)
                .collection("players").whereEqualTo("type", "WK")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<TeamPlayer> playerArrayList1;
                            playerArrayList1 = new ArrayList<>();
                            for( TeamPlayer player : task.getResult().toObjects(TeamPlayer.class)) {
                                playerArrayList1.add(player);
                            }
                            teamPlayerAdapter = new TeamPlayerAdapter(playerArrayList1, typeIcon, WkFragment.this);
                            recyclerView.setAdapter(teamPlayerAdapter);

                        }  else {
                            Toast.makeText(getParentFragment().getContext(),
                                    "No player exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }
    @Override
    public void onTeamPlayerClick(int position) {
        Toast.makeText(getContext(), position+" Clicked!", Toast.LENGTH_SHORT).show();
    }
}
