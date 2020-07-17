package com.vaavdevelopers.fantasysportsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vaavdevelopers.fantasysportsapp.models.MatchEvent;
import com.vaavdevelopers.fantasysportsapp.models.TeamPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeamActivity extends AppCompatActivity {

    public static FirebaseFirestore db;
    public static String matchId;
    private TextView team1, team2, time_of_match;
    private MatchEvent singleMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        db = FirebaseFirestore.getInstance();
        //set the POJO header
        team1 = findViewById(R.id.txtTeam1);
        team2 = findViewById(R.id.txtTeam2);
        time_of_match = findViewById(R.id.timestamp_match);


        Intent intent = getIntent();
        matchId = intent.getStringExtra("MatchId");
        singleMatch = intent.getParcelableExtra("MatchObject");

        Timestamp timeStamp = singleMatch.getTime_of_match();

        Date date = timeStamp.toDate();
        SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        String timestampStr = sf.format(date);

        time_of_match.setText(timestampStr);
        team1.setText(singleMatch.getTeam1());
        team2.setText(singleMatch.getTeam2());

        initViewPager();
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.playerCategoryTabs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments(new BatFragment(), "BAT");
        adapter.addFragments(new BowlFragment(), "BOWL");
        adapter.addFragments(new WkFragment(), "WK");
        adapter.addFragments(new AllRounderFragment(), "AR");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
