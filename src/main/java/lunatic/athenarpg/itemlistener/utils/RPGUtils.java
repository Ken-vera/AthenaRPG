package lunatic.athenarpg.itemlistener.utils;

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

    public String getRPGName(Player player) {
        ItemStack itemInHand = player.getItemInHand();

        // Check if the item has item meta
        if (itemInHand != null && itemInHand.hasItemMeta()) {
            String displayName = itemInHand.getItemMeta().getDisplayName();

            // Extract the RPG name without color codes
            int startIndex = displayName.indexOf("[");
            if (startIndex != -1) {
                return ChatColor.stripColor(displayName.substring(0, startIndex));
            }
        }

        // Default name if no RPG name is found or item meta is null
        return "Unknown RPG Name";
    }


    public int getRPGLevel(Player player) {
        ItemStack itemInHand = player.getItemInHand();
        int starCount = 0;

        // Check if the item has item meta
        if (itemInHand != null && itemInHand.hasItemMeta()) {
            String displayName = itemInHand.getItemMeta().getDisplayName();

            if (displayName.contains("§e✦§7✦✦✦✦✦✦§8")) {
                starCount = 1;
            }
            if (displayName.contains("§e✦✦§7✦✦✦✦✦§8")) {
                starCount = 2;
            }
            if (displayName.contains("§e✦✦✦§7✦✦✦✦§8")) {
                starCount = 3;
            }
            if (displayName.contains("§e✦✦✦✦§7✦✦✦§8")) {
                starCount = 4;
            }
            if (displayName.contains("§e✦✦✦✦✦§7✦✦§8")) {
                starCount = 5;
            }
            if (displayName.contains("§e✦✦✦✦✦✦§7✦§8")) {
                starCount = 6;
            }
            if (displayName.contains("§e✦✦✦✦✦✦✦§8")) {
                starCount = 7;
            }
        }

        // Return the determined level based on the star count
        return starCount;
    }
}
