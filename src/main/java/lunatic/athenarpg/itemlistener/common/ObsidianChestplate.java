package lunatic.athenarpg.itemlistener.common;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ObsidianChestplate implements Listener {
    private Main plugin;

    RPGUtils utils = new RPGUtils();

    public ObsidianChestplate(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            Entity damager = e.getDamager();
            Player damagedPlayer = (Player) e.getEntity();

            if (e.getDamage() > 0) {
                if (damager.getType() == EntityType.PLAYER) {
                    LivingEntity attacker = (LivingEntity) damager;

                    if (damagedPlayer instanceof Player) {
                        ItemStack chestplate = damagedPlayer.getInventory().getChestplate();
                        if (chestplate != null && utils.getChestplateName(chestplate).equals("Obsidian Chestplate")) {
                            handleObsidianChestplateReflection(damagedPlayer, attacker);
                        }
                    }
                }
            }
        }
    }

    private void handleObsidianChestplateReflection(Player damagedPlayer, LivingEntity attacker) {
        int rpgLevel = utils.getChestplateLevel(damagedPlayer.getInventory().getChestplate());

        // Calculate base chance and increase by 5% per level
        double baseChance = 0.05;
        double chanceIncrease = 0.05 * rpgLevel;
        double randDouble = Math.random();

        if (randDouble <= baseChance + chanceIncrease) {
            // Calculate damage increase based on RPG level
            double damageIncrease = 0.02 * rpgLevel;
            double reflectedDamage = attacker.getLastDamage() * damageIncrease;

            handleReflectionDamage(damagedPlayer, attacker, reflectedDamage);
        }
    }

    private void handleReflectionDamage(Player damagedPlayer, LivingEntity attacker, double damage) {
        damage = Double.parseDouble(String.format("%.2f", damage)); // Format to one decimal place
        attacker.damage(damage, damagedPlayer);
        attacker.sendMessage("§b" + damagedPlayer.getName() + " reflected " + damage + " damage from Obsidian Destruction!");
        damagedPlayer.sendMessage("§bYou reflected " + damage + " damage to " + attacker.getName());
        damagedPlayer.playSound(attacker.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10.0F, 1.4F);
    }
}
