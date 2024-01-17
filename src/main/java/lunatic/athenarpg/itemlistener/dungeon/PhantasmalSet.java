package lunatic.athenarpg.itemlistener.dungeon;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PhantasmalSet implements Listener {
    private Main plugin;
    RPGUtils rpgUtils;
    ItemConstructor itemConstructor;
    CooldownManager cooldownManager;
    StatusListener statusListener;

    public PhantasmalSet(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager(plugin);
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void fullSetPhantasmal(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getDamager();
            Entity damagedEntity = e.getEntity();

            if (e.getDamage() > 0) {
                if (rpgUtils.isFullSet(player, "Phantasmal")) {
                    if (damagedEntity.getName().equals("§5§lEldoria")) {
                        double currentDamage = e.getDamage();
                        e.setDamage(currentDamage * 1.80);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            if (rpgUtils.isFullSet(player, "Phantasmal")) {
                if (player.getWorld().getName().equals("worldDungeon")) {
                    int rpgLevel = rpgUtils.getRPGLevelInHand(player);
                    int vitalityCost = itemConstructor.getFullSetVitalityCost(player);
                    int cooldownTime = 30 - rpgLevel;
                    Long cooldown = cooldownManager.getCooldown("Phantasmal", player.getUniqueId());
                    if (cooldown == null) {
                        if (statusListener.haveEnoughVitality(player, vitalityCost)) {
                            if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 255));
                                player.playSound(player, Sound.BLOCK_ANVIL_PLACE, 20f, 0f);
                            }
                            statusListener.consumeVitality(player, vitalityCost);
                            int finalCooldownTime = cooldownTime;
                            cooldownManager.setCooldown("Phantasmal", finalCooldownTime, player.getUniqueId());
                        }
                    }
                }
            }
        }
    }
}
