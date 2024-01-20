package lunatic.athenarpg.itemlistener.dungeon;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhantasmalSet implements Listener {
    private Main plugin;
    RPGUtils rpgUtils;
    ItemConstructor itemConstructor;
    CooldownManager cooldownManager;
    StatusListener statusListener;

    private Map<UUID, Long> unhittablePlayers = new HashMap<>();

    public PhantasmalSet(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager();
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void fullSetPhantasmal(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getDamager();
            Entity damagedEntity = e.getEntity();

            if (e.getDamage() > 0) {
                if (rpgUtils.isFullSet(player, "Phantasmal")) {
                    if (damagedEntity.getName().equals("§5§lEldoria")) {
                        double currentDamage = e.getDamage();
                        e.setDamage(currentDamage * 1.80);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            if (rpgUtils.isFullSet(player, "Phantasmal")) {
                if (player.getWorld().getName().equals("worldDungeon")) {
                    int rpgLevel = rpgUtils.getRPGLevelInHand(player);
                    int vitalityCost = itemConstructor.getFullSetVitalityCost(player);
                    int cooldownTime = 30 - rpgLevel;
                    if (!cooldownManager.isOnCooldown(player.getName(), "Phantasmal")) {
                        if (statusListener.haveEnoughVitality(player, vitalityCost)) {
                            player.playSound(player, Sound.BLOCK_ANVIL_PLACE, 20f, 0f);
                            statusListener.consumeVitality(player, vitalityCost);

                            // Add the player to the unhittablePlayers map with the expiration time
                            unhittablePlayers.put(player.getUniqueId(), System.currentTimeMillis() + (cooldownTime * 1000));

                            // Schedule a task to remove the player from the map after 15 seconds
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                unhittablePlayers.remove(player.getUniqueId());
                            }, cooldownTime * 20);  // 20 ticks per second
                            cooldownManager.setCooldown(player.getName(), "Phantasmal", cooldownTime);
                        }
                    }else{
                        cooldownManager.sendCooldownMessage(player, "Phantasmal");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getDamager() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            // Check if the damaged player is currently unhittable
            if (isUnhittable(damagedPlayer) && !damagedPlayer.equals(damager)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isUnhittable(Player player) {
        return unhittablePlayers.containsKey(player.getUniqueId()) &&
                unhittablePlayers.get(player.getUniqueId()) > System.currentTimeMillis();
    }

}
