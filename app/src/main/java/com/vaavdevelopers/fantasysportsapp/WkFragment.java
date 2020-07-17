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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vaavdevelopers.fantasysportsapp.adapters.TeamPlayerAdapter;
import com.vaavdevelopers.fantasysportsapp.models.TeamPlayer;


import java.util.ArrayList;

import static com.vaavdevelopers.fantasysportsapp.TeamActivity.matchId;

/**
 * A simple {@link Fragment} subclass.
 */
public class WkFragment extends Fragment{

    RecyclerView recyclerView;
    TeamPlayerAdapter teamPlayerAdapter;
    FirebaseFirestore db;
    Drawable typeIcon;
    private TextView creditsLeft, selectedItems;
    ArrayList<TeamPlayer> playerArrayList1;



    public WkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_wk, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        selectedItems = getActivity().findViewById(R.id.textSelectedItems);
        creditsLeft = getActivity().findViewById(R.id.credits_left);

        typeIcon = getResources().getDrawable(R.drawable.wk_icon, null);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider,
                null));
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
                            playerArrayList1 = new ArrayList<>();
                            for( TeamPlayer player : task.getResult().toObjects(TeamPlayer.class)) {
                                playerArrayList1.add(player);
                            }
                            teamPlayerAdapter = new TeamPlayerAdapter(playerArrayList1, typeIcon);
                            teamPlayerAdapter.setSelectionListener(new TeamPlayerAdapter.selectionListener() {
                                @Override
                                public void reflectChanges(int position, boolean isSelected) {
                                    reflectChangesDefined(position, isSelected);
                                }
                            });

                            recyclerView.setAdapter(teamPlayerAdapter);

                        }  else {
                            Toast.makeText(getParentFragment().getContext(),
                                    "No player exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }


    private void reflectChangesDefined(int position, boolean isSelected) {
        Toast.makeText(getContext(),
                position+" Clicked!", Toast.LENGTH_SHORT).show();

        int currentcredits_left = Integer.parseInt(creditsLeft.getText().toString().split(":")[1].trim());
        int currentlyselected = Integer.parseInt(selectedItems.getText().toString().split("/")[0].trim());
        int creditUpdate = Integer.parseInt(playerArrayList1.get(position).getCredits().trim());

        if(isSelected) {
            selectedItems.setText((currentlyselected - 1)+"/11 selected");
            creditsLeft.setText("Credits Left : "+(currentcredits_left + creditUpdate));
        } else {
            selectedItems.setText((currentlyselected + 1)+"/11 selected");
            creditsLeft.setText("Credits Left : "+(currentcredits_left - creditUpdate));
        }
    }

}
