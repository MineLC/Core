package lc.mine.core;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import lc.mine.core.commands.admin.AdminCoreCommand;
import lc.mine.core.commands.user.CoinsCommand;
import lc.mine.core.config.ConfigManager;
import lc.mine.core.config.messages.StartMessages;
import lc.mine.core.database.Database;
import lc.mine.core.database.mongodb.StartMongodb;
import lc.mine.core.hook.placeholderapi.PlaceholderAPIHook;
import lc.mine.core.hook.vault.VaultHook;
import lc.mine.core.listener.PlayerJoinListener;
import lc.mine.core.listener.PlayerQuitListener;
import lc.mine.core.listener.data.ListenerRegister;

public final class CorePlugin extends JavaPlugin {
    
    private Database database;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        database = new StartMongodb().start(getLogger(), getConfig());
        if (database == null) {
            return;
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(database).register();
            getLogger().info("PlaceholderAPI hook!");
        }
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            new VaultHook(database).register(this);
            getLogger().info("Vault hook!");
        }

        new StartMessages().start(new ConfigManager(new Yaml(), this));
        new ListenerRegister(this).register(
            new PlayerJoinListener(database),
            new PlayerQuitListener(database)
        );
        getCommand("lcadmin").setExecutor(new AdminCoreCommand(database));
        getCommand("lcoins").setExecutor(new CoinsCommand(database));
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.close();
        }
    }

    public void load() {
        try {
            final ConfigManager configManager = new ConfigManager(new Yaml(), this);
            new StartMessages().start(configManager);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error reloading the plugin. Fix the error and execute again /lcadmin reload", e);
        }
    }
}