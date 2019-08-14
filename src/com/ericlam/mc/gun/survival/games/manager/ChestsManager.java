package com.ericlam.mc.gun.survival.games.manager;

import com.hypernite.mc.hnmc.core.builders.InventoryBuilder;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.core.utils.Tools;
import com.shampaggon.crackshot.CSUtility;
import me.DeeCaaD.CrackShotPlus.CSPapi;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChestsManager {
    private FileConfiguration chests;
    private ConfigManager configManager;
    private List<ItemStack> tie1Items = new ArrayList<>();
    private List<ItemStack> tie2Items = new ArrayList<>();
    private CSUtility csUtility;

    private HashMap<Location, Inventory> chestCachedItems = new HashMap<>();

    public ChestsManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.chests = configManager.getConfig("chests.yml");
        this.csUtility = new CSUtility();
    }

    public ChestsManager loadTie1Items() {
        List<String> tie1Normal = chests.getStringList("tie1");
        List<String> tie1Gun = chests.getStringList("tie1-guns");
        return loadItems(tie1Normal, tie1Gun, tie1Items);
    }

    public ChestsManager loadTie2Items() {
        List<String> tie2Normal = chests.getStringList("tie2");
        List<String> tie2Gun = chests.getStringList("tie2-guns");
        return loadItems(tie2Normal, tie2Gun, tie2Items);
    }

    private ChestsManager loadItems(List<String> tieNormal, List<String> tieGun, List<ItemStack> tieItems) {
        for (String mater : tieNormal) {
            String[] item = mater.split(":");
            Material material = Material.valueOf(item[0]);
            int amount = Integer.parseInt(item[1]);
            tieItems.add(new ItemStack(material, amount));
        }
        for (String mater : tieGun) {
            String[] title = mater.split(":");
            ItemStack original_gun = csUtility.generateWeapon(title[0]);
            if (original_gun == null) continue;
            ItemStack gun = CSPapi.updateItemStackFeaturesNonPlayer(title[0], original_gun);
            int amount = Integer.parseInt(title[1]);
            gun.setAmount(amount);
            tieItems.add(gun);
        }
        return this;
    }

    private List<ItemStack> getRandomItems(List<ItemStack> itemStacks, int max) {
        List<ItemStack> returnItems = new ArrayList<>();
        if (itemStacks == null || itemStacks.size() == 0) return returnItems;
        for (int i = 0; i < max; i++) {
            int random = Tools.randomWithRange(0, itemStacks.size() - 1);
            returnItems.add(itemStacks.get(random));
        }
        return returnItems;
    }

    public void refillChests() {
        chestCachedItems.clear();
    }

    @Nullable
    public Inventory getFakeInventory(Block lootable) {
        Location location = lootable.getLocation();
        Inventory inventory = new InventoryBuilder(27, "").build();
        if (chestCachedItems.containsKey(location)) return chestCachedItems.get(location);
        int max;
        List<ItemStack> tieItems;
        switch (lootable.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
                max = configManager.getData("maxTie1Items", Integer.class).orElse(4);
                tieItems = tie1Items;
                break;
            case ENDER_CHEST:
                max = configManager.getData("maxTie2Items", Integer.class).orElse(5);
                tieItems = tie2Items;
                break;
            default:
                return null;
        }
        for (ItemStack randomItem : getRandomItems(tieItems, max)) {
            int random;
            do {
                random = Tools.randomWithRange(0, 26);
            } while (inventory.getItem(random) != null);
            inventory.setItem(random, randomItem);
        }
        chestCachedItems.putIfAbsent(location, inventory);
        return inventory;
    }
}
