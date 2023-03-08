package me.rownox.rowmissiles.listeners;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.Missile;
import me.rownox.rowmissiles.objects.PlayerValues;
import me.rownox.rowmissiles.utils.MissileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Material item = p.getInventory().getItemInMainHand().getType();

        if (p.getGameMode() != GameMode.SURVIVAL) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        for (Missile missile : RowMissiles.missileList.keySet()) {
            if (missile.getMaterial().equals(item)) {
                Block b = e.getClickedBlock();
                if (b == null) return;
                BlockData blockData = b.getBlockData();

                if (blockData instanceof Directional directional) {

                    if (!b.getType().equals(Material.DISPENSER)) return;
                    e.setCancelled(true);

                    if (directional.getFacing() != BlockFace.UP) {
                        p.sendMessage("The dispenser is not facing upward.");
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        return;
                    }

                    Block blockUnder = b.getLocation().subtract(0, 1, 0).getBlock();
                    if (blockUnder.getType().equals(Material.LAVA_CAULDRON)) {
                        blockUnder.setType(Material.CAULDRON);
                        MissileUtils.launchMissile(p, null);
                    } else {
                        p.sendMessage(RowMissiles.prefix + ChatColor.YELLOW + "Fuel the launcher by placing a lava cauldron underneath the dispenser.");
                    }
                }
            }
        }
    }

    @EventHandler
    private void onCraft(CraftItemEvent e) {

    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerValues pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isSettingLocation()) {

            String message = e.getMessage();
            String[] parts = message.split(" ");
            double x, y, z;

            if (e.getMessage().equalsIgnoreCase("cancel")) {
                pValues.setSettingLocation(false);
                p.sendMessage(RowMissiles.prefix + ChatColor.GREEN + "Launch successfully aborted.");
            }

            if (parts.length != 3) {
                p.sendMessage(
                        RowMissiles.prefix + ChatColor.RED + "You did not enter the 3 numbers correctly. Please try again.",
                        RowMissiles.prefix + ChatColor.RED + "Type 'cancel' to abort this launch."
                );
                return;
            }

            try {
                x = Double.parseDouble(parts[0]);
                y = Double.parseDouble(parts[1]);
                z = Double.parseDouble(parts[2]);
            } catch (NumberFormatException ex) {
                return;
            }

            p.sendMessage(
                    RowMissiles.prefix + ChatColor.YELLOW + "You set the location to " + x + ", " + y +  ", " + z,
                    RowMissiles.prefix + ChatColor.GREEN + "You are now ready to launch!"
            );

            MissileUtils.launchMissile(p, new Location(p.getWorld(), x, y, z));
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        RowMissiles.playerValues.put(p.getUniqueId(), new PlayerValues());
    }
}
