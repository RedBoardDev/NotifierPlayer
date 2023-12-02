package fr.redboard.notifierplayer.commands;

import java.util.List;

import org.bukkit.entity.Player;

import fr.redboard.notifierplayer.utils.ColorsUtils;
import fr.redboard.notifierplayer.utils.LanguageLoader;

public class CmdHelp {

    public static void sendHelp(
            Player p,
            String namePlugin,
            String mentionFormat,
            int delay,
            double price,
            String symbol,
            boolean ecoBool,
            boolean delayBool,
            List<String> listPlayer) {
        p.sendMessage("§c§7§m                     §7[" + namePlugin + "§7 help§7]§7§m                     §7");
        String helpDelay = LanguageLoader.getTranslation("helpDelay");
        String helpEco = LanguageLoader.getTranslation("helpEco");
        String helpMessage = LanguageLoader.getTranslation("helpMessage").replace("%mentionFormat%", mentionFormat);
        if (delayBool)
            helpMessage = helpMessage.replace("%helpDelay%", helpDelay);
        else
            helpMessage = helpMessage.replace("%helpDelay%", "");

        if (ecoBool)
            helpMessage = helpMessage.replace("%helpEco%", helpEco);
        else
            helpMessage = helpMessage.replace("%helpEco%", "");
        helpMessage = helpMessage.replace("%player_bloqued%", listPlayer.toString());
        p.sendMessage(ColorsUtils.convertHelp(helpMessage, String.valueOf(delay), String.valueOf(price), symbol));
        if (p.hasPermission("notifier.admin")) {
            String helpAdmin = LanguageLoader.getTranslation("helpAdmin");
            p.sendMessage(ColorsUtils.convertHelp(helpAdmin, String.valueOf(delay), String.valueOf(price), symbol));
        }
    }
}
