package me.rownox.rowmissles.listeners;

import me.rownox.rowmissles.RowMissiles;
import me.rownox.rowmissles.objects.PlayerValues;
import me.rownox.rowmissles.utils.MissileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener {
    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        BlockData blockData = b.getBlockData();
        Player p = e.getPlayer();

        if (b.getType().equals(Material.DISPENSER)) {
            if (blockData instanceof Directional directional) {
                if (directional.getFacing() != BlockFace.UP) {
                    p.sendMessage("The dispenser is not facing upward.");
                    return;
                }
                p.sendMessage("You clicked a dispenser");

                Block blockUnder = b.getRelative(BlockFace.DOWN);
                Material material = blockUnder.getType();

                if (material == Material.CAULDRON) {
                    Levelled cauldron = (Levelled) blockUnder.getBlockData();
                    if (cauldron.getLevel() == cauldron.getMaximumLevel() && blockUnder.getRelative(BlockFace.DOWN).getType() == Material.LAVA) {
                        cauldron.setLevel(0);
                        MissileUtils.launchMissile(p, null);
                    } else {
                        p.sendMessage("Please refuel your launcher with lava.");
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
}
