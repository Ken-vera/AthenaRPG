package lunatic.athenarpg.itemlistener.utils;

import lunatic.athenarpg.Main;
import lunatic.athenarpg.itemlistener.Taser;

public class RPGListenerRegister {

    private Main plugin;

    public RPGListenerRegister(Main plugin){
        this.plugin = plugin;
    }
    public void registerRPGEventListener(){
        plugin.getServer().getPluginManager().registerEvents(new Taser(plugin), plugin);
    }
}
