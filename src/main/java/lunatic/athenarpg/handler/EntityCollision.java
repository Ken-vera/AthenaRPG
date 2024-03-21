package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntityCollision implements Listener {
    private Main plugin;

    public EntityCollision(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void entityCollision(EntitySpawnEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Monster || entity instanceof MagmaCube) {
            LivingEntity ent = (LivingEntity) entity;
            if (isSpawnedFromSpawner(ent)) {
                ent.setCollidable(false);
            }
        }
    }
    @EventHandler
    public void spawnerSetMetadata(SpawnerSpawnEvent event){
        Entity spawnedEntity = event.getEntity();

        spawnedEntity.setMetadata("spawned_from_spawner", new FixedMetadataValue(plugin, true));
    }
    private boolean isSpawnedFromSpawner(LivingEntity entity) {
        return entity.hasMetadata("spawned_from_spawner") && entity.getMetadata("spawned_from_spawner").get(0).asBoolean();
    }
}
