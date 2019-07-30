package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class PreDeathMatchTask extends GunSGTask {

    @Override
    public void initRun(PlayerManager playerManager) {
        int gamers = playerManager.getGamePlayer().size();
        List<Location> spawns = MinigamesCore.getApi().getArenaManager().getFinalArena().getWarp("deathmatch");
        for (int i = 0; i < Math.min(gamers, spawns.size()); i++) {
            playerManager.getGamePlayer().get(i).getPlayer().teleportAsync(spawns.get(i));
        }
        playerManager.getSpectators().forEach(g->{
            Player player = g.getPlayer();
            player.setSpectatorTarget(null);
            player.teleportAsync(spawns.get(0));
        });
    }

    @Override
    public void onCancel() {
        MinigamesCore.getApi().getGameManager().endGame(playerManager.getGamePlayer(), null, true);
    }

    @Override
    public void onFinish() {
        Bukkit.broadcastMessage(configManager.getMessage("peace-finish"));
        Bukkit.getOnlinePlayers().forEach(GunSG::playActiveSound);
    }

    @Override
    public long run(long l) {
        if (l % 10 == 0 || l < 6) {
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
        return configManager.getData("preDeathMatchTime", Long.class).orElse(20L);
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
