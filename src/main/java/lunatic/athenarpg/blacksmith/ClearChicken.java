package lunatic.athenarpg.blacksmith;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ClearChicken implements CommandExecutor {
    private Main plugin;

    public ClearChicken(Main plugin){
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        for (Entity entity : player.getNearbyEntities(205, 205, 205)){
            if (entity instanceof Chicken){
                entity.remove();
                player.sendMessage("Cleared");
            }
        }

        return false;
    }
}
