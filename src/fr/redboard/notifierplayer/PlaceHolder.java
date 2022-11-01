package fr.redboard.notifierplayer;

import org.bukkit.entity.Player;

import fr.redboard.notifierplayer.utils.ManagerPlayerList;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolder extends PlaceholderExpansion {

	private ManagerPlayerList config;

	public PlaceHolder(ManagerPlayerList managerPlayerlist) {
		super();
		this.config = managerPlayerlist;
	}

	@Override
	public String getAuthor() {
		return "RedBoard";
	}

	@Override
	public String getIdentifier() {
		return "mention";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String params) {
		if (p == null) {
			return "";
		}
		String player = p.toString();
		if(params.equals("status")) {
			if (config.checkPlayerExist(player)) {
				if (config.getPlayerStatus(player).contains("ALL"))
					return String.valueOf("False");
				else
					return String.valueOf("True");
			} else
				return String.valueOf("True");
		}
		return null;
	}
	
}
