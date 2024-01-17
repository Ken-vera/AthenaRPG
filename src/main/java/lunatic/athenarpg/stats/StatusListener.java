package lunatic.athenarpg.stats;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class StatusListener implements Listener {
    private final Main plugin;
    private final Map<UUID, Integer> lastMaxManaValues = new HashMap<>();

    public StatusListener(Main plugin) {
        this.plugin = plugin;
    }

    private PlayerStatus getPlayerStatus(Player player) {
        UUID playerId = player.getUniqueId();
        return plugin.playerStatusMap.computeIfAbsent(playerId, id ->
                new PlayerStatus(player, (int) player.getMaxHealth(), (int) player.getHealth(), 100, 100));
    }

    private void sendMessageAndPlaySound(Player player, String message) {
        player.sendMessage(message);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 30f, 1f);
    }

    public void setPlayerMaxStatus(Player player, int maxHealth, int maxMana) {
        PlayerStatus playerStatus = getPlayerStatus(player);
        playerStatus.setMaxHealth(maxHealth);
        playerStatus.setMaxMana(maxMana);
    }

    public void consumeVitality(Player player, int amount) {
        PlayerStatus playerStatus = getPlayerStatus(player);

        if (playerStatus.getCurrentVitality() >= amount) {
            playerStatus.consumeVitality(amount);
        } else {
            sendMessageAndPlaySound(player, "§cYou don't have enough vitality to do that!");
        }
    }

    public void consumeMana(Player player, int amount) {
        PlayerStatus playerStatus = getPlayerStatus(player);

        if (playerStatus.getCurrentMana() >= amount) {
            playerStatus.consumeMana(amount);
        } else {
            sendMessageAndPlaySound(player, "§cYou don't have enough mana to do that!");
        }
    }

    public boolean haveEnoughMana(Player player, int amount) {
        PlayerStatus playerStatus = getPlayerStatus(player);

        if (playerStatus.getCurrentMana() >= amount) {
            return true;
        } else {
            sendMessageAndPlaySound(player, "§cYou don't have enough mana to do that!");
            return false;
        }
    }

    public boolean haveEnoughVitality(Player player, int amount) {
        PlayerStatus playerStatus = getPlayerStatus(player);

        if (playerStatus.getCurrentVitality() >= amount) {
            return true;
        } else {
            sendMessageAndPlaySound(player, "§cYou don't have enough vitality to do that!");
            return false;
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        int currentHand = event.getPreviousSlot();
        ItemStack newHeldItem = player.getInventory().getItem(newSlot);
        PlayerStatus playerStatus = getPlayerStatus(player);
        ItemStack currentHeldItem = player.getInventory().getItem(currentHand);

        // Check if the held item is armor
        if (newHeldItem != null && isArmor(newHeldItem.getType())) {
            // Do nothing if it's armor
            return;
        }

        if (newHeldItem == null || newHeldItem.getType() == Material.AIR) {
            if (lastMaxManaValues.containsKey(player.getUniqueId())) {
                int lastMaxManaValue = lastMaxManaValues.get(player.getUniqueId());
                setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                lastMaxManaValues.remove(player.getUniqueId()); // Remove the entry for the player
            }
            return;
        }

        ItemConstructor itemConstructor = new ItemConstructor();
        int newMaxManaValue = itemConstructor.getMaxMana(newHeldItem);

        if (newMaxManaValue > 0) {
            // Player is holding an item with Max Mana lore
            setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() + newMaxManaValue);
            lastMaxManaValues.put(player.getUniqueId(), newMaxManaValue); // Update the lastMaxManaValue for the player
        } else if (newMaxManaValue == 0) {
            if (lastMaxManaValues.containsKey(player.getUniqueId())) {
                int lastMaxManaValue = lastMaxManaValues.get(player.getUniqueId());
                setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                lastMaxManaValues.remove(player.getUniqueId()); // Remove the entry for the player
            }
        }
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        int newSlot = player.getInventory().getHeldItemSlot();
        ItemStack newHeldItem = player.getInventory().getItem(newSlot);
        PlayerStatus playerStatus = getPlayerStatus(player);

        if (newHeldItem != null && isArmor(newHeldItem.getType())) {
            return;
        }

        if (event.getMainHandItem() != null && event.getOffHandItem().getType().equals(Material.AIR)) {
            handleMaxManaChange(player, playerStatus, event.getOffHandItem());
        } else if (event.getOffHandItem() != null && event.getMainHandItem().getType().equals(Material.AIR)) {
            Integer lastMaxManaValue = lastMaxManaValues.get(player.getUniqueId());
            if (lastMaxManaValue != null) {
                if (lastMaxManaValue != 0) {
                    setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                    lastMaxManaValues.remove(player.getUniqueId()); // Remove the entry for the player
                }
            }

        } else {
            // Player is swapping with non-affected items
            handleMaxManaChange(player, playerStatus, newHeldItem);
        }
        if (Objects.requireNonNull(event.getMainHandItem()).hasItemMeta()) {
            if (Objects.requireNonNull(event.getMainHandItem().getItemMeta()).hasLore()) {
                for (String itemLore : Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMainHandItem().getItemMeta()).getLore()))) {
                    if (itemLore.contains("Max Mana:")) {
                        String count = itemLore.replace("Max Mana:", "").replace("✤", "").replace("+", "").replace(" ", "");
                        count = ChatColor.stripColor(count);
                        int total = Integer.parseInt(count);

                        setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() + total);
                        lastMaxManaValues.put(player.getUniqueId(), total);
                    }

                }
            }
        }
    }

    private void handleMaxManaChange(Player player, PlayerStatus playerStatus, ItemStack newHeldItem) {
        if (newHeldItem == null || newHeldItem.getType() == Material.AIR) {
            if (lastMaxManaValues.containsKey(player.getUniqueId())) {
                int lastMaxManaValue = lastMaxManaValues.get(player.getUniqueId());
                setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                lastMaxManaValues.remove(player.getUniqueId()); // Remove the entry for the player
            }
            return;
        }

        ItemConstructor itemConstructor = new ItemConstructor();
        int newMaxManaValue = itemConstructor.getMaxMana(newHeldItem);

        if (newMaxManaValue == 0) {
            if (lastMaxManaValues.containsKey(player.getUniqueId())) {
                int lastMaxManaValue = lastMaxManaValues.get(player.getUniqueId());
                setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                lastMaxManaValues.remove(player.getUniqueId()); // Remove the entry for the player
            }
        } else {
            // Player is holding an item with Max Mana lore
            setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() + newMaxManaValue);
            lastMaxManaValues.put(player.getUniqueId(), newMaxManaValue); // Update the lastMaxManaValue for the player
        }
    }

    // Add a method to check if an item is an offhand item

    @EventHandler
    public void decreaseOnclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PlayerStatus playerStatus = getPlayerStatus(p);

        if (e.getCurrentItem() != null) {
            ItemStack item = e.getCurrentItem();

            if (p.getInventory().getHeldItemSlot() == e.getSlot()) {
                if (item.hasItemMeta()) {
                    if (Objects.requireNonNull(item.getItemMeta()).hasLore()) {
                        for (String itemLore : Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore())) {
                            if (itemLore.contains("Max Mana:")) {
                                String count = itemLore.replace("Max Mana:", "").replace("✤", "").replace("+", "").replace(" ", "");
                                count = ChatColor.stripColor(count);
                                int total = Integer.parseInt(count);

                                int lastMaxManaValue = total;
                                setPlayerMaxStatus(p, (int) p.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                                lastMaxManaValues.remove(p.getUniqueId()); // Remove the entry for the player

                            }
                        }
                    }
                }
            }
        }

        if (Objects.requireNonNull(e.getCursor()).hasItemMeta()) {
            if (p.getInventory().getHeldItemSlot() == e.getSlot()) {
                if (e.getCursor().hasItemMeta()) {
                    if (Objects.requireNonNull(e.getCursor().getItemMeta()).hasLore()) {
                        for (String itemLore : Objects.requireNonNull(Objects.requireNonNull(e.getCursor().getItemMeta()).getLore())) {
                            if (itemLore.contains("Max Mana:")) {
                                String count = itemLore.replace("Max Mana:", "").replace("✤", "").replace("+", "").replace(" ", "");
                                count = ChatColor.stripColor(count);
                                int total = Integer.parseInt(count);

                                int lastMaxManaValue = total;
                                setPlayerMaxStatus(p, (int) p.getMaxHealth(), playerStatus.getMaxMana() + lastMaxManaValue);
                                lastMaxManaValues.remove(p.getUniqueId()); // Remove the entry for the player

                                int maxMana = playerStatus.getMaxMana();
                                int mana = playerStatus.getCurrentMana();

                                if (mana > maxMana){
                                    playerStatus.setCurrentMana(playerStatus.getMaxMana());
                                }
                                if (maxMana < 100){
                                    playerStatus.setMaxMana(100);
                                }
                            }
                        }

                    }
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClick3(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.NUMBER_KEY) {
            int hotbarButton = event.getHotbarButton();

            // Check if the player is using a keyboard shortcut (e.g., 1-9 keys)
            if (hotbarButton >= 0 && hotbarButton <= 8) {
                ItemStack clickedItem = player.getInventory().getItem(hotbarButton);

                // Check if the clicked item is not null
                if (clickedItem != null) {
                    ItemConstructor itemConstructor = new ItemConstructor();
                    int lastMaxManaValue = itemConstructor.getMaxMana(clickedItem);
                    PlayerStatus playerStatus = getPlayerStatus(player);

                    setPlayerMaxStatus(player, (int) player.getMaxHealth(), playerStatus.getMaxMana() - lastMaxManaValue);
                    lastMaxManaValues.remove(player.getUniqueId()); // Remove the entry for the player
                }
            }
        }
    }


    // Method to check if an item is armor
    private boolean isArmor(Material material) {
        switch (material) {
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return true;
            default:
                return false;
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        getPlayerStatus(player);
    }
}
