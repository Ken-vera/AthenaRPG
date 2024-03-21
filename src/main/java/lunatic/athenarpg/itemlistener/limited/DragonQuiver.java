package lunatic.athenarpg.itemlistener.limited;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DragonQuiver implements Listener {
    private Main plugin;
    private RPGUtils rpgUtils;
    private Map<Player, Inventory> quiverInventories;
    private final Inventory dragonQuiverInventory;

    public DragonQuiver(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.quiverInventories = new HashMap<>();
        this.dragonQuiverInventory = createDragonQuiverInventory(9);
    }

    private void saveArrowCount(Player player, int rpgLevel, int arrowAmount) {
        plugin.fileManager.getConfig("quiverData.yml").set(player.getName() + ".level_" + rpgLevel, arrowAmount);
        plugin.fileManager.saveConfig("quiverData.yml");
    }



    private Inventory createDragonQuiverInventory(int size) {
        Inventory inventory = Bukkit.createInventory(null, size, "Dragon Quiver");

        return inventory;
    }

    @EventHandler
    public void quiverInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            String rpgName = rpgUtils.getRPGNameInHand(player);
            if (rpgName.equals("Dragon Quiver")) {
                int size = rpgUtils.getRPGLevelInHand(player) * 9;
                openQuiverInventory(player, size);
            }
        }
    }

    private void openQuiverInventory(Player player, int size) {
        Inventory quiverInventory = Bukkit.createInventory(null, size, "Dragon Quiver");

        int rpgLevel = rpgUtils.getRPGLevelInHand(player);
        String playerName = player.getName();
        int totalArrowAmount = plugin.fileManager.getConfig("quiverData.yml").get().getInt(playerName + ".level_" + rpgLevel, 0);

        int maxStackSize = Material.ARROW.getMaxStackSize();
        int remainingArrows = totalArrowAmount;

        while (remainingArrows > 0) {
            int arrowsInStack = Math.min(remainingArrows, maxStackSize);
            ItemStack arrowItem = new ItemStack(Material.ARROW, arrowsInStack);
            quiverInventory.addItem(arrowItem);

            remainingArrows -= arrowsInStack;
        }

        player.openInventory(quiverInventory);
        quiverInventories.put(player, quiverInventory);
    }



    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null && clickedItem.getType().equals(Material.ARROW)) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.hasItemMeta()) {
                    if (item.getItemMeta().getDisplayName().contains("§c§lDragon Quiver")) {
                        if (event.getSlot() == 8) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

        if (clickedInventory != null && quiverInventories.containsValue(clickedInventory)) {
            // Check if the clicked item is not an arrow
            if (clickedItem != null && !clickedItem.getType().equals(Material.ARROW)) {
                event.setCancelled(true);
            }
        }
        if (clickedInventory != null && quiverInventories.containsValue(event.getInventory())) {
            if (clickedItem != null && !clickedItem.getType().equals(Material.ARROW)) {
                event.setCancelled(true);
            }
        }
        if (event.getAction().name().contains("PLACE_ALL") && event.getSlotType() == InventoryType.SlotType.CONTAINER) {
            if (clickedInventory != null && quiverInventories.containsValue(event.getInventory())) {
                if (clickedItem != null && !clickedItem.getType().equals(Material.ARROW)) {
                    event.setCancelled(true);
                }
            }
        }
        if (clickedInventory != null && quiverInventories.containsValue(event.getInventory())) {
            if (event.getClick() == ClickType.NUMBER_KEY) {
                int hotbarButton = event.getHotbarButton();
                if (hotbarButton >= 0 && hotbarButton <= 8) {
                    ItemStack clickedItemHotbar = player.getInventory().getItem(hotbarButton);

                    if (clickedItemHotbar != null && !clickedItemHotbar.getType().equals(Material.ARROW)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        if (quiverInventories.containsValue(closedInventory)) {
            Player player = (Player) event.getPlayer();

            ItemStack[] contents = closedInventory.getContents();
            int arrowAmount = 0;

            for (ItemStack itemStack : contents) {
                if (itemStack != null && itemStack.getType().equals(Material.ARROW)) {
                    arrowAmount += itemStack.getAmount();
                }
            }
            int rpgLevel = rpgUtils.getRPGLevelInHand(player);
            saveArrowCount(player, rpgLevel, arrowAmount);
            quiverInventories.remove(player);
        }
    }
    @EventHandler
    public void onBowSwap(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);

        ItemMeta meta;
        List<String> lore = null;
        if (newItem != null && newItem.hasItemMeta() && newItem.getItemMeta().hasLore()) {
            meta = newItem.getItemMeta();
            lore = meta.getLore();
        }
        if (player.getInventory().getItem(newSlot) != null) {
            // Check if the new item is a bow
            if (player.getInventory().getItem(newSlot).getType().equals(Material.BOW) || (lore != null && lore.contains("§cCrossbow"))) {
                // Loop through the player's inventory to find Dragon Quivers
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta()) {
                        if (item.getItemMeta().getDisplayName().contains("Dragon Quiver")) {
                            String rpgName = rpgUtils.getRPGName(item);
                            int rpgLevel = rpgUtils.getRPGLevel(item);

                            // Check if the item is a Dragon Quiver with a non-zero RPG level
                            if (rpgName.equals("Dragon Quiver") && rpgLevel != 0) {
                                // Change the Dragon Quiver to arrows
                                int arrowAmount = plugin.fileManager.getConfig("quiverData.yml").get().getInt(player.getName() + ".level_" + rpgLevel, 0);
                                if (player.getInventory().getItem(8) == null) {
                                    if (arrowAmount >= 64) {
                                        ItemStack arrowStack = new ItemStack(Material.ARROW, 64);
                                        player.getInventory().setItem(8, arrowStack);
                                        player.updateInventory();
                                        List<String> finalLore = lore;
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                if (player.getInventory().getItem(newSlot) != null && player.getInventory().getItem(newSlot).getType().equals(Material.BOW) || (finalLore != null && finalLore.contains("§cCrossbow"))) {
                                                    if (player.getInventory().getItem(8) != null && player.getInventory().getItem(8).getType().equals(Material.ARROW) && player.getInventory().getItem(8).getAmount() < 64 && arrowAmount >= 1) {
                                                        ItemStack arrowStack = new ItemStack(Material.ARROW, 64);
                                                        player.getInventory().setItem(8, arrowStack);
                                                        player.updateInventory();
                                                        int arrowCount = plugin.fileManager.getConfig("quiverData.yml").get().getInt(player.getName() + ".level_" + rpgLevel, 0);
                                                        plugin.fileManager.getConfig("quiverData.yml").set(player.getName() + ".level_" + rpgLevel, arrowCount - 1);
                                                        plugin.fileManager.saveConfig("quiverData.yml");
                                                    } else if (arrowAmount < 1) {
                                                        player.sendMessage("§cYou're out of arrows!");
                                                        cancel();
                                                    }
                                                } else {
                                                    player.getInventory().setItem(8, null);
                                                    player.updateInventory();
                                                    cancel();
                                                }
                                            }
                                        }.runTaskTimer(plugin, 1L, 1L);
                                    } else {
                                        player.sendMessage("§cFor using quiver you need at least 64 arrows!");
                                    }
                                } else {
                                    player.sendMessage("§cYou need to clear slot 9 before using quiver!");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBowSwapRemove(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int oldSlot = event.getPreviousSlot();
        int newSlot = event.getNewSlot();
        ItemStack oldItem = player.getInventory().getItem(oldSlot);

        ItemMeta meta;
        List<String> lore = null;
        if (oldItem != null && oldItem.hasItemMeta() && oldItem.getItemMeta().hasLore()) {
            meta = oldItem.getItemMeta();
            lore = meta.getLore();
        }
        if (player.getInventory().getItem(oldSlot) != null) {
            // Check if the old item is a bow
            if (player.getInventory().getItem(oldSlot).getType().equals(Material.BOW) || (lore != null && lore.contains("§cCrossbow"))) {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta()) {
                        if (item.getItemMeta().getDisplayName().contains("Dragon Quiver")) {
                            String rpgName = rpgUtils.getRPGName(item);
                            int rpgLevel = rpgUtils.getRPGLevel(item);

                            // Check if the item is a Dragon Quiver with a non-zero RPG level
                            if (rpgName.equals("Dragon Quiver") && rpgLevel != 0) {
                                if (player.getInventory().getItem(8) != null && player.getInventory().getItem(8).getType().equals(Material.ARROW)) {
                                    player.getInventory().setItem(8, null);
                                    player.updateInventory();
                                    break; // Exit the loop after setting the head item
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
