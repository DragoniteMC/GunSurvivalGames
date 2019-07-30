package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;

public class PeaceTask extends GunSGTask {

    @Override
    public void initRun(PlayerManager playerManager) {
        Bukkit.broadcastMessage(configManager.getMessage("start"));
    }

    @Override
    public void onCancel() {
        MinigamesCore.getApi().getGameManager().endGame(playerManager.getGamePlayer(), null, true);
    }

    @Override
    public void onFinish() {
        Bukkit.broadcastMessage(configManager.getMessage("peace-finish"));
    }

    @Override
    public long run(long l) {
        if (l % 30 == 0 || l < 6){
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(configManager.getMessage("peace-countdown").replace("<time>", time));
        }
        int level = (int)l;
        Bukkit.getOnlinePlayers().forEach(p->p.setLevel(level));
        return l;
    }

    @Override
    public long getTotalTime() {
        return configManager.getData("peaceTime", Long.class).orElse(20L);
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
