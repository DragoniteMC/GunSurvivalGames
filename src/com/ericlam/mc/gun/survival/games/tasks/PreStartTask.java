package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class PreStartTask extends GunSGTask {

    private GameBoard gameBoard;
    private Arena arena;

    @Override
    public void initRun(PlayerManager playerManager) {
        MinigamesCore.getApi().getGameManager().setState(GameState.PRESTART);
        arena = MinigamesCore.getApi().getArenaManager().getFinalArena();
        List<Location> spawns = arena.getWarp("game");
        MinigamesCore.getApi().getGameUtils().noLagTeleport(playerManager.getGamePlayer(), spawns, 2L);
        gameBoard = GunSG.getPlugin(GunSG.class).getGameBoard();
        gameBoard.setLine("stats", "&7遊戲狀態: ".concat(GunSG.getMotd("preStart")));
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
        return InGameTask.updateTimeShow(l, gameBoard);
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
