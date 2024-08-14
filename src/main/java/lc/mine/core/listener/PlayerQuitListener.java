package lc.mine.core.listener;

import java.util.concurrent.CompletableFuture;

import org.bukkit.event.player.PlayerQuitEvent;

import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;
import lc.mine.core.listener.data.EventListener;

public final class PlayerQuitListener implements EventListener<PlayerQuitEvent> {

    private final Database database;

    public PlayerQuitListener(Database database) {
        this.database = database;
    }

    @Override
    public void handle(PlayerQuitEvent event) {
        final PlayerData data = database.getCached(event.getPlayer().getUniqueId());
        if (data == null ||
            (data.getLcoins() == data.getOldLcoins() && data.getVipPoins() == data.getOldVipPoints())) {
            return;
        }
        CompletableFuture.runAsync(() -> database.save(event.getPlayer()));
    }
    
}
