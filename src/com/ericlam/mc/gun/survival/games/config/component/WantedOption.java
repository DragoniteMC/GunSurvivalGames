package com.ericlam.mc.gun.survival.games.config.component;

import com.hypernite.mc.hnmc.core.config.Component;
import com.hypernite.mc.hnmc.core.config.Prop;
import org.bukkit.Material;

import java.util.List;

@Component
public class WantedOption {

    @Prop
    public Material material;
    @Prop
    public String name;
    @Prop
    public List<String> lore;
    @Prop
    public List<Double> money;

}
