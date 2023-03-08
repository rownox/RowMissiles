package me.rownox.rowmissles.missiles;

import me.rownox.rowmissles.RowMissles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        fill();
        addItems();
    }

    private void addItems() {
        for (Missile m : RowMissles.missileList) {
            ItemStack item = new ItemStack(m.getMaterial());
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + m.getName());
            itemMeta.setLore(m.getLore());
            item.setItemMeta(itemMeta);

            gui.setItem(m.getGuiSlot(), item);
        }
    }

    private void fill() {
        ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(ChatColor.BLACK + " ");
        pane.setItemMeta(paneMeta);

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, pane);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            if (e.getClickedInventory() == gui) {
                e.setCancelled(true);
            }
        }
    }
}
