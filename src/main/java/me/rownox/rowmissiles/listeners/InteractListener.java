package me.rownox.rowmissiles.listeners;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.MissileObject;
import me.rownox.rowmissiles.objects.PlayerValuesObject;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InteractListener implements Listener {
    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        PlayerValuesObject pValues = RowMissiles.playerValues.get(p.getUniqueId());
        Block b = e.getClickedBlock();
        if (b == null) return;

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        for (MissileObject MM : RowMissiles.missileList.keySet()) {
            if (MM.getItem().equals(item)) {
                if (b.getType().equals(Material.DISPENSER)) {
                    if (b.getBlockData() instanceof Directional directional) {
                        e.setCancelled(true);

                        if (directional.getFacing() != BlockFace.UP) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&cPlease face the launcher upward."));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                            return;
                        }

                        Block blockUnder = b.getLocation().subtract(0, 1, 0).getBlock();
                        if (blockUnder.getType().equals(Material.LAVA_CAULDRON)) {
                            new MissileObject(MM.getItem(), MM.getColor(), MM.getRange(), MM.getMagnitude(), MM.getSpeed()).launch(p, pValues.getTargetLoc(), blockUnder);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&eFuel the launcher by placing a lava cauldron underneath the dispenser."));
                        }
                    }
                    return;
                }
                MM.spawnMissile(b.getLocation());
                if (p.getGameMode() == GameMode.SURVIVAL)
                    p.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
            }
        }

    }

    private String isMissileArmorStand(ArmorStand armorStand) {
        NamespacedKey key = new NamespacedKey(RowMissiles.getInstance(), "missile");

        return armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player p = event.getPlayer();

        if (entity instanceof ArmorStand) {

            String missileKey = isMissileArmorStand((ArmorStand) entity);

            if (missileKey != null) {
                if (event.getPlayer().isSneaking()) {
                    MissileObject missile = matchMissileId(missileKey);
                    if (missile == null) return;

                    entity.getWorld().spawnParticle(Particle.SMOKE_LARGE, entity.getLocation(), 5, 0.2, 0.2, 0.2, 0);
                    p.getInventory().addItem(missile.getItem());
                    entity.remove();
                }
            }
        }
    }

    private MissileObject matchMissileId(String id) {
        for (MissileObject MM : RowMissiles.missileList.keySet()) {
            if (MM.getId().equalsIgnoreCase(id)) {
                return MM;
            }
        }
        return null;
    }
}
