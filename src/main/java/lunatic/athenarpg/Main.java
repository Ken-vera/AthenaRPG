package lunatic.athenarpg;

import lunatic.athenarpg.blacksmith.BlacksmithCommand;
import lunatic.athenarpg.data.FileManager;
import lunatic.athenarpg.db.Database;
import lunatic.athenarpg.dungeondrops.DropListener;
import lunatic.athenarpg.handler.SignEditorHandler;
import lunatic.athenarpg.itemlistener.dungeon.pve.PlayerStandHigh;
import lunatic.athenarpg.itemlistener.utils.RPGListenerRegister;
import lunatic.athenarpg.itemlistener.utils.SlimefunHandler;
import lunatic.athenarpg.quest.BryzleQuest;
import lunatic.athenarpg.reward.BoxOpenListener;
import lunatic.athenarpg.stats.PlayerStatus;
import lunatic.athenarpg.stats.StatusListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    public List<Quest> availableQuests = new ArrayList<>();
    public List<ActiveQuest> activeQuests = new ArrayList<>();
    RPGListenerRegister rpgRegister = new RPGListenerRegister(this);
    public Database database;
    private Map<String, Long> cooldowns = new HashMap<>();
    private FileManager playerDataFileManager;
    private FileConfiguration config;
    public FileManager fileManager;
    BryzleQuest bryzleQuest;

    public Map<UUID, PlayerStatus> playerStatusMap = new HashMap<>();

    private Economy economy;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BryzleQuest(this), this);
        getServer().getPluginManager().registerEvents(new BoxOpenListener(this), this);
        getServer().getPluginManager().registerEvents(new SignEditorHandler(this), this);
        getServer().getPluginManager().registerEvents(new SlimefunHandler(this), this);
        getServer().getPluginManager().registerEvents(new DropListener(this), this);
        getServer().getPluginManager().registerEvents(new StatusListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerStandHigh(this), this);
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("blacksmithrepair").setExecutor(new BlacksmithCommand(this));

        startStatusUpdateTask();

        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        this.config = getConfig();

        try {
            this.database = new Database(this);
            database.initializeDatabase();
        } catch (SQLException ex) {
            System.out.println("Failed to connect to the Database and create Tables!");
            ex.printStackTrace();
        }

        rpgRegister.registerRPGEventListener();

        fileManager = new FileManager(this);

        getCommand("rpgquest").setExecutor(this);
        getCommand("rpgquestcomplete").setExecutor(this);

        bryzleQuest = new BryzleQuest(this);
        bryzleQuest.setupQuests(); // Initialize available quests

        playerDataFileManager = new FileManager(this);


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
                        if (isCooldownExpired(player.getName())) {
                            bryzleQuest.startQuest(player);
                            Bukkit.getScheduler().runTaskLater(this, () -> {
                                ActiveQuest activeQuest = getActiveQuest(player);
                                if (activeQuest != null) {
                                    fileManager.getConfig("playerData.yml").set(player.getName() + ".quest", activeQuest.getQuest().getName());
                                    fileManager.getConfig("playerData.yml").save();
                                }
                            }, 40L);
                        }else{
                            player.sendMessage("§cYou're currently on cooldown!");
                        }
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
                            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
                            ItemMeta itemMeta = itemInMainHand.getItemMeta();

                            if (itemMeta != null && itemMeta.hasDisplayName()) {
                                String itemName = ChatColor.stripColor(itemMeta.getDisplayName());

                                if (itemName.equals("Bryzle Quest Skipper")) {
                                    bryzleQuest.skipQuest(player, activeQuest);
                                    setCooldown(player.getName());
                                    Bukkit.broadcastMessage("");
                                    Bukkit.broadcastMessage("§e" + player.getName() + " §fmelakukan §dSkip Quest " + activeQuest.getQuest().getName());
                                    Bukkit.broadcastMessage("");
                                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_SHOOT, 50f, 1f);
                                    }
                                    itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
                                    fileManager.getConfig("playerData.yml").set(player.getName() + ".quest", null);
                                    fileManager.getConfig("playerData.yml").save();
                                    return false;
                                }
                            }
                            if (bryzleQuest.playerHasRequiredItems(player, activeQuest)) {
                                if (isCooldownExpired(player.getName())) {
                                    bryzleQuest.completeQuest(player, activeQuest);
                                    setCooldown(player.getName());
                                    fileManager.getConfig("playerData.yml").set(player.getName() + ".quest", null);
                                    fileManager.getConfig("playerData.yml").save();
                                }
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
    private void updateCooldownsInYaml() {
        for (String playerName : cooldowns.keySet()) {
            long cooldownEnd = cooldowns.get(playerName);
            long currentTime = System.currentTimeMillis();

            if (currentTime > cooldownEnd) {
                // Cooldown has expired, remove from map and save to YAML
                cooldowns.remove(playerName);
                saveCooldownToYaml(playerName, 0);
            } else {
                // Calculate remaining cooldown in minutes
                long remainingCooldownMinutes = (cooldownEnd - currentTime) / (60 * 1000);
                saveCooldownToYaml(playerName, remainingCooldownMinutes);
            }
        }
    }

    private void saveCooldownToYaml(String playerName, long remainingCooldownMinutes) {
        playerDataFileManager.getConfig("playerData.yml").set(playerName + ".cooldown", remainingCooldownMinutes);
        playerDataFileManager.saveConfig("playerData.yml");
    }

    private boolean isCooldownExpired(String playerName) {
        if (cooldowns.containsKey(playerName)) {
            long cooldownEnd = cooldowns.get(playerName);
            long currentTime = System.currentTimeMillis();
            return currentTime > cooldownEnd;
        }
        return true;
    }

    private void setCooldown(String playerName) {
        // Set cooldown duration (5 minutes in milliseconds)
        long cooldownDuration = 5 * 60 * 1000;
        long cooldownEnd = System.currentTimeMillis() + cooldownDuration;
        cooldowns.put(playerName, cooldownEnd);
        saveCooldownToYaml(playerName, 5);
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }
    public void startStatusUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player != null && player.isOnline()) {
                        UUID playerId = player.getUniqueId();
                        updatePlayerStatus(player);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 10L);
        new BukkitRunnable(){
            @Override
            public void run(){
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player != null && player.isOnline()) {
                        UUID playerId = player.getUniqueId();
                        PlayerStatus playerStatus = playerStatusMap.get(playerId);
                        int maxMana = playerStatus.getMaxMana();
                        int mana = playerStatus.getCurrentMana();
                        int vitality = playerStatus.getCurrentVitality();
                        int manaRegenPerSecond = (int) (maxMana * 0.2);
                        int newMana = Math.min(maxMana, mana + manaRegenPerSecond);
                        if (mana < maxMana) {
                            playerStatus.setCurrentMana(newMana);
                        }
                        if (mana > maxMana){
                            playerStatus.setCurrentMana(playerStatus.getMaxMana());
                        }
                        if (maxMana < 100){
                            playerStatus.setMaxMana(100);
                        }
                        if (mana < 0){
                            playerStatus.setCurrentMana(0);
                        }

                        if (playerStatus.getMaxVitality() < 100){
                            playerStatus.setMaxVitality(100);
                        }
                        if (vitality < playerStatus.getMaxVitality()){
                            playerStatus.setCurrentVitality(playerStatus.getCurrentVitality() + 1);
                        }
                        playerStatus.setMaxHealth((int) player.getMaxHealth());
                        playerStatus.setHealth((int) player.getHealth());
                    }
                }
            }
        }.runTaskTimer(this, 0L, 35L);
    }

    public void updatePlayerStatus(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerStatus playerStatus = playerStatusMap.get(playerId);

        if (playerStatus == null) {
            playerStatus = new PlayerStatus(player, (int) player.getMaxHealth(), (int) player.getHealth(), 100, 100);
            playerStatusMap.put(playerId, playerStatus);
        }

        int currentHealth = playerStatus.getCurrentHealth();
        int maxHealth = playerStatus.getMaxHealth();
        int maxMana = playerStatus.getMaxMana();
        int mana = playerStatus.getCurrentMana();
        int vitality = playerStatus.getCurrentVitality();

        if (mana > maxMana){
            playerStatus.setCurrentMana(playerStatus.getMaxMana());
        }


        String actionBarMessage = "    §c" + currentHealth + "/" + maxHealth + "❤" +
                "    §7§l|    §b" + mana + "/" + maxMana + " ✤ " +
                "    §7§l|    §4" + vitality + "% Vitality♨";

        sendActionBarMessage(player, actionBarMessage);
    }
    public static void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}