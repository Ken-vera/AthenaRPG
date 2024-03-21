package lunatic.athenarpg.itemlistener.limited;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DragonforgedSet implements Listener {
    private final Main plugin;
    private final RPGUtils rpgUtils;
    private final ItemConstructor itemConstructor;
    private final CooldownManager cooldownManager;
    private final StatusListener statusListener;
    private final Map<Player, BukkitTask> particleTasks;

    public DragonforgedSet(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager();
        this.statusListener = new StatusListener(plugin);
        this.particleTasks = new HashMap<>();
    }

    @EventHandler
    public void onHelmetEquip(ArmorEquipEvent event) {
        if (event.getNewArmorPiece() != null) {
            if (event.getNewArmorPiece().hasItemMeta()) {
                if (event.getNewArmorPiece().getItemMeta().getDisplayName().equals("§c§lDragonforged Helmet §8[§e✦§8]")) {
                    Player player = event.getPlayer();
                    startParticleCircle(player);
                    // Schedule a delayed task to spawn a second particle after 20 ticks (1 second)
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            startParticleCircle(player);
                        }
                    }.runTaskLater(plugin, 33);
                }
            }
        }
    }

    private void startParticleCircle(Player player) {
        BukkitTask particleTask = new BukkitRunnable() {
            double angle = 0;

            @Override
            public void run() {
                double x = Math.cos(angle);
                double z = Math.sin(angle);

                player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(x, 2, z), 0, 0, 0, 0, 1);

                angle += Math.PI / 15; // Increase the angle for the next particle

                if (player.getInventory().getHelmet() == null) {
                    this.cancel();
                    return;
                }
                if (!player.getInventory().getHelmet().hasItemMeta() || !player.getInventory().getHelmet().getItemMeta().getDisplayName().equals("§c§lDragonforged Helmet §8[§e✦§8]")) {
                    // If player is not wearing the Dragonforged Helmet, stop the particle circle
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 2); // Run the task every 2 ticks (adjust as needed)
        particleTasks.put(player, particleTask);
    }
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        BukkitTask particleTask = particleTasks.get(player);
        if (particleTask != null && !player.getWorld().equals(event.getFrom())) {
            particleTask.cancel(); // Cancel the particle task if player changes world
            particleTasks.remove(player);
        }
    }

    @EventHandler
    public void DragonForgedFullSet(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            if (rpgUtils.isFullSet(player, "Dragonforged")) {
                int vitalityCost = itemConstructor.getFullSetVitalityCost(player);
                if (statusListener.haveEnoughVitality(player, vitalityCost)) {
                    statusListener.consumeVitality(player, vitalityCost);

                    Location spawnLocation = getRandomAirLocation(player.getLocation(), 5, 5); // Adjust the radius and attempts as needed

                    if (spawnLocation != null) {
                        ArmorStand dragonLaser = spawnLocation.getWorld().spawn(spawnLocation, ArmorStand.class);
                        dragonLaser.setMarker(true);
                        dragonLaser.setHelmet(Main.getHead("dragonLaser"));
                        dragonLaser.setInvisible(true);

                        ArmorStand dragonLaserHolo = dragonLaser.getWorld().spawn(dragonLaser.getLocation().add(0, 2.5, 0), ArmorStand.class);
                        dragonLaserHolo.setMarker(true);
                        dragonLaserHolo.setInvisible(true);
                        dragonLaserHolo.setCustomName("§c" + player.getName() + "'s Dragon Orb");
                        dragonLaserHolo.setCustomNameVisible(true);

                        plugin.armorStandList.add(dragonLaser);
                        plugin.armorStandList.add(dragonLaserHolo);

                        moveUpdragonLaser(dragonLaser, dragonLaserHolo);

                        // Schedule a task to shoot nearby entities every 2 seconds
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!dragonLaser.isDead()) {
                                    shootEntities(player, dragonLaser);
                                } else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0, 20); // Run the task every 40 ticks (2 seconds)
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                dragonLaser.remove();
                                dragonLaserHolo.remove();
                                plugin.armorStandList.remove(dragonLaser);
                                plugin.armorStandList.remove(dragonLaserHolo);
                                dragonLaser.getWorld().playSound(dragonLaser, Sound.ENTITY_ENDERMAN_DEATH, 1f, 0f);
                            }
                        }.runTaskLater(plugin, 100);
                    }
                }
            }
        }
    }

    private Location getRandomAirLocation(Location center, int radius, int attempts) {
        World world = center.getWorld();
        Random random = new Random();

        for (int i = 0; i < attempts; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);

            Location candidateLocation = new Location(world, x, center.getY(), z);

            if (isAirBlock(candidateLocation)) {
                return candidateLocation;
            }
        }

        return null; // Return null if no suitable location is found
    }

    // Helper method to check if a location is in an air block
    private boolean isAirBlock(Location location) {
        return location.getBlock().getType() == Material.AIR && location.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR;
    }


    private void moveUpdragonLaser(final ArmorStand dragonLaser, final ArmorStand dragonLaserholo) {
        new BukkitRunnable() {
            private boolean goingUp = true;
            Location loc = dragonLaser.getLocation();
            private final int maximumHeight;
            private final int minimumHeight;

            {
                maximumHeight = loc.getBlockY() + 2;
                minimumHeight = loc.getBlockY();
            }

            public void run() {
                if (dragonLaser.isDead()) {
                    dragonLaser.remove();
                    dragonLaserholo.remove();
                    plugin.armorStandList.remove(dragonLaser);
                    plugin.armorStandList.remove(dragonLaserholo);
                    cancel();
                } else {
                    if (goingUp) {
                        if (dragonLaser.getLocation().getY() > (double)maximumHeight) {
                            goingUp = false;
                        } else {
                            loc.setYaw(loc.getYaw() + 20.0F);
                            dragonLaser.teleport(loc.add(0.0D, 0.07D, 0.0D));
                            dragonLaserholo.teleport(loc.clone().add(0.0D, 2.3D, 0.0D));
                        }
                    } else if (dragonLaser.getLocation().getY() < (double)minimumHeight) {
                        goingUp = true;
                    } else {
                        loc.setYaw(loc.getYaw() + 20.0F);
                        dragonLaser.teleport(loc.add(0.0D, -0.07D, 0.0D));
                        dragonLaserholo.teleport(loc.clone().add(0.0D, 2.3D, 0.0D));
                    }

                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    // Method to shoot nearby entities
    private void shootEntities(Player player, ArmorStand dragonLaser) {
        for (Entity entity : dragonLaser.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
                LivingEntity target = (LivingEntity) entity;
                if (target != player) {

                    // Spawn line particle from ArmorStand location to entity location
                    Location startLocation = dragonLaser.getEyeLocation();
                    Location endLocation = target.getEyeLocation();
                    spawnLineParticle(startLocation, endLocation, 10);

                    // Deal 50 damage to the target
                    LivingEntity entities = (LivingEntity) entity;
                    entities.damage(40, player);
                    dragonLaser.getWorld().playSound(dragonLaser, Sound.ENTITY_ALLAY_ITEM_GIVEN, 20f, 0f);
                }
            }
        }
    }

    private void spawnLineParticle(Location startLocation, Location endLocation, int particleCount) {
        Vector direction = endLocation.toVector().subtract(startLocation.add(0, 1.95, 0).toVector()).normalize();
        double distance = startLocation.distance(endLocation);
        double interval = distance / particleCount;

        for (int i = 0; i < particleCount; i++) {
            Location particleLocation = startLocation.clone().add(direction.clone().multiply(interval * i));
            startLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1,
                    new Particle.DustOptions(Color.RED, 1));
        }
    }
}