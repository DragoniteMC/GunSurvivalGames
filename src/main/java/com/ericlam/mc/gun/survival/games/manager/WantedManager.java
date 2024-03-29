package com.ericlam.mc.gun.survival.games.manager;

import com.ericlam.mc.gun.survival.games.config.GSGConfig;
import com.ericlam.mc.gun.survival.games.config.LangConfig;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.dragonitemc.dragoneconomy.api.EconomyService;
import org.dragonitemc.dragoneconomy.api.UpdateResult;
import com.dragonite.mc.dnmc.core.builders.InventoryBuilder;
import com.dragonite.mc.dnmc.core.builders.ItemStackBuilder;
import com.dragonite.mc.dnmc.core.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WantedManager {

    private final Map<GamePlayer, ItemStack> itemStackMap = new HashMap<>();
    private final Map<Player, Map<Player, Double>> spentList = new HashMap<>();
    private final EconomyService economy;
    private ItemStack wantedItem;
    private Inventory inventory;
    private final Map<Player, Inventory> rewardInventory = new HashMap<>();
    private final GSGConfig.WantedItem wantedOption;
    private final LangConfig msg;

    public WantedManager(EconomyService economy, YamlManager yamlManager) {
        this.economy = economy;
        this.msg = yamlManager.getConfigAs(LangConfig.class);
        this.wantedOption = yamlManager.getConfigAs(GSGConfig.class).wantedItem;
    }

    public void loadWantedItem() {
        //Validate.notNull(inventory, "wanted inventory has not loaded");
        String name = wantedOption.name;
        List<String> lore = wantedOption.lore;
        Material material = wantedOption.material;
        if (material == null) material = Material.STONE;
        wantedItem = new ItemStackBuilder(material).displayName(name).lore(lore)
                .onClick(e -> {
                    e.setCancelled(true);
                    Player player = (Player) e.getWhoClicked();
                    if (MinigamesCore.getApi().getGameManager().getGameState() != GameState.IN_GAME) {
                        player.sendMessage(msg.get("only-in-game"));
                        return;
                    }
                    player.openInventory(inventory);
                }).build();
    }

    public ItemStack getWantedItem() {
        return wantedItem;
    }

    public void loadWantedInventory(List<GamePlayer> gamePlayers) {
        int row = (int) Math.ceil((double) gamePlayers.size() / 9);
        InventoryBuilder inventoryBuilder = new InventoryBuilder(row == 0 ? 1 : row, "&c懸賞名單");
        for (GamePlayer gamePlayer : gamePlayers) {
            Player player = gamePlayer.getPlayer();
            this.rewardInventory.putIfAbsent(player, this.createRewardInventory(player));
            ItemStack item = new ItemStackBuilder(Material.PLAYER_HEAD).displayName(ChatColor.YELLOW + player.getDisplayName()).lore("&b點擊設置懸賞數量").head(player.getUniqueId(), player.getName())
                    .onClick(e -> {
                        e.setCancelled(true);
                        Player clicker = (Player) e.getWhoClicked();
                        if (!player.isOnline()) {
                            clicker.sendMessage(MinigamesCore.getProperties().getMessageGetter().get("spectate-not-gamer"));
                            return;
                        }
                        clicker.openInventory(this.rewardInventory.get(player));
                    }).build();
            inventoryBuilder.item(item);
            this.itemStackMap.put(gamePlayer, item);
        }
        inventory = inventoryBuilder.build();
    }

    public void removeGamer(GamePlayer player) {
        Optional.ofNullable(this.itemStackMap.get(player)).ifPresent(e -> {
            inventory.remove(e);
            this.rewardInventory.remove(player.getPlayer());
        });
    }

    private Inventory createRewardInventory(@Nonnull Player player) {
        List<Double> doubleList = wantedOption.money;
        int row = (int) Math.ceil((double) doubleList.size() / 9);
        InventoryBuilder builder = new InventoryBuilder(row == 0 ? 1 : row, "&c懸賞 ".concat(player.getDisplayName()).concat(" 的價目"));
        for (double dou : doubleList) {
            new ItemStackBuilder(Material.GOLD_NUGGET).displayName(ChatColor.GOLD + "" + dou + " Gems").lore("&7點擊以懸賞")
                    .onClick(e -> {
                        e.setCancelled(true);
                        Player clicker = (Player) e.getWhoClicked();
                        if (!player.isOnline()) {
                            clicker.sendMessage(MinigamesCore.getProperties().getMessageGetter().get("spectate-not-gamer"));
                            return;
                        }
                        String msg = this.msg.get("no-gems");
                        if (economy.withdrawPlayer(clicker.getUniqueId(), dou) != UpdateResult.SUCCESS) {
                            clicker.sendMessage(msg);
                            return;
                        }
                        this.spentList.putIfAbsent(clicker, new HashMap<>());
                        this.spentList.get(clicker).computeIfPresent(player, (p, d) -> d + dou);
                        this.spentList.get(clicker).putIfAbsent(player, dou);
                        double bounty = this.spentList.values().stream().filter(map -> map.containsKey(player)).mapToDouble(map -> map.get(player)).sum();
                        Bukkit.broadcastMessage(this.msg.get("bounty-set")
                                .replace("<player>", clicker.getDisplayName())
                                .replace("<target>", player.getDisplayName()).replace("<money>", dou + "")
                                .replace("<bounty>", round(bounty)));
                    }).buildWithSkin(builder::item);
        }
        return builder.build();
    }

    private String round(double d) {
        return new BigDecimal(d).round(new MathContext(2, RoundingMode.HALF_EVEN)).toPlainString();
    }

    public void onBountyKill(Player killer, Player victim) {
        if (this.spentList.values().stream().noneMatch(map -> map.containsKey(victim))) return;
        double bounty = this.spentList.values().stream().filter(map -> map.containsKey(victim)).mapToDouble(map -> map.get(victim)).sum();
        economy.depositPlayer(killer.getUniqueId(), bounty);
        String msg = this.msg.get("bounty-get").replace("<player>", killer.getDisplayName()).replace("<target>", victim.getDisplayName()).replace("<money>", round(bounty));
        Bukkit.broadcastMessage(msg);
    }


    public void onBountyFail(Player victim) {
        if (this.spentList.values().stream().noneMatch(map -> map.containsKey(victim))) return;
        Map<Player, Map<Player, Double>> returnList = this.spentList.entrySet().stream().filter(map -> map.getValue().containsKey(victim)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        returnList.forEach((k, v) -> {
            double returnMoney = v.get(victim);
            economy.depositPlayer(k.getUniqueId(), returnMoney);
            k.sendMessage(msg.get("bounty-fail").replace("<money>", round(returnMoney)));
            v.remove(victim);
        });
    }


    public void onBountyWin(Player victim) {
        if (this.spentList.values().stream().noneMatch(map -> map.containsKey(victim))) return;
        double bounty = this.spentList.values().stream().filter(map -> map.containsKey(victim)).mapToDouble(map -> map.get(victim)).sum();
        economy.depositPlayer(victim.getUniqueId(), bounty);
        Bukkit.broadcastMessage(msg.get("bounty-win").replace("<player>", victim.getDisplayName()).replace("<money>", round(bounty)));
        this.spentList.forEach((k, v) -> v.remove(victim));
    }
}
