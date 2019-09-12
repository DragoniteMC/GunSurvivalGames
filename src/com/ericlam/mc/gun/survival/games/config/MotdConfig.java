package com.ericlam.mc.gun.survival.games.config;

import com.hypernite.mc.hnmc.core.config.Prop;
import com.hypernite.mc.hnmc.core.config.yaml.MessageConfiguration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;

@Resource(locate = "motd.yml")
public class MotdConfig extends MessageConfiguration {

    @Prop(path = "PRE-START")
    public String preStart;

    @Prop(path = "PEACE")
    public String peace;

    @Prop(path = "STARTING")
    public String starting;

    @Prop(path = "PRE-DEATHMATCH")
    public String preDeathMatch;

    @Prop(path = "DEATHMATCH")
    public String deathmatch;
}
