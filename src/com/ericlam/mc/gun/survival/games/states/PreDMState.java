package com.ericlam.mc.gun.survival.games.states;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;

public class PreDMState extends GunSGState {
    public PreDMState(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public String getStateName() {
        return "preDeathmatch";
    }
}
