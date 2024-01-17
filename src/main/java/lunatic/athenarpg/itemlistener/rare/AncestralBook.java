package lunatic.athenarpg.itemlistener.rare;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.PlayerStatus;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AncestralBook implements Listener {
    private Main plugin;
    private RPGUtils utils;
    private ItemConstructor itemConstructor;
    private CooldownManager cooldownManager;
    private StatusListener statusListener;

    public AncestralBook(Main plugin) {
        this.plugin = plugin;
        this.utils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager(plugin);
        this.statusListener = new StatusListener(plugin);
    }

    @EventHandler
    public void warpSwapper(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the item has lore and matches the specified format
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            String rpgName = utils.getRPGNameInHand(player);
            if (rpgName.equals("Ancestral Book")) {
                int cooldownTime = 0;
                cooldownTime = itemConstructor.getCooldown(item);
                int rpgLevel = utils.getRPGLevelInHand(player);
                ItemMeta meta = item.getItemMeta();
                Long cooldown = cooldownManager.getCooldown(utils.getRPGNameInHand(player), player.getUniqueId());
                if (cooldown == null) {
                    String currentLocation = getCurrentLocationFromLore(meta.getLore());
                    cooldownTime = itemConstructor.getCooldown(item);

                    // Check if the lore contains a valid location
                    if (currentLocation != null) {
                        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            String newLocation = getNextLocation(currentLocation);
                            updateLore(meta, newLocation);
                            item.setItemMeta(meta);
                            player.sendMessage(ChatColor.GREEN + "Location changed to: §c" + newLocation);
                            player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 20f, 1f);
                        } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            player.sendMessage(ChatColor.GREEN + "Teleporting to: §c" + currentLocation);

                            int finalCooldownTime = cooldownTime;
                            cooldownManager.setCooldown(rpgName, finalCooldownTime - (5 * rpgLevel), player.getUniqueId());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    teleportPlayer(player, currentLocation);
                                    player.playSound(player, Sound.BLOCK_PORTAL_TRAVEL, 10f, 1f);
                                    player.sendMessage(ChatColor.GREEN + "You are now immune!");
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 255));
                                    makePlayerImmune(player, rpgLevel);
                                }
                            }.runTaskLater(plugin, 60); // 60 ticks = 3 seconds
                        }
                    }
                } else {
                    cooldownManager.sendCooldownMessage(player, rpgName, cooldownTime);
                }
            }
        }
    }

    private void makePlayerImmune(Player player, int rpgLevel) {
        player.setInvulnerable(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setInvulnerable(false);
                player.sendMessage(ChatColor.RED + "You are no longer immune!");
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
        }.runTaskLater(plugin, 20 * (1 + rpgLevel)); // 20 ticks = 1 second, adjusted based on RPG level
    }

    private void teleportPlayer(Player player, String currentLocation) {
        switch (currentLocation) {
            case "Dungeon":
                String teleportCommand1 = "mv tp " + player.getName() + " worldDungeon";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand1);
                String teleportCommand2 = "tp " + player.getName() + " -54 72 -35";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand2);
                break;
            case "Eldoria":
                teleportCommand1 = "mv tp " + player.getName() + " worldDungeon";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand1);
                teleportCommand2 = "tp " + player.getName() + " 102 65 8";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand2);
                break;
            case "Dragon Nest":
                teleportCommand1 = "mv tp " + player.getName() + " Drago";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand1);
                teleportCommand2 = "tp " + player.getName() + " 0 262 0";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand2);
                break;
            case "Dungeon Desert":
                teleportCommand1 = "mv tp " + player.getName() + " worldDungeon";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand1);
                teleportCommand2 = "tp " + player.getName() + " 35 88 -55";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teleportCommand2);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Invalid location specified.");
        }
    }

    private String getCurrentLocationFromLore(java.util.List<String> lore) {
        for (String line : lore) {
            if (line.contains("Current Location:")) {
                return ChatColor.stripColor(line.replace("Current Location: ", ""));
            }
        }
        return null;
    }

    private String getNextLocation(String currentLocation) {
        // You can customize this logic based on your requirements
        String[] locations = {"Dungeon", "Eldoria", "Dragon Nest", "Dungeon Desert"};
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equals(currentLocation)) {
                return (i == locations.length - 1) ? locations[0] : locations[i + 1];
            }
        }
        return currentLocation; // Default to the current location if not found in the list
    }

    private void updateLore(ItemMeta meta, String newLocation) {
        java.util.List<String> lore = meta.getLore();
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("Current Location:")) {
                lore.set(i, ChatColor.GRAY + "Current Location: " + ChatColor.RED + newLocation);
            }
        }
        meta.setLore(lore);
    }
}
