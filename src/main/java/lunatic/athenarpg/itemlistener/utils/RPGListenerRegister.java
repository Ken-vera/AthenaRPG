package lunatic.athenarpg.itemlistener.utils;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.admin.BellKiamat;
import lunatic.athenarpg.itemlistener.common.ObsidianChestplate;
import lunatic.athenarpg.itemlistener.common.Taser;
import lunatic.athenarpg.itemlistener.dungeon.*;
import lunatic.athenarpg.itemlistener.dungeon.pvp.DiamondSetListener;
import lunatic.athenarpg.itemlistener.epic.Crusader;
import lunatic.athenarpg.itemlistener.epic.ElementalistBlade;
import lunatic.athenarpg.itemlistener.legendary.*;
import lunatic.athenarpg.itemlistener.limited.*;
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
        plugin.getServer().getPluginManager().registerEvents(new DahliaFlower(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DevilDagger(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SpiritPowerOrb(plugin), plugin);

        // DUNGEON
        plugin.getServer().getPluginManager().registerEvents(new EldoriaSet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PhantasmalSet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SerpentineSceptre(plugin, new StatusListener(plugin)), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EssenceEater(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EldoriaHeart(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BelialXiphos(plugin), plugin);

        // LIMITED
        plugin.getServer().getPluginManager().registerEvents(new PhoenixFan(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DragonforgedSet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DragonQuiver(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BianzhongBell(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EldritchChronomancy(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new RamadhanSet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new StarvingOrb(plugin), plugin);

        // DISABLE DIAMOND ARMOR PVP DUNGEON
        plugin.getServer().getPluginManager().registerEvents(new DiamondSetListener(plugin), plugin);

        // ADMIN
        plugin.getServer().getPluginManager().registerEvents(new BellKiamat(plugin), plugin);
    }
}
