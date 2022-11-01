package fr.redboard.notifierplayer.commands;

import java.util.List;

import org.bukkit.entity.Player;

import fr.redboard.notifierplayer.utils.ColorsUtils;
import fr.redboard.notifierplayer.utils.LanguageLoader;

public class CmdHelp {

	public static void sendHelp(Player p, String namePlugin, String mentionFormat, int delay, double price,
			String symbol, boolean ecoBool, boolean delayBool, List<String> listPlayer) {
		p.sendMessage("§c§7§m                     §7[" + namePlugin + "§7 help§7]§7§m                     §7");

		String helpDelay = LanguageLoader.translationMap.get("helpDelay");
		String helpEco = LanguageLoader.translationMap.get("helpEco");
		String helpMessage = LanguageLoader.translationMap.get("helpMessage").replace("%mentionFormat%", mentionFormat);

		if (delayBool)
			helpMessage = helpMessage.replace("%helpDelay%", helpDelay);
		else
			helpMessage = helpMessage.replace("%helpDelay%", "");

		if (ecoBool)
			helpMessage = helpMessage.replace("%helpEco%", helpEco);
		else
			helpMessage = helpMessage.replace("%helpEco%", "");

		
		//get list player and check all etc
		helpMessage = helpMessage.replace("%player_bloqued%", listPlayer.toString());

		p.sendMessage(ColorsUtils.convertHelp(helpMessage, String.valueOf(delay), String.valueOf(price), symbol));

		if (p.hasPermission("notifier.admin")) { // ici commande admins
			String helpAdmin = LanguageLoader.translationMap.get("helpAdmin");
			p.sendMessage(ColorsUtils.convertHelp(helpAdmin, String.valueOf(delay), String.valueOf(price), symbol));
		}
	}

}
