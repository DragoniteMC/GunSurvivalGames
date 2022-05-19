package com.ericlam.mc.gun.survival.games.command;

import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;

public class GunSGArenaCommand extends DefaultCommand {

    public GunSGArenaCommand() {
        super(null, "gsg-arena", Perm.OWNER, "Gunsg 場地指令", "gunsg-arena", "gsgarena", "gunsgarena");
    }
}
