package lc.mine.core.listener;

import java.lang.ref.WeakReference;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import lc.mine.core.CorePlugin;
import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;
import lc.mine.core.event.PlayerDataLoadEvent;
import lc.mine.core.listener.data.EventListener;

public class PlayerJoinListener implements EventListener<PlayerJoinEvent> {

    private final Database database;
    private final CorePlugin plugin;

    public PlayerJoinListener(CorePlugin plugin, Database database) {
        this.database = database;
        this.plugin = plugin;
    }

    public void handle(final PlayerJoinEvent event) {
        final WeakReference<Player> reference = new WeakReference<Player>(event.getPlayer());
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getExecutorService().execute(
            () -> {
                final Player player = reference.get();
                if (player == null || !player.isOnline()) {
                    return;
                }
                final PlayerData data = database.load(player.getUniqueId(), player.getName());
                plugin.getServer().getPluginManager().callEvent(new PlayerDataLoadEvent(data, player));
            }
        ), 20);
    }
}