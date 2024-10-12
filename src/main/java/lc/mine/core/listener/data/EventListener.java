package lc.mine.core.listener.data;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface EventListener<T extends Event> extends Listener {
    void handle(final T event);
}
