package lunatic.athenarpg.itemlistener.dungeon;

import io.lumine.mythic.core.skills.mechanics.ParticleEffect;
import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SerpentineSceptre implements Listener {
    private Main plugin;
    private final StatusListener statusListener;
    private final ItemConstructor itemConstructor;
    RPGUtils utils;

    public SerpentineSceptre(Main plugin, StatusListener statusListener) {
        this.plugin = plugin;
        this.utils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.statusListener = statusListener;
    }

    @EventHandler
    public void onSceptreRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String rpgName = utils.getRPGNameInHand(player);
        int rpgLevel = utils.getRPGLevelInHand(player);

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (rpgName.equals("Serpentine Sceptre")) {
                    int manaCost = itemConstructor.getMainHandManaCost(player);

                    if (statusListener != null && statusListener.haveEnoughMana(player, manaCost)) {

                        statusListener.consumeMana(player, manaCost);

                        player.playSound(player, Sound.ENTITY_BLAZE_SHOOT, 10f, 0f);

                        Bat bat = player.getWorld().spawn(player.getLocation().add(0, 1, 0), Bat.class);
                        bat.setAI(false);
                        bat.setAwake(true);
                        bat.setInvulnerable(true);

                        // Calculate speed based on RPG level
                        double speedMultiplier = 1.5 + (0.2 * rpgLevel);
                        int damageIncrease = 3 * rpgLevel;

                        new BukkitRunnable() {
                            int ticks = 0;

                            @Override
                            public void run() {
                                if (!bat.isDead()) {
                                    if (ticks < 50) {
                                        Vector direction = player.getLocation().add(0, 1, 0).getDirection().multiply(speedMultiplier);
                                        bat.teleport(bat.getLocation().add(direction));
                                        for (Entity ent : bat.getNearbyEntities(4, 4, 4)) {
                                            if (ent instanceof LivingEntity) {
                                                if (ent != player && !(ent instanceof Bat) && !(ent instanceof ArmorStand)) {
                                                    LivingEntity livingEntity = (LivingEntity) ent;
                                                    livingEntity.damage(20 + damageIncrease, player);
                                                    player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 30f, 0f);
                                                    bat.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, bat.getLocation(), 2);
                                                    bat.remove();
                                                    cancel();
                                                }
                                            }
                                        }
                                        if (bat.getLocation().getBlock().getType().isSolid()) {
                                            for (Entity ent : bat.getNearbyEntities(4, 4, 4)) {
                                                if (ent instanceof LivingEntity) {
                                                    if (ent != player && !(ent instanceof Bat) && !(ent instanceof ArmorStand)) {
                                                        LivingEntity livingEntity = (LivingEntity) ent;
                                                        livingEntity.damage(20 + damageIncrease, player);
                                                    }
                                                }
                                            }
                                            bat.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, bat.getLocation(), 2);
                                            player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 30f, 0f);
                                            bat.remove();
                                            cancel();
                                        }
                                        ticks++;
                                    } else {
                                        for (Entity ent : bat.getNearbyEntities(4, 4, 4)) {
                                            if (ent instanceof LivingEntity) {
                                                if (ent != player && !(ent instanceof Bat) && !(ent instanceof ArmorStand)) {
                                                    LivingEntity livingEntity = (LivingEntity) ent;
                                                    livingEntity.damage(20 + damageIncrease, player);
                                                }
                                            }
                                        }
                                        bat.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, bat.getLocation(), 2);
                                        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 30f, 0f);
                                        bat.remove();
                                        cancel();
                                    }
                                } else {
                                    bat.remove();
                                    cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0, 1);

                    }
                }
            }
        }
    }
}