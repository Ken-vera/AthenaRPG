package lunatic.athenarpg.itemlistener.epic;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Crusader implements Listener {
    private final Main plugin;
    private final RPGUtils utils;

    public Crusader(Main plugin) {
        this.plugin = plugin;
        this.utils = new RPGUtils();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || event.getDamage() <= 0) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Entity damagedEntity = event.getEntity();

        String mainHand = utils.getRPGNameInHand(damager);
        String offHand = utils.getRPGNameInOffHand(damager);
        int rpgLevelMainHand = utils.getRPGLevelInHand(damager);
        int rpgLevelOffHand = utils.getRPGLevelInOffHand(damager);

        if (damagedEntity instanceof LivingEntity) {

            if (mainHand.equals("Crusader of Wasting Time") && offHand.equals("Crusader of Wasting Time") && rpgLevelMainHand == rpgLevelOffHand) {
                double increaseMultiplier = getIncreaseMultiplier(rpgLevelMainHand);
                double increasedDamage = event.getDamage() * increaseMultiplier;
                damagedEntity.getWorld().playSound(damagedEntity.getLocation(), Sound.ENTITY_BLAZE_HURT, 50f, 1f);
                event.setDamage(increasedDamage);
            }
        }
    }

    private double getIncreaseMultiplier(int level) {
        switch (level) {
            case 1:
                return 2.0;
            case 2:
                return 2.3;
            case 3:
                return 2.6;
            case 4:
                return 3.0;
            default:
                return 1.0; // Default multiplier if level is not 1-4
        }
    }
}
