package com.ericlam.mc.gun.survival.games.implement.handler;

import com.ericlam.mc.gun.survival.games.config.GSGConfig;
import com.ericlam.mc.gun.survival.games.implement.player.GunSGPlayer;
import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.GamePlayerHandler;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GunSGPlayerHandler implements GamePlayerHandler {

    @Override
    public void onPlayerStatusChange(GamePlayer gamePlayer, GamePlayer.Status status) {
        Player player = gamePlayer.getPlayer();
        player.setGameMode(gamePlayer.getStatus() == GamePlayer.Status.SPECTATING ? GameMode.SPECTATOR : GameMode.ADVENTURE);
        Optional.ofNullable(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).ifPresent(attr -> player.setHealth(attr.getBaseValue()));
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.setLevel(0);
        player.setFireTicks(0);
        player.setGlowing(false);
        if (GunSG.getPlugin(GunSG.class).getGameBoard() != null && status == GamePlayer.Status.SPECTATING) {
            GunSG.getPlugin(GunSG.class).getGameBoard().addPlayer(gamePlayer);
        }
    }

    @Override
    public void onPlayerRemove(GamePlayer gamePlayer) {

    }

    @Override
    public GamePlayer createGamePlayer(Player player) {
        return new GunSGPlayer(player, null);
    }

    @Override
    public int requireStart() {
        return GunSG.getYamlManager().getConfigAs(GSGConfig.class).requiredPlayers;
    }


}
