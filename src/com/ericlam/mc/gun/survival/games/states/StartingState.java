package com.ericlam.mc.gun.survival.games.states;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;

public class StartingState extends GunSGState {
    public StartingState(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public String getStateName() {
        return "starting";
    }
}
