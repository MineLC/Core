package lc.mine.core.listener;

import org.bukkit.event.player.PlayerQuitEvent;

import lc.mine.core.database.Database;
import lc.mine.core.listener.data.EventListener;

public final class PlayerQuitListener implements EventListener<PlayerQuitEvent> {

    private final Database database;

    public PlayerQuitListener(Database database) {
        this.database = database;
    }

    @Override
    public void handle(PlayerQuitEvent event) {
        database.save(event.getPlayer());
    }
}
