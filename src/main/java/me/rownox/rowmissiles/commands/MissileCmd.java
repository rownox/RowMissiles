package me.rownox.rowmissiles.commands;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.guis.MissileGui;
import me.rownox.rowmissiles.objects.MissileObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                    p.getInventory().addItem(new ItemStack(Material.DISPENSER));
                    p.getInventory().addItem(new ItemStack(Material.CAULDRON));
                    p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("rowmissiles.reload")) {
                    RowMissiles.reloadConfigs();
                    p.sendMessage(ChatColor.GREEN + "RowMissiles configs successfully reloaded.");
                }
            } else {
                p.sendMessage("Commands: ", "/missiles", "/missiles give", "/missiles reload");
                return true;
            }
        }
        return false;
    }
}
