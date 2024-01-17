package lunatic.athenarpg.itemlistener.rare;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VindicatorCrossbow implements Listener {
    private Main plugin;
    private RPGUtils rpgUtils;
    private ItemConstructor itemConstructor;

    private StatusListener statusListener;

    public VindicatorCrossbow(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;

            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                if (damaged instanceof Player || damaged instanceof Monster) {
                    LivingEntity damagedEntity = (LivingEntity) damaged;
                    String rpgName = rpgUtils.getRPGNameInHand(shooter);
                    int rpgLevel = rpgUtils.getRPGLevelInHand(shooter);

                    if (rpgName.equals("Vindicator Crossbow")) {
                        if (rpgLevel >= 3) {
                            int vitalityCost = itemConstructor.getMainHandVitalityCost(shooter);

                            double altitudeDifference = shooter.getLocation().getY() - damagedEntity.getLocation().getY();
                            if (altitudeDifference > 5) {
                                double increasedDamage = 1.2 * altitudeDifference;
                                event.setDamage(event.getDamage() + increasedDamage);

                                statusListener.consumeVitality(shooter, vitalityCost);

                                damagedEntity.getWorld().playSound(damagedEntity, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10f, 1f);
                                shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 0));
                            }
                        }
                    }
                }
            }
        }
    }
}
