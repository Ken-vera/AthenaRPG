package lunatic.athenarpg.itemlistener.dungeon;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EssenceEater implements Listener {
    private Main plugin;
    private RPGUtils utils;
    private ItemConstructor itemConstructor;
    private CooldownManager cooldownManager;
    private StatusListener statusListener;

    public EssenceEater(Main plugin) {
        this.plugin = plugin;
        this.utils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager();
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void essenceSwapper(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the item has lore and matches the specified format
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            String rpgName = utils.getRPGNameInHand(player);
            if (rpgName.equals("Essence Eater")) {
                int cooldownTime = 0;
                cooldownTime = itemConstructor.getCooldown(item);
                int rpgLevel = utils.getRPGLevelInHand(player);
                ItemMeta meta = item.getItemMeta();
                if (!cooldownManager.isOnCooldown(player.getName(), rpgName)) {
                    String currentLocation = getCurrentEssenceFromLore(meta.getLore());
                    cooldownTime = itemConstructor.getCooldown(item);

                    if (currentLocation != null) {
                        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            String newLocation = getGetNextEssence(currentLocation);
                            updateLore(meta, newLocation);
                            item.setItemMeta(meta);
                            player.sendMessage(ChatColor.GREEN + "Essence changed to: §c" + newLocation);
                            player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 20f, 1f);
                        } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            int vitalityCost = itemConstructor.getMainHandVitalityCost(player);
                            if (statusListener.haveEnoughVitality(player, vitalityCost)) {
                                statusListener.consumeVitality(player, vitalityCost);
                                handleRightClickAction(player, meta, currentLocation, rpgLevel);
                            }
                        }
                    }
                } else {
                    cooldownManager.sendCooldownMessage(player, rpgName);
                }
            }
        }
    }
    private void handleRightClickAction(Player player, ItemMeta meta, String currentEssence, int rpgLevel) {
        if (currentEssence != null) {
            if (currentEssence.equals("Shadow Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    if (player.getHealth() != player.getMaxHealth()){
                        player.setHealth(player.getHealth() + 1);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            } else if (currentEssence.equals("Celestial Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * rpgLevel, rpgLevel));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            } else if (currentEssence.equals("Astral Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * rpgLevel, rpgLevel));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            } else if (currentEssence.equals("Bloodmoon Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * rpgLevel, rpgLevel));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            } else if (currentEssence.equals("Crystaline Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * rpgLevel, rpgLevel));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            } else if (currentEssence.equals("Nebula Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * rpgLevel, rpgLevel));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            } else if (currentEssence.equals("Emberwind Essence")) {
                ItemStack itemToRemove = new ItemStack(Material.AIR); // Default to air to prevent errors

                // Check if the item has the same name and color as the essence
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(currentEssence)) {
                        itemToRemove = item;
                        break;
                    }
                }

                // Check if an item was found in the player's inventory
                if (itemToRemove.getType() != Material.AIR) {
                    // Remove one item from the player's inventory
                    int amount = itemToRemove.getAmount();
                    if (amount > 1) {
                        itemToRemove.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(itemToRemove);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * rpgLevel, rpgLevel));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * rpgLevel, rpgLevel));
                    player.playSound(player, Sound.ENTITY_PLAYER_BURP, 10f, 0f);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have the required item in your inventory.");
                }
            }
        }
    }
    private String getCurrentEssenceFromLore(java.util.List<String> lore) {
        for (String line : lore) {
            if (line.contains("Current Essence:")) {
                return ChatColor.stripColor(line.replace("Current Essence: ", ""));
            }
        }
        return null;
    }

    private String getGetNextEssence(String currentEssence) {
        // You can customize this logic based on your requirements
        String[] essence = {"Shadow Essence", "Celestial Essence", "Astral Essence", "Bloodmoon Essence"
                , "Crystaline Essence", "Nebula Essence", "Emberwind Essence"};
        for (int i = 0; i < essence.length; i++) {
            if (essence[i].equals(currentEssence)) {
                return (i == essence.length - 1) ? essence[0] : essence[i + 1];
            }
        }
        return currentEssence; // Default to the current location if not found in the list
    }

    private void updateLore(ItemMeta meta, String newEssence) {
        java.util.List<String> lore = meta.getLore();
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("Current Essence:")) {
                lore.set(i, ChatColor.GRAY + "Current Essence: " + ChatColor.RED + newEssence);
                if (newEssence.equals("Shadow Essence")){
                    lore.set(4, "§8(§a+§8) §a1 Health");
                    lore.set(5, "§8(§c-§8) §cSlowness");
                }
                else if (newEssence.equals("Celestial Essence")){
                    lore.set(4, "§8(§a+§8) §aRegeneration");
                    lore.set(5, "§8(§c-§8) §cGlowing");
                }
                else if (newEssence.equals("Astral Essence")){
                    lore.set(4, "§8(§a+§8) §aInvisibility");
                    lore.set(5, "§8(§c-§8) §cBlindness");
                }
                else if (newEssence.equals("Bloodmoon Essence")){
                    lore.set(4, "§8(§a+§8) §aStrength");
                    lore.set(5, "§8(§c-§8) §cPoison");
                }
                else if (newEssence.equals("Crystaline Essence")){
                    lore.set(4, "§8(§a+§8) §aNight Vision");
                    lore.set(5, "§8(§c-§8) §cSlowness");
                }
                else if (newEssence.equals("Nebula Essence")){
                    lore.set(4, "§8(§a+§8) §aSaturation");
                    lore.set(5, "§8(§c-§8) §cSlowness");
                }
                else if (newEssence.equals("Emberwind Essence")){
                    lore.set(4, "§8(§a+§8) §aSpeed");
                    lore.set(5, "§8(§c-§8) §cBlindness");
                }
            }
        }
        meta.setLore(lore);
    }
}
