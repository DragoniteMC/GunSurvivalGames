package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.eld.ELDependenci;
import com.ericlam.mc.gun.survival.games.config.GSGConfig;
import com.ericlam.mc.gun.survival.games.config.LangConfig;
import com.ericlam.mc.gun.survival.games.config.MotdConfig;
import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import com.dragonite.mc.dnmc.core.managers.YamlManager;
import org.dragonitemc.dragoneconomy.api.AsyncEconomyService;

public abstract class GunSGTask implements SectionTask {
    YamlManager configManager;
    PlayerManager playerManager;
    GSGConfig gsgConfig;
    MotdConfig motdConfig;
    LangConfig msg;
    AsyncEconomyService economyService;
    private boolean running;

    public GunSGTask() {
        this.configManager = GunSG.getYamlManager();
        this.gsgConfig = configManager.getConfigAs(GSGConfig.class);
        this.motdConfig = configManager.getConfigAs(MotdConfig.class);
        this.msg = configManager.getConfigAs(LangConfig.class);
        this.economyService = ELDependenci.getApi().exposeService(AsyncEconomyService.class);
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
