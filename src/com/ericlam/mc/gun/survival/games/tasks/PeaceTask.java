package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;

public class PeaceTask extends GunSGTask {

    private GameBoard gameBoard;


    @Override
    public void initRun(PlayerManager playerManager) {
        Bukkit.broadcastMessage(configManager.getMessage("start"));
        gameBoard = GunSG.getPlugin(GunSG.class).getGameBoard();
        gameBoard.setLine("stats", "&7遊戲狀態: " + motdConfig.peace);
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
        if (l % 30 == 0 || l < 6) {
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(configManager.getMessage("peace-countdown").replace("<time>", time));
        }
        int level = (int) l;
        Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(level));
        return InGameTask.updateTimeShow(l, gameBoard);
    }

    @Override
    public long getTotalTime() {
        return gsgConfig.peaceTime;
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
