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
    HashMap<String, String> hMapTitle = new HashMap<>();
    HashMap<String, String> hMapSound = new HashMap<>();
    HashMap<String, String> hMapSoundEveryone = new HashMap<>();

    public ManagerConfig(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        //if (getEcoUse())
        //    main.ecoTest2();
        // initialize HashMap
        getterTitle();
        getterSound();
        getterSoundEv();
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
        ConfigurationSection getSection = get().getConfigurationSection("Title");
        for (String translation : getSection.getKeys(false)) {
            hMapTitle.put(translation, getSection.getString(translation));
        }
    }

    public HashMap<String, String> hMapTitle() {
        return this.hMapTitle;
    }

    public void getterSound() {
        ConfigurationSection getSection = get().getConfigurationSection("Sound");
        for (String translation : getSection.getKeys(false)) {
            hMapSound.put(translation, getSection.getString(translation));
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
        return LanguageLoader.getTranslation(path);
    }

    public boolean getErrorAction() {
        return get().getBoolean("errorAction");
    }

    public boolean getPosSymbol() {
        String posString = get().getString("posSymbol");
        return !posString.equalsIgnoreCase("before");
    }

    public boolean getMYourSelf() {
        return get().getBoolean("mentionYourSelf");
    }

    // Getting additional additions
    public boolean getActiveTitle() {
        return get().getBoolean("activeTitle");
    }

    public boolean getActiveActionBar() {
        return get().getBoolean("activeActionBar");
    }

    public boolean getActiveSound() {
        return get().getBoolean("activeSound");
    }

    // Getting everyone
    public String getFormatEveryone() {
        return get().getString("formattingEv");
    }

    public void getterSoundEv() {
        ConfigurationSection getSection = get().getConfigurationSection("SoundEv");
        for (String translation : getSection.getKeys(false)) {
            hMapSoundEveryone.put(translation, getSection.getString(translation));
        }
    }

    public HashMap<String, String> hMapSoundEv() {
        return this.hMapSoundEveryone;
    }

    // Getting Economy
    public boolean getEcoUse() {
        return get().getBoolean("EcoUse");
    }

    public double getEcoPrice() {
        return get().getDouble("ecoPrice");
    }

    public String getEcoSymbol() {
        return get().getString("ecoSymbol");
    }
}
