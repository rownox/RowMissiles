package me.rownox.rowmissiles.guis;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.MissileObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Bukkit.getServer;

public class MissileGui implements Listener {

    Inventory gui;

    public MissileGui(Player p) {
        int rows = RowMissiles.config.getInt("gui_rows");

        gui = Bukkit.createInventory(p, 9 * rows, ChatColor.RED + "" + ChatColor.BOLD + "Missiles");
        fill();
        addItems();

        RowMissiles.registerExternalListener(this, getServer());

        p.openInventory(gui);
    }

    private void addItems() {
        for (MissileObject missile : RowMissiles.missileList.keySet()) {
            gui.setItem(missile.getGuiSlot(), missile.getItem());
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
        if (e.getInventory() == gui) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    private void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory() == gui) {
            e.setCancelled(true);
        }
    }
}
