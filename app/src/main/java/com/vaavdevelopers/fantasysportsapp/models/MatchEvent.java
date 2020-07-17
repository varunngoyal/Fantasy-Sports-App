package com.vaavdevelopers.fantasysportsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class MatchEvent implements Parcelable {
    String team1, team2;
    Timestamp time_of_match;

    public MatchEvent() {

    }

    protected MatchEvent(Parcel in) {
        team1 = in.readString();
        team2 = in.readString();
        time_of_match = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<MatchEvent> CREATOR = new Creator<MatchEvent>() {
        @Override
        public MatchEvent createFromParcel(Parcel in) {
            return new MatchEvent(in);
        }

        @Override
        public MatchEvent[] newArray(int size) {
            return new MatchEvent[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(team1);
        dest.writeString(team2);
        dest.writeParcelable(time_of_match, flags);
    }
}
