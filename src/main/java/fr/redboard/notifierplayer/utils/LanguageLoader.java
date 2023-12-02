package fr.redboard.notifierplayer.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.redboard.notifierplayer.NotifierPlayer;

import static org.bukkit.Bukkit.getServer;

public class LanguageLoader {
    private static final HashMap<String, String> translationMap = new HashMap<>();
    private static File languageDirectory;

    public LanguageLoader(NotifierPlayer plugin) throws Error {
        String directoryName = "languages/";
        languageDirectory = getLanguageDirectory(plugin, directoryName);

        loadLanguageData(plugin);
    }

    public static void reload() {
        loadLanguageData(NotifierPlayer.instance);
    }

    public static String getTranslation(String key) {
        if (!translationMap.containsKey(key)) {
            return "";
        }
        return translationMap.get(key);
    }

    private static File getLanguageDirectory(NotifierPlayer plugin, String directoryName) {
        File languageDirectory = new File(plugin.getDataFolder(), directoryName);

        if (!languageDirectory.exists() || !languageDirectory.isDirectory()) {
            throw new Error(directoryName + " do not exist or can not be found in plugins/");
        }
        return languageDirectory;
    }

    private static FileConfiguration loadLanguageFile(NotifierPlayer plugin) {
        String locale;
        if (plugin.getConfig().getString("locale") == null) {
            locale = "en_US";
        } else {
            locale = plugin.getConfig().getString("locale");
        }
        locale = locale + ".yml";

        File localeFile = new File(languageDirectory, locale);
        if (!localeFile.exists() || !localeFile.isFile()) {
            throw new Error("The language file " + locale + " does not exist in the language directory.");
        }

        FileConfiguration translations;
        try {
            translations = YamlConfiguration.loadConfiguration(localeFile);
        } catch (Exception e) {
            throw new Error("Error while loading the language file " + locale + " in the language directory.");
        }
        return translations;
    }

    private static void loadLanguageData(NotifierPlayer plugin) {
        String directoryName = "languages/";
        languageDirectory = getLanguageDirectory(plugin, directoryName);

        translationMap.clear();
        try {
            FileConfiguration translations = loadLanguageFile(plugin);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        } catch (Exception e) {
            throw new Error("Error while loading the language file in the language directory.");
        }
    }
}
