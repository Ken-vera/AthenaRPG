package lunatic.athenarpg.itemlistener.limited;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class RamadhanSet implements Listener {
    private Main plugin;
    RPGUtils rpgUtils;
    CooldownManager cooldownManager;

    public RamadhanSet(Main plugin){
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.cooldownManager = new CooldownManager();
    }
    @EventHandler
    public void Peci(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager() instanceof LivingEntity) {
            Player player = (Player) event.getEntity();
            String rpgName = rpgUtils.getHelmetRPGName(player);
            Entity attacker = event.getDamager();

            if (rpgName.equals("Peci")) {
                if (!cooldownManager.isOnCooldown(player.getName(), "Peci")) {
                    LivingEntity entity = (LivingEntity) attacker;
                    entity.damage(event.getDamage() * 0.5D);
                    entity.sendMessage("§b" + player.getName() + " reflected " + event.getDamage() * 0.5 + " damage from Holy Burst!");
                    player.sendMessage("§bYou reflected " + event.getDamage() * 0.5 + " damage to " + entity.getName());
                    player.playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10.0F, 1.4F);
                    cooldownManager.setCooldown(player.getName(), "Peci", 1);
                }

            }
        }
    }
    @EventHandler
    public void BajuKoko(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager() instanceof LivingEntity) {
            Entity attacker = event.getDamager();
            Player player = (Player) event.getEntity();
            String rpgName = rpgUtils.getChestplateRPGName(player);
            if (rpgName.equals("Baju Koko")){
                double randDouble = Math.random();
                if (randDouble <= 0.3D) {
                    LivingEntity ent = (LivingEntity) attacker;
                    ent.damage(event.getDamage() * 2D, player);
                    ent.sendMessage("§b" + player.getName() + " reflected " + event.getDamage() * 2D + " damage from Holy Destruction!");
                    player.sendMessage("§bYou reflected " + event.getDamage() * 2D + " damage to " + ent.getName());
                    player.playSound(ent.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10.0F, 1.4F);
                }
            }
        }
    }
    @EventHandler
    public void sandalSneakEvent(PlayerToggleSneakEvent event){
        Player p = event.getPlayer();
        String rpgName = rpgUtils.getBootsRPGName(p);
        if (!p.isSneaking() && rpgName.equals("Sandal")) {
            if (!cooldownManager.isOnCooldown(p.getName(), "Sandal")) {
                Vector vec = p.getLocation().getDirection().multiply(6);
                vec.setY(1.5D);
                p.setVelocity(vec);
                cooldownManager.setCooldown(p.getName(), "Sandal", 7);
            }else{
                cooldownManager.sendCooldownMessage(p, "Sandal");
            }
        }
    }
    @EventHandler
    public void Sarung(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager() instanceof LivingEntity) {
            Entity attacker = event.getDamager();
            Player player = (Player) event.getEntity();
            String rpgName = rpgUtils.getLeggingsRPGName(player);
            if (rpgName.equals("Sarung")){
                double randDouble = Math.random();
                if (randDouble <= 0.2D) {
                    LivingEntity ent = (LivingEntity) attacker;
                    player.sendMessage("§bYou deflected " + event.getDamage()+ " from " + ent.getName());
                    player.playSound(ent.getLocation(), Sound.BLOCK_ANVIL_PLACE, 10.0F, 1.4F);
                    event.setDamage(0);
                }
            }
        }
    }
}
