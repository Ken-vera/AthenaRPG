package lunatic.athenarpg.itemlistener.epic;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.MMOItemsHook;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EclipseShroud implements Listener {
    private Main plugin;
    private RPGUtils utils;
    private ItemConstructor itemConstructor;
    private CooldownManager cooldownManager;

    private MMOItemsHook mmoItemsHook;

    public EclipseShroud(Main plugin){
        this.plugin = plugin;
        this.utils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager();
        this.mmoItemsHook = new MMOItemsHook();
    }

    @EventHandler
    public void onEclipseRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String rpgName = utils.getRPGNameInHand(player);
        ItemStack item = player.getItemInHand();
        int rpgLevel = utils.getRPGLevel(player.getItemInHand());
        int rpgCooldown = itemConstructor.getCooldown(player.getItemInHand()) - rpgLevel;
        int baseRange = 5;
        int rpgRange = baseRange + rpgLevel;

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (rpgName.equals("Eclipse Shroud")) {
                    if (!cooldownManager.isOnCooldown(player.getName(), rpgName)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0));
                        player.playSound(player.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 100f, 1f);
                        new BukkitRunnable() {
                            int counter = 0;

                            public void run() {
                                if (counter >= 5) {
                                    cancel();
                                    return;
                                }
                                for (Entity ent : player.getNearbyEntities(rpgRange, rpgRange, rpgRange)) {
                                    if (ent instanceof Monster) {
                                        Vector direction = ent.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                                        ent.setVelocity(direction.multiply(2));
                                        player.playSound(player.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 100f, 1f);
                                    }
                                }
                                counter++;
                            }
                        }.runTaskTimer(plugin, 20, 20);
                        cooldownManager.setCooldown(player.getName(), rpgName, rpgCooldown);
                    }else{
                        cooldownManager.sendCooldownMessage(player, rpgName);
                    }
                }
            }
        }
    }
}
