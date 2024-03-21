package lunatic.athenarpg.itemlistener.limited;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
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

public class BianzhongBell implements Listener {
    private Main plugin;
    private RPGUtils rpgUtils;
    private ItemConstructor itemConstructor;
    private StatusListener statusListener;

    public BianzhongBell(Main plugin){
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.statusListener = new StatusListener(plugin);
    }
    @EventHandler
    public void onBianzhongRightClick(PlayerInteractEvent event){
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand().equals(EquipmentSlot.HAND)) {
                Player player = event.getPlayer();
                String rpgName = rpgUtils.getRPGNameInHand(player);
                if (rpgName.equals("Bianzhong Bell")) {
                    int manaCost = itemConstructor.getMainHandManaCost(player);
                    if (statusListener.haveEnoughMana(player, manaCost)) {
                        for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                            if (entity instanceof Monster) {
                                ((Monster) entity).setTarget(player);
                                player.playSound(player, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 20f, 2f);
                                statusListener.consumeMana(player, manaCost);
                            }
                        }
                    }
                }
            }
        }
    }
}
