package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.HomeLinkedList.HomeNode;
import io.github.slazurin.slsethomes.HomeLinkedList.HomesList;
import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.command.TabExecutor;

public class Homes implements TabExecutor {

    private final SLSetHomes plugin;

    public Homes(SLSetHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == null) return true;
        if (!(sender instanceof Player)) {
            ChatUtils.sendNotPlayer(sender);
            return true;
        }
        
        if (!sender.hasPermission("slsethomes.homes")) {
            ChatUtils.sendNoPerm(sender);
            return true;
        }
        
        if (args.length > 1) {
            ChatUtils.sendMessageRed(sender, "Expected 0 or 1 argument");
            return true;
        }
        int page = 1;
        
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                ChatUtils.sendMessageRed(sender, "Page number invalid.");
                return true;
            }
        }
        
        
        listHomes((Player) sender, page);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] strings) {
        List l = new ArrayList<>();
        if (strings.length == 1) {
            l.add(ChatColor.ITALIC + "<page>");
        }
        return l;
    }

    private void listHomes(Player p, int page) {
        List<Map.Entry<String,String>> homes = this.plugin.getApi().getSortedHomesList(p.getUniqueId().toString());
        
        int pageCount = homes.size() / 10;
        if (homes.size() % 10 != 0) {
            pageCount += 1;
        }
        
        if (page > pageCount || page < 1) {
            ChatUtils.sendMessageRed(p, "Page number does not exist.");
            return;
        }
        
        p.sendMessage(ChatColor.YELLOW + "Your homes: (Page " + page + "/" + pageCount + ", total: " + homes.size() +")");
        if (this.plugin.getApi().hasDefaultHome(p.getUniqueId().toString()) && page == 1) {
            p.sendMessage(ChatColor.YELLOW + "(default): Your default home");
        }
        int displayNum = 10;
        if (page == pageCount && homes.size() % 10 != 0) {
            displayNum = homes.size() % 10;
        }
        
        int offset = page * 10 - 10;
        int i = 0;
        while (i < displayNum) {
            p.sendMessage(ChatColor.YELLOW + homes.get(offset+i).getKey() + ": " + homes.get(offset+i).getValue());
            i++;
        }
    }

}
