package lunatic.athenarpg.itemlistener.dungeon;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EldoriaSet implements Listener {
    private Main plugin;
    RPGUtils utils = new RPGUtils();

    public EldoriaSet(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getDamager();
            Entity damagedEntity = e.getEntity();

            if (e.getDamage() > 0) {
                if (utils.isFullSet(player, "Eldoria")) {
                    if (damagedEntity.getName().equals("§5§lEldoria")) {
                        double currentDamage = e.getDamage();
                        e.setDamage(currentDamage * 1.75);
                    } else {
                        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
                        }
                    }
                }
            }
        }
    }
}
