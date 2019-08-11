package com.ericlam.mc.gun.survival.games.command;

import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.exception.gamestats.PlayerNotExistException;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GunSGInfoCommand extends CommandNode {
    public GunSGInfoCommand() {
        super(null, "gsg-info", null, "查看戰績指令", "[player]", "gsginfo", "gunsg-info");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        CoreConfig coreConfig = HyperNiteMC.getAPI().getCoreConfig();
        Player target;
        if (list.size() < 1) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(coreConfig.getPrefix() + coreConfig.getNotPlayer());
                return true;
            }
            target = (Player) commandSender;
        } else {
            final String name = list.get(0);
            target = Bukkit.getPlayer(name);
            if (target == null) {
                commandSender.sendMessage(coreConfig.getPrefix() + coreConfig.getNotFoundPlayer());
                return true;
            }
        }

        Optional<MinigamesAPI> apiSafe = MinigamesCore.getApiSafe();
        if (apiSafe.isEmpty()) {
            commandSender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + "Minigames-API 沒有被啟動。");
        }
        MinigamesAPI api = apiSafe.get();
        try {
            commandSender.sendMessage(Arrays.stream(api.getGameStatsManager().getStatsInfo(target)).map(l -> l.replace("<player>", target.getDisplayName())).toArray(String[]::new));
        } catch (PlayerNotExistException e) {
            commandSender.sendMessage(GunSG.config().getMessage("gamestats-not-found"));
        }
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        return null;
    }
}
