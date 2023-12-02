package fr.redboard.notifierplayer.utils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import fr.redboard.notifierplayer.NotifierPlayer;

public class ManagerPlayerList {

    private FileConfiguration config;
    private final NotifierPlayer main;

    public ManagerPlayerList(NotifierPlayer notifierPlayer) {
        this.main = notifierPlayer;
        reload();
    }

    public void reload() {
        save();
        config = main.getCustomConfig();
    }

    public void save() {
        try {
            main.getCustomConfig().save(main.configPlayerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T get(Function<MemoryConfiguration, T> call) {
        return call.apply(config);
    }

    public MemoryConfiguration get() {
        return config;
    }

    // WrapperConfig
    public List<String> listPlayer(String pName) {
        return get().getStringList(pName);
    }

    public void playerSender(String pName, boolean bool) {
        if (bool) {
            get().set(pName, "ALL");
        } else {
            get().set(pName, null);
        }
    }

    public boolean checkPlayerExist(String pName) {
        return get().get(pName) != null;
    }

    public List<?> getPlayerStatus(String pName) {
        return (get().getList(pName));
    }

    public boolean checkPlayerList(String pName) {
        return !get().getStringList(pName).isEmpty();
    }

    public boolean checkPlayerContain(String pName, String str) {
        return get().getStringList(pName).contains(str);
    }

    public void addStrPlayer(String pName, String str) {
        List<String> list = get().getStringList(pName);
        list.add(str);
        get().set(pName, list);
    }

    public void removeStrPlayer(String pName, String str) {
        List<String> list = get().getStringList(pName);
        list.remove(str);
        get().set(pName, list);
    }
}
