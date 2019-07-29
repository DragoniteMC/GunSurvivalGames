package com.ericlam.mc.gun.survival.games.implement.area;

import com.ericlam.mc.minigames.core.arena.CreateArena;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GunSGArena implements CreateArena {

    private String author;
    private String arenaName;
    private String displayName;
    private Map<String, List<Location>> warpMap;
    private boolean changed;
    private World world;
    private List<String> description;

    public GunSGArena(String author, String arenaName, String displayName, Map<String, List<Location>> warpMap, World world, List<String> description) {
        this.author = author;
        this.arenaName = arenaName;
        this.displayName = displayName;
        this.warpMap = warpMap;
        this.changed = false;
        this.world = world;
        this.description = description;
    }

    public GunSGArena(String author, String arenaName, World world){
        this(author, arenaName, arenaName, new HashMap<>(), world, new ArrayList<>());
    }

    @Override
    public void setAuthor(String s) {
        this.author = s;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void setArenaName(String s) {
        this.arenaName = s;
    }

    @Override
    public void setDisplayName(String s) {
        this.displayName = ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public void setLocationMap(Map<String, List<Location>> map) {
        this.warpMap = map;
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public void setChanged(Boolean aBoolean) {
        this.changed = aBoolean;
    }

    @Override
    public boolean isSetupCompleted() {
        return false;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public String getArenaName() {
        return arenaName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Map<String, List<Location>> getLocationsMap() {
        return warpMap;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public String[] getInfo() {
        return new String[]{

        };
    }
}
