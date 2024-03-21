package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class SpiritPowerOrb implements Listener {
    private Main plugin;
    private final RPGUtils rpgUtils;
    private final ItemConstructor itemConstructor;

    public SpiritPowerOrb(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
    }

    @EventHandler
    public void SpiritFlux(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String rpgName = rpgUtils.getRPGNameInHand(e.getPlayer());
        if (rpgName.equals("Spirit Power Orb")) {
            if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                ArmorStand spirit = p.getWorld().spawn(p.getLocation().add(0, 1, 0), ArmorStand.class);
                ArmorStand spiritholo = p.getWorld().spawn(p.getLocation().add(0, 1, 0), ArmorStand.class);
                spirit.setHelmet(Main.getHead("spirit"));
                spirit.setVisible(false);
                spirit.setInvulnerable(true);
                spirit.setCustomName("spiritflux");
                spirit.setGravity(false);
                spirit.setMarker(true);

                spiritholo.setVisible(false);
                spiritholo.setInvulnerable(true);
                spiritholo.setCustomName("§6" + (p.getName()) + "'s Spirit Power Orb");
                spiritholo.setCustomNameVisible(true);
                spiritholo.setGravity(false);

                plugin.armorStandList.add(spirit);
                plugin.armorStandList.add(spiritholo);

                moveUpSpirit(spirit, spiritholo, p);
                secondDamageSpirit(p, spirit, spiritholo);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    spirit.remove();
                    spiritholo.remove();
                    plugin.armorStandList.remove(spirit);
                    plugin.armorStandList.remove(spiritholo);
                    p.sendMessage("§aYour flux has been removed!");
                }, 200);
            } else {
                e.setCancelled(true);
                p.sendMessage("§cYou don't have enough mana!");
            }
        }
    }


    private void secondDamageSpirit(Player p, ArmorStand spirit, ArmorStand spiritholo) {
        spiritholo.setCustomName("§6" + (p.getName()) + "'s Spirit Power Orb §e§l" + 10 + "s");
        new BukkitRunnable() {
            int time = 9;

            @Override
            public void run() {
                spiritholo.setCustomName("§6" + (p.getName()) + "'s Spirit Power Orb §e§l" + time + "s");
                if (time > 0) {
                    if (!spirit.isDead()) {
                        time--;
                        for (Entity ent : spirit.getNearbyEntities(18, 18, 18)) {
                            if (ent instanceof Player) {
                                ((Player) ent).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2));
                            }
                            if (ent instanceof Monster) {
                                Objects.requireNonNull(spirit.getLocation().getWorld()).playSound(spirit.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 5, 2);
                                Objects.requireNonNull(ent.getLocation().getWorld()).playSound(ent.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 5, 0);
                                ent.getLocation().getWorld().playEffect(ent.getLocation(), Effect.MOBSPAWNER_FLAMES, 2004);
                                drawLineSpiritFlux(spirit.getEyeLocation().add(0, 2, 0), ((Monster) ent).getEyeLocation(), 1D);
                                if (((Monster) ent).getHealth() < 2000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.01);
                                }
                                if (((Monster) ent).getHealth() > 2000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.01);
                                }
                                if (((Monster) ent).getHealth() > 2000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.02);
                                }
                                if (((Monster) ent).getHealth() > 4000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.03);
                                }
                                if (((Monster) ent).getHealth() > 6000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.04);
                                }
                                if (((Monster) ent).getHealth() > 8000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.05);
                                }
                                if (((Monster) ent).getHealth() > 10000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.06);
                                }
                                if (((Monster) ent).getHealth() > 12000) {
                                    Monster monster = (Monster) ent;
                                    monster.damage(monster.getHealth() * 0.07);
                                }
                            }
                        }
                    }
                }
                if(time == 0){
                    spirit.remove();
                    spiritholo.remove();
                }
            }
        }.runTaskTimer(plugin, 20,20);
    }


    private void moveUpSpirit(ArmorStand spirit, ArmorStand spiritholo, Player p) {
        new BukkitRunnable() {
            private boolean goingUp = true;
            Location loc = spirit.getLocation();
            private final int maximumHeight = loc.getBlockY() + 2;
            private final int minimumHeight = loc.getBlockY();

            @Override
            public void run() {
                if (spirit.isDead()) {
                    spirit.remove();
                    spiritholo.remove();
                    plugin.armorStandList.remove(spirit);
                    plugin.armorStandList.remove(spiritholo);
                    cancel();
                    return;
                }
                if(spirit.getWorld().getName().equalsIgnoreCase(p.getWorld().getName())) {
                    if (goingUp) {
                        if (spirit.getLocation().getY() > maximumHeight) {
                            goingUp = false;
                        } else {
                            loc.setYaw(loc.getYaw() + (float) 20);
                            spirit.teleport(loc.add(0, 0.07, 0));
                            spiritholo.teleport(loc.clone().add(0, 0.22, 0));
                        }
                    } else {
                        if (spirit.getLocation().getY() < minimumHeight) {
                            goingUp = true;
                        } else {
                            loc.setYaw(loc.getYaw() + (float) 20);
                            spirit.teleport(loc.add(0, -0.07, 0));
                            spiritholo.teleport(loc.clone().add(0, 0.22, 0));
                        }
                    }
                }else{
                    spirit.remove();
                    spiritholo.remove();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }
    public void drawLineSpiritFlux(
            /* Would be your orange wool */Location point1,
            /* Your white wool */ Location point2,
            /*Space between each particle*/double space
    ) {

        World world = point1.getWorld();

        /*Throw an error if the points are in different worlds*/

        /*Distance between the two particles*/
        double distance = point1.distance(point2);

        /* The points as vectors */
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();

        /* Subtract gives you a vector between the points, we multiply by the space*/
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);

        /*The distance covered*/
        double covered = 0;

        /* We run this code while we haven't covered the distance, we increase the point by the space every time*/
        for (; covered < distance; p1.add(vector)) {
            /*Spawn the particle at the point*/
            assert world != null;
            world.spawnParticle(Particle.FLAME, p1.getX(), p1.getY(), p1.getZ(), 10, 0F, 0F, 0F, 0F);

            /* We add the space covered */
            covered += space;
        }
    }
}
