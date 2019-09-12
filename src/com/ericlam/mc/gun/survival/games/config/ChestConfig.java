package com.ericlam.mc.gun.survival.games.config;

import com.hypernite.mc.hnmc.core.config.Prop;
import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;

import java.util.List;

@Resource(locate = "chests.yml")
public class ChestConfig extends Configuration {

    @Prop(path = "tie1")
    public List<String> tie1Items;

    @Prop(path = "tie1-guns")
    public List<String> tie1Guns;

    @Prop(path = "tie2")
    public List<String> tie2Items;

    @Prop(path = "tie2-guns")
    public List<String> tie2Guns;

}
