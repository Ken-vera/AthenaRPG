package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DummyHandler implements Listener, CommandExecutor {
    private Main plugin;

    private List<Player> cooldown = new ArrayList<>();

    public DummyHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Usage: /createdummy <type>");
            return true;
        }

        String typeArg = args[0].toUpperCase();
        DummyType type = DummyType.valueOf(typeArg);
        if (type == null) {
            player.sendMessage("Invalid dummy type.");
            return true;
        }

        spawnDummy(player, type);
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityCombust(EntityCombustEvent e) {
        if (e.getEntity() instanceof Monster && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equalsIgnoreCase("§a§lMonster Dummy §7(§cDamage Tester§7)")) {
            e.setCancelled(true);
            e.setDuration(0);
        }
    }

    @EventHandler
    public void onDummyAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        Player player = (Player) e.getDamager();
        Entity entity = e.getEntity();

        if (entity instanceof Monster && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("§a§lMonster Dummy §7(§cDamage Tester§7)")) {
            ((Monster) entity).setHealth(((Monster) entity).getMaxHealth());
            int damage = (int) e.getDamage();
            player.sendMessage("§aYou deal §c" + damage + " §a to Dummy!");
            Bukkit.getScheduler().runTaskLater(plugin, () -> ((Monster) entity).setNoDamageTicks(0), 2L);
        }
    }

    @EventHandler
    public void onDummyInteract(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (e.getRightClicked() instanceof Monster && e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equalsIgnoreCase("§a§lMonster Dummy §7(§cDamage Tester§7)")) {
            e.setCancelled(true);
            openDummyGUI(player);
        }
    }

    private void openDummyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§a§lSelect Dummy Type");

        for (DummyType type : DummyType.values()) {
            ItemStack egg = new ItemStack(type.spawnEgg);
            ItemMeta meta = egg.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(type.displayName);
                meta.setLore(type.lore);
                egg.setItemMeta(meta);
            }
            gui.setItem(type.slot, egg);
        }
        if (player.hasPermission("dummy.delete")) {
            gui.setItem(49, new ItemStack(Material.BARRIER));
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onGUIInteract(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("§a§lSelect Dummy Type")) {

            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot >= 0 && slot < 45) {
                DummyType type = DummyType.getBySlot(slot);
                if (type != null) {
                    setDummyType(player, type);
                }
            }
            if (player.hasPermission("dummy.delete")) {
                if (slot == 49) {
                    setDummyType(player, null);
                }
            }
        }
    }

    private void setDummyType(Player player, DummyType type) {
        if (cooldown.contains(player)) return;
        Entity dummy = getDummyNearby(player);
        if (dummy != null) {
            cooldown.add(player);
            dummy.remove();
            Monster newDummy = (Monster) dummy.getWorld().spawn(dummy.getLocation(), type.entityClass);
            newDummy.setAI(false);
            newDummy.setCustomName("§a§lMonster Dummy §7(§cDamage Tester§7)");
            newDummy.setInvulnerable(false);
            newDummy.setSilent(true);
            newDummy.setMaxHealth(100000);
            newDummy.setHealth(100000);
            newDummy.setCustomNameVisible(true);
            player.sendMessage("§aSuccessfully set Dummy type to " + type.displayName);
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(plugin, () -> cooldown.remove(player), 400L);
        }
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Monster) {
            Arrow arrow = (Arrow) e.getDamager();
            Monster dummy = (Monster) e.getEntity();

            if (dummy.getCustomName() != null && dummy.getCustomName().equalsIgnoreCase("§a§lMonster Dummy §7(§cDamage Tester§7)")) {
                if (arrow.getShooter() instanceof Player){
                    ((Player) arrow.getShooter()).getPlayer().sendMessage("§aYou deal §c" + e.getDamage() + " §a to Dummy!");
                    Bukkit.getScheduler().runTaskLater(plugin, () -> ((Monster) dummy).setNoDamageTicks(0), 2L);
                }

            }
        }
    }

    private Entity getDummyNearby(Player player) {
        for (Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
            if (entity instanceof Monster && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("§a§lMonster Dummy §7(§cDamage Tester§7)")) {
                return entity;
            }
        }
        return null;
    }

    @EventHandler
    private void onEndermanTeleport(EntityTeleportEvent e) {
        if (e.getEntity() instanceof Enderman && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equalsIgnoreCase("§a§lMonster Dummy §7(§cDamage Tester§7)")) {
            e.setCancelled(true);
        }
    }

    private enum DummyType {
        ZOMBIE(10, Material.ZOMBIE_SPAWN_EGG, "§aSet type to Zombie", "§7Set the dummy to §3Zombie§7!", "§e§lCLICK!", Zombie.class),
        WITHER_SKELETON(11, Material.WITHER_SKELETON_SPAWN_EGG, "§aSet type to Wither Skeleton", "§7Set the dummy to §3Wither Skeleton§7!", "§e§lCLICK!", WitherSkeleton.class),
        EVOKER(12, Material.EVOKER_SPAWN_EGG, "§aSet type to Evoker", "§7Set the dummy to §3Evoker§7!", "§e§lCLICK!", Evoker.class),
        STRAY(13, Material.STRAY_SPAWN_EGG, "§aSet type to Stray", "§7Set the dummy to §3Stray§7!", "§e§lCLICK!", Stray.class),
        SPIDER(14, Material.SPIDER_SPAWN_EGG, "§aSet type to Spider", "§7Set the dummy to §3Spider§7!", "§e§lCLICK!", Spider.class),
        ENDERMAN(15, Material.ENDERMAN_SPAWN_EGG, "§aSet type to Enderman", "§7Set the dummy to §3Enderman§7!", "§e§lCLICK!", Enderman.class),
        ENDERMITE(16, Material.ENDERMITE_SPAWN_EGG, "§aSet type to Endermite", "§7Set the dummy to §3Endermite§7!", "§e§lCLICK!", Endermite.class),
        CREEPER(19, Material.CREEPER_SPAWN_EGG, "§aSet type to Creeper", "§7Set the dummy to §3Creeper§7!", "§e§lCLICK!", Creeper.class),
        SKELETON(20, Material.SKELETON_SPAWN_EGG, "§aSet type to Skeleton", "§7Set the dummy to §3Skeleton§7!", "§e§lCLICK!", Skeleton.class),
        WITCH(21, Material.WITCH_SPAWN_EGG, "§aSet type to Witch", "§7Set the dummy to §3Witch§7!", "§e§lCLICK!", Witch.class),
        BLAZE(22, Material.BLAZE_SPAWN_EGG, "§aSet type to Blaze", "§7Set the dummy to §3Blaze§7!", "§e§lCLICK!", Blaze.class),
        DROWNED(23, Material.DROWNED_SPAWN_EGG, "§aSet type to Drowned", "§7Set the dummy to §3Drowned§7!", "§e§lCLICK!", Drowned.class),
        ELDER_GUARDIAN(24, Material.ELDER_GUARDIAN_SPAWN_EGG, "§aSet type to Elder Guardian", "§7Set the dummy to §3Elder Guardian§7!", "§e§lCLICK!", ElderGuardian.class),
        HUSK(25, Material.HUSK_SPAWN_EGG, "§aSet type to Husk", "§7Set the dummy to §3Husk§7!", "§e§lCLICK!", Husk.class),
        ILLUSIONER(28, Material.EVOKER_SPAWN_EGG, "§aSet type to Illusioner", "§7Set the dummy to §3Illusioner§7!", "§e§lCLICK!", Illusioner.class),
        PILLAGER(29, Material.PILLAGER_SPAWN_EGG, "§aSet type to Pillager", "§7Set the dummy to §3Pillager§7!", "§e§lCLICK!", Pillager.class),
        SILVERFISH(30, Material.SILVERFISH_SPAWN_EGG, "§aSet type to Silverfish", "§7Set the dummy to §3Silverfish§7!", "§e§lCLICK!", Silverfish.class);

        int slot;
        Material spawnEgg;
        String displayName;
        List<String> lore;
        Class<? extends Monster> entityClass;

        DummyType(int slot, Material spawnEgg, String displayName, String loreLine, String clickPrompt, Class<? extends Monster> entityClass) {
            this.slot = slot;
            this.spawnEgg = spawnEgg;
            this.displayName = displayName;
            this.lore = List.of("", loreLine, "", clickPrompt);
            this.entityClass = entityClass;
        }

        public static DummyType getBySlot(int slot) {
            for (DummyType type : values()) {
                if (type.slot == slot) {
                    return type;
                }
            }
            return null;
        }
    }
    private void spawnDummy(Player player, DummyType type) {
        if (cooldown.contains(player)) {
            player.sendMessage("You are currently on cooldown. Please try again later.");
            return;
        }

        Location location = player.getLocation().add(0, 0, 0); // Adjust spawn location as needed
        Monster dummy = (Monster) location.getWorld().spawn(location, type.entityClass);
        dummy.setCustomName("§a§lMonster Dummy §7(§cDamage Tester§7)");
        dummy.setCustomNameVisible(true);
        dummy.setMaxHealth(1000000);
        dummy.setHealth(1000000);
        dummy.setAI(false);
        dummy.setInvulnerable(false);
        dummy.setSilent(true);

        player.sendMessage("Successfully spawned a Dummy of type " + type.displayName);
        cooldown.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> cooldown.remove(player), 400L);

        plugin.entityList.add(dummy);
    }

}
