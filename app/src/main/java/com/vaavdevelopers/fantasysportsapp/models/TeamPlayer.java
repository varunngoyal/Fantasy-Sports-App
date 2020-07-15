package com.vaavdevelopers.fantasysportsapp.models;

public class TeamPlayer {

    String name;
    String team;
    String credits;
    String type;

    public TeamPlayer() {

    }

    public TeamPlayer(String name, String team, String credits, String type) {
        this.name = name;
        this.team = team;
        this.credits = credits;
        this.type = type;
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
