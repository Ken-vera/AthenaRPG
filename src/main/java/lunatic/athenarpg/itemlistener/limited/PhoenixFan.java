package lunatic.athenarpg.itemlistener.limited;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoenixFan implements Listener {
    private final Main plugin;
    private final RPGUtils rpgUtils;
    private final ItemConstructor itemConstructor;
    private final StatusListener statusListener;

    // Map to track tornado state for each player
    private final Map<Player, Boolean> tornadoStates = new HashMap<>();

    // Initialize angle as a class variable
    private double angle = 0;

    public PhoenixFan(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void fanTornado(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (rpgUtils.getRPGNameInHand(player).equals("Opened Phoenix Fan")) {
                    int manaCost = itemConstructor.getMainHandManaCost(player);
                    if (statusListener.haveEnoughMana(player, manaCost)) {
                        Block targetBlock = player.getTargetBlock(null, 10);

                        // Check if the target block is not null and is a solid block
                        if (targetBlock != null && targetBlock.getType().isSolid()) {
                            // Get the location of the targeted block
                            Location location = targetBlock.getLocation();

                            statusListener.consumeMana(player, manaCost);

                            runTornado(location, player);
                        } else {
                            player.sendMessage("§cTargeted block must be solid or within 10 blocks!");
                        }
                    }
                } else if (rpgUtils.getRPGNameInHand(player).equals("Phoenix Fan")) {
                    player.sendMessage("§cYou must open the fan first!");
                }
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (rpgUtils.getRPGNameInHand(player).equals("Phoenix Fan")) {
                itemConstructor.setRPGNameInHand(player, "§c§lOpened Phoenix Fan §8[§e✦§8]");
            } else if (rpgUtils.getRPGNameInHand(player).equals("Opened Phoenix Fan")) {
                itemConstructor.setRPGNameInHand(player, "§c§lPhoenix Fan §8[§e✦§8]");
            }
        }
    }


    // Method to run tornado logic
    private void runTornado(Location location, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int max_height = 10;
                double max_radius = 8;
                int lines = 10;
                double height_increment = 0.2;
                double radius_increment = max_radius / max_height;

                // Loop through each line of particles
                for (int t = 0; t < 5; t++) { // Loop for 5 times
                    // Loop through each line of particles
                    for (int l = 0; l < lines; l++) {
                        // Loop through each height level
                        for (double y = 0; y < max_height; y += height_increment) {
                            double radius = y * radius_increment;
                            double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                            double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;

                            // Display cloud particles at the calculated position with a delay
                            double finalY = y;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    location.getWorld().spawnParticle(Particle.CLOUD, location.clone().add(x, finalY, z), 1, 0, 0, 0, 0);

                                    double detectionRadius = 10; // Adjust the detection radius as needed

                                    player.playSound(player, Sound.ENTITY_WOLF_SHAKE, 0.1f, 2f);

                                    List<Entity> nearbyEntities = location.getWorld().getEntities();
                                    for (Entity entity : nearbyEntities) {
                                        if (entity instanceof Monster && entity.getLocation().distance(location) <= detectionRadius && !(entity instanceof Player)) {
                                            // Make the entity fly towards the center of the tornado with a +5 Y offset
                                            Vector direction = location.toVector().subtract(entity.getLocation().toVector()).normalize();
                                            direction.setY(direction.getY() + 1); // Adjust the Y offset as needed
                                            entity.setVelocity(direction.multiply(0.5)); // Adjust the velocity as needed
                                            LivingEntity ent = (LivingEntity) entity;
                                            ent.damage(2, player);
                                        }
                                    }
                                }
                            }.runTaskLater(plugin, (long) (t * lines * 2 + l * 2 + y * 2)); // Adjust the delay as needed
                        }
                    }

                    // Detect and attract nearby monsters if the tornado is active for the player
                }

                // Increase the angle for the next iteration
                angle += 5; // You can adjust the increment value

                // You might want to schedule a task to reset the angle after a certain duration
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        angle = 0;
                    }
                }.runTaskLater(plugin, 20L); // Reset angle after 1 second (20 ticks)
            }
        }.runTask(plugin); // Run the task synchronously on the main thread
    }
}
