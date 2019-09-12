package com.ericlam.mc.gun.survival.games.implement.player;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GunSGPlayer implements GamePlayer {

    private final Player player;
    private Status status;

    public GunSGPlayer(Player player, Status status) {
        this.player = player;
        Optional.ofNullable(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).ifPresent(a -> {
            a.setBaseValue(100);
            player.setHealthScale(20);
            player.setHealth(a.getBaseValue());
            player.setFoodLevel(20);
        });
        this.status = status;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }
}
