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

    public static HashMap<String, String> translationMap = new HashMap<>();

    public LanguageLoader(NotifierPlayer plugin) throws Error { // plutot load chaque fichier yml trouvé dans languages/
        File languageDirectory = new File(plugin.getDataFolder(), "languages/");
        File test = new File(plugin.getDataFolder(), "testt/");
        File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
        File File_fr_FR = new File(plugin.getDataFolder(), "languages/fr_FR.yml");
        File File_de_DE = new File(plugin.getDataFolder(), "languages/de_DE.yml");

        if (!test.exists() || !test.isDirectory()) {
            throw new Error("testt/ n'existe pas ou n'est pas un répertoire.");
        }

        if (!languageDirectory.isDirectory()) {
            languageDirectory.mkdir();

            if (!defaultLanguageFile.exists() || !File_fr_FR.exists() || !File_de_DE.exists()) {
                defaultLanguageFile.getParentFile().mkdirs();
                plugin.saveResource("languages/en_US.yml", false);
                plugin.saveResource("languages/fr_FR.yml", false);
                plugin.saveResource("languages/de_DE.yml", false);
            }
        }

        if (plugin.getConfig().getString("locale") != null && !plugin.getConfig().getString("locale").equals("en_US")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(
                    new File(plugin.getDataFolder(), "languages/" + plugin.getConfig().getString("locale") + ".yml"));
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        } else {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(defaultLanguageFile);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        }
    }
}
