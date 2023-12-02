package fr.redboard.notifierplayer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fr.redboard.notifierplayer.commands.ManagerCmd;
import fr.redboard.notifierplayer.commands.TabCompletor;
import fr.redboard.notifierplayer.listeners.CheckChat;
import fr.redboard.notifierplayer.utils.LanguageLoader;
import fr.redboard.notifierplayer.utils.ManagerConfig;
import fr.redboard.notifierplayer.utils.ManagerPlayerList;

public class NotifierPlayer extends JavaPlugin {

    public static NotifierPlayer instance;

    public File configPlayerFile;
    private FileConfiguration configPlayer;

    private final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        try {
            createPlayerYml();
            saveDefaultConfig();

            ManagerConfig managerConfig = new ManagerConfig(this, this);
            ManagerPlayerList managerPlayerlist = new ManagerPlayerList(this);

            new LanguageLoader(this);

            getCommand("notifier").setExecutor(new ManagerCmd(managerConfig, managerPlayerlist));
            getCommand("notifier").setTabCompleter(new TabCompletor());
            Bukkit.getPluginManager().registerEvents(new CheckChat(managerConfig, managerPlayerlist, this), this);
            instance = this;

            // Metrics bStats
            int pluginId = 11283;
            @SuppressWarnings("unused")
            MetricsLite metricsLite = new MetricsLite(this, pluginId);
            //ecoTest();
            //if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            //    new PlaceHolder(managerPlayerlist).register();
            //}
        } catch (Error e) {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.configPlayer;
    }

    private void createPlayerYml() {
        configPlayerFile = new File(getDataFolder(), "PlayerList.yml");
        if (!configPlayerFile.exists()) {
            if (configPlayerFile.getParentFile().mkdirs()) {
                saveResource("PlayerList.yml", false);
            }
        }
        configPlayer= new YamlConfiguration();
        try {
            configPlayer.load(configPlayerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
