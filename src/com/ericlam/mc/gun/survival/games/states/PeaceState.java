package com.ericlam.mc.gun.survival.games.states;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;

public class PeaceState extends GunSGState {
    public PeaceState(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public String getStateName() {
        return "peace";
    }
}
