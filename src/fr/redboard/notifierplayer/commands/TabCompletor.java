package fr.redboard.notifierplayer.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletor implements TabCompleter {

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
		List<String> resultat = new ArrayList<>();
		if (sender.hasPermission("notifier.admin") ||sender.isOp()) {
			if (args.length == 1) {
				for (String str : this.argumentsAdmin) {
					if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
						resultat.add(str);
					}
				}
				return resultat;
			}
		} else {
			if (args.length == 1) {
				for (String str : this.arguments1) {
					if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
						resultat.add(str);
					}
				}
				return resultat;
			}
		}
		return null;
	}
}
