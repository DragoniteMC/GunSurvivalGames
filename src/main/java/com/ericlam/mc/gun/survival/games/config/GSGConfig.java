package com.ericlam.mc.gun.survival.games.config;

import com.dragonite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonite.mc.dnmc.core.config.yaml.Resource;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

@Resource(locate = "config.yml")
public class GSGConfig extends Configuration {

    public int maxLoadArenas;

    public int maxItemPerChest;

    public Location lobbyLocation;

    public int maxItemPerTie2;

    public int requiredPlayers;

    public int boostPlayers;

    public long gameTime;

    public long peaceTime;

    public long countDownTime;

    public long boostTime;

    public long preStartTime;

    public long preDeathMatchTime;

    public long deathMatchTime;

    public String fallbackServer;

    public int compassMaxTrack;

    public String countDownSound;


    public String activeSound;

    public WantedItem wantedItem;

    public static class WantedItem {
        public Material material;
        public String name;
        public List<String> lore;
        public List<Double> money;
    }
}
