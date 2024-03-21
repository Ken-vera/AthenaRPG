package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DevilDagger implements Listener {
    private Main plugin;
    private RPGUtils rpgUtils;
    private ItemConstructor itemConstructor;
    private StatusListener statusListener;

    public DevilDagger(Main plugin){
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.statusListener = new StatusListener(plugin);
    }
    @EventHandler
    public void DevilDagger(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String rpgName = rpgUtils.getRPGNameInHand(player);
        int rpgLevel = rpgUtils.getRPGLevelInHand(player);

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (rpgName.equals("Devil Dagger")){
                    int manaCost = itemConstructor.getMainHandManaCost(player);
                    if (statusListener.haveEnoughMana(player, manaCost)){
                        for (Entity entity : player.getNearbyEntities(3, 3, 3)){
                            if (entity instanceof Monster) {
                                LivingEntity monster = (LivingEntity) entity;
                                monster.damage(monster.getHealth() * 0.01, player);

                                int baseDurationInSeconds = 3;
                                int additionalDurationPerLevel = 3;
                                int durationInSeconds = baseDurationInSeconds + (rpgLevel * additionalDurationPerLevel);

                                int amplifier = 255;

                                PotionEffectType stunEffect = PotionEffectType.SLOW;
                                PotionEffect stunPotionEffect = new PotionEffect(stunEffect, durationInSeconds * 20, amplifier);
                                monster.addPotionEffect(stunPotionEffect);

                                double baseHealAmount = 2;
                                double additionalHealPerLevel = 2;
                                double healAmount = baseHealAmount + (rpgLevel * additionalHealPerLevel);

                                if (player.getHealth() + healAmount < player.getMaxHealth()){
                                    player.setHealth(player.getHealth() + healAmount);
                                }

                                player.playSound(player, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 2f);
                                statusListener.consumeMana(player, manaCost);
                            }
                        }
                    }
                }
            }
        }
    }
}
