package lunatic.athenarpg.itemlistener.limited;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class StarvingOrb implements Listener {
    private Main plugin;
    RPGUtils rpgUtils;
    CooldownManager cooldownManager;

    public StarvingOrb(Main plugin){
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.cooldownManager = new CooldownManager();
    }
    @EventHandler
    public void StarvingOrb(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String rpgName = rpgUtils.getRPGNameInHand(e.getPlayer());
        if (rpgName.equals("Starving Orb")) { // Changed to "Starving Orb"
            if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                if (!cooldownManager.isOnCooldown(p.getName(), rpgName)) {

                    e.setCancelled(true);
                    ArmorStand starving = p.getWorld().spawn(p.getLocation().add(0, 1, 0), ArmorStand.class); // Changed variable name
                    ArmorStand starvingHolo = p.getWorld().spawn(p.getLocation().add(0, 2, 0), ArmorStand.class); // Changed variable name
                    starving.setHelmet(Main.getHead("starvingorb")); // Assuming you have a method to get the head for "starving"
                    starving.setVisible(false);
                    starving.setInvulnerable(true);
                    starving.setCustomName("starvingorb"); // Changed to "starvingorb"
                    starving.setGravity(false);
                    starving.setMarker(true);

                    starvingHolo.setVisible(false);
                    starvingHolo.setInvulnerable(true);
                    starvingHolo.setCustomName("§6" + (p.getName()) + "'s Starving Orb"); // Changed to "Starving Orb"
                    starvingHolo.setCustomNameVisible(true);
                    starvingHolo.setGravity(false);
                    starvingHolo.setMarker(true);

                    plugin.armorStandList.add(starving);
                    plugin.armorStandList.add(starvingHolo);

                    moveUpStarving(starving, starvingHolo, e.getPlayer());
                    applyOrbEffects(starving, e.getPlayer());

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        starving.remove();
                        starvingHolo.remove();
                        plugin.armorStandList.remove(starving);
                        plugin.armorStandList.remove(starvingHolo);
                    }, 200);

                    cooldownManager.setCooldown(p.getName(), rpgName, 15);
                }else{
                    cooldownManager.sendCooldownMessage(p, rpgName);
                }
            }
        }
    }
    private void moveUpStarving(ArmorStand starving, ArmorStand starvingHolo, Player p) {
        new BukkitRunnable() {
            private boolean goingUp = true;
            Location loc = starving.getLocation();
            private final int maximumHeight = loc.getBlockY() + 2;
            private final int minimumHeight = loc.getBlockY();

            @Override
            public void run() {
                if (starving.isDead()) {
                    starving.remove();
                    starvingHolo.remove();
                    plugin.armorStandList.remove(starving);
                    plugin.armorStandList.remove(starvingHolo);
                    cancel();
                    return;
                }
                if (starving.getWorld().getName().equalsIgnoreCase(p.getWorld().getName())) {
                    if (goingUp) {
                        if (starving.getLocation().getY() > maximumHeight) {
                            goingUp = false;
                        } else {
                            loc.setYaw(loc.getYaw() + (float) 20);
                            starving.teleport(loc.add(0, 0.07, 0));
                            starvingHolo.teleport(loc.clone().add(0, 2.22, 0));
                        }
                    } else {
                        if (starving.getLocation().getY() < minimumHeight) {
                            goingUp = true;
                        } else {
                            loc.setYaw(loc.getYaw() + (float) 20);
                            starving.teleport(loc.add(0, -0.07, 0));
                            starvingHolo.teleport(loc.clone().add(0, 2.22, 0));
                        }
                    }
                } else {
                    starving.remove();
                    starvingHolo.remove();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }
    private void applyOrbEffects(ArmorStand starving, Player owner) {
        int radiusSquared = 10; // Adjust the radius as needed (5 blocks squared)
        int duration = 200; // Duration in ticks (20 ticks per second, so 100 ticks = 5 seconds)

        new BukkitRunnable() {
            int timer = duration;

            @Override
            public void run() {
                if (starving.isDead() || owner == null || !owner.isOnline()) {
                    cancel();
                    return;
                }

                if (timer <= 0) {
                    cancel();
                    return;
                }

                owner.setFoodLevel(20);

                Location orbLoc = starving.getLocation();
                if (!starving.getWorld().getName().equals("spawn-mix")) {
                    for (Player player : orbLoc.getWorld().getPlayers()) {
                        if (!player.equals(owner) && player.getLocation().distanceSquared(orbLoc) <= radiusSquared) {
                            // Apply starving effect
                            player.setFoodLevel(0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                        }
                    }
                }

                timer--;
            }
        }.runTaskTimer(plugin, 0, 1); // Run every tick
    }

}
