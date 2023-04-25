package me.rownox.rowmissiles.objects;

import me.rownox.rowmissiles.RowMissiles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MissileObject {

    private final String name;
    private final List<String> lore;
    private final ItemStack item;
    private final PotionType color;
    private final Material material;
    private final int range;
    private final int magnitude;
    private final int speed;
    private final boolean nuclear;
    private int duration;
    private int guiSlot;

    public MissileObject(ItemStack item, PotionType color, int range, int magnitude, int speed, boolean nuclear) {
        this.item = item;
        this.lore = item.getItemMeta().getLore();
        this.name = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.color = color;
        this.range = range;
        this.magnitude = magnitude;
        this.speed = speed;
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
    public PotionType getColor() { return color; }
    public int getRange() {
        return range;
    }
    public int getMagnitude() {
        return magnitude;
    }
    public int getSpeed() {
        return speed;
    }
    public boolean isNuclear() {
        return nuclear;
    }
    public void setGuiSlot(int num) { guiSlot = num;}
    public int getGuiSlot() { return guiSlot; }

    public void launch(Player p, @Nullable Location target, Block b) {
        PlayerValuesObject pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isReadyToLaunch()) {
            if (RowMissiles.broadcastEnabled) {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    broadcastLaunch(op);
                }
            } else {
                broadcastLaunch(p);
            }

            b.setType(Material.CAULDRON);
            pValues.setReadyToLaunch(false);

            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);

            Firework firework = p.getWorld().spawn(b.getLocation().add(0, 2, 0), Firework.class);

            FireworkMeta meta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder()
                    .withColor(Color.WHITE)
                    .withFade(Color.RED)
                    .with(FireworkEffect.Type.BURST)
                    .flicker(true)
                    .trail(true)
                    .build();
            meta.addEffect(effect);
            meta.setPower(10);
            firework.setFireworkMeta(meta);

            new BukkitRunnable() {
                @Override
                public void run() {
                    firework.detonate();
                }
            }.runTaskLater(RowMissiles.getInstance(), 40);

            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, b.getLocation().add(0.5, 0.5, 0.5), 3);

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

                if (isNuclear()) {
                    duration = 150;
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (duration > 0) {
                            for (Player op : Bukkit.getOnlinePlayers()) {
                                if (!radiusCheck(op, target)) continue;
                                op.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2));
                                duration--;
                            }
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(RowMissiles.getInstance(), 20, 20*10);
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

    private void broadcastLaunch(Player p) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&b&lA " + name + " &b&lmissile was launched."));
        p.playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 2, 1);
    }
}
