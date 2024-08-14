package lc.mine.core.listener;

import java.util.concurrent.CompletableFuture;

import org.bukkit.event.player.PlayerJoinEvent;

import lc.mine.core.database.Database;
import lc.mine.core.listener.data.EventListener;

public class PlayerJoinListener implements EventListener<PlayerJoinEvent> {

    private final Database database;

    public PlayerJoinListener(Database database) {
        this.database = database;
    }

    public void handle(final PlayerJoinEvent event) {
        CompletableFuture.runAsync(() -> database.load(event.getPlayer()));
    }    
}