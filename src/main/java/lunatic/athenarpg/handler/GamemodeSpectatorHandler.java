package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GamemodeSpectatorHandler implements Listener {
    private Main plugin;

    public GamemodeSpectatorHandler(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void changeToSpectatorStaff(PlayerGameModeChangeEvent event){
        Player player = event.getPlayer();
        if (event.getNewGameMode().equals(GameMode.SPECTATOR)){
            // Check if the player's inventory is full
            if (hasItemInArmorSlot(player)) {
                player.sendMessage("");
                player.sendMessage("§c[STAFF] §fKAMU MASIH MENGGUNAKAN ARMOR SAAT SPECTATOR! PERLU DIPERHATIKAN BAHWA ARMOR YANG MEMILIKI PARTICLE DAPAT DILIHAT OLEH PLAYER LAIN!");
                player.sendMessage("");
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 20f, 0f);
            }
        }
    }
    private boolean hasItemInArmorSlot(Player player) {
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        for (ItemStack item : armorContents) {
            if (item != null) {
                // Found an item in the armor slot
                return true;
            }
        }
        // No item found in the armor slot
        return false;
    }

    private void unequipArmor(Player player) {
        // Get the player's current armor
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        Inventory playerInventory = player.getInventory();

        // Loop through each armor piece
        for (ItemStack armorPiece : armorContents) {
            // Check if the armor piece is not null (i.e., the player is wearing armor)
            if (armorPiece != null) {
                // Try to add the armor piece to the player's inventory
                HashMap<Integer, ItemStack> remaining = playerInventory.addItem(armorPiece);

                // If there's any remaining armor that couldn't be added to the inventory, drop it at the player's location
                for (ItemStack remainingItem : remaining.values()) {
                    // You can handle this case however you prefer; in this case, we simply leave the item on the ground
                    player.getWorld().dropItem(player.getLocation(), remainingItem);
                }
            }
        }

        // Clear the player's armor slots
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

}