package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PharaohArmor implements Listener {
    private Main plugin;
    RPGUtils rpgUtils;
    ItemConstructor itemConstructor;
    CooldownManager cooldownManager;
    StatusListener statusListener;

    public PharaohArmor(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager();
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getDamager();
            Entity damagedEntity = e.getEntity();

            if (damagedEntity instanceof Player) {

                if (e.getDamage() > 0) {
                    Player damagedPlayer = (Player) damagedEntity;
                    if (rpgUtils.getChestplateRPGName(player).equals("Pharaoh Armor")) {
                        ItemStack chestplate = player.getInventory().getChestplate();
                        if (chestplate != null && chestplate.getType() != Material.AIR) {
                            int rpgLevel = rpgUtils.getRPGLevelInChestplate(player);
                            if (damagedPlayer.getInventory().getHelmet() != null
                                    && damagedPlayer.getInventory().getChestplate() != null
                                    && damagedPlayer.getInventory().getLeggings() != null
                                    && damagedPlayer.getInventory().getBoots() != null
                                    && damagedPlayer.getInventory().getHelmet().getType().equals(Material.NETHERITE_HELMET)
                                    && damagedPlayer.getInventory().getChestplate().getType().equals(Material.NETHERITE_CHESTPLATE)
                                    && damagedPlayer.getInventory().getLeggings().getType().equals(Material.NETHERITE_LEGGINGS)
                                    && damagedPlayer.getInventory().getBoots().getType().equals(Material.NETHERITE_BOOTS)) {

                                double damageIncrease = 2.0 + (rpgLevel * 0.2);

                                double originalDamage = e.getDamage();
                                double increasedDamage = originalDamage * (1 + damageIncrease);
                                e.setDamage(increasedDamage);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void PharaohDeath(EntityDamageEvent event){
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (((Player) event.getEntity()).getHealth() <= 5) {
                String rpgName = rpgUtils.getChestplateRPGName(player);
                int rpgLevel = rpgUtils.getRPGLevelInChestplate(player);
                if (rpgName.equals("Pharaoh Armor")) {
                    int vitalityCost = itemConstructor.getChestplateVitalityCost(player);
                    if (statusListener.haveEnoughVitality(player, vitalityCost)) {
                        event.setCancelled(true);
                        player.setHealth(player.getMaxHealth());

                        statusListener.consumeVitality(player, vitalityCost);
                        int potionDuration = 3 + rpgLevel;

                        player.playSound(player, Sound.ENTITY_BLAZE_AMBIENT, 20f, 0f);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, potionDuration * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, potionDuration * 20, 1));
                    }
                }
            }
        }
    }
}
