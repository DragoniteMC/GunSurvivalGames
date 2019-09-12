package com.ericlam.mc.gun.survival.games.config;

import com.ericlam.mc.gun.survival.games.config.component.WantedOption;
import com.hypernite.mc.hnmc.core.config.Prop;
import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;

import java.util.Map;

@Resource(locate = "config.yml")
public class GSGConfig extends Configuration {

    @Prop(path = "max-loaded-arenas")
    public int maxLoadArenas;

    @Prop(path = "max-items-per-chest")
    public int maxItemPerChest;

    @Prop(path = "lobby-location")
    public Map<String, Object> location;

    @Prop(path = "max-items-per-tie2")
    public int maxItemPerTie2;

    @Prop(path = "required-players")
    public int requiredPlayers;

    @Prop(path = "boost-players")
    public int boostPlayers;

    @Prop(path = "game-time")
    public long gameTime;

    @Prop(path = "peace-time")
    public long peaceTime;

    @Prop(path = "countdown-time")
    public long countDownTime;

    @Prop(path = "boost-time")
    public long boostTime;

    @Prop(path = "prestart-time")
    public long preStartTime;

    @Prop(path = "predeathmatch-time")
    public long preDMTime;

    @Prop(path = "deathmatch-time")
    public long deathMatchTime;

    @Prop(path = "fallback-server")
    public String fallbackServer;

    @Prop(path = "compass-max-track")
    public int compassMaxTrack;

    @Prop(path = "reward-kills")
    public double rewardKills;

    @Prop(path = "reward-wins")
    public double rewardWins;

    @Prop(path = "count-down-sound")
    public String countDownSound;

    @Prop(path = "active-sound")
    public String activeSound;

    @Prop(path = "wanted-item")
    public WantedOption wantedOption;
}
