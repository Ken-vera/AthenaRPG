package lunatic.athenarpg.reward;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoxOpenListener implements Listener {
    private Main plugin;

    public BoxOpenListener(Main plugin) {
        this.plugin = plugin;
    }

    public List<String> COMMON_REWARD = Arrays.asList(
            "COBBLESTONE_FRAGMENT",
            "SHARP_INGOT",
            "TASER_BATTERY",
            "OBSIDIAN_CHUNK"
    );
    public List<String> RARE_REWARD = Arrays.asList(
            "HEART_POWER",
            "POSEIDON_SCRAP",
            "ANCESTRAL_PAPER",
            "VINDICATOR_EYES"
    );

    public List<String> EPIC_REWARD = Arrays.asList(
            "ENCHANTED_FARMING_BLOCK",
            "UNIDENTIFIED_14"
    );

    public List<String> LEGENDARY_REWARD = Arrays.asList(
            "SHINY_FARMING_BLOCK",
            "SPEEDY_POWDER",
            "EMPOWERED_WITHER_SKULL",
            "DRAGOPTICS_LOST_EYES",
            "RED_CROWN"
    );

    public List<String> ESSENCE_REWARD = Arrays.asList(
            "SHADOW_ESSENCE",
            "CELESTIAL_ESSENCE",
            "ASTRAL_ESSENCE",
            "BLOODMOON_ESSENCE",
            "CRYSTALINE_ESSENCE",
            "NEBULA_ESSENCE",
            "EMBERWIND_ESSENCE"
    );

    public List<String> DUNGEON_KEY = Arrays.asList(
            "dungeon"
    );

    private static final Random random = new Random();

    @EventHandler
    public void onBoxInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack item = event.getItem();
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    String displayName = item.getItemMeta().getDisplayName();
                    if (displayName.equals("§a§lCommon Box §7(§aQuest§7)")
                            || displayName.equals("§9§lRare Box §7(§aQuest§7)")
                            || displayName.equals("§5§lEpic Box §7(§aQuest§7)")
                            || displayName.equals("§e§lLegendary Box §7(§aQuest§7)")
                            || displayName.equals("§d§lDungeon Coupon")
                            || displayName.equals("§b§lEssence Box")) {
                        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
                        Player player = event.getPlayer();
                        event.setCancelled(true);

                        // Subtract one from the quantity of the held item
                        if (item.getAmount() > 1) {
                            item.setAmount(item.getAmount() - 1);
                            player.updateInventory();
                        } else {
                            // Player was holding only one item, remove it from the hand
                            player.getInventory().setItemInMainHand(null);
                        }

                        // Continue with the reward logic
                        if (displayName.equals("§a§lCommon Box §7(§aQuest§7)")) {
                            int randomAmount = random.nextInt(10) + 1; // 1 to 10 (inclusive)
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + getRandomElement(COMMON_REWARD) + " " + player.getName() + " " + randomAmount);
                            player.playSound(player, Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 100f, 0f);
                        } else if (displayName.equals("§9§lRare Box §7(§aQuest§7)")) {
                            int randomAmount = random.nextInt(5) + 1; // 1 to 5 (inclusive)
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + getRandomElement(RARE_REWARD) + " " + player.getName() + " " + randomAmount);
                            Bukkit.broadcastMessage("§e" + player.getName() + " §fbaru saja membuka §9§lRare Box §7(§aQuest§7)§f!");
                            playSoundToAllPlayer(Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 100f, 0f);
                        } else if (displayName.equals("§5§lEpic Box §7(§aQuest§7)")) {
                            int randomAmount = random.nextInt(3) + 1; // 1 to 3 (inclusive)
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + getRandomElement(EPIC_REWARD) + " " + player.getName() + " " + randomAmount);
                            Bukkit.broadcastMessage("§e" + player.getName() + " §fbaru saja membuka §5§lEpic Box §7(§aQuest§7)§f!");
                            playSoundToAllPlayer(Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 100f, 0f);
                        } else if (displayName.equals("§e§lLegendary Box §7(§aQuest§7)")) {
                            int randomAmount = random.nextInt(1) + 1; // 1 (inclusive)
                            String legendaryItem = getRandomElement(LEGENDARY_REWARD);
                            String formattedItemName = formatItemName(legendaryItem);
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + legendaryItem + " " + player.getName() + " " + randomAmount);
                            Bukkit.broadcastMessage("§e" + player.getName() + " §fbaru saja membuka §e§lLegendary Box §7(§aQuest§7)§f dan mendapatkan §e" + formattedItemName + "§f!");
                            playSoundToAllPlayer(Sound.ENTITY_ENDER_DRAGON_GROWL, 100f, 0f);
                        } else if (displayName.equals("§d§lDungeon Coupon")) {
                            int randomAmount = random.nextInt(2) + 1; // 1 (inclusive)
                            String itemDrop = getRandomElement(DUNGEON_KEY);
                            Bukkit.dispatchCommand(consoleCommandSender, "excellentcrates key give " + player.getName() + " " + itemDrop + " " + randomAmount);
                            player.playSound(player, Sound.ENTITY_BLAZE_BURN, 10f, 1f);
                        } else if (displayName.equals("§b§lEssence Box")) {
                            int randomAmount = random.nextInt(16) + 8;
                            String itemDrop = getRandomElement(ESSENCE_REWARD);
                            String formattedItemName = formatItemName(itemDrop);
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give ESSENCE " + itemDrop + " " + player.getName() + " " + randomAmount);
                            Bukkit.broadcastMessage("§e" + player.getName() + " §fbaru saja membuka §b§lEssence Box §fdan mendapatkan §e" + formattedItemName + "§f!");
                            playSoundToAllPlayer(Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 100f, 0f);
                        }
                    }
                }
            }
        }
    }
    private static String formatItemName(String itemName) {
        String[] words = itemName.split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }
        return String.join(" ", words);
    }

    public void playSoundToAllPlayer(Sound sound, float volume, float pitch){
        for (Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player, sound, volume, pitch);
        }
    }

    public String getRandomElement(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}
