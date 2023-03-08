package me.rownox.rowmissiles.commands;

import me.rownox.rowmissiles.guis.MissileGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            new MissileGui(p);
            return true;
        }
        return false;
    }
}
