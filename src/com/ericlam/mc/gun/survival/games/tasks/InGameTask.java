package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;

public class InGameTask extends GunSGTask {

    private boolean DMEnabled;
    private int dmLocationSize;
    private GameBoard gameBoard;
    private Arena arena;

    static long updateTimeShow(long l, GameBoard gameBoard) {
        int level = (int) l;
        MinigamesAPI api = MinigamesCore.getApi();
        Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(level));
        String timer = api.getGameUtils().getTimer(l);
        Arena arena = api.getArenaManager().getFinalArena();
        PlayerManager playerManager = api.getPlayerManager();
        gameBoard.setTitle(arena.getDisplayName().concat("§7 - §f").concat(timer));
        gameBoard.setLine("game", "&e存活者: &f".concat(playerManager.getGamePlayer().size() + ""));
        gameBoard.setLine("spec", "&e觀戰者: &f".concat(playerManager.getSpectators().size() + ""));
        return l;
    }

    @Override
    public void onCancel() {
        MinigamesCore.getApi().getGameManager().endGame(playerManager.getGamePlayer(), null, true);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void initRun(PlayerManager playerManager) {
        MinigamesCore.getApi().getGameManager().setState(GameState.IN_GAME);
        arena = MinigamesCore.getApi().getArenaManager().getFinalArena();
        this.dmLocationSize = arena.getWarp("deathmatch").size();
        this.DMEnabled = playerManager.getGamePlayer().size() > dmLocationSize;
        gameBoard = GunSG.getPlugin(GunSG.class).getGameBoard();
        gameBoard.setLine("stats", "&7遊戲狀態: ".concat(GunSG.getMotd("starting")));
    }

    @Override
    public long run(long l) {
        if (l % 60 == 0) {
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(configManager.getMessage("game-count").replace("<time>", time));
        }
        final long half = this.getTotalTime() / 2;
        if ((l - half == 30 || l - half == 20 || l - half == 10 || l - half < 6) && l > half) {
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l - half);
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            Bukkit.broadcastMessage(configManager.getMessage("chest-refill-count").replace("<time>", time));
        }

        if (l == half) {
            Bukkit.broadcastMessage(configManager.getMessage("chest-refill"));
            GunSG.getPlugin(GunSG.class).getChestsManager().refillChests();
        }

        if (((DMEnabled && playerManager.getGamePlayer().size() <= dmLocationSize)) && l > 30) {
            l = 30;
        }

        if (l == 30 || l == 15 || l < 6) {
            Bukkit.getOnlinePlayers().forEach(GunSG::playCountSound);
            String time = MinigamesCore.getApi().getGameUtils().getTimeWithUnit(l);
            ;
            Bukkit.broadcastMessage(configManager.getMessage("pre-deathmatch").replace("<time>", time));
        }

        return updateTimeShow(l, gameBoard);
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
