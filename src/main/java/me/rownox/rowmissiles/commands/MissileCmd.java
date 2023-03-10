package me.rownox.rowmissiles.commands;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.guis.MissileGui;
import me.rownox.rowmissiles.objects.MissileObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissileCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length < 1) {
                new MissileGui(p);
                return true;
            } else if (args[0].equalsIgnoreCase("give")) {
                if (p.hasPermission("rowmissiles.give")) {
                    for (MissileObject missile : RowMissiles.missileList.keySet()) {
                        p.getInventory().addItem(missile.getItem());
                    }
                    return true;
                }
            } else {
                p.sendMessage("Commands: ", "/missiles", "/missiles give");
                return true;
            }
        }
        return false;
    }
}
