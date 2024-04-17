package lunatic.athenarpg.itemlistener.limited;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class EldritchChronomancy implements Listener {
    private final Main plugin;
    private final RPGUtils rpgUtils;
    StatusListener statusListener;
    ItemConstructor itemConstructor;

    public EldritchChronomancy(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.statusListener = new StatusListener(plugin);
        this.itemConstructor = new ItemConstructor();
    }

    @EventHandler
    public void shootBalloon(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getItem() != null && rpgUtils.getRPGNameInHand(player).equals("Eldritch Chronomancy")) {
            if (e.getHand().equals(EquipmentSlot.HAND) && e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                if (statusListener.haveEnoughMana(player, itemConstructor.getMainHandManaCost(player))) {


                    Vector dir = player.getEyeLocation().getDirection().multiply(2);
                    ArmorStand balloon = player.getWorld().spawn(player.getLocation().add(0.0D, -0.1D, 0.0D), ArmorStand.class);
                    balloon.setHelmet(new org.bukkit.inventory.ItemStack(Material.CREEPER_HEAD));
                    balloon.setGravity(false);
                    balloon.setInvulnerable(true);
                    balloon.setVisible(false);
                    balloon.setMarker(true);
                    forwardOnly(balloon, dir, player);
                    new BukkitRunnable() {
                        public void run() {
                            balloon.remove();
                        }
                    }.runTaskLater(plugin, 15L);

                    statusListener.consumeMana(player, itemConstructor.getMainHandManaCost(player));

                }
            }
        }
    }

    private void forwardOnly(final ArmorStand balloon, final Vector dir, final Player p) {
        new BukkitRunnable() {
            public void run() {
                if (!balloon.isDead()) {
                    EulerAngle getPose = balloon.getHeadPose();
                    balloon.setHeadPose(getPose.add(0.0D, 0.2D, 0.0D));

                    if (balloon.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                        balloon.remove();

                        // Calculate the rotated direction for the firework
                        Vector rotatedDir = dir.clone().rotateAroundY(Math.toRadians(90)); // Adjust the rotation angle as needed

                        Firework fx = (Firework) balloon.getLocation().getWorld().spawn(balloon.getLocation().add(rotatedDir).add(0, 4, 0), Firework.class);
                        FireworkMeta fm = fx.getFireworkMeta();

                        fx.setMetadata("nodamage", new org.bukkit.metadata.FixedMetadataValue(plugin, true));
                        randomCreeperColor(fm);
                        fx.setSilent(true);
                        fm.setPower(1);
                        fx.setFireworkMeta(fm);

                        for (Entity ent : balloon.getNearbyEntities(3,3,3)){
                            if (ent instanceof LivingEntity){
                                LivingEntity enemy = (LivingEntity) ent;
                                enemy.damage(15, p);
                            }
                        }

                        fx.detonate();

                        if (balloon.isDead()) {
                            this.cancel();
                        }
                    } else {
                        balloon.teleport(balloon.getLocation().add(dir)); // Teleport the balloon forward
                    }
                }
                else{
                    balloon.remove();

                    // Calculate the rotated direction for the firework
                    Vector rotatedDir = dir.clone().rotateAroundY(Math.toRadians(90)); // Adjust the rotation angle as needed

                    Firework fx = (Firework) balloon.getLocation().getWorld().spawn(balloon.getLocation().add(rotatedDir).add(0, 4, 0), Firework.class);
                    FireworkMeta fm = fx.getFireworkMeta();

                    fx.setMetadata("nodamage", new org.bukkit.metadata.FixedMetadataValue(plugin, true));
                    randomCreeperColor(fm);
                    fx.setSilent(true);
                    fm.setPower(1);
                    fx.setFireworkMeta(fm);

                    for (Entity ent : balloon.getNearbyEntities(3,3,3)){
                        if (ent instanceof LivingEntity){
                            LivingEntity enemy = (LivingEntity) ent;
                            enemy.damage(15, p);
                        }
                    }

                    fx.detonate();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private void randomCreeperColor(FireworkMeta fm) {
        fm.clearEffects();
        fm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .trail(true)
                .withColor(Color.LIME)
                .withColor(Color.BLACK)
                .with(FireworkEffect.Type.CREEPER)
                .build());
    }
}
