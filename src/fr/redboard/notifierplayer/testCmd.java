package fr.redboard.notifierplayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.redboard.notifierplayer.utils.ManagerPlayerList;

public class testCmd implements CommandExecutor {
	
	private ManagerPlayerList config;

	public testCmd(ManagerPlayerList config) {
		this.config = config;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test")) {
			System.out.println(config.checkPlayerList(sender.getName()));
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("add")) {
					config.addStrPlayer(sender.getName(), args[1]);
				}
				if(args[0].equalsIgnoreCase("remove")) {
					config.removeStrPlayer(sender.getName(), args[1]);
				}
				if(args[0].equalsIgnoreCase("check")) {
					System.out.println(config.checkPlayerExist(args[1]));
				}
			}
		}
		return false;
	}

}
