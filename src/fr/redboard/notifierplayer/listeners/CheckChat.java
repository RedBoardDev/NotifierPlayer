package fr.redboard.notifierplayer.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

import fr.redboard.notifierplayer.NotifierPlayer;
import fr.redboard.notifierplayer.utils.ColorsUtils;
import fr.redboard.notifierplayer.utils.LanguageLoader;
import fr.redboard.notifierplayer.utils.ManagerConfig;
import fr.redboard.notifierplayer.utils.ManagerPlayerList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CheckChat implements Listener {

	private List<String> playerTime;
	private ManagerConfig config;
	private NotifierPlayer vault;
	private int permissionPlayer;
	private ManagerPlayerList configPlayer;

	public CheckChat(ManagerConfig config, ManagerPlayerList configPlayer, NotifierPlayer eco) {
		this.config = config;
		this.configPlayer = configPlayer;
		playerTime = new ArrayList<String>();
		vault = eco;
	}

	@EventHandler
	public void onPreSendMessage(AsyncPlayerChatEvent e) {
		String[] messageList = e.getMessage().split(" ");
		for (int i = 0; i < messageList.length; i++) {
			final Player pCaller = e.getPlayer(); // appellant
			final String pluginName = config.getNamePlugin();

			// set perm pCaller
			permissionPlayer = setPermPlayer(pCaller);

			// test everyone
			if (ChatColor.stripColor(messageList[i]).equals(setPosSymbol("everyone"))) {
				if (permissionPlayer >= 2)
					everyone(e, messageList[i], 1, pCaller, pluginName);
				else {
					everyone(e, messageList[i], 0, pCaller, pluginName);
				}
			}

			// partie mention
			for (Player pCall : Bukkit.getOnlinePlayers()) {
				final String callName = pCall.getName(); // appeler
				if (!ChatColor.stripColor(messageList[i].toLowerCase()).equals(setPosSymbol(callName.toLowerCase())))
					continue;

				final String pluginNameConvert = ColorsUtils.convert(pluginName);
				if (permissionPlayer >= 1 || (!configPlayer.checkPlayerContain(pCall.toString(), "ALL")
						&& !configPlayer.checkPlayerContain(pCall.toString(), pCaller.getName()))) {
					if (permissionPlayer >= 1 || !playerTime.contains(callName)) {
						if ((pCaller != pCall) || (config.getMYourSelf())) {
							if (!isVanished(pCall) && pCaller.canSee(pCall)) {

								if (config.getEcoUse()) {
									if (ecoUse(pCaller, pluginNameConvert, e, callName, pluginName,
											messageList[i]) == false)
										break;
								}
								final String callerName = pCaller.getName();
								final String mentionChange = ColorsUtils.convert(config.getFormatMention() + "§r",
										callName, callerName);
								final String mentionChangeNickName = ColorsUtils.convert(
										config.getFormatMention() + "§r", ChatColor.stripColor(pCall.getDisplayName()),
										callerName);
								setMessage(e, messageList[i], mentionChange, mentionChangeNickName);

								// additional additions
								sendScreenDisplay(pCall, callerName, callName);
								if (config.getActivSound())
									playSound(pCall);

								managerDelay(pCaller, callName);

							} else
								e.setMessage(e.getMessage().replace(messageList[i], callName));

						} else
							canceller("yourselfMention", pCaller, callName, e, messageList[i], pluginName);
					} else
						canceller("delayMention", pCaller, callName, e, messageList[i], pluginName);
				} else
					canceller("playerListError", pCaller, callName, e, messageList[i], pluginName);
			}
		}
	}

	private void managerDelay(Player pCaller, String callName) {
		if ((config.getDelay() > 0)) {
			if (!pCaller.isOp()) {
				if (!(permissionPlayer >= 1)) {
					playerTime.add(callName);
					runStop(callName);
				}
			}
		}
	}

	private int setPermPlayer(Player pCaller) {
		if (pCaller.hasPermission("notifier.admin"))
			return 3;
		else if (pCaller.hasPermission("notifier.everyone"))
			return 2;
		else if (pCaller.hasPermission("notifier.bypass"))
			return 1;
		else
			return 0;
	}

	private void setMessage(AsyncPlayerChatEvent e, CharSequence messageListI, String strNoNick, String strYesNick) {
		// voir pour nickname SI le réglage est en true dans config.yml
		if (config.getNickname())
			e.setMessage(e.getMessage().replace(messageListI, strYesNick));
		else
			e.setMessage(e.getMessage().replace(messageListI, strNoNick));

	}

	private void everyone(AsyncPlayerChatEvent e, CharSequence messageListI, int code, Player pCaller,
			String pluginName) {
		if (code == 1) {
			String mentionChange = ColorsUtils.convert(config.getFormatEveryone() + "§r");
			e.setMessage(e.getMessage().replace(messageListI, mentionChange));
			for (Player p : Bukkit.getOnlinePlayers()) {
				sendScreenDisplay(p, "everyone", "everyone");
				if (config.getActivSound())
					playSoundEv(p);
			}
		} else {
			canceller("permissionMessage", pCaller, String.valueOf(messageListI), e, messageListI, pluginName);
			e.setMessage(e.getMessage());
		}

	}

	private void sendScreenDisplay(Player pCall, String callerName, String callName) {
		if (config.getActivTitle())
			sendTitle(callName, callerName, pCall);
		if (config.getActivActionBar())
			sendActionBar(callName, callerName, pCall);
	}

	private String setPosSymbol(String callName) {
		if (config.getPosSymbol())
			return callName + config.getSymbol();
		else
			return config.getSymbol() + callName;
	}

	private boolean ecoUse(Player pCaller, String pluginNameConvert, AsyncPlayerChatEvent e, String callName,
			String pluginName, CharSequence messageListI) {
		double price = config.getEcoPrice();
		if (vault.eco.getBalance(pCaller) >= price) {
			pCaller.sendMessage(
					pluginNameConvert + " " + ColorsUtils.convert(LanguageLoader.translationMap.get("paymentMessage"),
							String.valueOf(price), config.getEcoSymbol().toString()));

			vault.eco.withdrawPlayer(pCaller, price);
			return true;
		} else {
			canceller("enoughMoney", pCaller, callName, e, messageListI, pluginName);
			return false;
		}
	}

	private void canceller(String path, Player pCaller, String callName, AsyncPlayerChatEvent e,
			CharSequence messageListI, String pluginName) {
		pCaller.sendMessage(ColorsUtils.convert(pluginName + " " + config.getLanguagePath(path)));
		if (config.getErrorAction()) {
			e.setMessage(e.getMessage().replace(messageListI, callName));
		} else {
			e.setCancelled(true);
		}
	}

	private void runStop(String p) {
		int delay = config.getDelay() * 20;
		BukkitScheduler scheduler = NotifierPlayer.instance.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(NotifierPlayer.instance, new Runnable() {
			@Override
			public void run() {
				playerTime.remove(p);
			}
		}, delay);
	}

	private String gethMapTitle(String path) {
		return config.hMapTitle().get(path);
	}

	private void sendTitle(String callName, String callerName, Player pCall) {
		pCall.sendTitle(ColorsUtils.convert(gethMapTitle("mainTitle"), callName, callerName),
				ColorsUtils.convert(gethMapTitle("subTitle"), callName, callerName),
				Integer.parseInt(gethMapTitle("fadeIn")), Integer.parseInt(gethMapTitle("stay")),
				Integer.parseInt(gethMapTitle("fadeOut")));
	}

	private String gethMapSound(String path) {
		return config.hMapSound().get(path);
	}

	private String gethMapSoundEv(String path) {
		return config.hMapSoundEv().get(path);
	}

	private void playSound(Player pCall) {
		pCall.playSound(pCall.getLocation(), Sound.valueOf((gethMapSound("type"))),
				Float.parseFloat(gethMapSound("volume")), Float.parseFloat(gethMapSound("pitch")));
	}

	private void playSoundEv(Player pCall) {
		pCall.playSound(pCall.getLocation(), Sound.valueOf((gethMapSoundEv("type"))),
				Float.parseFloat(gethMapSoundEv("volume")), Float.parseFloat(gethMapSoundEv("pitch")));
	}

	private void sendActionBar(String callName, String callerName, Player pCall) {
		pCall.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				new TextComponent(ColorsUtils.convert(config.getActionBar(), callName, callerName)));
	}

	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean())
				return true;
		}
		return false;
	}
}
