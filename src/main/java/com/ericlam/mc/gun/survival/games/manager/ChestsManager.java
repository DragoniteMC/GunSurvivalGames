package com.ericlam.mc.gun.survival.games.manager;

import com.ericlam.mc.gun.survival.games.config.ChestConfig;
import com.ericlam.mc.gun.survival.games.config.GSGConfig;
import com.hypernite.mc.hnmc.core.builders.InventoryBuilder;
import com.hypernite.mc.hnmc.core.utils.Tools;
import com.shampaggon.crackshot.CSUtility;
import me.DeeCaaD.CrackShotPlus.CSPapi;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChestsManager {
    private final ChestConfig config;
    private final GSGConfig gsgConfig;
    private final List<ItemStack> tie1Items = new ArrayList<>();
    private final List<ItemStack> tie2Items = new ArrayList<>();
    private final CSUtility csUtility;

    private final HashMap<Location, Inventory> chestCachedItems = new HashMap<>();

    public ChestsManager(ChestConfig config, GSGConfig gsgConfig) {
        this.config = config;
        this.gsgConfig = gsgConfig;
        this.csUtility = new CSUtility();
    }

    public void loadTie1Items() {
        List<String> tie1Normal = config.tie1Items;
        List<String> tie1Gun = config.tie1Guns;
        loadItems(tie1Normal, tie1Gun, tie1Items);
    }

    public void loadTie2Items() {
        List<String> tie2Normal = config.tie2Items;
        List<String> tie2Gun = config.tie2Guns;
        loadItems(tie2Normal, tie2Gun, tie2Items);
    }

    private void loadItems(List<String> tieNormal, List<String> tieGun, List<ItemStack> tieItems) {
        for (String mater : tieNormal) {
            var en = ChestConfig.parseItem(mater);
            if (en.getKey() == null) continue;
            tieItems.add(new ItemStack(en.getKey(), en.getValue()));
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
                max = gsgConfig.maxItemPerChest;
                tieItems = tie1Items;
                break;
            case ENDER_CHEST:
                max = gsgConfig.maxItemPerTie2;
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
