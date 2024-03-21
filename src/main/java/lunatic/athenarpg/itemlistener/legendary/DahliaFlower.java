package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class DahliaFlower implements Listener {
    private final Main plugin;
    private final RPGUtils rpgUtils;
    private final ItemConstructor itemConstructor;
    CooldownManager cooldownManager;

    public DahliaFlower(Main plugin) {
        this.plugin = plugin;
        rpgUtils = new RPGUtils();
        itemConstructor = new ItemConstructor();
        cooldownManager = new CooldownManager();
    }

    @EventHandler
    public void DahliaFlower(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String rpgName = rpgUtils.getRPGNameInHand(player);
        int rpgLevel = rpgUtils.getRPGLevelInHand(player);

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (rpgName.equals("Dahlia Flower")) {
                    if (!cooldownManager.isOnCooldown(player.getName(), rpgName)) {
                        Vector dir = player.getEyeLocation().getDirection().multiply(1);
                        ArmorStand dahlia = player.getWorld().spawn(player.getLocation().add(0.0D, 0.0D, 0.0D), ArmorStand.class);
                        ItemStack rose = new ItemStack(Material.WITHER_ROSE, 1);
                        dahlia.setRightArmPose(dahlia.getRightArmPose().add(0.0D, 0.0D, 0.0D));
                        dahlia.setHeadPose(dahlia.getHeadPose().add(0.0D, 0.0D, 0.0D));
                        dahlia.setItemInHand(rose);
                        dahlia.setVisible(false);
                        dahlia.setInvulnerable(false);
                        dahlia.setGravity(false);
                        dahlia.setPersistent(true);
                        dahlia.setMarker(true);
                        dahlia.setCustomName("dahlia");

                        DahliaForward(dahlia, dir, player);
                        plugin.armorStandList.add(dahlia);

                        int cooldownTime = itemConstructor.getMainHandCooldown(player);
                        cooldownManager.setCooldown(player.getName(), rpgName, cooldownTime);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.0F, 0.0F);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            dahlia.remove();
                        }, 80L);
                    } else {
                        cooldownManager.sendCooldownMessage(player, rpgName);
                    }
                }
            }
        }
    }

    private void DahliaForward(final ArmorStand dahlia, Vector dir, final Player p) {
        new BukkitRunnable() {
            private boolean goingUp = true;
            private Location loc = dahlia.getLocation();
            private Location max;
            private Vector forward;
            private Vector back;

            {
                max = loc.getWorld().getSpawnLocation();
                forward = p.getLocation().getDirection().multiply(2);
                back = p.getLocation().getDirection().multiply(-1);
            }

            @Override
            public void run() {
                if (!dahlia.isDead()) {
                    if (p.getItemInHand().getType() == Material.WITHER_ROSE) {
                        dahlia.teleport(dahlia.getLocation().add(forward));
                        Iterator<Entity> iterator = dahlia.getNearbyEntities(3.0D, 5.0D, 3.0D).iterator();

                        while (iterator.hasNext()) {
                            Entity ent = iterator.next();
                            if (ent instanceof Monster) {
                                LivingEntity entity = (LivingEntity) ent;
                                dahlia.teleport(ent.getLocation());
                                entity.damage(30.0D, p);
                            }
                        }
                    } else {
                        dahlia.remove();
                        cancel();
                    }
                }else{
                    dahlia.remove();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }


}
