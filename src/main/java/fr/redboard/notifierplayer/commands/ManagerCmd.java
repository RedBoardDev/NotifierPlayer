package fr.redboard.notifierplayer.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.redboard.notifierplayer.NotifierPlayer;
import fr.redboard.notifierplayer.utils.ColorsUtils;
import fr.redboard.notifierplayer.utils.LanguageLoader;
import fr.redboard.notifierplayer.utils.ManagerConfig;
import fr.redboard.notifierplayer.utils.ManagerPlayerList;

public class ManagerCmd implements CommandExecutor {

    private final ManagerConfig config;
    private final ManagerPlayerList configPlayer;

    public ManagerCmd(ManagerConfig config, ManagerPlayerList configPlayer) {
        this.config = config;
        this.configPlayer = configPlayer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("notifier"))
            return false;
        if (!(sender instanceof Player p))
            return false;
        String pName = p.toString();
        String namePlugin = ColorsUtils.convert(config.getNamePlugin());

        if (args.length == 0) {
            toggleGlobalNotification(p, pName, namePlugin);
            configPlayer.reload();
            return true;
        }

        switch (args[0]) {
            case "toggle":
                if (args.length == 2) {
                    toggleSpecificNotification(p, pName, namePlugin, args[1]);
                } else {
                    toggleGlobalNotification(p, pName, namePlugin);
                }
                break;

            case "reload":
                if (!p.hasPermission("notifier.admin")) {
                    sendMessage(namePlugin, "permissionMessage", p);
                    break;
                }
                config.reload();
                configPlayer.reload();
                LanguageLoader.reload();
                sendMessage(namePlugin, "reload", p);
                break;

            case "help":
                handleHelp(p, namePlugin, pName);
                break;

            default:
                toggleGlobalNotification(p, pName, namePlugin);
        }
        configPlayer.reload();
        return true;
    }

    private void handleHelp(Player p, String namePlugin, String pName) {
        String mentionFormat = ColorsUtils.convertHelp(config.getFormatMention());
        int delay = config.getDelay();
        double price = config.getEcoPrice();
        String symbol = config.getEcoSymbol();
        boolean delayBool = config.getDelay() > 0;

        List<String> listPlayer = configPlayer.listPlayer(pName);
        final String helpInfoIgnore1 = config.getLanguagePath("helpInfoIgnore1");
        final String helpInfoIgnore2 = config.getLanguagePath("helpInfoIgnore2");
        if (listPlayer.isEmpty()) {
            listPlayer.add(helpInfoIgnore1);
        } else if (listPlayer.contains("ALL")) {
            listPlayer.clear();
            listPlayer.add(helpInfoIgnore2);
        }
        CmdHelp.sendHelp(p, namePlugin, mentionFormat, delay, price, symbol, config.getEcoUse(), delayBool, listPlayer);
    }

    private void toggleSpecificNotification(Player p, String pName, String namePlugin, String argPlayer) {
        if (!(configPlayer.checkPlayerExist(pName))) {
            configPlayer.addStrPlayer(pName, argPlayer);
            sendMessage(namePlugin, "disableMentionPlayer", p, argPlayer);
            return;
        }
        if (configPlayer.checkPlayerContain(pName, argPlayer)) {
            configPlayer.removeStrPlayer(pName, argPlayer);
            sendMessage(namePlugin, "enableMentionPlayer", p, argPlayer);
        } else {
            configPlayer.addStrPlayer(pName, argPlayer);
            sendMessage(namePlugin, "disableMentionPlayer", p, argPlayer);
        }
    }

    private void toggleGlobalNotification(Player p, String pName, String namePlugin) {
        if (!(configPlayer.checkPlayerExist(pName))) {
            configPlayer.addStrPlayer(pName, "ALL");
            sendMessage(namePlugin, "disableMention", p);
            return;
        }
        if (configPlayer.checkPlayerContain(pName, "ALL")) {
            configPlayer.removeStrPlayer(pName, "ALL");
            sendMessage(namePlugin, "enableMention", p);
        } else {
            configPlayer.addStrPlayer(pName, "ALL");
            sendMessage(namePlugin, "disableMention", p);
        }
    }

    private void sendMessage(String namePlugin, String path, Player p) {
        p.sendMessage(namePlugin + " " + ColorsUtils.convert(config.getLanguagePath(path)));
    }

    private void sendMessage(String namePlugin, String path, Player p, String pName) {
        Map<String, String> replacements = Map.of(
                "%player%", pName
        );
        p.sendMessage(namePlugin + " " + ColorsUtils.replaceAndConvert(config.getLanguagePath(path), replacements));
    }
}
