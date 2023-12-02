package fr.redboard.notifierplayer.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import fr.redboard.notifierplayer.utils.ManagerConfig;
import fr.redboard.notifierplayer.utils.ManagerPlayerList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CheckChat implements Listener {
    private final List<String> playerTime;
    private final ManagerConfig config;
    private int permissionPlayer;
    private final ManagerPlayerList configPlayer;

    public CheckChat(ManagerConfig config, ManagerPlayerList configPlayer, NotifierPlayer eco) {
        this.config = config;
        this.configPlayer = configPlayer;
        playerTime = new ArrayList<String>();
    }

    @EventHandler
    public void onPreSendMessage(AsyncPlayerChatEvent e) {
        String[] messageList = e.getMessage().split(" ");
        for (String message : messageList) {
            final Player pCaller = e.getPlayer(); // appellant
            final String pluginName = config.getNamePlugin();
            permissionPlayer = setPermPlayer(pCaller);
            if (ChatColor.stripColor(message).equals(setPosSymbol("everyone"))) {
                callEveryone(e, message, permissionPlayer >= 2, pCaller, pluginName);
            }
            for (Player pCall : Bukkit.getOnlinePlayers()) {
                verifyPerPlayer(pCaller, pCall, pluginName, message, e);
            }
        }
    }

    private void verifyPerPlayer(Player pCaller, Player pCall, String pluginName, String message, AsyncPlayerChatEvent e) {
        final String callName = pCall.getName(); // appeler
        if (!ChatColor.stripColor(message.toLowerCase()).equals(setPosSymbol(callName.toLowerCase())))
            return;
        final String pluginNameConvert = ColorsUtils.convert(pluginName);

        if (!errorHandling(e, callName, pluginNameConvert, message, pCaller, pCall))
            return;

        final String callerName = pCaller.getName();
        final String mentionChange = ColorsUtils.replaceAndConvert(config.getFormatMention() + "§r", Map.of(
                "%player%", callName,
                "%caller%", callerName
        ));
        final String mentionChangeNickName = ColorsUtils.replaceAndConvert(config.getFormatMention() + "§r", Map.of(
                "%player%", ChatColor.stripColor(pCall.getDisplayName()),
                "%caller%", callerName
        ));

        setMessage(e, message, mentionChange, mentionChangeNickName);
        sendScreenDisplay(pCall, callerName, callName);
        if (config.getActiveSound())
            playSound(pCall);
        managerDelay(pCaller, callName);
    }

    private boolean errorHandling(AsyncPlayerChatEvent e, String callName, String pluginName, String message, Player pCaller, Player pCall) {
        if (permissionPlayer == 0) {
            canceller("permissionMessage", pCaller, callName, e, message, pluginName);
            return false;
        }
        if (configPlayer.checkPlayerContain(pCall.toString(), "ALL") || configPlayer.checkPlayerContain(pCall.toString(), pCaller.getName())) {
            canceller("playerListError", pCaller, callName, e, message, pluginName);
            return false;
        }
        if (playerTime.contains(callName) && permissionPlayer < 1) {
            canceller("delayMention", pCaller, callName, e, message, pluginName);
            return false;
        }
        if (pCaller == pCall && !config.getMYourSelf()) {
            canceller("yourselfMention", pCaller, callName, e, message, pluginName);
            return false;
        }
        if (isVanished(pCall) || !pCaller.canSee(pCall)) {
            e.setMessage(e.getMessage().replace(message, callName));
            return false;
        }
        return true;
    }

    private void managerDelay(Player pCaller, String callName) {
        if (config.getDelay() < 1 || pCaller.isOp() || permissionPlayer < 1)
            return;
        playerTime.add(callName);
        runStop(callName);
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
        if (config.getNickname())
            e.setMessage(e.getMessage().replace(messageListI, strYesNick));
        else
            e.setMessage(e.getMessage().replace(messageListI, strNoNick));
    }

    private void callEveryone(AsyncPlayerChatEvent e, CharSequence messageListI, boolean code, Player pCaller,
                          String pluginName) {
        if (!code) {
            canceller("permissionMessage", pCaller, String.valueOf(messageListI), e, messageListI, pluginName);
            e.setMessage(e.getMessage());
            return;
        }
        String mentionChange = ColorsUtils.convert(config.getFormatEveryone() + "§r");
        e.setMessage(e.getMessage().replace(messageListI, mentionChange));
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendScreenDisplay(p, "everyone", "everyone");
            if (config.getActiveSound())
                playSoundEv(p);
        }
    }

    private void sendScreenDisplay(Player pCall, String callerName, String callName) {
        if (config.getActiveTitle())
            sendTitle(callName, callerName, pCall);
        if (config.getActiveActionBar())
            sendActionBar(callName, callerName, pCall);
    }

    private String setPosSymbol(String callName) {
        return (config.getPosSymbol() ? callName + config.getSymbol() : config.getSymbol() + callName);
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

    private String getMapTitle(String path) {
        return config.hMapTitle().get(path);
    }

    private void sendTitle(String callName, String callerName, Player pCall) {
        Map<String, String> replacements = Map.of(
                "%player%", callName,
                "%caller%", callerName
        );

        pCall.sendTitle(ColorsUtils.replaceAndConvert(getMapTitle("mainTitle"), replacements),
                ColorsUtils.replaceAndConvert(getMapTitle("subTitle"),replacements),
                Integer.parseInt(getMapTitle("fadeIn")), Integer.parseInt(getMapTitle("stay")),
                Integer.parseInt(getMapTitle("fadeOut")));
    }

    private String getMapSound(String path) {
        return config.hMapSound().get(path);
    }

    private String getMapSoundEv(String path) {
        return config.hMapSoundEv().get(path);
    }

    private void playSound(Player pCall) {
        pCall.playSound(pCall.getLocation(), Sound.valueOf((getMapSound("type"))),
                Float.parseFloat(getMapSound("volume")), Float.parseFloat(getMapSound("pitch")));
    }

    private void playSoundEv(Player pCall) {
        pCall.playSound(pCall.getLocation(), Sound.valueOf((getMapSoundEv("type"))),
                Float.parseFloat(getMapSoundEv("volume")), Float.parseFloat(getMapSoundEv("pitch")));
    }

    private void sendActionBar(String callName, String callerName, Player pCall) {
        Map<String, String> replacements = Map.of(
                "%player%", callName,
                "%caller%" , callerName
        );
        pCall.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent(ColorsUtils.replaceAndConvert(config.getActionBar(), replacements)));
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean())
                return true;
        }
        return false;
    }
}
