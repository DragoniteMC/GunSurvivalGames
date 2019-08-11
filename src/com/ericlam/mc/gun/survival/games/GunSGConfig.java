package com.ericlam.mc.gun.survival.games;

import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.google.common.collect.ImmutableMap;
import com.hypernite.mc.hnmc.core.config.ConfigSetter;
import com.hypernite.mc.hnmc.core.config.Extract;
import com.hypernite.mc.hnmc.core.utils.converters.LocationSerializer;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class GunSGConfig extends ConfigSetter implements ArenaConfig {

    private final File arenaFolder;
    private String fallbackServer;
    private String gamePrefix;
    private ConfigurationSection lobby;
    private int maxLoadedArenas;
    private File configFile;
    private FileConfiguration config;
    @Extract
    private String wantedItemMaterial;
    @Extract
    private String wantedItemName;
    @Extract
    private List<String> wantedItemLore;
    @Extract
    private List<Double> wantedDoubleList;

    @Extract private int requiredPlayers;
    @Extract private int maxTie1Items;
    @Extract private int maxTie2Items;
    @Extract private int boostPlayers;

    @Extract private long gameTime;
    @Extract private long peaceTime;
    @Extract private long countdownTime;
    @Extract private long boostTime;
    @Extract private long preStartTime;
    @Extract private long preDeathMatchTime;
    @Extract private long deathMatchTime;

    @Extract private int compassMaxTrack;
    @Extract private int rewardKills;
    @Extract private int rewardWins;

    @Extract private String[] countdownSound;
    @Extract private String[] activeSound;

    @Extract private String preStart;
    @Extract private String peace;
    @Extract private String starting;
    @Extract private String preDeathmatch;
    @Extract private String deathmatch;

    public GunSGConfig(Plugin plugin) {
        super(plugin, "config.yml", "chests.yml", "lang.yml", "motd.yml");
        this.arenaFolder = new File(plugin.getDataFolder(), "Arenas");
        if (!arenaFolder.exists()) arenaFolder.mkdirs();
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    @Override
    public void loadConfig(Map<String, FileConfiguration> map) {
        config = map.get("config.yml");
        this.fallbackServer = config.getString("fallback-server");
        this.gamePrefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(map.get("lang.yml").getString("prefix")));
        this.lobby = config.getConfigurationSection("lobby-location");
        this.maxLoadedArenas = config.getInt("max-loaded-arenas");

        this.requiredPlayers = config.getInt("required-players");
        this.maxTie1Items = config.getInt("max-items-per-chest");
        this.maxTie2Items = config.getInt("max-items-per-tie2");
        this.boostPlayers = config.getInt("boost-players");
        this.gameTime = config.getLong("game-time");
        this.peaceTime = config.getLong("peace-time");
        this.countdownTime = config.getLong("countdown-time");
        this.boostTime = config.getLong("boost-time");
        this.preStartTime = config.getLong("prestart-time");
        this.preDeathMatchTime = config.getLong("predeathmatch-time");
        this.deathMatchTime = config.getLong("deathmatch-time");
        this.compassMaxTrack = config.getInt("compass-max-track");
        this.rewardKills = config.getInt("reward-kills");
        this.rewardWins = config.getInt("reward-wins");
        this.wantedItemLore = config.getStringList("wanted-item.lore");
        this.wantedItemName = config.getString("wanted-item.name");
        this.wantedItemMaterial = config.getString("wanted-item.material");
        this.wantedDoubleList = config.getDoubleList("wanted-item.money");
        this.countdownSound = Objects.requireNonNull(config.getString("count-down-sound")).split(":");
        this.activeSound = Objects.requireNonNull(config.getString("active-sound")).split(":");

        FileConfiguration motd = map.get("motd.yml");
        this.preStart = motd.getString("PRE-START");
        this.peace = motd.getString("PEACE");
        this.starting = motd.getString("STARTING");
        this.preDeathmatch = motd.getString("PRE-DEATHMATCH");
        this.deathmatch = motd.getString("DEATHMATCH");
    }

    @Override
    public File getArenaFolder() {
        return arenaFolder;
    }

    @Override
    public int getMaxLoadArena() {
        return maxLoadedArenas;
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
        return lobby == null ? null : LocationSerializer.mapToLocation(lobby).orElse(null);
    }

    @Override
    public String getFallBackServer() {
        return fallbackServer;
    }

    @Override
    public String getGamePrefix() {
        return gamePrefix;
    }

    @Override
    public CompletableFuture<Boolean> setLobbyLocation(Location location) {
        return CompletableFuture.supplyAsync(()->{
            config.createSection("lobby-location", LocationSerializer.locToConfigSection(location));
            try {
                config.save(configFile);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
