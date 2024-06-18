package lunatic.athenarpg.itemlistener.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private HashMap<String, HashMap<String, Long>> cooldowns;
    private Cache<String, Cache<String, Long>> activeSkill;

    public CooldownManager() {
        cooldowns = new HashMap<>();
        activeSkill = Caffeine.newBuilder().build();
    }

    public void setActiveSkill(String playerName, String RPGName, long cooldownInSeconds) {
        Cache<String, Long> activeSkillMap = Caffeine.newBuilder()
                .expireAfterWrite(cooldownInSeconds, TimeUnit.SECONDS)
                .build();
        activeSkillMap.put(playerName, System.currentTimeMillis());
        activeSkill.put(RPGName, activeSkillMap);
    }

    public void clearActiveSkill(String playerName, String RPGName) {
        Cache<String, Long> activeSkillMap = activeSkill.getIfPresent(playerName);
        if (activeSkillMap != null) {
            activeSkill.invalidate(RPGName);
        }
    }

    public boolean isSkillActive(String playerName, String RPGName) {
        Cache<String, Long> activeSkillMap = activeSkill.getIfPresent(playerName);
        return activeSkillMap != null && activeSkillMap.getIfPresent(RPGName) > System.currentTimeMillis();
    }

    public void sendSkillStatus(Player player, String RPGSkill) {
        player.sendMessage("§c" + RPGSkill + " §6is active!");
    }

    public void setCooldown(String playerName, String RPGName, long cooldownInSeconds) {
        long cooldownInMillis = cooldownInSeconds * 1000;
        cooldowns.computeIfAbsent(playerName, k -> new HashMap<>()).put(RPGName, System.currentTimeMillis() + cooldownInMillis);
    }

    public void clearCooldown(String playerName, String RPGName) {
        HashMap<String, Long> playerCooldowns = cooldowns.get(playerName);
        if (playerCooldowns != null) {
            playerCooldowns.remove(RPGName);
        }
    }

    public void subtractCooldown(String playerName, String RPGName, long amountInSeconds) {
        long amountInMillis = amountInSeconds * 1000;
        HashMap<String, Long> playerCooldowns = cooldowns.get(playerName);
        if (playerCooldowns != null) {
            playerCooldowns.merge(RPGName, System.currentTimeMillis() + amountInMillis, Long::max);
        }
    }

    public void addCooldown(String playerName, String RPGName, long amountInSeconds) {
        long amountInMillis = amountInSeconds * 1000;
        HashMap<String, Long> playerCooldowns = cooldowns.get(playerName);
        if (playerCooldowns != null) {
            playerCooldowns.merge(RPGName, System.currentTimeMillis() - amountInMillis, Long::max);
        }
    }

    public boolean isOnCooldown(String playerName, String RPGName) {
        HashMap<String, Long> playerCooldowns = cooldowns.get(playerName);
        return playerCooldowns != null && playerCooldowns.getOrDefault(RPGName, 0L) > System.currentTimeMillis();
    }

    public long getRemainingCooldown(String playerName, String RPGName) {
        HashMap<String, Long> playerCooldowns = cooldowns.get(playerName);
        if (playerCooldowns != null) {
            long remainingCooldown = playerCooldowns.getOrDefault(RPGName, 0L) - System.currentTimeMillis();
            return Math.max(remainingCooldown, 0) / 1000; // Convert to seconds
        }
        return 0;
    }

    public void sendCooldownMessage(Player player, String rpgName){
        player.sendMessage("§cYou're currently on " +getRemainingCooldown(player.getName(), rpgName) + " seconds cooldown!");
    }
}
