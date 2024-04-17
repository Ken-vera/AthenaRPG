package lunatic.athenarpg.blacksmith;

import lunatic.athenarpg.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ClearArmorstand implements CommandExecutor {
    private Main plugin;

    public ClearArmorstand(Main plugin){
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        for (Entity entity : player.getNearbyEntities(5, 5, 5)){
            if (entity instanceof ArmorStand){
                entity.remove();
                player.sendMessage("Cleared");
            }
        }

        return false;
    }
}
