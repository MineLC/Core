package lc.mine.core.config.messages;

import java.util.Map;

import org.bukkit.command.CommandSender;

public final class Messages {

    private static Messages instance;
    private final Map<String, String> messages;
    
    Messages(Map<String, String> messages) {
        this.messages = messages;
    }

    public static String get(final String key) {
        final String message = instance.messages.get(key);
        return (message == null) ? "" : message;
    }

    public static void send(final CommandSender sender, final String key) {
        final String message = get(key);
        if (message != null) {
            sender.sendMessage(message);
        }
    }

    public static void set(final Messages newInstance) {
        instance = newInstance;
    }
}
