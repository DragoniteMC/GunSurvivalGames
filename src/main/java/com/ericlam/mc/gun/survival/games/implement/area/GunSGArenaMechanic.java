package com.ericlam.mc.gun.survival.games.implement.area;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.arena.ArenaMechanic;
import com.ericlam.mc.minigames.core.arena.CreateArena;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class GunSGArenaMechanic implements ArenaMechanic {

    @Override
    public CreateArena loadCreateArena(FileConfiguration fileConfiguration, Arena arena) {
        return new GunSGArena(arena.getAuthor(), arena.getArenaName(), arena.getDisplayName(), arena.getLocationsMap(), arena.getWorld(), arena.getDescription());
    }

    @Override
    public CreateArena createArena(@Nonnull String s, @Nonnull Player player) {
        return new GunSGArena(player.getName(), s, player.getWorld());
    }

    @Override
    public void saveExtraArenaSetting(FileConfiguration fileConfiguration, Arena arena) {
        //
    }
}
