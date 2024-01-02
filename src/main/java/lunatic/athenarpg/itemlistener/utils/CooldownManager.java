package lunatic.athenarpg.itemlistener.utils;

import lunatic.athenarpg.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    private Main plugin;

    public CooldownManager(Main plugin){
        this.plugin = plugin;
    }

    private final Map<String, Long> cooldowns = new HashMap<>();

    public boolean isOnCooldown(Player player, String itemName) {
        String key = getKey(player, itemName);
        return cooldowns.containsKey(key) && cooldowns.get(key) > System.currentTimeMillis();
    }

    public void setCooldown(Player player, String itemName, int seconds) {
        String key = getKey(player, itemName);
        long cooldownExpiration = System.currentTimeMillis() + seconds * 1000L;
        cooldowns.put(key, cooldownExpiration);

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.remove(key);
            }
        }.runTaskLater(plugin, seconds * 20L); // Run once after the specified seconds
    }

    private String getKey(Player player, String itemName) {
        return player.getName() + ":" + itemName; // You can use a better key generation logic if needed
    }
}
