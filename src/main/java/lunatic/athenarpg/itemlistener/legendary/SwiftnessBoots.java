package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class SwiftnessBoots implements Listener {
    private Main plugin;
    RPGUtils utils = new RPGUtils();
    ItemConstructor itemConstructor;
    CooldownManager cooldownManager = new CooldownManager();;

    public SwiftnessBoots(Main plugin){
        this.plugin = plugin;
        this.itemConstructor = new ItemConstructor();
    }
    @EventHandler
    public void onSwiftnessSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!event.isSneaking()){
            String rpgName = utils.getBootsRPGName(player);
            if (rpgName.equals("Swiftness Boots")){
                int cooldownTime = itemConstructor.getBootsCooldown(player);
                if (!cooldownManager.isOnCooldown(player.getName(), rpgName)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 4));
                    for (Entity ent : player.getNearbyEntities(5, 5, 5)) {
                        if (ent instanceof LivingEntity) {
                            LivingEntity entity = (LivingEntity) ent;
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                        }
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_SHAKE, 100f, 1);
                    cooldownManager.setCooldown(player.getName(), rpgName, cooldownTime);
                } else {
                    cooldownManager.sendCooldownMessage(player, rpgName);
                }
            }
        }
    }
}
