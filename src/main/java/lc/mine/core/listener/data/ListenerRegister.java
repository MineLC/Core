package lc.mine.core.listener.data;

import java.lang.reflect.Method;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ListenerRegister {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final EventExecutor EXECUTOR = (listener, event) -> ((EventListener)listener).handle(event);
    private final JavaPlugin plugin;

    public ListenerRegister(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final EventListener<?>... listeners) {
        for (final EventListener<?> listener : listeners) {
            final Class<? extends Event> data = getData(listener);
            if (data == null) {
                plugin.getLogger().warning("Error on starting listener " + listener.getClass() + ". No method found with listener data annontation. Contact to the dev");
                continue;
            }
            plugin.getServer().getPluginManager().registerEvent(data, listener, EventPriority.HIGHEST, EXECUTOR, plugin);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Event> getData(final EventListener<?> listener) {
        final Method[] methods = listener.getClass().getMethods();
        for (final Method method : methods) {
            if (!(method.getName().equals("handle"))) {
                continue;
            }
            return (Class<? extends Event>) method.getParameterTypes()[0];
        }
        return null;
    }
}