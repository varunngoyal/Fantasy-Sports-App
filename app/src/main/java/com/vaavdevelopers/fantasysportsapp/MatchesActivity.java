package com.vaavdevelopers.fantasysportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vaavdevelopers.fantasysportsapp.adapters.MatchEventListAdapter;
import com.vaavdevelopers.fantasysportsapp.models.MatchEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MatchesActivity extends AppCompatActivity {

    private static final String PREFERENCES_APP = "AllPrefs";
    ArrayList<MatchEvent> matchesArrayList;
    /*MatchEvent dummy;
    Date date;
    SimpleDateFormat ft;
    String inputDate;*/
    RecyclerView matchesRecyclerView;
    FirebaseFirestore db;
    TextView welcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        db = FirebaseFirestore.getInstance();
        matchesArrayList = new ArrayList<>();
        welcomeUser = findViewById(R.id.welcome_user);

        matchesRecyclerView = findViewById(R.id.list_matches);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "0");
        welcomeUser.setText("Welcome, "+username+"!" );

        /*
        ft = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        inputDate = "2020-07-18:15:00:00";
        try {
            date = ft.parse(inputDate);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        dummy = new MatchEvent();
        dummy.setTeam1("A");
        dummy.setTeam2("B");
        dummy.setTime_of_match(new Timestamp(date));
        matchesArrayList.add(dummy);
        matchesArrayList.add(dummy);
        matchesArrayList.add(dummy);*/

        db.collection("matches")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    Toast.makeText(MatchesActivity.this, "fetching successful!", Toast.LENGTH_SHORT).show();

                    for(MatchEvent match : task.getResult().toObjects(MatchEvent.class)) {
                        Toast.makeText(MatchesActivity.this,
                                match.toString(), Toast.LENGTH_SHORT).show();

                        matchesArrayList.add(match);
                        String added_content = matchesArrayList.get(matchesArrayList.size() - 1).toString();
                        Toast.makeText(MatchesActivity.this,
                                "added content: "+added_content, Toast.LENGTH_SHORT).show();

                        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(MatchesActivity.this));
                        matchesRecyclerView.setAdapter(new MatchEventListAdapter(matchesArrayList));
                    }

                } else {
                    Toast.makeText(MatchesActivity.this,
                            "Error fetching list of matches", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
