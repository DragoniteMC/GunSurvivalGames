package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;

import java.util.List;

public class DeathMatchTask extends GunSGTask {

    private GameBoard gameBoard;

    @Override
    public void initRun(PlayerManager playerManager) {
        gameBoard = GunSG.getPlugin(GunSG.class).getGameBoard();
        gameBoard.setLine("stats", "&7遊戲狀態: " + motdConfig.deathmatch);
    }

    @Override
    public void onCancel() {
        this.onFinish();
    }

    @Override
    public void onFinish() {
        MinigamesCore.getApi().getGameManager().endGame(List.of(), null, true);
    }

    @Override
    public long run(long l) {
        if (l % 60 == 0 || l < 6 || l == 10) {
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.broadcastMessage(msg.get("deathmatch-count").replace("<time>", time));
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            if (l == 60) {
                Bukkit.broadcastMessage(msg.get("glowing-mode"));
                playerManager.getGamePlayer().forEach(g -> g.getPlayer().setGlowing(true));
            }
        }
        int level = (int) l;
        Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(level));
        return InGameTask.updateTimeShow(l, gameBoard);
    }

    @Override
    public long getTotalTime() {
        return gsgConfig.deathMatchTime;
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
