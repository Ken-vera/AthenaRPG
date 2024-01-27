package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FixQueue implements Listener {
    private Main plugin;

    public FixQueue(Main plugin) {
        this.plugin = plugin;
    }

}
