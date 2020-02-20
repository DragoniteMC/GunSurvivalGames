package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class PreDeathMatchTask extends GunSGTask {

    private GameBoard gameBoard;

    @Override
    public void initRun(PlayerManager playerManager) {
        List<Location> spawns = MinigamesCore.getApi().getArenaManager().getFinalArena().getWarp("deathmatch");
        MinigamesCore.getApi().getGameUtils().noLagTeleport(playerManager.getGamePlayer(), spawns, 2L);
        playerManager.getSpectators().forEach(g -> {
            Player player = g.getPlayer();
            player.setSpectatorTarget(null);
        });
        MinigamesCore.getApi().getGameUtils().noLagTeleport(playerManager.getSpectators(), 2L, spawns.get(0));
        gameBoard = GunSG.getPlugin(GunSG.class).getGameBoard();
        gameBoard.setLine("stats", "&7遊戲狀態: ".concat(motdConfig.preDeathMatch));
    }

    @Override
    public void onCancel() {
        MinigamesCore.getApi().getGameManager().endGame(playerManager.getGamePlayer(), null, true);
    }

    @Override
    public void onFinish() {
        Bukkit.broadcastMessage(msg.get("peace-finish"));
        Bukkit.getOnlinePlayers().forEach(GunSG::playActiveSound);
    }

    @Override
    public long run(long l) {
        if (l % 10 == 0 || l < 6) {
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(msg.get("peace-countdown").replace("<time>", time));
        }
        int level = (int) l;
        Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(level));
        return InGameTask.updateTimeShow(l, gameBoard);
    }

    @Override
    public long getTotalTime() {
        return gsgConfig.preDeathMatchTime;
    }

    @Override
    public boolean shouldCancel() {
        return playerManager.getGamePlayer().size() <= 1;
    }
}
