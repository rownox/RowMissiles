package me.rownox.rowmissles.missiles;

import me.rownox.rowmissles.RowMissles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MissileGui implements Listener {

    Inventory gui;

    public MissileGui(Player p) {
        p.openInventory(gui);

        gui = Bukkit.createInventory(p, 9*3, ChatColor.RED + "" + ChatColor.BOLD + "Missiles");

    }

    private void addItems() {
        int x = 11;
        for (Missile m : RowMissles.missileList) {
            ItemStack item = new ItemStack(m.getMaterial());
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + m.getName());
            itemMeta.setLore(m.getLore());
            item.setItemMeta(itemMeta);

            gui.setItem(x, item);
            x++;
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            if (e.getClickedInventory() == gui) {

            }
        }
    }
}
