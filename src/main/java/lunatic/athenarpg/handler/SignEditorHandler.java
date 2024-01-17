package lunatic.athenarpg.handler;

import lunatic.athenarpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSignOpenEvent;

import java.util.Arrays;
import java.util.List;

public class SignEditorHandler implements Listener {
    private Main plugin;

    // List of keywords to check for in signLines
    private List<String> signKeywords = Arrays.asList("[RP]", "[PRIVATE]", "[LOCK]", "[SHOP]");

    public SignEditorHandler(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSignTryChange(PlayerSignOpenEvent event){
        String signLines = Arrays.toString(event.getSign().getLines());

        // Check if any of the keywords are present in signLines (case-insensitive)
        for (String keyword : signKeywords) {
            if (signLines.toLowerCase().contains(keyword.toLowerCase())) {
               event.setCancelled(true);
            }
        }
    }
}
