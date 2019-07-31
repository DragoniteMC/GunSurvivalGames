package com.ericlam.mc.gun.survival.games.tasks;

import com.ericlam.mc.csweapon.MolotovManager;
import com.ericlam.mc.gun.survival.games.main.GunSG;
import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.FireWorkManager;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class PreEndTask extends GunSGTask {

    private Player survivor;
    private FireWorkManager fireWorkManager;
    private Arena arena;

    @Override
    public void initRun(PlayerManager playerManager) {
        Bukkit.getOnlinePlayers().forEach(p->p.setLevel(0));
        this.fireWorkManager = MinigamesCore.getApi().getFireWorkManager();
        arena = MinigamesCore.getApi().getArenaManager().getFinalArena();
        arena.getWorld().getEntities().forEach(e->{
            if (e instanceof Item || e instanceof Projectile) e.remove();
        });
        fireWorkManager.spawnFireWork(arena.getWarp("game"));
        fireWorkManager.spawnFireWork(arena.getWarp("deathmatch"));
        if (GunSG.customEnabled) {
            MolotovManager.getInstance().resetFires();
            MolotovManager.getInstance().resetLavaBlocks();
        }
        this.survivor = playerManager.getGamePlayer().size() == 1 ? playerManager.getGamePlayer().get(0).getPlayer() : null;
        if (survivor != null) survivor.sendTitle(configManager.getPureMessage("win-title"), "", 20, 100, 20);
        Bukkit.broadcastMessage(configManager.getMessage("game-end").replace("<arena>", arena.getDisplayName()).replace("<player>", (survivor == null ? "無" : survivor.getDisplayName())));

        MinigamesCore.getApi().getGameStatsManager().saveAll().whenComplete((v,ex)->{
            if (ex != null){
                ex.printStackTrace();
                return;
            }

            GunSG.getProvidingPlugin(GunSG.class).getLogger().info("All player stats data has been saved");
        });
    }

    @Override
    public void onCancel() {
        this.onFinish();
    }

    @Override
    public void onFinish() {
        arena.getWorld().getEntities().forEach(e->{
            if (e instanceof Item || e instanceof Projectile) e.remove();
        });
        MinigamesCore.getApi().getGameManager().setState(GameState.ENDED);
    }

    @Override
    public long run(long l) {
        if (l % 3 == 0) {
            if (survivor != null) fireWorkManager.spawnFireWork(survivor);
        }
        return l;
    }

    @Override
    public long getTotalTime() {
        return 10;
    }

    @Override
    public boolean shouldCancel() {
        return false;
    }
}
