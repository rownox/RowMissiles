package me.rownox.rowmissiles.objects;

import me.rownox.rowmissiles.RowMissiles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;

public class Missile {

    private final String name;
    private final List<String> lore;
    private final ItemStack item;
    private final Material material;
    private final int range;
    private final int radius;
    private final int speed;
    private final boolean nuclear;
    private final int guiSlot;

    public Missile(String name, List<String> lore, ItemStack item, Material material, int range, int radius, int speed, boolean radioactive, int guiSlot) {
        this.name = name;
        this.lore = lore;
        this.item = item;
        this.material = material;
        this.range = range;
        this.radius = radius;
        this.speed = speed;
        this.nuclear = radioactive;
        this.guiSlot = guiSlot;
    }

    public String getName() {
        return name;
    }
    public List<String> getLore() {
        return lore;
    }
    public ItemStack getItem() { return item; }
    public Material getMaterial() { return material; }
    public int getRange() {
        return range;
    }
    public int getRadius() {
        return radius;
    }
    public int getSpeed() {
        return speed;
    }
    public boolean isNuclear() {
        return nuclear;
    }
    public int getGuiSlot() { return guiSlot; }

    public void launch(Player p, @Nullable Location target, Block b) {
        PlayerValues pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isReadyToLaunch()) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&b&lAn intercontinental ballistic missile was launched."));
                op.playSound(op.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 2, 1);
            }
            b.setType(Material.CAULDRON);
            pValues.setReadyToLaunch(false);

            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);

            land(p, target);
        } else {
            p.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',RowMissiles.prefix + "&ePlease enter the location of your target. Type '&ccancel&e' to abort the launch.'"),
                    ChatColor.translateAlternateColorCodes('&',RowMissiles.prefix + "&eFormat: &cX Z")
            );
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            pValues.setSettingLocation(true);
        }
    }

    private void land(Player p, Location target) {
        int distance = getDistance(p.getLocation(), target);
        int time = distance / speed;

        new BukkitRunnable() {
            public void run() {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            RowMissiles.prefix + "&b&lThe bomb has blown up at &c&l" + (int) target.getX() + "&b&l, &c&l" + (int) target.getY() + "&b&l, &c&l" + (int) target.getZ()));
                    op.playSound(op.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2, 1);
                }
                TNTPrimed bomb = (TNTPrimed) p.getWorld().spawn(target, TNTPrimed.class);
                bomb.setFuseTicks(10);
                bomb.setYield(radius);
            }
        }.runTaskLater(RowMissiles.getInstance(), 20L * (time + 5));
    }

    public int getDistance(Location loc1, Location loc2) {
        int x1 = loc1.getBlockX();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int z2 = loc2.getBlockZ();
        int dx = Math.abs(x1 - x2);
        int dz = Math.abs(z1 - z2);
        return dx + dz;
    }
}
