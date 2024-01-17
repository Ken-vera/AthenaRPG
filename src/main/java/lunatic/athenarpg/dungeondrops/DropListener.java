package lunatic.athenarpg.dungeondrops;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import lunatic.athenarpg.Main;
import lunatic.athenarpg.reward.BoxOpenListener;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class DropListener implements Listener {
    private Main plugin;

    BoxOpenListener dropList = new BoxOpenListener(plugin);

    public DropListener(Main plugin) {
        this.plugin = plugin;
    }

    private static final Random random = new Random();

    @EventHandler
    public void onDungeonMobDeath(MythicMobDeathEvent event) {
        if (event.getKiller() instanceof Player) {
            if (event.getEntity().getWorld().getName().equals("worldDungeon")) {
                if (event.getEntity().getName().contains("Ancient")) {
                    if (event.getEntity().getName().contains("Griffin") || event.getEntity().getName().contains("Wither")) {
                        double chance = 0.15;
                        if (random.nextDouble() < chance) {
                            double commonChance = 0.6;
                            double rareChance = 0.3;
                            double epicChance = 0.1;

                            double randomValue = random.nextDouble();

                            int randomAmount = random.nextInt(2) + 1; // 1 to 2 (inclusive)
                            Player player = (Player) event.getKiller();
                            ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

                            if (randomValue < commonChance) {
                                // Execute common drop
                                Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + dropList.getRandomElement(dropList.COMMON_REWARD) + " " + player.getName() + " " + randomAmount);
                            } else if (randomValue < (commonChance + rareChance)) {
                                // Execute rare drop
                                Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + dropList.getRandomElement(dropList.RARE_REWARD) + " " + player.getName() + " " + randomAmount);
                            } else {
                                // Execute epic drop
                                Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + dropList.getRandomElement(dropList.EPIC_REWARD) + " " + player.getName() + " " + randomAmount);
                            }
                        }
                    } else {
                        double chance = 0.10;
                        if (random.nextDouble() < chance) {
                            double commonChance = 0.6;
                            double rareChance = 0.3;
                            double epicChance = 0.1;

                            double randomValue = random.nextDouble();

                            int randomAmount = random.nextInt(1) + 1; // 1 to 2 (inclusive)
                            Player player = (Player) event.getKiller();
                            ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

                            if (randomValue < commonChance) {
                                // Execute common drop
                                Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + dropList.getRandomElement(dropList.COMMON_REWARD) + " " + player.getName() + " " + randomAmount);
                            } else if (randomValue < (commonChance + rareChance)) {
                                // Execute rare drop
                                Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + dropList.getRandomElement(dropList.RARE_REWARD) + " " + player.getName() + " " + randomAmount);
                            } else {
                                // Execute epic drop
                                Bukkit.dispatchCommand(consoleCommandSender, "mi give MATERIAL " + dropList.getRandomElement(dropList.EPIC_REWARD) + " " + player.getName() + " " + randomAmount);
                            }
                        }
                    }
                }
            }
        }
    }

}
