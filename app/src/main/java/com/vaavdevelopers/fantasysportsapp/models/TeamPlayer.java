package com.vaavdevelopers.fantasysportsapp.models;

public class TeamPlayer {

    String name;
    String team;
    String credits;
    String type;
    boolean isChecked;

    public TeamPlayer() {

    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public TeamPlayer(String name, String team, String credits, String type) {
        this.name = name;
        this.team = team;
        this.credits = credits;
        this.type = type;
        this.isChecked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
