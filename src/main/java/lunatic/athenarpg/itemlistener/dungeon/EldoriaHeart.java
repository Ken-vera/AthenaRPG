package lunatic.athenarpg.itemlistener.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EldoriaHeart implements Listener {
    private JavaPlugin plugin;
    private Set<UUID> playersWithEffect = new HashSet<>();

    public EldoriaHeart(JavaPlugin plugin) {
        this.plugin = plugin;

        // Schedule a task to check player inventories periodically
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkPlayerInventories, 20L, 20L); // Check every second
    }

    private void checkPlayerInventories() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasEldoriaHeart(player.getInventory()) && !playersWithEffect.contains(player.getUniqueId())) {
                // Apply Health Boost effect level 1
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 0, false, false));
                playersWithEffect.add(player.getUniqueId());
            }
            else if (!hasEldoriaHeart(player.getInventory()) && playersWithEffect.contains(player.getUniqueId())){
                playersWithEffect.remove(player.getUniqueId());
                player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            }
        }
    }

    private boolean hasEldoriaHeart(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                    && item.getItemMeta().getDisplayName().equals("§5§lEldoria's Heart")) {
                return true;
            }
        }
        return false;
    }
}
