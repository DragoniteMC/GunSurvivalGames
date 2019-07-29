package com.ericlam.mc.gun.survival.games.states;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;

public class PreStartState extends GunSGState {

    public PreStartState(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public String getStateName() {
        return "preStart";
    }
}
