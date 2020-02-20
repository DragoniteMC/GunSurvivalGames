package com.ericlam.mc.gun.survival.games.implement.area;

import com.ericlam.mc.gun.survival.games.config.GSGConfig;
import com.ericlam.mc.gun.survival.games.config.LangConfig;
import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.google.common.collect.ImmutableMap;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class GunSGArenaConfig implements ArenaConfig {

    private final GSGConfig gsgConfig;
    private final Plugin plugin;

    public GunSGArenaConfig(GSGConfig gsgConfig, Plugin plugin) {
        this.gsgConfig = gsgConfig;
        this.plugin = plugin;
    }

    @Override
    public File getArenaFolder() {
        return new File(plugin.getDataFolder(), "Arenas");
    }

    @Override
    public int getMaxLoadArena() {
        return gsgConfig.maxLoadArenas;
    }

    @Override
    public void setExtraWorldSetting(@Nonnull World world) {
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
    }

    @Override
    public ImmutableMap<String, Integer> getAllowWarps() {
        return ImmutableMap.<String, Integer>builder().put("deathmatch", 5).put("game", 24).build();
    }

    @Override
    public Location getLobbyLocation() {
        return gsgConfig.lobbyLocation;
    }

    @Override
    public String getFallBackServer() {
        return gsgConfig.fallbackServer;
    }

    @Override
    public String getGamePrefix() {
        return GunSG.getYamlManager().getConfigAs(LangConfig.class).getPrefix();
    }

    @Override
    public CompletableFuture<Boolean> setLobbyLocation(Location location) {
        return CompletableFuture.supplyAsync(() -> {
            gsgConfig.lobbyLocation = location;
            try {
                gsgConfig.save();
                return true;
            } catch (IOException e) {
                return false;
            }
        });
    }
}
