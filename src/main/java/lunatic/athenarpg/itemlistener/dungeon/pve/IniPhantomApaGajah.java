package lunatic.athenarpg.itemlistener.dungeon.pve;

import lunatic.athenarpg.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class IniPhantomApaGajah implements Listener {
    private Main plugin;

    public IniPhantomApaGajah(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onPhantomSpawn(EntitySpawnEvent event){
        if (event.getEntity().getType() == EntityType.PHANTOM){
            Phantom phantom = (Phantom) event.getEntity();
            phantom.setSize(500);
        }
    }
}
