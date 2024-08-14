package lc.mine.core.hook.placeholderapi;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public final class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Database database;

    public PlaceholderAPIHook(Database database) {
        this.database = database;
    }

    @Override
    public @NotNull String getAuthor() {
        return "MineLC";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lc";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String key) {
        PlayerData data;
        switch (key) {
            case "lcoins":
                data = database.getCached(player.getUniqueId());
                return (data == null) ? "0" : String.valueOf(data.getLcoins());
            case "vippoints":
                data = database.getCached(player.getUniqueId());
                return (data == null) ? "0" : String.valueOf(data.getVipPoins());
            case "int_lcoins":
                data = database.getCached(player.getUniqueId());
                return (data == null) ? "0" : String.valueOf((int)data.getLcoins());
            case "int_vippoints":
                data = database.getCached(player.getUniqueId());
                return (data == null) ? "0" : String.valueOf((int)data.getVipPoins());
            default:
                return null;
        }
    }
}