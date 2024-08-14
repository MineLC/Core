package lc.mine.core.commands.admin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import lc.mine.core.config.messages.Messages;
import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;
import lc.mine.core.utils.IntegerUtils;

public final class AdminCoreCommand implements TabExecutor {

    private final Database database;

    public AdminCoreCommand(Database database) {
        this.database = database;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("set", "add", "remove", "multiply");
        }
        if (args.length == 2) {
            return List.of("1", "2");
        }
        if (args.length == 3) {
            return List.of("lcoins", "vipPoints");
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {      
        if (!(sender.hasPermission("lcadmin.use"))){ 
            Messages.send(sender, "admin-no-permission");
            return true;
        }
        if (args.length < 4) {
            sender.sendMessage(format());
            return true;
        }
        final int amount = IntegerUtils.parsePositive(args[1], -1);
        if (amount == -1) {
            sender.sendMessage("§cOnly positive numbers in amount. Example: §c6/lcadmin add 320 lcoins pepe");
            return true;
        }

        final Player player = Bukkit.getPlayer(args[3]);
        if (player == null) {
            sender.sendMessage("§cThe player " + args[3] + " don't exist");
            return true;
        }

        PlayerData data = database.getCached(player.getUniqueId());
        if (data == null) {
            data = new PlayerData.New(player.getName());
            database.create(player, data);
        }

        final Action action = switch (args[0].toLowerCase()) {
            case "set" -> (in) -> amount;
            case "add" -> (in) -> amount + in;
            case "remove" -> (in) -> (in - amount) < 0 ? 0 : in - amount;
            case "multiply" -> (in) -> in * amount;
            case "divide" -> (in) -> (in == 0) ? 0 : in / amount;
            default -> null;
        };

        if (action == null) {
            sender.sendMessage("§cActions available: §ce(set/add/remove/multiply/divide) ");
            return true;
        }

        switch (args[2].toLowerCase()) {
            case "lcoins":
                sender.sendMessage("§cOld amount: §6" + data.getLcoins());
                data.setLcoins(action.handle(data.getLcoins()));
                sender.sendMessage("§aNew amount: §6" + data.getLcoins());
                break;
            case "vippoints":
                sender.sendMessage("§cOld amount: §6" + data.getVipPoins());
                data.setVipPoins(action.handle(data.getVipPoins()));
                sender.sendMessage("§aNew amount: §6" + data.getVipPoins());
                break;
            default:
                sender.sendMessage("§cYou only can §6change§c lcoins and §6vippoints");
                break;
        }

        return true;
    }
    
    private String format() {
        return
        """
            
            §b§lMineLC §8- §6§lCore

            §f/lcadmin §e(set/add/remove/multiply/divide) §6(amount) §c(lcoins/vippoints) §d(player)
            
        """;
    }

    private static interface Action {
        double handle(double input);
    }
}
