package lunatic.athenarpg.itemlistener.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RPGUtils {

    private static final Map<ChatColor, Integer> colorToLevelMap = new HashMap<>();

    static {
        colorToLevelMap.put(ChatColor.GOLD, 1);
        colorToLevelMap.put(ChatColor.YELLOW, 2);
        // Add more mappings as needed
    }

    public String getRPGName(ItemStack item) {
        // Check if the item has item meta
        if (item != null && item.hasItemMeta()) {
            String displayName = item.getItemMeta().getDisplayName();

            // Extract the RPG name without color codes
            int startIndex = displayName.indexOf("[");
            if (startIndex != -1) {
                String extractedName = ChatColor.stripColor(displayName.substring(0, startIndex)).trim();
                return extractedName.isEmpty() ? "Unknown RPG Name" : extractedName;
            }
        }

        // Default name if no RPG name is found or item meta is null
        return "Unknown RPG Name";
    }


    public int getRPGLevel(ItemStack item) {
        int starCount = 0;

        // Check if the item has item meta
        if (item != null && item.hasItemMeta()) {
            String displayName = item.getItemMeta().getDisplayName();

            if (displayName.contains("§e✦")) {
                starCount = 1;
            }
            if (displayName.contains("§e✦✦")) {
                starCount = 2;
            }
            if (displayName.contains("§e✦✦✦")) {
                starCount = 3;
            }
            if (displayName.contains("§e✦✦✦✦")) {
                starCount = 4;
            }
            if (displayName.contains("§e✦✦✦✦✦")) {
                starCount = 5;
            }
            if (displayName.contains("§e✦✦✦✦✦✦")) {
                starCount = 6;
            }
            if (displayName.contains("§e✦✦✦✦✦✦✦")) {
                starCount = 7;
            }
            // Add more starCount conditions as needed
        }

        // Return the determined level based on the star count
        return starCount;
    }

    public String getHelmetName(ItemStack helmet) {
        return getRPGName(helmet);
    }

    public int getHelmetLevel(ItemStack helmet) {
        return getRPGLevel(helmet);
    }

    public String getChestplateName(ItemStack chestplate) {
        return getRPGName(chestplate);
    }

    public int getChestplateLevel(ItemStack chestplate) {
        return getRPGLevel(chestplate);
    }

    public String getLeggingsName(ItemStack leggings) {
        return getRPGName(leggings);
    }

    public int getLeggingsLevel(ItemStack leggings) {
        return getRPGLevel(leggings);
    }

    public String getBootsName(ItemStack boots) {
        return getRPGName(boots);
    }

    public int getBootsLevel(ItemStack boots) {
        return getRPGLevel(boots);
    }
    public String getRPGNameInHand(Player player) {
        ItemStack itemInHand = player.getItemInHand();
        return getRPGName(itemInHand);
    }
    public String getRPGNameInOffHand(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInOffHand();
        return getRPGName(itemInHand);
    }
    public int getRPGLevelInHand(Player player) {
        ItemStack itemInHand = player.getItemInHand();
        return getRPGLevel(itemInHand);
    }

    public String getHelmetRPGName(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        return getHelmetName(helmet);
    }

    public String getChestplateRPGName(Player player){
        ItemStack chestplate = player.getInventory().getChestplate();
        return getChestplateName(chestplate);
    }
    public String getLeggingsRPGName(Player player){
        ItemStack leggings = player.getInventory().getLeggings();
        return getLeggingsName(leggings);
    }
    public String getBootsRPGName(Player player){
        ItemStack boots = player.getInventory().getBoots();
        return  getBootsName(boots);
    }
    public int getRPGLevelInOffHand(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInOffHand();
        return getRPGLevel(itemInHand);
    }

    public int getRPGLevelInHelmet(Player player) {
        ItemStack itemInHead = player.getInventory().getHelmet();
        return getRPGLevel(itemInHead);
    }

    public int getRPGLevelInChestplate(Player player) {
        ItemStack itemInChestplate = player.getInventory().getChestplate();
        return getRPGLevel(itemInChestplate);
    }

    public int getRPGLevelInLeggings(Player player) {
        ItemStack itemInLeggings = player.getInventory().getLeggings();
        return getRPGLevel(itemInLeggings);
    }

    public int getRPGLevelInBoots(Player player) {
        ItemStack itemInBoots = player.getInventory().getBoots();
        return getRPGLevel(itemInBoots);
    }
    public boolean isFullSet(Player player, String name) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        if (helmet == null || chestplate == null || leggings == null || boots == null) {
            return false;
        }

        String helmetName = getHelmetName(helmet);
        String chestplateName = getChestplateName(chestplate);
        String leggingsName = getLeggingsName(leggings);
        String bootsName = getBootsName(boots);

        if (helmetName.isEmpty() || chestplateName.isEmpty() || leggingsName.isEmpty() || bootsName.isEmpty()) {
            return false;
        }

        if (!helmetName.contains(name) && !chestplateName.contains(name) && !leggingsName.contains(name) && !bootsName.contains(name)){
            return false;
        }

        return true;
    }
}
