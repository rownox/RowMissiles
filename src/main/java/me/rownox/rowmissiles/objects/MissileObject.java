package me.rownox.rowmissiles.objects;

import me.rownox.rowmissiles.RowMissiles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;

public class MissileObject {

    private final String name;
    private final List<String> lore;
    private final ItemStack item;
    private final Material material;
    private final int range;
    private final int magnitude;
    private final int speed;
    private final boolean cluster;
    private final boolean nuclear;
    private int guiSlot;

    public MissileObject(ItemStack item, int range, int magnitude, int speed, boolean cluster, boolean nuclear) {
        this.item = item;
        this.lore = item.getItemMeta().getLore();
        this.name = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.range = range;
        this.magnitude = magnitude;
        this.speed = speed;
        this.cluster = cluster;
        this.nuclear = nuclear;
        this.guiSlot = 0;
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
    public int getMagnitude() {
        return magnitude;
    }
    public int getSpeed() {
        return speed;
    }
    public boolean isCluster() { return cluster; }
    public boolean isNuclear() {
        return nuclear;
    }
    public void setGuiSlot(int num) { guiSlot = num;}
    public int getGuiSlot() { return guiSlot; }

    public void launch(Player p, @Nullable Location target, Block b) {
        PlayerValuesObject pValues = RowMissiles.playerValues.get(p.getUniqueId());

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
        World world = p.getWorld();

        new BukkitRunnable() {
            public void run() {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            RowMissiles.prefix + "&b&lThe bomb has blown up at &c&l" + (int) target.getX() + "&b&l, &c&l" + (int) target.getY() + "&b&l, &c&l" + (int) target.getZ()));
                    op.playSound(op.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2, 1);
                }
                if (isCluster()) {
                    int x, y, z;
                    for (int i = 0; i < 4; i++) {
                        int randomAngle = (int) (Math.random() * Math.PI * 2);
                        int randomRadius = (int) (Math.random() * magnitude);
                        x = (int) (target.getX() + randomRadius * Math.cos(randomAngle));
                        z = (int) (target.getZ() + randomRadius * Math.sin(randomAngle));
                        y = target.getWorld().getHighestBlockYAt((int) x, (int) z);
                        Location clusterLocation = new Location(world, x,y,z);
                        explode(clusterLocation, world);
                    }
                }
                if (isNuclear()) {
                    new BukkitRunnable() {
                        public void run() {

                        }
                    }.runTaskLater(RowMissiles.getInstance(), 20*60*5);
                    new BukkitRunnable() {
                        public void run() {
                            if (isNuclear()) {
                                for (Player op : Bukkit.getOnlinePlayers()) {
                                    if (p.getLocation().distance(target) < magnitude) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 2));
                                    }
                                }
                            } else {
                                cancel();
                            }
                        }
                    }.runTaskTimer(RowMissiles.getInstance(), 20, 100);
                }
                explode(target, world);
            }
        }.runTaskLater(RowMissiles.getInstance(), 20L * (time + 5));
    }

    private void explode(Location target, World world) {
        TNTPrimed bomb = (TNTPrimed) world.spawn(target, TNTPrimed.class);
        bomb.setFuseTicks(10);
        bomb.setYield(magnitude);
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
