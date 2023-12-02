package fr.redboard.notifierplayer.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    List<String> arguments1 = new ArrayList<>();
    List<String> argumentsAdmin = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        if (this.arguments1.isEmpty()) {
            this.arguments1.add("toggle");
            this.arguments1.add("help");
        }
        if (this.argumentsAdmin.isEmpty()) {
            this.argumentsAdmin.add("toggle");
            this.argumentsAdmin.add("help");
            this.argumentsAdmin.add("reload");
        }
        List<String> result = new ArrayList<>();

        if (args.length != 1)
            return null;
        if (sender.hasPermission("notifier.admin") ||sender.isOp()) {
                for (String str : this.argumentsAdmin) {
                    if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(str);
                    }
                }
                return result;
        }
        for (String str : this.arguments1) {
            if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
                result.add(str);
            }
        }
        return result;
    }
}
