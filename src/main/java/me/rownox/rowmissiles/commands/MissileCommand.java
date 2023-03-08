package me.rownox.rowmissiles.commands;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.guis.MissileGui;
import me.rownox.rowmissiles.objects.Missile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MissileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length < 1) {
                new MissileGui(p);
                return true;
            } else if (args[0].equalsIgnoreCase("give")) {
                if (p.hasPermission("rowmissiles.give")) {
                    for (Missile missile : RowMissiles.missileList.keySet()) {
                        p.getInventory().addItem(new ItemStack(missile.getMaterial()));
                        return true;
                    }
                }
            } else {
                p.sendMessage("Commands: ", "/missiles", "/missiles give");
            }
        }
        return false;
    }
}
