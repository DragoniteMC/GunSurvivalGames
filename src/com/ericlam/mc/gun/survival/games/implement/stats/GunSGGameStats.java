package com.ericlam.mc.gun.survival.games.implement.stats;

import com.ericlam.mc.minigames.core.gamestats.GameStatsEditor;

public class GunSGGameStats implements GameStatsEditor {

    private int kills;
    private int deaths;
    private int played;
    private int wins;

    public GunSGGameStats(int kills, int deaths, int played, int wins) {
        this.kills = kills;
        this.deaths = deaths;
        this.played = played;
        this.wins = wins;
    }

    public GunSGGameStats(){
        this(0,0,0,0);
    }

    @Override
    public void setKills(int i) {
        this.kills = i;
    }

    @Override
    public void setDeaths(int i) {
        this.deaths = i;
    }

    @Override
    public void setPlayed(int i) {
        this.played = i;
    }

    @Override
    public void setWins(int i) {
        this.wins = i;
    }

    @Override
    public int getPlayed() {
        return played;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public int getWins() {
        return wins;
    }

    @Override
    public double getScores() {
        return kills * 3 - deaths * 2 + wins * 5;
    }

    @Override
    public String[] getInfo() {
        return new String[0];
    }
}
