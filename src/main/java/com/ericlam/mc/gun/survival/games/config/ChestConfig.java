package com.ericlam.mc.gun.survival.games.config;

import com.ericlam.mc.minigames.core.function.GameEntry;
import com.dragonite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonite.mc.dnmc.core.config.yaml.Resource;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@Resource(locate = "chests.yml")
public class ChestConfig extends Configuration {

    public List<String> tie1Items;

    public List<String> tie1Guns;

    public List<String> tie2Items;

    public List<String> tie2Guns;

    public static Map.Entry<Material, Integer> parseItem(String item) {
        String[] str = item.split(":");
        Material material = Material.getMaterial(str[0]);
        int amount = Integer.parseInt(str[1]);
        return new GameEntry<>(material, amount);
    }

}
