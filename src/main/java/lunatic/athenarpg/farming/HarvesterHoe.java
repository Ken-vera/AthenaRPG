package lunatic.athenarpg.farming;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.utils.CooldownManager;
import lunatic.athenarpg.itemlistener.utils.ItemConstructor;
import lunatic.athenarpg.itemlistener.utils.RPGUtils;
import lunatic.athenarpg.stats.StatusListener;

public class HarvesterHoe implements Listener {
    private Main plugin;
    private RPGUtils rpgUtils;
    private ItemConstructor itemConstructor;
    private CooldownManager cooldownManager;
    private StatusListener statusListener;

    public HarvesterHoe(Main plugin) {
        this.plugin = plugin;
        this.rpgUtils = new RPGUtils();
        this.itemConstructor = new ItemConstructor();
        this.cooldownManager = new CooldownManager(plugin);
        this.statusListener = new StatusListener(plugin);
    }

    // ...

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String rpgName = rpgUtils.getRPGNameInHand(player);
        int rpgLevel = rpgUtils.getRPGLevelInHand(player);

        if (rpgName.equals("Harvester Hoe")) {
            Block block = event.getBlock();
            BlockState blockState = block.getState();
            BlockData blockData = blockState.getBlockData();
            if (isSeed(block.getType())) {
                if (blockData instanceof org.bukkit.block.data.Ageable) {
                    // Calculate the chance of replanting based on the level
                    double chance = 0.05 + (rpgLevel * 0.05);

                    if (Math.random() < chance) {
                        event.setCancelled(true);

                        Ageable ageable = (Ageable) block.getBlockData();
                        ageable.setAge(0);
                        block.setBlockData(ageable);
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                        // Instead of setting to Material.AIR, set the block back to its original state

                    }
                }
            }
        }
        if (rpgName.equals("Advanced Harvester Hoe")) {
            Block block = event.getBlock();
            BlockState blockState = block.getState();
            BlockData blockData = blockState.getBlockData();
            if (isSeed(block.getType())) {
                if (blockData instanceof org.bukkit.block.data.Ageable) {
                    // Calculate the chance of replanting based on the level
                    double chance = 0.25 + (rpgLevel * 0.05);

                    if (Math.random() < chance) {
                        event.setCancelled(true);

                        Ageable ageable = (Ageable) block.getBlockData();
                        ageable.setAge(0);
                        block.setBlockData(ageable);
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));


                    }
                }
            }
        }
    }

// ...


    // Helper method to check if a block type is a seed
    private boolean isSeed(Material material) {
        // Add more seed types if needed
        return material == Material.WHEAT || material == Material.CARROTS || material == Material.POTATOES
                || material == Material.BEETROOTS;
    }
}
