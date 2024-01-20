package lunatic.athenarpg.itemlistener.dungeon.pve;

import lunatic.athenarpg.Main;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class PlayerStandHigh implements Listener {
    private Main plugin;
    private Map<Player, Integer> hitCounterMap = new HashMap<>();
    private Map<Player, Location> lastPlayerLocations = new HashMap<>();

    public PlayerStandHigh(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getDamager();
            Entity damagedEntity = e.getEntity();

            // Check if the damaged entity is an "Ancient"
            if (player.getWorld().getName().equals("worldDungeon")) {
                if (e.getDamage() > 0 && damagedEntity.getName().contains("Ancient")) {
                    Location playerLocation = player.getLocation();
                    Location entityLocation = damagedEntity.getLocation();
                    Vector direction = entityLocation.toVector().subtract(playerLocation.toVector()).normalize();

                    // If the player hits 5 times and remains in the same block location, force push the player
                    int hitCounter = hitCounterMap.getOrDefault(player, 0);
                    Location lastLocation = lastPlayerLocations.getOrDefault(player, null);

                    if (hitCounter >= 2 && lastLocation != null && lastLocation.getBlock().equals(playerLocation.getBlock()) && playerLocation.getBlockY() - entityLocation.getBlockY() >= 2) {
                        player.setVelocity(direction.multiply(new Vector(1, 1, 1).multiply(10))); // Adjust the vector for the desired knockback effect
                        hitCounterMap.put(player, 0); // Reset the hit counter
                    } else {
                        // Increment the hit counter and update the last location
                        hitCounterMap.put(player, hitCounter + 1);
                        lastPlayerLocations.put(player, playerLocation);
                    }
                }
                if (e.getDamage() > 0 && damagedEntity.getName().contains("Eldoria")) {
                    Location playerLocation = player.getLocation();
                    Location entityLocation = damagedEntity.getLocation();
                    Vector direction = entityLocation.toVector().subtract(playerLocation.toVector()).normalize();
                    if (playerLocation.getBlockY() - entityLocation.getBlockY() >= 5){
                        player.teleport(entityLocation);
                    }
                }
            }
        }
    }
}
