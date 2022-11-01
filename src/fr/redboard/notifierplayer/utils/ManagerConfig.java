package fr.redboard.notifierplayer.utils;

import java.util.HashMap;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import fr.redboard.notifierplayer.NotifierPlayer;

public class ManagerConfig {

	private final Plugin plugin;
	private FileConfiguration config;
	private HashMap<String, String> translateGet;

	HashMap<String, String> hMapTitle = new HashMap<>();
	HashMap<String, String> hMapSound = new HashMap<>();
	private NotifierPlayer main;
	HashMap<String, String> hMapSoundEveryone = new HashMap<>();

	public ManagerConfig(Plugin plugin, NotifierPlayer notifierPlayer) {
		this.plugin = plugin;
		this.main = notifierPlayer;
		reload();
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig();
		if (getEcoUse())
			main.ecoTest2();

		// initialize HashMap
		getterTitle();
		getterSound();
		getterSoundEv();
		translateGet = LanguageLoader.translationMap;
	}

	public void save() {
		plugin.saveConfig();
	}

	public <T> T get(Function<MemoryConfiguration, T> call) {
		return call.apply(config);
	}

	public MemoryConfiguration get() {
		return config;
	}

	// WrapperConfig

	public String getFormatMention() {
		return get().getString("formatting");
	}

	public String getActionBar() {
		return get().getString("ActionBar");
	}

	public void getterTitle() {
		ConfigurationSection getsection = get().getConfigurationSection("Title");
		for (String translation : getsection.getKeys(false)) {
			hMapTitle.put(translation, getsection.getString(translation));
		}
	}

	public HashMap<String, String> hMapTitle() {
		return this.hMapTitle;
	}

	public void getterSound() {
		ConfigurationSection getsection = get().getConfigurationSection("Sound");
		for (String translation : getsection.getKeys(false)) {
			hMapSound.put(translation, getsection.getString(translation));
		}
	}

	public HashMap<String, String> hMapSound() {
		return this.hMapSound;
	}

	public String getSymbol() {
		return get().getString("symbol");
	}

	public int getDelay() {
		return get().getInt("Delay");
	}

	public String getNamePlugin() {
		return get().getString("namePlugin");
	}

	public boolean getNickname() {
		return get().getBoolean("nickname");
	}

	// getter Language File

	public String getLanguagePath(String path) {
		return translateGet.get(path);
	}

	public boolean getErrorAction() {
		return get().getBoolean("errorAction");
	}

	public boolean getPosSymbol() {
		String posString = get().getString("posSymbol");
		if (posString.equalsIgnoreCase("before"))
			return false;
		else
			return true;
	}

	public boolean getMYourSelf() {
		return get().getBoolean("mentionYourSelf");
	}

	// Getting additional additions

	public boolean getActivTitle() {
		return get().getBoolean("activTitle");
	}

	public boolean getActivActionBar() {
		return get().getBoolean("activActionBar");
	}

	public boolean getActivSound() {
		return get().getBoolean("activSound");
	}

	// Getting everyone

	public String getFormatEveryone() {
		return get().getString("formattingEv");
	}

	public void getterSoundEv() {
		ConfigurationSection getsection = get().getConfigurationSection("SoundEv");
		for (String translation : getsection.getKeys(false)) {
			hMapSoundEveryone.put(translation, getsection.getString(translation));
		}
	}

	public HashMap<String, String> hMapSoundEv() {
		return this.hMapSoundEveryone;
	}

	// Getting Economy
	public boolean getEcoUse() {
		if (get().getBoolean("EcoUse"))
			return true;
		else
			return false;
	}

	public double getEcoPrice() {
		return get().getDouble("ecoPrice");
	}

	public String getEcoSymbol() {
		return get().getString("ecoSymbol");
	}

}
