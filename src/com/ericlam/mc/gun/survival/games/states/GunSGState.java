package com.ericlam.mc.gun.survival.games.states;

import com.ericlam.mc.minigames.core.game.InGameState;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import org.bukkit.ChatColor;

public abstract class GunSGState implements InGameState {

    private final ConfigManager configManager;

    public GunSGState(ConfigManager configManager){
        this.configManager = configManager;
    }

    @Override
    public String getMotd() {
        String motd = ChatColor.translateAlternateColorCodes('&', configManager.getData(getStateName(), String.class).orElse(""));
        return motd.isEmpty() ? null : motd;
    }
}
