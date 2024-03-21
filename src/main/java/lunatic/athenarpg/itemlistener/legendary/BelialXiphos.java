package lunatic.athenarpg.itemlistener.legendary;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class BelialXiphos implements Listener {
    private Main plugin;
    private final HashSet<Player> cdgiant = new HashSet<>();
    RPGUtils rpgUtils;

    public BelialXiphos(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
    }

    @EventHandler
    public void onXiphosKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof MagmaCube) || event.getEntity().getKiller() == null) return;

        Player player = event.getEntity().getKiller();
        if (!(player.getItemInHand().getType().equals(Material.NETHERITE_SWORD) &&
                Objects.requireNonNull(player.getItemInHand().getItemMeta()).hasDisplayName() &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§c§lBelial Xiphos"))) {
            return;
        }


        String entityName = ChatColor.stripColor(event.getEntity().getName());
        if (!entityName.equalsIgnoreCase("Belial")) return;
        if (!player.getWorld().getName().equals("worldAsmodeus")) return;

        ItemStack item = player.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.contains("Belial Killed : ")) {
                int count = Integer.parseInt(ChatColor.stripColor(line.replace("Belial Killed : ", "")));
                lore.set(i, ChatColor.GRAY + "Belial Killed : " + ChatColor.RED + (count + 1));
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                break;
            }
        }
    }

    @EventHandler
    public void onBelialXiphos(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.getType().equals(Material.NETHERITE_SWORD) ||
                !Objects.requireNonNull(item.getItemMeta()).hasDisplayName() ||
                !item.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lBelial Xiphos") ||
                !(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
            return;
        }

        if (cdgiant.contains(player)) {
            player.sendMessage(ChatColor.RED + "This skill is on cooldown.");
            return;
        }

        Giant giant = player.getWorld().spawn(player.getLocation().add(0, -1, 0), Giant.class);
        giant.setInvisible(true);
        giant.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
        giant.setInvulnerable(true);
        giant.setPersistent(true);
        giant.setCustomName("Dinnerbone");
        giant.setSilent(true);
        giant.setAI(false);
        giant.setCollidable(true);

        player.playSound(giant.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 0.0f);

        plugin.entityList.add(giant);

        cdgiant.add(player);
        removeGiant(giant);

        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        int belialDamage = 0;
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.contains("Belial Killed : ")) {
                belialDamage = Integer.parseInt(ChatColor.stripColor(line.replace("Belial Killed : ", "").replace("%", "")));
                break;
            }
        }
        for (Entity ent : giant.getNearbyEntities(5, 5, 5)){
            if (ent instanceof LivingEntity) {
                if (ent != player) {
                    LivingEntity e = (LivingEntity) ent;
                    e.damage(80 + belialDamage, player);
                    e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                }
            }

        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            cdgiant.remove(player);
            player.sendMessage(ChatColor.GREEN + "You can use your Gigantic Slam again!");
        }, 450L);
    }

    private void removeGiant(Giant giant) {
        Bukkit.getScheduler().runTaskLater(plugin, giant::remove, 35L);
    }
}
