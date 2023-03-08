package me.rownox.rowmissiles.commands;

import me.rownox.rowmissiles.guis.MissileGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (p.hasPermission("rowMissiles.give")) {
                for (Missile missile : RowMissiles.missileList) {
                    p.getInventory().addItems(new ItemStack(missile.getMaterial));
                }
            }
        }
        return false;
    }
}
