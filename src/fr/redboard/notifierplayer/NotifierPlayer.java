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
import net.milkbowl.vault.economy.Economy;

public class NotifierPlayer extends JavaPlugin {

	private ManagerConfig managerConfig;
	public static NotifierPlayer instance;
	
    public File configPlayerFile;
    private FileConfiguration configPlayer;

	private final Logger log = Logger.getLogger("Minecraft");
	public Economy eco = null;
	private ManagerPlayerList managerPlayerlist;

	@Override
	public void onEnable() {
		createPlayerYml();

		saveDefaultConfig();
		managerConfig = new ManagerConfig(this, this);
		managerPlayerlist = new ManagerPlayerList(this);
		new LanguageLoader(this);
		getCommand("notifier").setExecutor(new ManagerCmd(managerConfig, managerPlayerlist));
		getCommand("notifier").setTabCompleter(new TabCompletor());
		Bukkit.getPluginManager().registerEvents(new CheckChat(managerConfig, managerPlayerlist, this), this);
		instance = this;

		// Metrics bStats
		int pluginId = 11283;
		@SuppressWarnings("unused")
		MetricsLite metricsLite = new MetricsLite(this, pluginId);
		ecoTest();
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			 new PlaceHolder(managerPlayerlist).register();
		}

	}

	public void ecoTest2() {
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	public void ecoTest() {
		if (managerConfig.getEcoUse()) {
			if (!setupEconomy()) {
				log.severe(
						String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		eco = rsp.getProvider();
		return eco != null;
	}
	
	public FileConfiguration getCustomConfig() {
        return this.configPlayer;
    }

    private void createPlayerYml() {
    	configPlayerFile = new File(getDataFolder(), "PlayerList.yml");
        if (!configPlayerFile.exists()) {
        	configPlayerFile.getParentFile().mkdirs();
            saveResource("PlayerList.yml", false);
         }
        configPlayer= new YamlConfiguration();
        try {
        	configPlayer.load(configPlayerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
