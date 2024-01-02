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

    private static final List<String> COMMON_REWARD = Arrays.asList(
            "COBBLESTONE_FRAGMENT",
            "SHARP_INGOT"
    );
    private static final List<String> RARE_REWARD = Arrays.asList(
            "HEART_POWER"
    );

    private static final List<String> EPIC_REWARD = Arrays.asList(
            "ENCHANTED_FARMING_BLOCK"
    );

    private static final List<String> LEGENDARY_REWARD = Arrays.asList(
            "ENCHANTED_FARMING_BLOCK"
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
                            || displayName.equals("§e§lLegendary Box §7(§aQuest§7)")) {
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
                            int randomAmount = random.nextInt(15) + 1; // 1 to 15 (inclusive)
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + getRandomElement(COMMON_REWARD) + " " + player.getName() + " " + randomAmount);
                            player.playSound(player, Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 100f, 0f);
                        } else if (displayName.equals("§9§lRare Box §7(§aQuest§7)")) {
                            int randomAmount = random.nextInt(10) + 1; // 1 to 10 (inclusive)
                            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + getRandomElement(RARE_REWARD) + " " + player.getName() + " " + randomAmount);
                            Bukkit.broadcastMessage("§e" + player.getName() + " §fbaru saja membuka §9§lRare Box §7(§aQuest§7)§f!");
                            playSoundToAllPlayer(Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 100f, 0f);
                        } else if (displayName.equals("§5§lEpic Box §7(§aQuest§7)")) {
                            int randomAmount = random.nextInt(5) + 1; // 1 to 5 (inclusive)
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

    private static String getRandomElement(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}
