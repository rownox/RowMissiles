package me.rownox.rowmissiles.objects;

import me.rownox.rowmissiles.RowMissiles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MissileObject {

    private final String name;
    private final List<String> lore;
    private final ItemStack item;
    private final PotionType color;
    private final Material material;
    private final int range;
    private final int magnitude;
    private final int speed;
    private final String id;


    public MissileObject(ItemStack item, PotionType color, int range, int magnitude, int speed) {
        this.item = item;
        this.lore = item.getItemMeta().getLore();
        this.name = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.color = color;
        this.range = range;
        this.magnitude = magnitude;
        this.speed = speed;
        this.id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }
    public List<String> getLore() {
        return lore;
    }
    public ItemStack getItem() {
        return item;
    }
    public Material getMaterial() {
        return material;
    }
    public PotionType getColor() {
        return color;
    }
    public int getRange() {
        return range;
    }
    public int getMagnitude() {
        return magnitude;
    }
    public int getSpeed() {
        return speed;
    }
    public String getId() {
        return id;
    }


    public void launch(Player p, @Nullable Location target, Block b) {
        PlayerValuesObject pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isReadyToLaunch()) {
            if (RowMissiles.broadcastEnabled) {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    broadcastLaunch(op, p, target);
                }
            } else {
                broadcastLaunch(p, p, target);
            }

            b.setType(Material.CAULDRON);
            pValues.setReadyToLaunch(false);

            if (p.getGameMode() == GameMode.SURVIVAL) {
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            }


            shootMissile(b.getLocation());
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
            @Override
            public void run() {
                if (RowMissiles.broadcastEnabled) {
                    for (Player op : Bukkit.getOnlinePlayers()) {
                        broadcastLand(op, target);
                    }
                } else {
                    broadcastLand(p, target);
                }

                int x, y, z;
                for (int i = 0; i < magnitude; i++) {
                    Random random = new Random();
                    x = (int) (target.getX() + random.nextInt(-magnitude/4, magnitude/4));
                    z = (int) (target.getZ() + random.nextInt(-magnitude/4, magnitude/4));
                    y = target.getWorld().getHighestBlockYAt((int) x, z);
                    Location clusterLocation = new Location(world, x,y,z);
                    clusterBomb(clusterLocation, world);
                }

                centerBomb(target, world);
            }
        }.runTaskLater(RowMissiles.getInstance(), 20L * (time + 5));
    }

    private void clusterBomb(Location target, World world) {
        TNTPrimed bomb = world.spawn(target, TNTPrimed.class);
        bomb.setFuseTicks(0);
        bomb.setYield((float) (10));
        bomb.setIsIncendiary(true);
    }

    private void centerBomb(Location target, World world) {
        TNTPrimed bomb = world.spawn(target, TNTPrimed.class);
        bomb.setFuseTicks(0);
        bomb.setYield((float) (magnitude));
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (!radiusCheck(op, target)) continue;
            op.damage(20);
            op.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*60, 1));
            op.setFireTicks(20*20);
        }
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

    private boolean radiusCheck(Player p, Location loc) {
        if (p.getLocation().distance(loc) > magnitude) return false;
        return p.getGameMode() != GameMode.CREATIVE;
    }

    private void broadcastLand(Player p, Location target) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                RowMissiles.prefix + "&b&lThe bomb has blown up at &c&l" + (int) target.getX() + "&b&l, &c&l" + (int) target.getY() + "&b&l, &c&l" + (int) target.getZ()));
        p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2, 1);
    }

    private void broadcastLaunch(Player p, Player sender, Location target) {
        p.sendMessage(ChatColor
                .translateAlternateColorCodes('&',
                        RowMissiles.prefix + "&b&l" + sender.getName() + " launched a " + name
                                + " &b&lmissile from X:" + (int) sender.getLocation().getX() + " Y:" + (int) sender.getLocation().getZ()
                                + " bound to hit X:" + (int) target.getX() + " Y:" + (int) target.getZ()));
        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2, 1);
    }

    private void shootMissile(Location loc) {

        World world = loc.getWorld();

        if (world == null) return;
        TNTPrimed tntMissile = (TNTPrimed) world.spawnEntity(loc.add(0, 2, 0), EntityType.PRIMED_TNT);
        Vector vector = new Vector(0, 1, 0).multiply(1.5);

        BukkitTask particleTask = new BukkitRunnable() {
            @Override
            public void run() {
                tntMissile.setVelocity(vector);
                world.spawnParticle(Particle.FLAME, tntMissile.getLocation(), 5, 0.2, 0.2, 0.2, 0);
                world.spawnParticle(Particle.EXPLOSION_LARGE, tntMissile.getLocation(), 5, 0.2, 0.2, 0.2, 0);
                world.spawnParticle(Particle.SMOKE_LARGE, tntMissile.getLocation(), 5, 0.2, 0.2, 0.2, 0);
            }
        }.runTaskTimer(RowMissiles.getInstance(), 0, 2);

        new BukkitRunnable() {
            @Override
            public void run() {
                tntMissile.remove();
                particleTask.cancel();
            }
        }.runTaskLater(RowMissiles.getInstance(), 20*3);
    }
}
