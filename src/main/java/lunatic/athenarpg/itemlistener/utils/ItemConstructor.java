package lunatic.athenarpg.itemlistener.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemConstructor {

    public int getVitalityCost(Player player, ItemStack itemStack) {
        return getCostFromLore(itemStack, "Vitality");
    }

    public int getManaCost(Player player, ItemStack itemStack) {
        return getCostFromLore(itemStack, "Mana");
    }

    public int getHealthCost(Player player, ItemStack itemStack) {
        return getCostFromLore(itemStack, "Health");
    }

    public int getCostFromLore(ItemStack itemStack, String costType) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                if (ChatColor.stripColor(line).contains(costType + " Cost:")) {
                    String strippedLine = ChatColor.stripColor(line);

                    strippedLine = strippedLine.replace("%", "");
                    strippedLine = strippedLine.replace("❤", "");
                    strippedLine = strippedLine.replace("♨", "");
                    strippedLine = strippedLine.replace("✤", "");

                    // Use regex to extract the cost value
                    String regex = "([0-9]+)"; // Matches the percentage
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(strippedLine);
                    if (matcher.find()) {
                        String costString = matcher.group(1); // Use group(1) to get the captured value
                        return Integer.parseInt(costString);
                    }
                }
            }
        }
        return 0; // Return 0 if the cost is not found or cannot be parsed
    }

    public int getMaxMana(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                if (ChatColor.stripColor(line).contains("Max Mana:")) {
                    String strippedLine = ChatColor.stripColor(line);

                    // Use regex to extract the max mana value
                    String regex = "([+-]?[0-9]+)"; // Matches the integer value (positive or negative)
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(strippedLine);
                    if (matcher.find()) {
                        String maxManaString = matcher.group(1); // Use group(1) to get the captured value
                        return Integer.parseInt(maxManaString);
                    }
                }
            }
        }
        return 0; // Return 0 if the max mana value is not found or cannot be parsed
    }

    public int getCooldown(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                if (ChatColor.stripColor(line).contains("Cooldown:")) {
                    String strippedLine = ChatColor.stripColor(line);

                    // Use regex to extract the cooldown value
                    String regex = "([0-9]+)s"; // Matches the integer value followed by 's' for seconds
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(strippedLine);
                    if (matcher.find()) {
                        String cooldownString = matcher.group(1); // Use group(1) to get the captured value
                        return Integer.parseInt(cooldownString);
                    }
                }
            }
        }
        return 0; // Return 0 if the cooldown value is not found or cannot be parsed
    }

    public int getMainHandManaCost(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        return mainHandItem != null ? getManaCost(player, mainHandItem) : 0;
    }

    public int getMainHandVitalityCost(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        return mainHandItem != null ? getVitalityCost(player, mainHandItem) : 0;
    }

    public int getOffhandManaCost(Player player) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        return offhandItem != null ? getManaCost(player, offhandItem) : 0;
    }

    public int getOffhandVitalityCost(Player player) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        return offhandItem != null ? getVitalityCost(player, offhandItem) : 0;
    }

    public int getHelmetManaCost(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        return helmet != null ? getManaCost(player, helmet) : 0;
    }

    public int getHelmetVitalityCost(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        return helmet != null ? getVitalityCost(player, helmet) : 0;
    }

    public int getChestplateManaCost(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        return chestplate != null ? getManaCost(player, chestplate) : 0;
    }

    public int getChestplateVitalityCost(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        return chestplate != null ? getVitalityCost(player, chestplate) : 0;
    }

    public int getLeggingsManaCost(Player player) {
        ItemStack leggings = player.getInventory().getLeggings();
        return leggings != null ? getManaCost(player, leggings) : 0;
    }

    public int getLeggingsVitalityCost(Player player) {
        ItemStack leggings = player.getInventory().getLeggings();
        return leggings != null ? getVitalityCost(player, leggings) : 0;
    }

    public int getBootsManaCost(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null ? getManaCost(player, boots) : 0;
    }

    public int getBootsVitalityCost(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null ? getVitalityCost(player, boots) : 0;
    }

    public int getFullSetManaCost(Player player){
        int helmet = getHelmetManaCost(player);
        int chestplate = getChestplateManaCost(player);
        int leggings = getLeggingsManaCost(player);
        int boots = getBootsManaCost(player);

        return helmet + chestplate + leggings + boots;
    }
    public int getFullSetVitalityCost(Player player){
        int helmet = getHelmetVitalityCost(player);
        int chestplate = getChestplateVitalityCost(player);
        int leggings = getLeggingsVitalityCost(player);
        int boots = getBootsVitalityCost(player);

        return helmet + chestplate + leggings + boots;
    }

    public int getMainHandCooldown(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        return mainHandItem != null ? getCooldown(mainHandItem) : 0;
    }

    public int getOffhandCooldown(Player player) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        return offhandItem != null ? getCooldown(offhandItem) : 0;
    }

    public int getHelmetCooldown(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        return helmet != null ? getCooldown(helmet) : 0;
    }

    public int getChestplateCooldown(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        return chestplate != null ? getCooldown(chestplate) : 0;
    }

    public int getLeggingsCooldown(Player player) {
        ItemStack leggings = player.getInventory().getLeggings();
        return leggings != null ? getCooldown(leggings) : 0;
    }

    public int getBootsCooldown(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null ? getCooldown(boots) : 0;
    }
    public void setRPGNameInHand(Player player, String itemName) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(itemName);
            itemInHand.setItemMeta(itemMeta);
        }
    }

}