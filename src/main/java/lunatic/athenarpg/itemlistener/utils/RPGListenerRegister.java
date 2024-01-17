package lunatic.athenarpg.itemlistener.utils;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.farming.HarvesterHoe;
import lunatic.athenarpg.itemlistener.common.ObsidianChestplate;
import lunatic.athenarpg.itemlistener.common.Taser;
import lunatic.athenarpg.itemlistener.dungeon.EldoriaSet;
import lunatic.athenarpg.itemlistener.dungeon.PhantasmalSet;
import lunatic.athenarpg.itemlistener.dungeon.SerpentineSceptre;
import lunatic.athenarpg.itemlistener.epic.Crusader;
import lunatic.athenarpg.itemlistener.epic.ElementalistBlade;
import lunatic.athenarpg.itemlistener.legendary.PharaohArmor;
import lunatic.athenarpg.itemlistener.legendary.SwiftnessBoots;
import lunatic.athenarpg.itemlistener.legendary.WitherMask;
import lunatic.athenarpg.itemlistener.rare.AncestralBook;
import lunatic.athenarpg.itemlistener.rare.PoseidonTrident;
import lunatic.athenarpg.itemlistener.rare.VindicatorCrossbow;
import lunatic.athenarpg.stats.StatusListener;

public class RPGListenerRegister {

    private Main plugin;

    public RPGListenerRegister(Main plugin){
        this.plugin = plugin;
    }
    public void registerRPGEventListener(){
        // COMMON
        plugin.getServer().getPluginManager().registerEvents(new Taser(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ObsidianChestplate(plugin), plugin);
        // RARE
        plugin.getServer().getPluginManager().registerEvents(new PoseidonTrident(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new AncestralBook(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VindicatorCrossbow(plugin), plugin);
        // EPIC
        plugin.getServer().getPluginManager().registerEvents(new Crusader(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ElementalistBlade(plugin), plugin);
        // LEGENDARY
        plugin.getServer().getPluginManager().registerEvents(new SwiftnessBoots(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PharaohArmor(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new WitherMask(plugin), plugin);
        // DUNGEON
        plugin.getServer().getPluginManager().registerEvents(new EldoriaSet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PhantasmalSet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SerpentineSceptre(plugin, new StatusListener(plugin)), plugin);
        // LIMITED

        // FARMING
        plugin.getServer().getPluginManager().registerEvents(new HarvesterHoe(plugin), plugin);
    }
}
