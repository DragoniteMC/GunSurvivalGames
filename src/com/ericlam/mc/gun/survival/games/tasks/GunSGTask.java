package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;

public abstract class GunSGTask implements SectionTask {
    ConfigManager configManager;
    PlayerManager playerManager;
    private boolean running;

    public GunSGTask(){
        this.configManager = GunSG.config();
        this.running = false;
    }

    @Override
    public void initTimer(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.initRun(playerManager);
    }

    public abstract void initRun(PlayerManager playerManager);

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }
}
