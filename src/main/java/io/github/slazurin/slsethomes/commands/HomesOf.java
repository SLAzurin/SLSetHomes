package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.beans.Home;
import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.command.TabExecutor;

public class HomesOf implements TabExecutor {

    private final SLSetHomes plugin;

    public HomesOf(SLSetHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == null) return true;
        if (!(sender instanceof Player)) {
            ChatUtils.sendNotPlayer(sender);
            return true;
        }
        
        if (!sender.hasPermission("slsethomes.homes-of")) {
            ChatUtils.sendNoPerm(sender);
            return true;
        }
        
        if (args.length == 0 || args.length > 2) {
            ChatUtils.sendMessageRed(sender, "Expected 1 or 2 arguments");
            return true;
        }
        
        OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
        if (!p.hasPlayedBefore()) {
            ChatUtils.sendMessageRed(sender, "Player not found");
            return true;
        }
        
        int page = 1;
        
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                ChatUtils.sendMessageRed(sender, "Page number invalid.");
                return true;
            }
        }
        
        listHomesOf((Player) sender, p, page);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            for (String name : this.plugin.getApi().getPlayersWithHomes()) {
                if (name.toUpperCase().startsWith(args[0].toUpperCase())) {
                    l.add(name);
                }
            }
        }
        if (args.length == 2) {
            l.add("<page>");
        }
        return l;
    }
    
    private void listHomesOf(Player p, OfflinePlayer ofPlayer, int page) {
        List<Home> homes = this.plugin.getApi().getHomes(ofPlayer.getUniqueId().toString());
        
        int pageCount = homes.size() / 10;
        if (homes.size() % 10 != 0) {
            pageCount += 1;
        }
        
        if (page > pageCount || page < 1) {
            ChatUtils.sendMessageRed(p, "Page number does not exist.");
            return;
        }
        
        p.sendMessage(ChatColor.GOLD + ofPlayer.getName() + "'s homes: (Page " + page + "/" + pageCount + ", total: " + homes.size() +")");
        if (this.plugin.getApi().hasDefaultHome(ofPlayer.getUniqueId().toString()) && page == 1) {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "(default) " + ChatColor.YELLOW + ofPlayer.getName() + "'s default home");
        }
        int displayNum = 10;
        if (page == pageCount && homes.size() % 10 != 0) {
            displayNum = homes.size() % 10;
        }
        
        int offset = page * 10 - 10;
        int i = 0;
        while (i < displayNum) {
            Home h = homes.get(offset+i);
            String w = "World";
            if (h.getWorld().endsWith("_the_end")) {
                w = "End";
            }
            if (h.getWorld().endsWith("_nether")) {
                w = "Nether";
            }
            p.sendMessage(ChatColor.LIGHT_PURPLE + h.getName() + " " + ChatColor.YELLOW + h.getDesc() + ChatColor.GRAY + " (" + w + ")");
            i++;
        }
    }

}
