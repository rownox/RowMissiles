package me.rownox.rowmissiles.listeners;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.OreObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockBreakListener implements Listener {

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Material m = b.getType();

        if (p.getGameMode() != GameMode.SURVIVAL) return;
        for (OreObject ore : RowMissiles.ores) {
            Bukkit.broadcastMessage(ore.getBlockFrom().toString());
            Bukkit.broadcastMessage(m.toString());
            if (ore.getBlockFrom().equals(m)) {
                e.setCancelled(true);
                b.setType(Material.AIR);

                ItemStack unrefinedOre = new ItemStack(ore.getUnrefinedMat(), 1);
                ItemMeta itemMeta = unrefinedOre.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ore.getUnrefinedName()));
                itemMeta.addItemFlags(ItemFlag.values());
                unrefinedOre.setItemMeta(itemMeta);

                p.getWorld().dropItemNaturally(b.getLocation(), unrefinedOre);
                return;
            }
        }
    }
}
