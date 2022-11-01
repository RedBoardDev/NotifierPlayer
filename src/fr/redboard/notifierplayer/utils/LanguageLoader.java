package fr.redboard.notifierplayer.utils;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.redboard.notifierplayer.NotifierPlayer;

public class LanguageLoader {

	public static HashMap<String, String> translationMap = new HashMap<>(); // ici toutes les string get

	public LanguageLoader(NotifierPlayer plugin) {
		File languageDirectory = new File(plugin.getDataFolder(), "languages/");
		File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
		File File_fr_FR = new File(plugin.getDataFolder(), "languages/fr_FR.yml");
		File File_de_DE = new File(plugin.getDataFolder(), "languages/de_DE.yml");

		// Partie 1
		if (!languageDirectory.isDirectory()) { // test if chemin is repertoire
			languageDirectory.mkdir(); // create repertoire languages

			if (!defaultLanguageFile.exists() || !File_fr_FR.exists() || !File_de_DE.exists()) { // create fichier if not exist
				defaultLanguageFile.getParentFile().mkdirs();
				plugin.saveResource("languages/en_US.yml", false);
				plugin.saveResource("languages/fr_FR.yml", false);
				plugin.saveResource("languages/de_DE.yml", false);
			}
		}

		// Partie 2
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