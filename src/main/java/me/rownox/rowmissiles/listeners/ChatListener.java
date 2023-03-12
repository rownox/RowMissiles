package me.rownox.rowmissiles.listeners;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.MissileObject;
import me.rownox.rowmissiles.objects.PlayerValuesObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    private void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerValuesObject pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isSettingLocation()) {

            for (MissileObject missile : RowMissiles.missileList.keySet()) {
                if (missile.getItem().equals(p.getInventory().getItemInMainHand())) {
                    e.setCancelled(true);

                    String message = e.getMessage();
                    String[] parts = message.split(" ");
                    double x, z;

                    if (message.equalsIgnoreCase("cancel")) {
                        pValues.setSettingLocation(false);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&aLaunch successfully aborted."));
                        p.playSound(p.getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 1, 1);
                        return;
                    }

                    if (parts.length != 2) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&cYou didn't enter both coordinates correctly. Please try again."));
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        return;
                    }

                    try {
                        x = Double.parseDouble(parts[0]);
                        z = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException ex) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&cYou didn't enter valid coordinates. Please try again."));
                        return;
                    }

                    int highestY = p.getWorld().getHighestBlockYAt(new Location(p.getWorld(), x, 0, z));
                    Location targetLoc = new Location(p.getWorld(), x, highestY, z);
                    if (missile.getDistance(p.getLocation(), targetLoc) > missile.getRange()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&cThe coordinates you entered were out of missile range. Please re-enter them."));
                        return;
                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&aThe missile is now ready to launch. It's bound to hit &c" + (int) x + "&a, &c" + (int) z));
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);

                    pValues.setSettingLocation(false);
                    pValues.setReadyToLaunch(true);
                    pValues.setTargetLoc(targetLoc);
                }
            }
        }
    }
}
