package lunatic.athenarpg.reward;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RewardManager {
    private Main plugin;

    public RewardManager(Main plugin){
        this.plugin = plugin;
    }
    public void giveRPGBoxReward(Player player, String type){
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        if (type.equalsIgnoreCase("COMMON_BOX")){
            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL COMMON_BOX " + player.getName() + " 1");
        }
        else if (type.equalsIgnoreCase("RARE_BOX")){
            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL RARE_BOX " + player.getName() + " 1");
        }
        else if (type.equalsIgnoreCase("EPIC_BOX")){
            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL EPIC_BOX " + player.getName() + " 1");
        }
        else if (type.equalsIgnoreCase("LEGENDARY_BOX")){
            Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL LEGENDARY_BOX " + player.getName() + " 1");
        }
    }
}
