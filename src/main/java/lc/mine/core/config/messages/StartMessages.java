package lc.mine.core.config.messages;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lc.mine.core.config.Config;
import lc.mine.core.config.ConfigManager;

public final class StartMessages {

    public void start(final ConfigManager manager) {
        manager.createIfAbsent(null, "messages");
        final Config messages = manager.of(null, "messages");

        final Set<Entry<String, Object>> entries = messages.map().entrySet();
        final Map<String, String> parsedMessages = new HashMap<>(entries.size());
        addEntries(parsedMessages, entries, "");
        Messages.set(new Messages(parsedMessages));
    }

    @SuppressWarnings("unchecked")
    private void addEntries(final Map<String, String> out, final Set<Entry<String, Object>> entries, final String key) {
        for (final Entry<String, Object> entry : entries) {
            if (!(entry instanceof Map map)) {
                out.put(key + entry.getKey(), Color.toString(entry.getValue()));
                continue;
            }
            addEntries(out, ((Map<String, Object>)map).entrySet(), key + '.');
        }
    }
}
