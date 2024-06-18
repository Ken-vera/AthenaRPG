package lunatic.athenarpg.itemlistener.epic;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.MMOItemsHook;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class PandoraBow implements Listener {
    private Main plugin;
    private RPGUtils utils;
    private ItemConstructor itemConstructor;
    private CooldownManager cooldownManager;
    private MMOItemsHook mmoItemsHook;

    Random random = new Random();

    public PandoraBow(Main plugin){
        this.plugin = plugin;
        this.utils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager();
        this.mmoItemsHook = new MMOItemsHook();
    }



    @EventHandler
    public void PandoraBowShoot(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            String rpgName = utils.getRPGNameInHand(player);
            ItemStack item = player.getItemInHand();
            int rpgLevel = utils.getRPGLevel(player.getItemInHand());

            if (rpgName.equals("Pandora Bow")){
                Arrow arrow = (Arrow) event.getProjectile();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (arrow.isDead() || arrow.isOnGround()) {
                            this.cancel();
                            return;
                        }

                        List<Entity> nearbyEntities = arrow.getNearbyEntities(5, 5, 5);
                        LivingEntity nearestEntity = null;
                        double nearestDistance = Double.MAX_VALUE;

                        for (Entity entity : nearbyEntities) {
                            if (entity instanceof LivingEntity && entity != player) {
                                double distance = entity.getLocation().distance(arrow.getLocation());
                                if (distance < nearestDistance) {
                                    nearestEntity = (LivingEntity) entity;
                                    nearestDistance = distance;
                                }
                            }
                        }

                        if (nearestEntity != null) {
                            Vector direction = nearestEntity.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize();
                            arrow.setVelocity(direction.multiply(1));
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }
    }
}
