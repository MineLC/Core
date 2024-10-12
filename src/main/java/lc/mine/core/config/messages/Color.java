package lc.mine.core.config.messages;

import java.util.List;
import net.md_5.bungee.api.ChatColor;

public final class Color {

    public static String toString(final Object object) {
        if (object == null) {
            return null;
        }
        String message = object.toString();

        if (object instanceof List<?>) {
            final List<?> list = (List<?>)object;
            final StringBuilder builder = new StringBuilder();
            int size = list.size();
            int i = 0;
            for (final Object object2 : list) {
                builder.append(object2);
                if (++i == size) {
                    continue;
                }
                builder.append('\n');
            }
            message = builder.toString();
        }

        if (message.isEmpty() || message.isBlank()) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
