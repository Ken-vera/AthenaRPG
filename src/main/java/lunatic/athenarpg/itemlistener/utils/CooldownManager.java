package lunatic.athenarpg.itemlistener.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lunatic.athenarpg.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private Main plugin;
    RPGUtils utils = new RPGUtils();

    public CooldownManager(Main plugin){
        this.plugin = plugin;
    }

    private final Cache<String, Cache<UUID, Long>> cooldowns = Caffeine.newBuilder().build();

    public void setCooldown(String cooldownType, int cooldownTime, UUID uuid) {
        Cache<UUID, Long> newCooldownMap = Caffeine.newBuilder()
                .expireAfterWrite(cooldownTime, TimeUnit.SECONDS)
                .build();
        newCooldownMap.put(uuid, System.currentTimeMillis());
        cooldowns.put(cooldownType, newCooldownMap);
    }

    public Long getCooldown(String cooldownType, UUID uuid) {
        Cache<UUID, Long> cooldownMap = cooldowns.getIfPresent(cooldownType);

        if (cooldownMap != null) {
            return cooldownMap.getIfPresent(uuid);
        }
        return null;
    }

    public void resetCooldown(String cooldownType, UUID uuid) {
        Cache<UUID, Long> cooldownMap = cooldowns.getIfPresent(cooldownType);

        if (cooldownMap != null) {
            cooldownMap.invalidate(uuid);
        }
    }
    public void sendCooldownMessage(Player player, String rpgName, int cooldownTime) {
        Long cooldown = getCooldown(rpgName, player.getUniqueId());
        player.sendMessage(ChatColor.RED + rpgName + " still on " + TimeUnit.MILLISECONDS.toSeconds((cooldownTime * 1000) - (System.currentTimeMillis() - cooldown)) + "s cooldown!");
    }

    public int getCooldownInteger(String cooldownType, UUID uuid) {
        Long remainingCooldown = getCooldown(cooldownType, uuid);

        if (remainingCooldown != null) {
            long currentTime = System.currentTimeMillis();
            int secondsRemaining = (int) TimeUnit.MILLISECONDS.toSeconds(remainingCooldown - currentTime);
            return Math.max(0, secondsRemaining); // Ensure non-negative value
        }
        return 0; // Return 0 if no cooldown is found
    }
}