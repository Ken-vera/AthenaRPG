package lunatic.athenarpg;

import lunatic.athenarpg.data.FileManager;
import lunatic.athenarpg.itemlistener.utils.RPGListenerRegister;
import lunatic.athenarpg.quest.BryzleQuest;
import lunatic.athenarpg.reward.BoxOpenListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    public List<Quest> availableQuests = new ArrayList<>();
    public List<ActiveQuest> activeQuests = new ArrayList<>();
    RPGListenerRegister rpgRegister = new RPGListenerRegister(this);

    public FileManager fileManager;

    BryzleQuest bryzleQuest;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BryzleQuest(this), this);
        getServer().getPluginManager().registerEvents(new BoxOpenListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);

        rpgRegister.registerRPGEventListener();

        fileManager = new FileManager(this);

        getCommand("rpgquest").setExecutor(this);
        getCommand("rpgquestcomplete").setExecutor(this);

        bryzleQuest = new BryzleQuest(this);
        bryzleQuest.setupQuests(); // Initialize available quests


    }
    @Override
    public void onDisable(){

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Load player's quest data from the file
        String questName = fileManager.getConfig("playerData.yml").get().getString(player.getName() + ".quest");
        if (questName != null) {
            Quest quest = findQuestByName(questName);
            if (quest != null) {
                ActiveQuest activeQuest = new ActiveQuest(player, quest);
                activeQuests.add(activeQuest);
            }
        }
    }


    private Quest findQuestByName(String questName) {
        for (Quest quest : availableQuests) {
            if (quest.getName().equalsIgnoreCase(questName)) {
                return quest;
            }
        }
        return null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use plugin command.");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("rpgquest")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("bryzle")) {
                    if (!hasActiveQuest(player)) {
                        bryzleQuest.startQuest(player);
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                            ActiveQuest activeQuest = getActiveQuest(player);
                            if (activeQuest != null) {
                                fileManager.getConfig("playerData.yml").set(player.getName() + ".quest", activeQuest.getQuest().getName());
                                fileManager.getConfig("playerData.yml").save();
                            }
                        }, 40L);
                    } else {
                        player.performCommand("rpgquestcomplete bryzle");
                    }
                    return true;
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("rpgquestcomplete")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("bryzle")) {
                    if (hasActiveQuest(player)) {
                        ActiveQuest activeQuest = getActiveQuest(player);
                        if (activeQuest != null) {
                            if (bryzleQuest.playerHasRequiredItems(player, activeQuest)) {
                                bryzleQuest.completeQuest(player, activeQuest);
                            }
                        }
                    } else {
                        player.sendMessage("§cYou don't have an active quest. Use /rpgquest to start one.");
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasActiveQuest(Player player) {
        return getActiveQuest(player) != null;
    }
    public ActiveQuest getActiveQuest(Player player) {
        for (ActiveQuest activeQuest : activeQuests) {
            if (activeQuest.getPlayer().equals(player)) {
                return activeQuest;
            }
        }
        return null;
    }
    public static class Quest {
        private String name;
        private String description;
        private Map<Material, Integer> requiredMaterials;

        public Quest(String name, String description, Map<Material, Integer> requiredMaterials) {
            this.name = name;
            this.description = description;
            this.requiredMaterials = requiredMaterials;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Map<Material, Integer> getRequiredMaterials() {
            return requiredMaterials;
        }
    }

    public static class ActiveQuest {
        private Player player;
        private Quest quest;
        private Map<Material, Integer> progress;

        public ActiveQuest(Player player, Quest quest) {
            this.player = player;
            this.quest = quest;
            this.progress = new HashMap<>();
            // Initialize progress for each required material
            for (Material material : quest.getRequiredMaterials().keySet()) {
                progress.put(material, 0);
            }
        }

        public Player getPlayer() {
            return player;
        }

        public Quest getQuest() {
            return quest;
        }

        public Map<Material, Integer> getProgress() {
            return progress;
        }

        public void incrementProgress(Material material, int amount) {
            int currentProgress = progress.getOrDefault(material, 0);
            progress.put(material, currentProgress + amount);
        }

        public boolean isCompleted() {
            for (Map.Entry<Material, Integer> entry : quest.getRequiredMaterials().entrySet()) {
                Material material = entry.getKey();
                int requiredAmount = entry.getValue();
                int currentAmount = progress.getOrDefault(material, 0);
                if (currentAmount < requiredAmount) {
                    return false;
                }
            }
            return true;
        }
    }

}