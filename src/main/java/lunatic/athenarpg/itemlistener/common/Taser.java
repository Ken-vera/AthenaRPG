package lunatic.athenarpg.itemlistener.common;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.util.Collection;
import java.util.Random;

public class Taser implements Listener {
    private Main plugin;
    private RPGUtils utils = new RPGUtils();
    private final CooldownManager cooldownManager = new CooldownManager(plugin);

    public Taser(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTaserSkill(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Random random = new Random();
            Player player = (Player) event.getDamager();
            String rpgName = utils.getRPGNameInHand(player);

            if (event.getDamage() > 0) {
                if (rpgName.contains("Taser")) {
                    Long cooldown = cooldownManager.getCooldown(utils.getRPGNameInHand(player), player.getUniqueId());
                    if (cooldown == null) {
                        int randomNumber = random.nextInt(100) + 1;

                        if (randomNumber <= 40 && event.getEntity() instanceof LivingEntity) {
                            LivingEntity enemy = (LivingEntity) event.getEntity();
                            enemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 5));
                            enemy.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 5));
                            enemy.getWorld().strikeLightningEffect(enemy.getLocation());
                            player.sendMessage("§bYou just stunned " + enemy.getName() + "!");
                        }
                    } else {

                    }
                }
            }
        }
    }

    @EventHandler
    public void onTaserRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String rpgName = utils.getRPGNameInHand(player);
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (rpgName.equals("Taser")) {
                int level = utils.getRPGLevelInHand(player);
                int cooldownTime = 0;
                if (level != 7) {
                    cooldownTime = Math.max(1, 15 - (level - 1) * 2);
                } else {
                    cooldownTime = 5;
                }

                event.setCancelled(true);
                // Check if the RPG level is at least 2 (assuming Long Range Electricity I is at level 2)
                Long cooldown = cooldownManager.getCooldown(utils.getRPGNameInHand(player), player.getUniqueId());
                if (cooldown == null) {
                    double maxRange = 5.0;

                    // Get player's line of sight
                    BlockIterator blockIterator = new BlockIterator(player, (int) maxRange);

                    // Iterate through blocks in the player's line of sight
                    while (blockIterator.hasNext()) {
                        Block block = blockIterator.next();
                        Location blockLocation = block.getLocation();
                        Collection<Entity> entities = block.getWorld().getNearbyEntities(blockLocation, 1, 1, 1);

                        // Check for living entities in the block
                        for (Entity entity : entities) {
                            if (entity instanceof Player && entity != player) {
                                Player targetPlayer = (Player) entity;
                                if (!targetPlayer.hasMetadata("NPC")) {

                                    // Apply stun effects to the targeted player
                                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 5));
                                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 5));
                                    targetPlayer.getWorld().strikeLightningEffect(targetPlayer.getLocation());
                                    cooldownManager.setCooldown(rpgName, cooldownTime, player.getUniqueId());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    cooldownManager.sendCooldownMessage(player, rpgName, cooldownTime);
                }

            }
        }
    }

}