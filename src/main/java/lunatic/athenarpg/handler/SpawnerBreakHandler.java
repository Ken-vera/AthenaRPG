package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnerBreakHandler implements Listener {

    private Main plugin;

    public SpawnerBreakHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();

        // Check if the placed block is a spawner
        if (block.getType() == Material.SPAWNER) {
            // Set metadata to indicate it was placed by a player
            if (!event.getPlayer().hasPermission("athena.bypass.spawner")) {
                block.setMetadata("placedbyplayer", new FixedMetadataValue(plugin, true));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!event.getPlayer().hasPermission("athena.bypass.spawner")) {
            if (block.getType() == Material.SPAWNER) {
                if (!block.hasMetadata("placedbyplayer")) {
                    int currentCount = plugin.fileManager.getConfig("spawnerData.yml").get().getInt(player.getName() + ".spawner", 0);
                    plugin.fileManager.getConfig("spawnerData.yml").set(player.getName() + ".spawner", currentCount + 1);
                    plugin.fileManager.saveConfig("spawnerData.yml");

                    for (Player staff : Bukkit.getOnlinePlayers()) {
                        if (staff.hasPermission("essentials.jail")) {
                            staff.sendMessage("§c[STAFF] §e" + player.getName() + " §fBaru saja menghancurkan spawner! §7(§e" + (currentCount + 1) + " Spawner Total§7)");
                        }
                    }

                }
            }
        }
    }
}
