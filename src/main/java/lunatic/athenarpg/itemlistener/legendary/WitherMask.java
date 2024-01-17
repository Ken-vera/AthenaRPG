package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitherMask implements Listener {
    private Main plugin;
    RPGUtils rpgUtils;
    ItemConstructor itemConstructor;
    CooldownManager cooldownManager;
    StatusListener statusListener;

    public WitherMask(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager(plugin);
        this.statusListener = new StatusListener(plugin);
    }
    @EventHandler
    public void WitherMaskSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            String rpgName = rpgUtils.getHelmetRPGName(player);
            int rpgLevel = rpgUtils.getRPGLevelInHelmet(player);
            int manaCost = itemConstructor.getHelmetManaCost(player);
            int cooldownTime = itemConstructor.getHelmetCooldown(player);
            if (rpgName.equals("Wither Mask")) {
                if (rpgLevel >= 3) {
                    Long cooldown = cooldownManager.getCooldown(rpgName, player.getUniqueId());
                    if (cooldown == null) {
                        if (statusListener.haveEnoughMana(player, manaCost)) {
                            player.getWorld().strikeLightningEffect(player.getLocation());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1));
                            cooldownManager.setCooldown(rpgName, cooldownTime, player.getUniqueId());
                            statusListener.consumeMana(player, manaCost);
                        }
                    }else {
                        cooldownManager.sendCooldownMessage(player, rpgName, cooldownTime);
                    }
                }
            }
        }
    }
}
