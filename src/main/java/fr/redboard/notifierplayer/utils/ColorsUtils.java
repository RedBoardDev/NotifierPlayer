package fr.redboard.notifierplayer.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class ColorsUtils {

    private static final Pattern PATTERN = Pattern.compile("\\{#[a-fA-F0-9]{6}\\}");

    public static String convert(String msg) {
        msg = String.format(msg);
        return ChatColor.translateAlternateColorCodes((char) '&', matcher(msg));
    }

    public static String convert(String msg, String replace1) {
        msg = String.format(msg.replaceAll("%player%", replace1));
        return ChatColor.translateAlternateColorCodes((char) '&', matcher(msg));
    }

    public static String convert(String msg, String replace1, String replace2) {
        msg = String.format(msg.replaceAll("%player%", replace1).replaceAll("%caller%", replace2)
                .replaceAll("%price%", replace1).replace("%symbol%", replace2));
        return ChatColor.translateAlternateColorCodes((char) '&', matcher(msg));
    }


    public static String convertHelp(String msg) {
        msg = String.format(msg.replaceAll("%n", System.lineSeparator()).replaceAll("%player%", "player")
                .replaceAll("%caller%", "player"));
        return ChatColor.translateAlternateColorCodes((char) '&', matcher(msg));
    }

    public static String convertHelp(String msg, String delay, String price, String symbol) {
        msg = String.format(msg.replaceAll("%n", System.lineSeparator()).replaceAll("%player%", "player")
                .replaceAll("%caller%", "player").replaceAll("%delay%", delay).replaceAll("%price%", price)
                .replace("%symbol%", symbol));
        return ChatColor.translateAlternateColorCodes((char) '&', matcher(msg));
    }

    // Matchmaking find and replace
    private static String matcher(String msg) {
        Matcher matcher = PATTERN.matcher(msg);
        while (matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            String color1 = color.replace("{", "").replace("}", "");
            if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17"))
                msg = msg.replace(color, (Object) ChatColor.of((String) color1) + "");
            else {
                String red = color1.substring(1, 3);
                String green = color1.substring(3, 5);
                String blue = color1.substring(5);
                String colorRGB = fromRGB(Integer.parseInt(red, 16), Integer.parseInt(green, 16),
                        Integer.parseInt(blue, 16)) + "";
                msg = msg.replace(color, colorRGB);
            }
            matcher = PATTERN.matcher(msg);
        }
        return msg;
    }

    // Color management for version <1.16
    private static ChatColor fromRGB(int r, int g, int b) {
        TreeMap<Integer, ChatColor> closest = new TreeMap<Integer, ChatColor>();
        colorMap.forEach((color, set) -> {
            int red = Math.abs(r - set.getRed());
            int green = Math.abs(g - set.getGreen());
            int blue = Math.abs(b - set.getBlue());
            closest.put(red + green + blue, color);
        });
        return closest.firstEntry().getValue();
    }

    private static Map<ChatColor, ColorSet<Integer, Integer, Integer>> colorMap = new HashMap<ChatColor, ColorSet<Integer, Integer, Integer>>();
    static {
        colorMap.put(ChatColor.BLACK, new ColorSet<Integer, Integer, Integer>(0, 0, 0));
        colorMap.put(ChatColor.DARK_BLUE, new ColorSet<Integer, Integer, Integer>(0, 0, 170));
        colorMap.put(ChatColor.DARK_GREEN, new ColorSet<Integer, Integer, Integer>(0, 170, 0));
        colorMap.put(ChatColor.DARK_AQUA, new ColorSet<Integer, Integer, Integer>(0, 170, 170));
        colorMap.put(ChatColor.DARK_RED, new ColorSet<Integer, Integer, Integer>(170, 0, 0));
        colorMap.put(ChatColor.DARK_PURPLE, new ColorSet<Integer, Integer, Integer>(170, 0, 170));
        colorMap.put(ChatColor.GOLD, new ColorSet<Integer, Integer, Integer>(255, 170, 0));
        colorMap.put(ChatColor.GRAY, new ColorSet<Integer, Integer, Integer>(170, 170, 170));
        colorMap.put(ChatColor.DARK_GRAY, new ColorSet<Integer, Integer, Integer>(85, 85, 85));
        colorMap.put(ChatColor.BLUE, new ColorSet<Integer, Integer, Integer>(85, 85, 255));
        colorMap.put(ChatColor.GREEN, new ColorSet<Integer, Integer, Integer>(85, 255, 85));
        colorMap.put(ChatColor.AQUA, new ColorSet<Integer, Integer, Integer>(85, 255, 255));
        colorMap.put(ChatColor.RED, new ColorSet<Integer, Integer, Integer>(255, 85, 85));
        colorMap.put(ChatColor.LIGHT_PURPLE, new ColorSet<Integer, Integer, Integer>(255, 85, 255));
        colorMap.put(ChatColor.YELLOW, new ColorSet<Integer, Integer, Integer>(255, 255, 85));
        colorMap.put(ChatColor.WHITE, new ColorSet<Integer, Integer, Integer>(255, 255, 255));
    }

    private static class ColorSet<R, G, B> {
        R red = null;
        G green = null;
        B blue = null;

        ColorSet(R red, G green, B blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public R getRed() {
            return red;
        }

        public G getGreen() {
            return green;
        }

        public B getBlue() {
            return blue;
        }
    }
}
