package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.beans.Home;
import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
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
        List<String> l = new ArrayList<String>();
        if (strings.length == 1) {
            l.add(ChatColor.ITALIC + "<page>");
        }
        return l;
    }

    private void listHomes(Player p, int page) {
        List<Home> homes = this.plugin.getApi().getHomes(p.getUniqueId().toString());
        
        int homesPerPage = 15;
        
        int pageCount = homes.size() / homesPerPage;
        if (homes.size() % homesPerPage != 0) {
            pageCount += 1;
        }
        
        if (page > pageCount || page < 1) {
            ChatUtils.sendMessageRed(p, "Page number does not exist.");
            return;
        }
        
        p.sendMessage(ChatColor.GOLD + "Your homes: (Page " + page + "/" + pageCount + ", total: " + homes.size() +")");
//        if (this.plugin.getApi().hasDefaultHome(p.getUniqueId().toString()) && page == 1) {
//            p.sendMessage(ChatColor.LIGHT_PURPLE + "(default) " + ChatColor.DARK_PURPLE + "Your default home");
//        }
        int displayNum = homesPerPage;
        if (page == pageCount && homes.size() % homesPerPage != 0) {
            displayNum = homes.size() % homesPerPage;
        }
        
        int offset = page * homesPerPage - homesPerPage;
        int i = 0;
        while (i < displayNum) {
            Home h = homes.get(offset+i);
            String w = "World";
            if (h.getWorld().contains("_the_end")) {
                w = "End";
            }
            if (h.getWorld().contains("_nether")) {
                w = "Nether";
            }
            p.sendMessage(ChatColor.LIGHT_PURPLE + h.getName() + " " + ChatColor.YELLOW + h.getDesc() + ChatColor.GRAY + " (" + w + ")");
            i++;
        }
    }

}
