package com.ericlam.mc.gun.survival.games.main;

import com.ericlam.mc.gun.survival.games.GunSGConfig;
import com.ericlam.mc.gun.survival.games.command.GunSGArenaCommand;
import com.ericlam.mc.gun.survival.games.implement.area.GunSGArenaMechanic;
import com.ericlam.mc.gun.survival.games.implement.handler.GunSGPlayerHandler;
import com.ericlam.mc.gun.survival.games.implement.handler.GunSGStatsHandler;
import com.ericlam.mc.gun.survival.games.listener.GunSGListener;
import com.ericlam.mc.gun.survival.games.manager.ChestsManager;
import com.ericlam.mc.gun.survival.games.states.*;
import com.ericlam.mc.gun.survival.games.tasks.*;
import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.event.arena.FinalArenaLoadedEvent;
import com.ericlam.mc.minigames.core.event.section.GamePreEndEvent;
import com.ericlam.mc.minigames.core.event.section.GameStartEvent;
import com.ericlam.mc.minigames.core.event.section.GameVotingEvent;
import com.ericlam.mc.minigames.core.factory.compass.CompassTracker;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.registable.Compulsory;
import com.ericlam.mc.minigames.core.registable.Voluntary;
import com.hypernite.mc.hnmc.core.builders.InventoryBuilder;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GunSG extends JavaPlugin implements Listener {

    private static ConfigManager configManager;

    public static ConfigManager config() {
        return configManager;
    }

    public static void playActiveSound(Player player){
        String[] sounds = configManager.getData("activeSound", String[].class).orElse("BLOCK_NOTE_BLOCK_PLING:1:1".split(":"));
        MinigamesCore.getApi().getGameUtils().playSound(player, sounds);
    }

    public static void playCountSound(Player player){
        String[] sounds = configManager.getData("countdownSound", String[].class).orElse("BLOCK_NOTE_BLOCK_SNARE:1:1".split(":"));
        MinigamesCore.getApi().getGameUtils().playSound(player, sounds);
    }

    public static boolean customEnabled, corpseEnabled;

    private ChestsManager chestsManager;
    private InGameState peaceState;
    private CompassTracker compassTracker;

    public ChestsManager getChestsManager() {
        return chestsManager;
    }

    public InGameState getPeaceState() {
        return peaceState;
    }

    @Override
    public void onEnable() {
        GunSGConfig config = new GunSGConfig(this);
        configManager = HyperNiteMC.getAPI().registerConfig(config);
        configManager.setMsgConfig("lang.yml");
        chestsManager = new ChestsManager(configManager);
        peaceState = new PeaceState(configManager);
        customEnabled = getServer().getPluginManager().getPlugin("CustomCSWeapon") != null;
        corpseEnabled = getServer().getPluginManager().getPlugin("CorpseReborn") != null;
        this.getServer().getPluginManager().registerEvents(this, this);
        Compulsory compulsory = MinigamesCore.getRegistration().getCompulsory();
        compulsory.registerGamePlayerHandler(new GunSGPlayerHandler());
        compulsory.registerGameStatsHandler(new GunSGStatsHandler());
        compulsory.registerArenaConfig(config);
        compulsory.registerArenaMechanic(new GunSGArenaMechanic());
        compulsory.registerArenaCommand(new GunSGArenaCommand(), this);
        compulsory.registerVoteGUI(new InventoryBuilder(3, "&9地圖投票").ring(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE)), 11, 13, 15);
        compulsory.registerLobbyTask(new CountdownTask());
        compulsory.registerEndTask(new PreEndTask());
        Voluntary voluntary = MinigamesCore.getRegistration().getVoluntary();
        voluntary.registerGameTask(new PreStartState(configManager), new PreStartTask());
        voluntary.registerGameTask(peaceState, new PeaceTask());
        voluntary.registerGameTask(new StartingState(configManager), new InGameTask());
        voluntary.registerGameTask(new PreDMState(configManager), new PreDeathMatchTask());
        voluntary.registerGameTask(new DeathmatchState(configManager), new DeathMatchTask());
        compassTracker = MinigamesCore.getProperties().getGameFactory()
                .getCompassFactory()
                .setTrackerRange(configManager.getData("compassMaxTrack", Integer.class).orElse(100))
                .setSearchingText("&b&l搜&r&7索中...", "&7搜&b&l索&r&7中...", "&7搜索&b&l中&r&7...")
                .setCaughtText("&e玩家:&f <target> &7| &e距離:&f <distance>").build();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onGameActivated(GameVotingEvent e){
        this.getServer().getPluginManager().registerEvents(new GunSGListener(), this);
    }

    @EventHandler
    public void onGamePreStart(GameStartEvent e){
        compassTracker.launch();
    }

    @EventHandler
    public void onGamePreEnd(GamePreEndEvent e){
        compassTracker.destroy();
    }

    @EventHandler
    public void onFinalArenaLoaded(FinalArenaLoadedEvent e) {
        Arena arena = e.getFinalArena();
        Bukkit.broadcastMessage(GunSG.config().getMessage("map-final-result").replace("<map>", arena.getDisplayName()));
        chestsManager.loadTie1Items();
        chestsManager.loadTie2Items();
    }


}
