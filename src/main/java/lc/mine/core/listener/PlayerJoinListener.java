package lc.mine.core.listener;

import java.util.concurrent.CompletableFuture;

import org.bukkit.event.player.PlayerJoinEvent;

import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;
import lc.mine.core.listener.data.EventListener;

public class PlayerJoinListener implements EventListener<PlayerJoinEvent> {

    private final Database database;

    public PlayerJoinListener(Database database) {
        this.database = database;
    }

    public void handle(final PlayerJoinEvent event) {
        final PlayerData data = database.getCached(event.getPlayer().getUniqueId());
        if (data == null ||
            (data.getLcoins() == data.getOldLcoins() && data.getVipPoins() == data.getOldVipPoints())) {
            return;
        }
        CompletableFuture.runAsync(() -> database.load(event.getPlayer()));
    }    
}