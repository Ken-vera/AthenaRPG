package lunatic.athenarpg.itemlistener.limited;

import io.lumine.mythic.lib.api.item.NBTItem;
import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.MMOItemsHook;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.lumine.mythic.core.spawning.spawners.SpawnerAttribute.RADIUS;

public class AdhaSet implements Listener {
    private Main plugin;
    CooldownManager cooldownManager;
    private final Map<UUID, AttributeModifier> attackSpeedModifiers = new HashMap<>();
    private String helmetPattern = "SACRIFICIAL.*";
    private String chestPlatePattern = "SACRIFICIAL.*";
    private String leggingsPattern = "SACRIFICIAL.*";
    private String bootsPattern = "SACRIFICIAL.*";
    private static final int DURATION_TICKS = 40; // 5 seconds * 20 ticks per second
    private static final int PARTICLES_PER_RING = 5; // Number of particles in each ring
    private static final double RADIUS = 2.0;       // Radius of the vortex
    private static final double RING_DISTANCE = 0.5; // Distance between rings
    private static final double UPWARD_SPEED = 0.5;

    public AdhaSet(Main plugin){
        this.plugin = plugin;
        this.cooldownManager = new CooldownManager();
    }

    @EventHandler
    public void helmet(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager() instanceof LivingEntity) {
            Player player = (Player) event.getEntity();
            ItemStack helmet = player.getInventory().getHelmet();

            if (plugin.getMmoItemsHook().isMMOItem(helmet)) {
                if (plugin.getMmoItemsHook().getMMOItemName(helmet).matches(helmetPattern)) {
                    if (!cooldownManager.isOnCooldown(player.getName(), plugin.getMmoItemsHook().getMMOItemName(helmet))) {
                        LivingEntity entity = (LivingEntity) event.getDamager();

                        if (Math.random() < 0.15) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 2));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 2));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                            if (entity instanceof Player) {
                                ((Player) entity).playSound(entity, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f, 1.0f);
                            }
                            cooldownManager.setCooldown(player.getName(), plugin.getMmoItemsHook().getMMOItemName(helmet), 15);
                            cooldownManager.sendSkillStatus(player, "Poisonous Horn");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void chestPlate(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager() instanceof LivingEntity) {
            Player player = (Player) event.getEntity();
            ItemStack chestPlate = player.getInventory().getChestplate();

            if (plugin.getMmoItemsHook().isMMOItem(chestPlate)) {
                if (plugin.getMmoItemsHook().getMMOItemName(chestPlate).matches(chestPlatePattern)) {
                    if (cooldownManager.isSkillActive(player.getName(), plugin.getMmoItemsHook().getMMOItemName(chestPlate))) {
                        event.setCancelled(true);
                        player.playSound(player, Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);

                        if (event.getDamager() instanceof Player) {
                            ((Player) event.getDamager()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0f, 1.0f);
                        }
                    } else {
                        if (!cooldownManager.isOnCooldown(player.getName(), plugin.getMmoItemsHook().getMMOItemName(chestPlate))) {
                            if (Math.random() < 0.3) {
                                cooldownManager.setActiveSkill(player.getName(), plugin.getMmoItemsHook().getMMOItemName(chestPlate), 5);
                                cooldownManager.setCooldown(player.getName(), plugin.getMmoItemsHook().getMMOItemName(chestPlate), 35);
                                cooldownManager.sendSkillStatus(player, "Holy Shield");
                                event.setCancelled(true);
                                player.playSound(player, Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
                                if (event.getDamager() instanceof Player attacker) {
                                    ((Player) event.getDamager()).playSound(attacker, Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void leggings(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity && event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();
            ItemStack leggings = player.getInventory().getLeggings();
            //spawnParticle(player, player.getLocation());

            if (plugin.getMmoItemsHook().isMMOItem(leggings)) {
                if (plugin.getMmoItemsHook().getMMOItemId(leggings).matches(leggingsPattern)) {
                    if (!cooldownManager.isOnCooldown(player.getName(), plugin.getMmoItemsHook().getMMOItemName(leggings))) {
                        if (Math.random() < 0.15) {
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                Location playerLoc = player.getLocation();
                                Location targetLoc = target.getLocation();

                                Vector direction = playerLoc.subtract(targetLoc).toVector().normalize();
                                target.setVelocity(direction.multiply(1.0));
                                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 255));
                                target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 255));
                                target.getWorld().spawnParticle(Particle.SPELL_WITCH, target.getLocation(), 20, 0.5, 1, 0.5);
                                cooldownManager.setCooldown(player.getName(), plugin.getMmoItemsHook().getMMOItemName(leggings), 65);
                                cooldownManager.sendSkillStatus(player, "Gravity Well");
                            });
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void fullSetBonus(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestPlate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        if (!player.isSneaking()) {
            if (plugin.getMmoItemsHook().isMMOItem(helmet) &&
                    plugin.getMmoItemsHook().isMMOItem(chestPlate) &&
                    plugin.getMmoItemsHook().isMMOItem(leggings) &&
                    plugin.getMmoItemsHook().isMMOItem(boots)) {
                System.out.println(plugin.getMmoItemsHook().getMMOItemId(helmet));
                if (plugin.getMmoItemsHook().getMMOItemId(helmet).matches(helmetPattern) &&
                        plugin.getMmoItemsHook().getMMOItemId(chestPlate).matches(chestPlatePattern) &&
                        plugin.getMmoItemsHook().getMMOItemId(leggings).matches(leggingsPattern) &&
                        plugin.getMmoItemsHook().getMMOItemId(boots).matches(bootsPattern)) {
                    if (!cooldownManager.isOnCooldown(player.getName(), "Bloodthirst")) {
                        double attackSpeedBoost = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue() * 0.5;
                        increaseAttackSpeed(player, attackSpeedBoost, 5);
                        cooldownManager.setActiveSkill(player.getName(), "Bloodthirst", 5);
                        cooldownManager.sendSkillStatus(player, "Bloodthirst");
                        cooldownManager.setCooldown(player.getName(), "Bloodthirst", 30);
                    } else {
                        cooldownManager.sendCooldownMessage(player, "Bloodthirst");
                    }
                }
            }
        }
    }

    @EventHandler
    public void bloodthirst(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();

            if (cooldownManager.isSkillActive(player.getName(), "Bloodthirst")) {
                double damage = event.getDamage();
                double health = player.getHealth();
                double maxHealth = player.getMaxHealth();
                double healAmount = damage * 0.2;
                if ((health + healAmount) <= maxHealth) {
                    player.setHealth(health + healAmount);
                } else {
                    player.setHealth(maxHealth);
                }
            }
        }
    }

    private void increaseAttackSpeed(Player player, double amount, int durationSeconds) {
        UUID uuid = player.getUniqueId();

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "Bloodthirst", amount, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        attackSpeedModifiers.put(uuid, modifier);

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(modifier);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(attackSpeedModifiers.get(uuid));
                attackSpeedModifiers.remove(uuid);
            }
        }.runTaskLater(plugin, durationSeconds * 20L);
    }

    private void spawnParticle(Player player, Location location) {
        new BukkitRunnable() {
            double t = 0; // Time variable
            @Override
            public void run() {
                if (t < DURATION_TICKS) {
                    for (double y = 0; y <= RADIUS; y += RING_DISTANCE) {
                        double r = Math.sqrt(RADIUS * RADIUS - y * y); // Radius of the current ring

                        for (int i = 0; i < PARTICLES_PER_RING; i++) {
                            double theta = (2 * Math.PI * i) / PARTICLES_PER_RING;
                            double x = r * Math.cos(theta);
                            double z = r * Math.sin(theta);
                            location.getWorld().spawnParticle(Particle.CLOUD, location.getX() + x, location.getY() + y, location.getZ() + z, 0, 0, 0, 0);
                        }
                    }
                    t++;
                    location.add(0, UPWARD_SPEED, 0); // Move upward each tick
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
