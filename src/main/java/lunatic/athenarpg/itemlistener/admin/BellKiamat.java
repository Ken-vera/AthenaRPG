package lunatic.athenarpg.itemlistener.admin;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

public class BellKiamat implements Listener {
    private Main plugin;
    private RPGUtils rpgUtils;

    public BellKiamat(Main plugin){
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
    }
    @EventHandler
    public void onBellKiamatInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (event.getHand() == EquipmentSlot.HAND) {
                Player player = event.getPlayer();
                String rpgName = rpgUtils.getRPGNameInHand(player);
                if (rpgName.equals("Bell Kiamat")) {
                    for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                        if (!entity.isDead()) {
                            rotateEntity(entity);
                        }
                    }
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.1f, 0F);
                }
            }
        }
    }

    private void rotateEntity(Entity entity) {
        new BukkitRunnable() {
            double angle = 0;
            final double radiansPerTick = Math.PI / 8; // Adjust the speed of rotation

            @Override
            public void run() {
                angle += radiansPerTick;
                entity.setRotation((float) (entity.getLocation().getYaw() + angle), entity.getLocation().getPitch());
                entity.getLocation().getWorld().playSound(entity, Sound.BLOCK_ANVIL_PLACE, 0.1f, 0f);
                if (angle >= 45 * Math.PI) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1); // Adjust the delay and period as needed
    }
}
