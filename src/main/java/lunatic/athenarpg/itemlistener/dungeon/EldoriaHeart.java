package lunatic.athenarpg.itemlistener.dungeon;

import lunatic.athenarpg.Main;
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
    private Main plugin;
    private Set<UUID> playersWithEffect = new HashSet<>();

    public EldoriaHeart(Main plugin) {
        this.plugin = plugin;

        // Schedule a task to check player inventories periodically
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkPlayerInventories, 200L, 200L); // Check every second
    }

    private void checkPlayerInventories() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasEldoriaHeart(player)) {
                applyEffect(player);
            } else {
                removeEffect(player);
            }
        }
    }

    private boolean hasEldoriaHeart(Player player) {
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                    && item.getItemMeta().getDisplayName().equals("§5§lEldoria's Heart")) {
                return true;
            }
        }
        return false;
    }

    private void applyEffect(Player player) {
        if (!playersWithEffect.contains(player.getUniqueId())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 0, false, false));
            playersWithEffect.add(player.getUniqueId());
        }
    }

    private void removeEffect(Player player) {
        if (playersWithEffect.contains(player.getUniqueId())) {
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            playersWithEffect.remove(player.getUniqueId());
        }
    }
}
