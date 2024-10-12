package lc.mine.core.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lc.mine.core.database.PlayerData;

public final class PlayerDataLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerData data;
    private final Player player;

    public PlayerDataLoadEvent(PlayerData data, Player player) {
        this.data = data;
        this.player = player;
    }

    public PlayerData getData() {
        return data;
    }
    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
