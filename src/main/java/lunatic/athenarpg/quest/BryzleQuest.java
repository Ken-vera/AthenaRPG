package lunatic.athenarpg.quest;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.dialogue.NPCDialogue;
import lunatic.athenarpg.reward.RewardManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BryzleQuest implements Listener {

    private Main plugin;

    RewardManager rewardManager;

    public BryzleQuest(Main plugin){
        this.plugin = plugin;
        rewardManager = new RewardManager(plugin);
    }

    public void setupQuests() {
        // Add quests to the availableQuests list

        // EASY QUEST
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] White Ingot?", "Collect 15 iron ingots", createMaterialMap(Material.IRON_INGOT, 15)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Teleport and Diamond!", "Collect 6 ender pearls and 5 diamonds",
                createMaterialMap(Material.ENDER_PEARL, 6, Material.DIAMOND, 5)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Creeper Repellent", "Defeat 15 creepers", createMaterialMap(Material.GUNPOWDER, 15)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Lumberjack", "Collect 64 oak logs", createMaterialMap(Material.OAK_LOG, 64)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Mushroom Forager", "Gather 20 red mushrooms and 20 brown mushrooms",
                createMaterialMap(Material.RED_MUSHROOM, 20, Material.BROWN_MUSHROOM, 20)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Novice Farmer", "Harvest 50 wheat", createMaterialMap(Material.WHEAT, 50)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Chicken Herder", "Collect 20 eggs from chickens", createMaterialMap(Material.EGG, 20)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] Green Thumb", "Plant and grow 30 saplings", createMaterialMap(Material.OAK_SAPLING, 30)));
        plugin.availableQuests.add(new Main.Quest("[EASY] [Bryzle] [Clairmmont] Tree Puncher", "Collect 32 Oak Log", createMaterialMap(Material.OAK_LOG, 32)));

        // MEDIUM QUEST
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Lost Miner", "Collect 20 Diamond, 20 Redstone, 20 Gold Ingot",
                createMaterialMap(Material.DIAMOND, 20, Material.REDSTONE, 20, Material.GOLD_INGOT, 20)));
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Fisherman's Delight", "Catch 30 different types of fish", createMaterialMap(
                Material.COD, 5, Material.SALMON, 5, Material.TROPICAL_FISH, 5,
                Material.PUFFERFISH, 5, Material.TROPICAL_FISH, 5, Material.INK_SAC, 5)
        ));
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Potion Master", "Brew 15 different potions", createMaterialMap(
                Material.BREWING_STAND, 1, Material.BLAZE_POWDER, 10, Material.GHAST_TEAR, 5,
                Material.GLOWSTONE_DUST, 10, Material.REDSTONE, 10, Material.MAGMA_CREAM, 5,
                Material.SPIDER_EYE, 10, Material.SUGAR, 10, Material.FERMENTED_SPIDER_EYE, 5,
                Material.GLISTERING_MELON_SLICE, 5, Material.GLISTERING_MELON_SLICE, 5, Material.GOLDEN_CARROT, 5,
                Material.POTION, 15)));

        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Beekeeper", "Harvest honey from 10 beehives", createMaterialMap(Material.HONEY_BOTTLE, 10)));
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Nether Explorer", "Collect 30 blaze rods and 20 ghast tears",
                createMaterialMap(Material.BLAZE_ROD, 30, Material.GHAST_TEAR, 20)));
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Artisan Baker", "Craft 50 loaves of bread", createMaterialMap(Material.BREAD, 50)));
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] Guardian Slayer", "Defeat 10 elder guardians", createMaterialMap(Material.SEA_LANTERN, 10)));
        plugin.availableQuests.add(new Main.Quest("[MEDIUM] [Bryzle] [Clairmmont] Mine Mine Mine!", "Collect 10 Iron Ingot, 15 Redstone, 20 Gold Ingot",
                createMaterialMap(Material.IRON_INGOT, 10, Material.REDSTONE, 15, Material.GOLD_INGOT, 20)));

        // HARD QUEST
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Enchanting Master", "Enchant 10 different items with various enchantments",
                createMaterialMap(Material.ENCHANTING_TABLE, 1, Material.DIAMOND_SWORD, 1, Material.DIAMOND_PICKAXE, 1,
                        Material.DIAMOND_CHESTPLATE, 1, Material.BOW, 1, Material.BOOKSHELF, 5,
                        Material.EXPERIENCE_BOTTLE, 10, Material.LAPIS_LAZULI, 64, Material.OBSIDIAN, 10)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Strong Man", "Collect 16 Golden Apple, 16 Golden Carrot, 2 Netherite Ingot",
                createMaterialMap(Material.GOLDEN_APPLE, 16, Material.GOLDEN_CARROT, 16, Material.NETHERITE_INGOT, 2)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Master Architect", "Build a grand structure with at least 5 unique rooms",
                createMaterialMap(Material.STONE_BRICKS, 500, Material.OAK_PLANKS, 300, Material.GLASS_PANE, 200,
                        Material.BOOKSHELF, 50, Material.DIAMOND_BLOCK, 10)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Wither Hunter", "Defeat 5 withers", createMaterialMap(Material.NETHER_STAR, 5)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Ice Sculptor", "Collect 100 packed ice blocks", createMaterialMap(Material.PACKED_ICE, 100)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Thunderstruck", "Collect 5 tridents", createMaterialMap(Material.TRIDENT, 5)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] Endless Night", "Survive 7 nights without sleeping", createMaterialMap(Material.CLOCK, 1)));
        plugin.availableQuests.add(new Main.Quest("[HARD] [Bryzle] [Clairmmont] Exploring The Sea", "Collect 32 Prismarine Shard, 32 Prismarine Crystals,",
                createMaterialMap(Material.PRISMARINE_SHARD, 32, Material.PRISMARINE_CRYSTALS, 32)));
        // EXPERT QUEST
        plugin.availableQuests.add(new Main.Quest("[EXPERT] [Bryzle] Water Hunter", "Collect 10 Heart of The Sea",
                createMaterialMap(Material.HEART_OF_THE_SEA, 10)));
        plugin.availableQuests.add(new Main.Quest("[EXPERT] [Bryzle] Redstone Engineer", "Create a fully automated redstone contraption",
                createMaterialMap(Material.REDSTONE_BLOCK, 20, Material.DROPPER, 10, Material.HOPPER, 10,
                        Material.PISTON, 10, Material.OBSERVER, 5, Material.REPEATER, 10)));
        plugin.availableQuests.add(new Main.Quest("[EXPERT] [Bryzle] Slime Scientist", "Collect 350 slimeballs", createMaterialMap(Material.SLIME_BALL, 350)));
        plugin.availableQuests.add(new Main.Quest("[EXPERT] [Bryzle] Potion of Flight", "Craft a potion of slow falling and elytra",
                createMaterialMap(Material.POTION, 1, Material.PHANTOM_MEMBRANE, 5, Material.ELYTRA, 1)));
        plugin.availableQuests.add(new Main.Quest("[EXPERT] [Bryzle] Guardian of the Sea", "Create an underwater base",
                createMaterialMap(Material.PRISMARINE, 200, Material.SEA_LANTERN, 50, Material.CONDUIT, 5)));
        plugin.availableQuests.add(new Main.Quest("[EXPERT] [Bryzle] [Clairmmont] The Dark Descent", "Collect 15 Sculk Catalyst",
                createMaterialMap(Material.SCULK_CATALYST, 15)));
    }

    private Map<Material, Integer> createMaterialMap(Object... materialAndAmount) {
        if (materialAndAmount.length % 2 != 0) {
            throw new IllegalArgumentException("Arguments must be in pairs of Material and Integer.");
        }

        Map<Material, Integer> materialMap = new HashMap<>();
        for (int i = 0; i < materialAndAmount.length; i += 2) {
            if (!(materialAndAmount[i] instanceof Material) || !(materialAndAmount[i + 1] instanceof Integer)) {
                throw new IllegalArgumentException("Invalid argument types.");
            }

            Material material = (Material) materialAndAmount[i];
            int amount = (Integer) materialAndAmount[i + 1];
            materialMap.put(material, amount);
        }

        return materialMap;
    }

    public void startQuest(Player player) {
        // Get a random available quest
        Main.Quest quest = getRandomQuest();
        if (quest != null) {
            plugin.activeQuests.add(new Main.ActiveQuest(player, quest));

            // Get random NPC dialogue
            String greeting = NPCDialogue.getGreeting();
            String questIntro = NPCDialogue.getQuestIntro(quest.getName());
            String itemNeeded = NPCDialogue.getQuestItemNeeded(quest.getRequiredMaterials());

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            // Simulate showing quest dialog
            scheduler.runTaskLater(plugin, () -> player.sendMessage("\n§a§lBryzle §7» §f" + greeting + "\n§a"), 0L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 0L);

            // Delayed task for the second message (2 seconds delay)
            scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §f" + questIntro + "\n§a"), 60L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 60L);

            // Delayed task for the third message (4 seconds delay)
            scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §f" + itemNeeded + "\n§a"), 100L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 100L);

            if (quest.getName().contains("[EASY]")){
                scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fAku memiliki hadiah berupa §a§lCommon Chest §funtukmu.\n§a"), 140L);
                scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 140L);
            }
            else if (quest.getName().contains("[MEDIUM]")){
                scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fAku memiliki hadiah berupa §9§lRare Chest §funtukmu.\n§a"), 140L);
                scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 140L);
            }
            else if (quest.getName().contains("[HARD]")){
                scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fAku memiliki hadiah berupa §5§lEpic Chest §funtukmu.\n§a"), 140L);
                scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 140L);
            }
            else if (quest.getName().contains("[EXPERT]")){
                scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fAku memiliki hadiah berupa §e§lLegendary Chest §funtukmu.\n§a"), 140L);
                scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 140L);
            }
        }
    }

    private Main.Quest getRandomQuest() {
        if (!plugin.availableQuests.isEmpty()) {
            Random random = new Random();
            return plugin.availableQuests.get(random.nextInt(plugin.availableQuests.size()));
        }
        return null;
    }


    public void completeQuest(Player player, Main.ActiveQuest activeQuest) {
        Map<Material, Integer> requiredMaterials = activeQuest.getQuest().getRequiredMaterials();

        // Check if items have custom display names
        if (!areItemsVanilla(player, requiredMaterials)) {
            player.sendMessage("§cItem yang kamu serahkan memiliki nama kustom. Gunakan item vanilla untuk menyelesaikan quest!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100f, 0f);
            return;
        }

        // Remove required items from the player's inventory
        for (Map.Entry<Material, Integer> entry : requiredMaterials.entrySet()) {
            Material material = entry.getKey();
            int requiredAmount = entry.getValue();
            int remainingAmount = requiredAmount - activeQuest.getProgress().getOrDefault(material, 0);

            removeItemsFromInventory(player, material, remainingAmount);
        }
        // Simulate quest completion rewards or messages
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        String thankYou = NPCDialogue.getQuestCompletedMessage();
        scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §f" + thankYou + "\n§a"), 0L);
        scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 0L);
        if (activeQuest.getQuest().getName().contains("[EASY]")){
            scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fSeperti janjiku sebelumnya, aku akan memberikan §a§lCommon Chest §fkepadamu! Terimalah ini!\n§a"), 50L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 50L);
        }
        else if (activeQuest.getQuest().getName().contains("[MEDIUM]")) {
            scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fSeperti janjiku sebelumnya, aku akan memberikan §9§lRare Chest §fkepadamu! Terimalah ini!\n§a"), 50L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 50L);
        }
        else if (activeQuest.getQuest().getName().contains("[HARD]")) {
            scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fSeperti janjiku sebelumnya, aku akan memberikan §5§lEpic Chest §fkepadamu! Terimalah ini!\n§a"), 50L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 50L);
        }
        else if (activeQuest.getQuest().getName().contains("[EXPERT]")) {
            scheduler.runTaskLater(plugin, () -> player.sendMessage("§a§lBryzle §7» §fSeperti janjiku sebelumnya, aku akan memberikan §e§lLegendary Chest §fkepadamu! Terimalah ini!\n§a"), 50L);
            scheduler.runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 100f, 0f), 50L);
        }
        if (activeQuest.getQuest().getName().contains("[EASY]")){
            rewardManager.giveRPGBoxReward(player, "COMMON_BOX");
        }
        else if (activeQuest.getQuest().getName().contains("[MEDIUM]")){
            rewardManager.giveRPGBoxReward(player, "RARE_BOX");
        }
        else if (activeQuest.getQuest().getName().contains("[HARD]")){
            rewardManager.giveRPGBoxReward(player, "EPIC_BOX");
        }
        else if (activeQuest.getQuest().getName().contains("[EXPERT]")){
            rewardManager.giveRPGBoxReward(player, "LEGENDARY_BOX");
        }
        plugin.activeQuests.remove(activeQuest);
    }
    private boolean areItemsVanilla(Player player, Map<Material, Integer> requiredMaterials) {
        for (Map.Entry<Material, Integer> entry : requiredMaterials.entrySet()) {
            Material material = entry.getKey();
            int requiredAmount = entry.getValue();

            int count = countVanillaItemsInInventory(player, material);

            if (count < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    private int countVanillaItemsInInventory(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material && !hasCustomDisplayName(item)) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private boolean hasCustomDisplayName(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName();
    }

    private void removeItemsFromInventory(Player player, Material material, int amount) {
        int remainingAmount = amount;

        for (ItemStack item : player.getInventory().getContents()) {
            if (remainingAmount <= 0) {
                break;
            }

            if (item != null && item.getType() == material) {
                int itemAmount = item.getAmount();
                if (itemAmount <= remainingAmount) {
                    // Remove the entire stack
                    player.getInventory().remove(item);
                    remainingAmount -= itemAmount;
                } else {
                    // Partially remove the stack
                    item.setAmount(itemAmount - remainingAmount);
                    remainingAmount = 0;
                }
            }
        }
    }

    public boolean playerHasRequiredItems(Player player, Main.ActiveQuest activeQuest) {
        Map<Material, Integer> requiredMaterials = activeQuest.getQuest().getRequiredMaterials();
        Map<Material, Integer> playerInventory = getPlayerInventoryContents(player);

        boolean hasAllItems = true;
        StringBuilder missingItemsMessage = new StringBuilder("\n§cKamu masih kekurangan item yang dibutuhkan : ");

        for (Map.Entry<Material, Integer> entry : requiredMaterials.entrySet()) {
            Material material = entry.getKey();
            int requiredAmount = entry.getValue();
            int playerAmount = playerInventory.getOrDefault(material, 0);

            if (playerAmount < requiredAmount) {
                hasAllItems = false;
                int missingAmount = requiredAmount - playerAmount;
                String itemName = formatItemName(material); // Use the custom format method
                missingItemsMessage.append("\n§7- ").append("§d").append(itemName).append(" §ex"+missingAmount);
            }
        }

        if (!hasAllItems) {
            player.sendMessage(missingItemsMessage.toString());
            player.sendMessage("§cSegera cari item tersebut!\n§c");
            player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 100f, 0f);
        }

        return hasAllItems;
    }
    private static String formatItemName(Material material) {
        // Customize plugin method based on your desired format
        String itemName = material.toString().toLowerCase().replace("_", " ");
        itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1).toLowerCase();
        return "§d" + itemName;
    }
    private Map<Material, Integer> getPlayerInventoryContents(Player player) {
        Map<Material, Integer> playerInventory = new HashMap<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                Material material = item.getType();
                int amount = playerInventory.getOrDefault(material, 0) + item.getAmount();
                playerInventory.put(material, amount);
            }
        }

        return playerInventory;
    }


    private int countItemsInInventory(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }
}
