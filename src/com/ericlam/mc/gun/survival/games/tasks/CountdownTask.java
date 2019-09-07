package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class CountdownTask extends GunSGTask {

    private boolean cancel = true;

    @Override
    public void initRun(PlayerManager playerManager) {
        Bukkit.getOnlinePlayers().forEach(GunSG::playActiveSound);
        Bukkit.broadcastMessage(configManager.getMessage("countdown-start"));
    }

    @Override
    public void onCancel() {
        Bukkit.broadcastMessage(configManager.getMessage("countdown-cancel"));
        playerManager.getWaitingPlayer().forEach(p -> p.getPlayer().setLevel(0));
    }

    @Override
    public void onFinish() {
        playerManager.getWaitingPlayer().forEach(g->playerManager.setGamePlayer(g));
    }

    @Override
    public long run(long l) {
        if (l == 6){
            MinigamesCore.getApi().getLobbyManager().runFinalResult();
            Arena arena = MinigamesCore.getApi().getArenaManager().getFinalArena();
            List<Location> spawns = arena.getWarp("game");
            MinigamesCore.getApi().getGameUtils().unLagIterate(spawns, location -> location.getChunk().load(true), 5L);
            cancel = false;
        }
        int boost = configManager.getData("boostPlayers", Integer.class).orElse(23);
        long boostTime = configManager.getData("boostTime", Long.class).orElse(30L);
        if (playerManager.getWaitingPlayer().size() >= boost && l > boostTime+1){
            Bukkit.broadcastMessage(configManager.getMessage("lobby-boost"));
            l -= boostTime;
        }
        int level = (int)l;
        playerManager.getWaitingPlayer().forEach(g->g.getPlayer().setLevel(level));
        return l;
    }

    @Override
    public long getTotalTime() {
        return configManager.getData("countdownTime", Long.class).orElse(30L);
    }

    @Override
    public boolean shouldCancel() {
        return cancel && playerManager.getWaitingPlayer().size() < configManager.getData("requiredPlayers", Integer.class).orElse(2);
    }
}
