package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class InGameTask extends GunSGTask {

    private boolean DMEnabled;
    private int dmLocationSize;

    @Override
    public void initRun(PlayerManager playerManager) {
        MinigamesCore.getApi().getGameManager().setState(GameState.IN_GAME);
        this.dmLocationSize = MinigamesCore.getApi().getArenaManager().getFinalArena().getWarp("deathmatch").size();
        this.DMEnabled = playerManager.getGamePlayer().size() > dmLocationSize;
    }

    @Override
    public void onCancel() {
        MinigamesCore.getApi().getGameManager().endGame(playerManager.getGamePlayer(), null, true);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void run(long l) {
        if (l % 60 == 0){
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(configManager.getMessage("game-count").replace("<time>", time));
        }
        final long half = this.getTotalTime() / 2;
        if (l - half == 30 || l - half == 20 || l - half == 10 || l - half < 6){
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(configManager.getMessage("chest-refill-count").replace("<time>", time));
        }

        if (l == l - half){
            Bukkit.broadcastMessage(configManager.getMessage("chest-refill"));
            GunSG.getPlugin(GunSG.class).getChestsManager().refillChests();
        }

        if ((DMEnabled && playerManager.getGamePlayer().size() <= dmLocationSize || playerManager.getGamePlayer().size() == 2) && l > 30){
            l = 30;
        }

        if (l == 30 || l == 15 || l < 6) {
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);;
            Bukkit.broadcastMessage(configManager.getMessage("pre-deathmatch").replace("<time>", time));
        }

        int level = (int)l;
        Bukkit.getOnlinePlayers().forEach(p->p.setLevel(level));
    }

    @Override
    public long getTotalTime() {
        return configManager.getData("gameTime", Long.class).orElse(600L);
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
