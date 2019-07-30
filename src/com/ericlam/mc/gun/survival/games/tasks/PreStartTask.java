package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class PreStartTask extends GunSGTask {

    @Override
    public void initRun(PlayerManager playerManager) {
        MinigamesCore.getApi().getGameManager().setState(GameState.PRESTART);
        List<Location> spawns = MinigamesCore.getApi().getArenaManager().getFinalArena().getWarp("game");
        for (int i = 0; i < Math.min(spawns.size(), playerManager.getGamePlayer().size()); i++) {
            playerManager.getGamePlayer().get(i).getPlayer().teleportAsync(spawns.get(i));
        }
    }

    @Override
    public void onCancel() {
        MinigamesCore.getApi().getGameManager().endGame(playerManager.getGamePlayer(), null, true);
    }

    @Override
    public void onFinish() {
        playerManager.getGamePlayer().forEach(g->{
            Player player = g.getPlayer();
            GunSG.playActiveSound(player);
            player.sendTitle("", configManager.getPureMessage("start-title"), 0, 30, 20);
        });
    }

    @Override
    public long run(long l) {
        if (l % 10 == 0 || l <= 5){
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            String subTitle = configManager.getPureMessage("pre-start-title").replace("<time>", time);
            playerManager.getGamePlayer().forEach(g->{
                Player player = g.getPlayer();
                player.sendTitle("", subTitle, 10 , 20, 10);
                GunSG.playCountSound(player);
            });
            Bukkit.broadcastMessage(configManager.getMessage("pre-start".concat(l <= 5 ? "-5" : "")).replace("<time>", time));
        }
        int level = (int)l;
        Bukkit.getOnlinePlayers().forEach(p->p.setLevel(level));
        return l;
    }

    @Override
    public long getTotalTime() {
        return configManager.getData("preStartTime", Long.class).orElse(15L);
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
