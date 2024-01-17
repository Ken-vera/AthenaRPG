package lunatic.athenarpg.itemlistener.rare;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.Collection;

public class PoseidonTrident implements Listener {
    private final Main plugin;
    private final RPGUtils utils;
    private final CooldownManager cooldownManager;

    public PoseidonTrident(Main plugin) {
        this.plugin = plugin;
        this.utils = new RPGUtils();
        this.cooldownManager = new CooldownManager(plugin);
    }

    @EventHandler
    public void onPoseidonTridentRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String rpgName = utils.getRPGNameInHand(player);

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (rpgName.equals("Poseidon Trident")) {
                int level = utils.getRPGLevelInHand(player);
                int baseCooldown = 20;
                int cooldownReductionPerLevel = 2;
                int cooldownTime = Math.max(1, baseCooldown - (level - 1) * cooldownReductionPerLevel);

                double maxRange = 10.0 + level;
                int potionEffectDuration = 5 + level;

                int levitationAmplifier = (level / 2);

                event.setCancelled(true);
                Long cooldown = cooldownManager.getCooldown(rpgName, player.getUniqueId());

                if (cooldown == null) {
                    BlockIterator blockIterator = new BlockIterator(player, (int) maxRange);
                    boolean playerAffected = false; // Flag to track if a player has been affected

                    while (blockIterator.hasNext()) {
                        Block block = blockIterator.next();
                        Location blockLocation = block.getLocation();
                        Collection<Entity> entities = block.getWorld().getNearbyEntities(blockLocation, 1, 1, 1);

                        // Check for living entities in the block
                        for (Entity entity : entities) {
                            if (entity instanceof Player && entity != player) {
                                Player targetPlayer = (Player) entity;
                                if (!targetPlayer.hasMetadata("NPC") && !playerAffected) {

                                    new BukkitRunnable() {
                                        int duration = potionEffectDuration * 20; // Convert to ticks

                                        @Override
                                        public void run() {
                                            targetPlayer.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                                            targetPlayer.removePotionEffect(PotionEffectType.REGENERATION);

                                            duration--;
                                            if (duration <= 0) {
                                                this.cancel();
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0, 1); // Run every tick

                                    // Apply Levitation 2 with amplifier for specified duration
                                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, potionEffectDuration * 20, levitationAmplifier));

                                    targetPlayer.getWorld().strikeLightningEffect(targetPlayer.getLocation());
                                    cooldownManager.setCooldown(rpgName, cooldownTime, player.getUniqueId());
                                    playerAffected = true; // Set the flag to true
                                    break;
                                }
                            }
                        }

                        if (playerAffected) {
                            break; // If a player has been affected, exit the loop
                        }
                    }
                } else {
                    cooldownManager.sendCooldownMessage(player, rpgName, cooldownTime);
                }
            }
        }
    }
}