package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwiftnessBoots implements Listener {
    private Main plugin;
    RPGUtils utils = new RPGUtils();
    private final CooldownManager cooldownManager;

    public SwiftnessBoots(Main plugin){
        this.plugin = plugin;
        this.cooldownManager = new CooldownManager(plugin);
    }
    @EventHandler
    public void onSwiftnessSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if (!player.isSneaking()){
            ItemStack boots = player.getInventory().getBoots();
            if (utils.getRPGName(boots).equals("Swiftness Boots")){
                String rpgName = utils.getRPGName(boots);
                Long cooldown = cooldownManager.getCooldown(rpgName, player.getUniqueId());
                if (cooldown == null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 4));
                    for (Entity ent : player.getNearbyEntities(5, 5, 5)) {
                        if (ent instanceof LivingEntity) {
                            LivingEntity entity = (LivingEntity) ent;
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                        }
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_SHAKE, 100f, 1);
                    cooldownManager.setCooldown(rpgName, 20, player.getUniqueId());
                }else{
                    cooldownManager.sendCooldownMessage(player, rpgName, 20);
                }
            }
        }
    }
}
