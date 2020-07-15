package com.vaavdevelopers.fantasysportsapp.models;

import android.service.autofill.FieldClassification;

import com.google.firebase.Timestamp;

public class MatchEvent {
    String team1, team2;
    Timestamp time_of_match;

    public MatchEvent() {

    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public Timestamp getTime_of_match() {
        return time_of_match;
    }

    public void setTime_of_match(Timestamp time_of_match) {
        this.time_of_match = time_of_match;
    }

    public String toString() {
        return team1 + ","+team2+","+time_of_match;
    }
}
