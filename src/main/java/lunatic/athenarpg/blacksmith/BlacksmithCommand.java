package lunatic.athenarpg.blacksmith;

import lunatic.athenarpg.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlacksmithCommand implements CommandExecutor {
    private Main plugin;

    public BlacksmithCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // No arguments provided, show repair confirmation message for normal
            showRepairMessage(player, "normal");
        } else if (args.length == 1) {
            // Handle subcommands (cancel or confirm)
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("cancel")) {
                // Handle cancel logic
                for (int i = 0; i < 50; i++) {
                    player.sendMessage("§c");
                }
                player.sendMessage("§a§lAtlas §7» §fMungkin lain kali!");

            } else if (subcommand.equals("confirm")) {
                // Handle confirm logic based on the item category
                player.sendMessage("§cInvalid subcommand. Usage: /blacksmithrepair [cancel|confirm]");
            } else {
                // Check the item category and handle accordingly
                if (subcommand.equals("conqueror") || subcommand.equals("revenant") || subcommand.equals("normal")) {
                    showRepairMessage(player, subcommand);
                } else {
                    // Invalid subcommand
                    player.sendMessage("§cInvalid subcommand. Usage: /blacksmithrepair [cancel|confirm] [conqueror|revenant|normal]");
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("confirm")) {
            // Handle confirm logic based on the item category
            String category = args[1].toLowerCase();
            if (category.equals("conqueror") || category.equals("revenant") || category.equals("normal")) {
                repairPlayerTools(player, category);
            } else {
                player.sendMessage("§cInvalid item category. Usage: /blacksmithrepair confirm [conqueror|revenant|normal]");
            }
        } else {
            // Invalid number of arguments
            player.sendMessage("§cUsage: /blacksmithrepair [cancel|confirm] [conqueror|revenant|normal]");
        }

        return true;
    }

    private void repairPlayerTools(Player player, String category) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        int maxDurability = itemInHand.getType().getMaxDurability();
        int currentDurability = maxDurability - itemInHand.getDurability();
        int missingDurability = maxDurability - currentDurability;

        double repairCost = calculateRepairCost(missingDurability, category);

        String itemName = itemInHand.getType().toString();

        if (repairCost > 0) {
            if (itemInHand.getType() != Material.AIR) {
                if (itemInHand.getDurability() < maxDurability - 1) {
                    if (plugin.getEconomy().getBalance(player) >= repairCost) {
                        plugin.getEconomy().withdrawPlayer(player, repairCost);
                        itemInHand.setDurability((short) 0);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 100f, 0f);
                        player.sendMessage("§a§lAtlas §7» §fAhhh ini dia! Aku sudah berhasil memperbaiki §b" + itemName + "§f!");
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
                        player.sendMessage("§a§lAtlas §7» §fAku pikir kamu perlu mencari uang terlebih dahulu untuk memperbaiki barang ini!");
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
                    player.sendMessage("§a§lAtlas §7» §fItem ini masih bagus, aku tidak perlu memperbaikinya!");
                }
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
                player.sendMessage("§a§lAtlas §7» §fAku tidak pernah mempelajari item ini...");
            }
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
            player.sendMessage("§a§lAtlas §7» §fItem ini masih bagus, aku tidak perlu memperbaikinya!");
        }
    }

    private double calculateRepairCost(int missingDurability, String category) {
        double baseCost = missingDurability * 2.0;
        if (category.equals("conqueror")) {
            // Conqueror repair cost is 50% of the normal cost
            return baseCost * 0.5;
        } else if (category.equals("revenant")) {
            // Revenant repair is free
            return 1;
        } else {
            // Normal repair cost
            return baseCost;
        }
    }

    private void showRepairMessage(Player player, String category) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        int maxDurability = itemInHand.getType().getMaxDurability();
        int currentDurability = maxDurability - itemInHand.getDurability();
        int missingDurability = maxDurability - currentDurability;

        double repairCost = calculateRepairCost(missingDurability, category);

        String itemName = itemInHand.getType().toString();

        if (repairCost > 0) {
            if (itemInHand.getType() != Material.AIR) {
                if (itemInHand.getDurability() < maxDurability - 1) {
                    BaseComponent[] message = new ComponentBuilder("§a§lAtlas §7» §fApakah kamu ingin memperbaiki ")
                            .append(itemName).color(net.md_5.bungee.api.ChatColor.GOLD)
                            .append(" dengan harga ").color(net.md_5.bungee.api.ChatColor.WHITE)
                            .append(String.format("%.2f", repairCost)).color(net.md_5.bungee.api.ChatColor.YELLOW)
                            .append(" Peso? ").color(net.md_5.bungee.api.ChatColor.WHITE)
                            .append("\n")
                            .append("\n")
                            .append(" [").color(net.md_5.bungee.api.ChatColor.WHITE)
                            .append("§lYES").color(net.md_5.bungee.api.ChatColor.GREEN)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blacksmithrepair confirm " + category))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Confirm Repair").create()))
                            .append("]").color(net.md_5.bungee.api.ChatColor.WHITE)
                            .append("\u00A0\u00A0\u00A0\u00A0\u00A0")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()))
                            .append(" [").color(net.md_5.bungee.api.ChatColor.WHITE)
                            .append("§lNO").color(net.md_5.bungee.api.ChatColor.RED)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blacksmithrepair cancel"))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cancel Repair").create()))
                            .append("]").color(net.md_5.bungee.api.ChatColor.WHITE)
                            .create();
                    player.spigot().sendMessage(message);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 100f, 0f);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
                    player.sendMessage("§a§lAtlas §7» §fAku tidak pernah mempelajari item ini...");
                }
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
                player.sendMessage("§a§lAtlas §7» §fAku tidak pernah mempelajari item ini...");
            }
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0f);
            player.sendMessage("§a§lAtlas §7» §fItem ini masih bagus, aku tidak perlu memperbaikinya!");
        }
    }
}
