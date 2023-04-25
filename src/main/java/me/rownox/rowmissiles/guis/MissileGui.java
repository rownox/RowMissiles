package me.rownox.rowmissiles.guis;

import me.rownox.rowmissiles.ConfigUtils;
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
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class MissileGui implements Listener {

    private static Inventory gui;

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

            ItemStack shopItem = ConfigUtils.missileItem(missile.getName(), missile.getColor());

            List<String> lore = List.of(
                    ChatColor.GRAY + "Range: " + missile.getRange(),
                    ChatColor.GRAY + "Radius: " + missile.getMagnitude(),
                    ChatColor.GRAY + "Speed: " + missile.getSpeed(),
                    ChatColor.GRAY + "Nuclear: " + missile.isNuclear()
            );

            ItemMeta meta = shopItem.getItemMeta();
            meta.setLore(lore);
            shopItem.setItemMeta(meta);

            gui.setItem(missile.getGuiSlot(), shopItem);
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
//            for (MissileObject missile : RowMissiles.missileList.keySet()) {
//                if (e.getCurrentItem() == missile.getItem()) {
//                    new RecipeGui(RowMissiles.missileList.get(missile));
//                }
//            }
        }
    }

    @EventHandler
    private void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory() == gui) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryMove(InventoryInteractEvent e){
        if (e.getInventory() == gui) {
            e.setCancelled(true);
        }
    }
}
