package com.ericlam.mc.gun.survival.games.config;

import com.dragonite.mc.dnmc.core.config.yaml.MessageConfiguration;
import com.dragonite.mc.dnmc.core.config.yaml.Resource;

@Resource(locate = "motd.yml")
public class MotdConfig extends MessageConfiguration {

    public String preStart;

    public String peace;

    public String starting;

    public String preDeathMatch;

    public String deathmatch;
}
