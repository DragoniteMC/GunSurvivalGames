package com.ericlam.mc.gun.survival.games.states;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;

public class DeathmatchState extends GunSGState {
    public DeathmatchState(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public String getStateName() {
        return "deathmatch";
    }
}
