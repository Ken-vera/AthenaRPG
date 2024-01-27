package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
public class CancelGift implements Listener, CommandExecutor {
    private Main plugin;
    private final List<String> loreKeywords = Arrays.asList(
            "§a§lCOMMON", "§9§lRARE", "§5§lEPIC", "§e§lLEGENDARY",
            "§c§lBOSS DROP", "§c§lLIMITED", "§6§lDUNGEON", "§b§lESSENCE"
    );

    public CancelGift(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        if (message.startsWith("/gift")) {
            Player player = event.getPlayer();
            ItemStack heldItem = player.getInventory().getItemInMainHand();

            if (hasLoreKeyword(heldItem)) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot use /gift with this item.");
            }
        }
    }

    private boolean hasLoreKeyword(ItemStack item) {
        if (item.getType() != Material.AIR && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                for (String loreLine : meta.getLore()) {
                    for (String keyword : loreKeywords) {
                        if (loreLine.contains(keyword)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("This is a sample plugin. Use /gift to test.");
        }
        return true;
    }
}
