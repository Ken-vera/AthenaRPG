package lunatic.athenarpg.itemlistener.dungeon.pvp;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DiamondSetListener implements Listener {
    private Main plugin;

    public DiamondSetListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableDiamondSet(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                Player damagerPlayer = (Player) e.getDamager();
                if (player.getWorld().getName().equals("worldDungeon") && e.getDamage() > 0) {
                    if (isFullSetDiamondNotEnchanted(player)) {
                        e.setCancelled(true);
                    }
                }
            }
            if (e.getEntity() instanceof LivingEntity) {
                Player damagerPlayer = (Player) e.getDamager();
                if (isFullSetDiamondNotEnchanted(damagerPlayer)) {
                    if (damagerPlayer.getWorld().getName().equals("worldDungeon")) {
                        LivingEntity livingEntity = (LivingEntity) e.getEntity();
                        if (livingEntity.getName().equals("§5§lEldoria")) {
                            e.setDamage(e.getDamage() * 0.5);
                        }
                    }
                }
            }
        }
    }

    public boolean isFullSetDiamondNotEnchanted(Player player){
        return isDiamondHelmetUnenchanted(player) &&
                isDiamondChestplateUnenchanted(player) &&
                isDiamondLeggingsUnenchanted(player) &&
                isDiamondBootsUnenchanted(player);
    }

    private boolean isDiamondHelmetUnenchanted(Player player) {
        return isItemUnenchanted(player.getInventory().getHelmet(), Material.DIAMOND_HELMET);
    }

    private boolean isDiamondChestplateUnenchanted(Player player) {
        return isItemUnenchanted(player.getInventory().getChestplate(), Material.DIAMOND_CHESTPLATE);
    }

    private boolean isDiamondLeggingsUnenchanted(Player player) {
        return isItemUnenchanted(player.getInventory().getLeggings(), Material.DIAMOND_LEGGINGS);
    }

    private boolean isDiamondBootsUnenchanted(Player player) {
        return isItemUnenchanted(player.getInventory().getBoots(), Material.DIAMOND_BOOTS);
    }

    private boolean isItemUnenchanted(ItemStack item, Material material) {
        return item != null && item.getType() == material && item.getEnchantments().isEmpty();
    }
}
