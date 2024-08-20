package lc.mine.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import lc.mine.core.CorePlugin;
import lc.mine.core.database.Database;
import lc.mine.core.listener.data.EventListener;

public class PlayerJoinListener implements EventListener<AsyncPlayerPreLoginEvent> {

    private final Database database;
    private final CorePlugin plugin;

    public PlayerJoinListener(CorePlugin plugin, Database database) {
        this.database = database;
        this.plugin = plugin;
    }

    public void handle(final AsyncPlayerPreLoginEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> database.load(event.getUniqueId(), event.getName()), 20);
    }    
}