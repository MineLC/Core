package lc.mine.core.database;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface Database {
    
    PlayerData getCached(final UUID uuid);
    void save(final Player player);
    PlayerData load(final UUID uuid, final String name);
    void create(final Player player, final PlayerData data);
    void close();
    void deleteFromCache(final Player player);
}
