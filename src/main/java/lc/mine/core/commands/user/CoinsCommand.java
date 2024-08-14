package lc.mine.core.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lc.mine.core.config.messages.Messages;
import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;

public final class CoinsCommand implements CommandExecutor {

    private final Database database;

    public CoinsCommand(Database database) {
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Use: /ladmin");
            return true;
        }
        PlayerData data = database.getCached(player.getUniqueId());
        if (data == null) {
            data = new PlayerData.New(player.getName());
        }
        sender.sendMessage(
            Messages.get("coins")
            .replace("%lcoins%", String.valueOf(data.getLcoins()))
            .replace("%vipPoints%", String.valueOf(data.getVipPoins())));

        return true;
    }
}
