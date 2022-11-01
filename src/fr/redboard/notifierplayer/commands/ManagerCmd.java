package fr.redboard.notifierplayer.commands;

import java.util.List;

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

	public static boolean confirm;
	private ManagerConfig config;
	private ManagerPlayerList configPlayer;

	public ManagerCmd(ManagerConfig config, ManagerPlayerList configPlayer) {
		this.config = config;
		this.configPlayer = configPlayer;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("notifier")) {
			if (!(sender instanceof Player))
				return false;
			Player p = (Player) sender;
			String pName = p.toString();
//			String pName = p.getAddress().toString(); A test (online/crack mode)
			String namePlugin = ColorsUtils.convert(config.getNamePlugin());
			if (args.length >= 1) {
				switch (args[0]) {

				case "toggle": // IN DEVELOPEMENT
					if (args.length == 1) {
						// on/off all -> partie 1
						if (configPlayer.checkPlayerExist(pName)) { // check if player exist
							if (configPlayer.checkPlayerContain(pName, "ALL")) { // check if ALL exist in player
								sendMessage(namePlugin, "enableMention", p);
								configPlayer.removeStrPlayer(pName, "ALL"); // remove all for player
							} else { // if all not exist to player
								sendMessage(namePlugin, "disableMention", p);
								configPlayer.addStrPlayer(pName, "ALL"); // add all for player
							}
						} else { // if player not exist
							sendMessage(namePlugin, "disableMention", p);
							configPlayer.addStrPlayer(pName, "ALL"); // add all for player
						}

					} else if (args.length >= 2) {
						String arsg1 = args[1];
						// on/off player -> partie 2
						System.out.println(arsg1);
						if (configPlayer.checkPlayerExist(pName)) { // check if player exist
							if (configPlayer.checkPlayerContain(pName, arsg1)) { // check if args1 not exist
								sendMessage(namePlugin, "enableMentionPlayer", p, arsg1);
								configPlayer.removeStrPlayer(pName, arsg1); // remove args1 for player
							} else {
								sendMessage(namePlugin, "disableMentionPlayer", p, arsg1);
								configPlayer.addStrPlayer(pName, arsg1); // add args1 for player
							}
						} else { // if all not exist to player
							sendMessage(namePlugin, "disableMentionPlayer", p, arsg1);
							configPlayer.addStrPlayer(pName, arsg1); // add args1 for player
						}
					}

					break;

				case "reload":
					if (p.hasPermission("notifier.admin")) {
						config.reload();
						configPlayer.reload();
						sendMessage(namePlugin, "reload", p);
						new LanguageLoader(NotifierPlayer.instance);
//							sendMessage(namePlugin, "reloadError", p); // faire le message d'erreur dans en_US.yml + gestion error
					} else
						sendMessage(namePlugin, "permissionMessage", p);
					break;

//				case "clear": // a refaire par player ?
//					if(args.length >= 2) {
//						if (p.hasPermission("notifier.admin")) {
//							if (configPlayer.checkPlayerList(args[1])) {
//								confirm = true;
//								runClear(); // check and remake
//								sendMessage(namePlugin, "clear", p); //modif message
//							} // else
//								//message qui dit que le joueur n'existe pos
//						} else
//							sendMessage(namePlugin, "permissionMessage", p);
//					}
//					break;

//				case "confirm": // a refaire par player
//					if (p.hasPermission("notifier.admin")) {
//						if (confirm) {
//							confirm = false;
//							playerList.clear();
//							sendMessage(namePlugin, "confirm", p);
//						} else
//							sendMessage(namePlugin, "confirmError", p);
//					}
//					break;

				case "help":
					String mentionFormat = ColorsUtils.convertHelp(config.getFormatMention());
					int delay = config.getDelay();
					double price = config.getEcoPrice();
					String symbol = config.getEcoSymbol().toString();
					boolean delayBool;
					if (config.getDelay() > 0)
						delayBool = true;
					else
						delayBool = false;

					List<String> listPlayer = configPlayer.listPlayer(pName);
					final String helpInfoIgnore1 = config.getLanguagePath("helpInfoIgnore1");
					final String helpInfoIgnore2 = config.getLanguagePath("helpInfoIgnore2");
					if (listPlayer.isEmpty()) {
						listPlayer.clear();
						listPlayer.add(helpInfoIgnore1);
					} else if (listPlayer.contains("ALL")) {
						listPlayer.clear();
						listPlayer.add(helpInfoIgnore2);
					}

					CmdHelp.sendHelp(p, namePlugin, mentionFormat, delay, price, symbol, config.getEcoUse(), delayBool,
							listPlayer);
					break;

				default:
					// on/off all -> partie 1
					if (configPlayer.checkPlayerExist(pName)) { // check if player exist
						if (configPlayer.checkPlayerContain(pName, "ALL")) { // check if ALL exist in player
							sendMessage(namePlugin, "enableMention", p);
							configPlayer.removeStrPlayer(pName, "ALL"); // remove all for player
						} else { // if all not exist to player
							sendMessage(namePlugin, "disableMention", p);
							configPlayer.addStrPlayer(pName, "ALL"); // add all for player
						}
					} else { // if player not exist
						sendMessage(namePlugin, "disableMention", p);
						configPlayer.addStrPlayer(pName, "ALL"); // add all for player
					}
				}
			} else {
				// on/off all -> partie 1
				if (configPlayer.checkPlayerExist(pName)) { // check if player exist
					if (configPlayer.checkPlayerContain(pName, "ALL")) { // check if ALL exist in player
						sendMessage(namePlugin, "enableMention", p);
						configPlayer.removeStrPlayer(pName, "ALL"); // remove all for player
					} else { // if all not exist to player
						sendMessage(namePlugin, "disableMention", p);
						configPlayer.addStrPlayer(pName, "ALL"); // add all for player
					}
				} else { // if player not exist
					sendMessage(namePlugin, "disableMention", p);
					configPlayer.addStrPlayer(pName, "ALL"); // add all for player
				}
			}
			configPlayer.reload();
		}
		return false;
	}

	private void sendMessage(String namePlugin, String path, Player p) {
		p.sendMessage(namePlugin + " " + ColorsUtils.convert(config.getLanguagePath(path)));
	}

	private void sendMessage(String namePlugin, String path, Player p, String pName) {
		p.sendMessage(namePlugin + " " + ColorsUtils.convert(config.getLanguagePath(path), pName));
	}

//	private void runClear() {
//		int delay = 10 * 20;
//		BukkitScheduler scheduler = NotifierPlayer.instance.getServer().getScheduler();
//		scheduler.scheduleSyncDelayedTask(NotifierPlayer.instance, new Runnable() {
//			@Override
//			public void run() {
//				confirm = false;
//			}
//		}, delay);
//	}
}
