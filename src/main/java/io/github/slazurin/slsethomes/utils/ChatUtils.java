package io.github.slazurin.slsethomes.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils {
    public static void broadcastMessage(String msg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(msg);
        });
    }

    public static void sendMessageRed(CommandSender cs, String msg) {
        cs.sendMessage(ChatColor.RED + msg);
    }

    public static void sendMessageAqua(CommandSender cs, String msg) {
        cs.sendMessage(ChatColor.AQUA + msg);
    }

    public static void sendNotPlayer(CommandSender cs) {
        sendMessageRed(cs, "Only players can use this command.");
    }

    public static void sendNoPerm(CommandSender cs) {
        sendMessageRed(cs, "You do not have permission use this command.");
    }
}
