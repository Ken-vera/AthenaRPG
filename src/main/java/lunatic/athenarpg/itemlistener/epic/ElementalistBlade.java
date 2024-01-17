package lunatic.athenarpg.itemlistener.epic;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class ElementalistBlade implements Listener {
    private Main plugin;
    RPGUtils utils = new RPGUtils();

    public ElementalistBlade(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void elementalistMode(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        ItemStack blade = player.getInventory().getItemInMainHand();
        if (blade.getType() != Material.DIAMOND_SWORD || !blade.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = blade.getItemMeta();
        assert itemMeta != null;
        if (!itemMeta.hasLore()) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null || lore.size() <= 6) {
            return;
        }
        if (hasLoreContaining(lore, "Active Element:")) {
            int loreLine = findLoreContaining(lore, "Active Element:");
            String element = getLoreLineValue(lore, loreLine);
            assert element != null;

            player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 50, 5);

            switch (element) {
                case "§9Water":
                    lore.set(loreLine, ChatColor.GRAY + "Active Element: §cFire");
                    player.sendMessage(ChatColor.GREEN + "Active Element: " + ChatColor.RED + "Fire");
                    player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 10, 2);
                    break;
                case "§cFire":
                    lore.set(loreLine, ChatColor.GRAY + "Active Element: §bIce");
                    player.sendMessage(ChatColor.GREEN + "Active Element: " + ChatColor.AQUA + "Ice");
                    player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 10, 2);
                    break;
                case "§bIce":
                    lore.set(loreLine, ChatColor.GRAY + "Active Element: §9Water");
                    player.sendMessage(ChatColor.GREEN + "Active Element: " + ChatColor.BLUE + "Water");
                    player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 10, 2);
                    break;
            }

            itemMeta.setLore(lore);
            blade.setItemMeta(itemMeta);
            player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 50, 5);
            return;
        }
    }

    @EventHandler
    public void elementalistWater(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        double maxHealth = victim.getMaxHealth();
        double health = victim.getHealth();

        ItemStack blade = victim.getInventory().getItemInMainHand();
        if (blade.getType() != Material.DIAMOND_SWORD || !blade.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = blade.getItemMeta();
        assert itemMeta != null;
        if (!itemMeta.hasLore()) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            return;
        }

        if (hasLoreContaining(lore, "§7Active Element: §9Water")) {
            if (health <= (0.1 * maxHealth)) {
                int rpgLevel = utils.getRPGLevelInHand(victim.getPlayer()); // Assuming you have a method to get the RPG level for the Elementalist Blade

                int duration = 120 + (rpgLevel * 20);
                victim.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 2));
                victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, 100f, 0f);
            }
        }
    }

    @EventHandler
    public void elementalistFire(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        LivingEntity target = (LivingEntity) event.getEntity();
        Player player = (Player) event.getDamager();
        ItemStack blade = player.getInventory().getItemInMainHand();
        if (blade.getType() != Material.DIAMOND_SWORD || !blade.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = blade.getItemMeta();
        assert itemMeta != null;
        if (!itemMeta.hasLore()) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            return;
        }

        if (hasLoreContaining(lore, "§7Active Element: §cFire")) {

            target.setFireTicks(60);
            int rpgLevel = utils.getRPGLevelInHand(player); // Assuming you have a method to get the RPG level for the Elementalist Blade

            new BukkitRunnable() {
                int ticksRemaining = 6; // 0.5 seconds = 6 ticks (20 ticks per second)
                @Override
                public void run() {
                    if (ticksRemaining <= 0 || target.isDead()) {
                        cancel();
                        return;
                    }
                    target.damage(2 + rpgLevel); // Increase fire damage by +1 per RPG level
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_BLAZE_HURT, 100f, 1f);
                    ticksRemaining--;
                }
            }.runTaskTimer(plugin, 0, 10); // 10 ticks = 0.5 seconds (20 ticks per second)
        }
    }


    @EventHandler
    public void elementalistIce(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        LivingEntity victim = (LivingEntity) event.getEntity();
        Player player = (Player) event.getDamager();
        ItemStack blade = player.getInventory().getItemInMainHand();
        if (blade.getType() != Material.DIAMOND_SWORD || !blade.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = blade.getItemMeta();
        assert itemMeta != null;
        if (!itemMeta.hasLore()) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            return;
        }

        if (hasLoreContaining(lore, "§7Active Element: §bIce")) {
            int rpgLevel = utils.getRPGLevelInHand(player); // Assuming you have a method to get the RPG level for the Elementalist Blade

            double chance = 0.1 * rpgLevel; // Increase chance by 10% per RPG level
            if (Math.random() <= chance) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
                victim.setFreezeTicks(140);
                victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_GLASS_BREAK, 100f, 0f);
            }
        }
    }

    private int findLoreContaining(List<String> lore, String searchString) {
        if (lore == null || searchString == null) {
            return -1;
        }
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.contains(searchString)) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasLoreContaining(List<String> lore, String searchString) {
        if (lore == null || searchString == null) {
            return false;
        }
        for (String line : lore) {
            if (line.contains(searchString)) {
                return true;
            }
        }
        return false;
    }

    private String getLoreLineValue(List<String> lore, int lineNumber) {
        if (lineNumber <= 0 || lineNumber > Objects.requireNonNull(lore).size()) {
            return null;
        }
        String line = lore.get(lineNumber);
        String[] parts = line.split(": ");
        if (parts.length >= 2) {
            return parts[1].trim();
        }
        return null;
    }
}
