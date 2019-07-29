package com.ericlam.mc.gun.survival.games.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

public class HideTagManager {

    public static void hideTagPlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Snowball hide = (Snowball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SNOWBALL);
        hide.setBounce(false);
        hide.setSilent(true);
        hide.setInvulnerable(true);
        player.addPassenger(hide);
    }

    public static void hideAllPlayer() {
        MinigamesCore.getApi().getPlayerManager().getGamePlayer().forEach(HideTagManager::hideTagPlayer);
    }

    public static void showTagAllPlayer() {
        MinigamesCore.getApi().getPlayerManager().getGamePlayer().forEach(HideTagManager::showTagPlayer);
    }

    public static void showTagPlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        player.getPassengers().forEach(player::removePassenger);
    }
}
