package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.TabExecutor;

public class SetHome implements TabExecutor {
    private final SLSetHomes plugin;

    public SetHome(SLSetHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == null) return true;
        if (!(sender instanceof Player)) {
            ChatUtils.sendNotPlayer(sender);
            return true;
        }
        
        Player p = (Player) sender;
        
        if (!p.hasPermission("slsethomes.sethome")) {
            ChatUtils.sendNoPerm(sender);
            return true;
        }
        
        if (args.length == 0) {
            this.setDefaultHome(p);
        } else {
            if (args[0].contains(".")) {
                ChatUtils.sendMessageRed(p, "Cannot have period in name.");
                return true;
            }
            this.setHome(p, args);
        }
        return true;
    }
    
    private void setDefaultHome(Player p) {
        String uuid = p.getUniqueId().toString();
        if (this.plugin.getApi().hasDefaultHome(uuid)) {
            ChatUtils.sendMessageRed(p, "Default home already set.");
            return;
        }
        this.plugin.getApi().setHome(uuid, null, null, p.getLocation());
        ChatUtils.sendMessageAqua(p, "Default home is now set.");
    }
    
    private void setHome(Player p, String[] args) {
        String uuid = p.getUniqueId().toString();
        if (this.plugin.getApi().hasHome(uuid, args[0])) {
            ChatUtils.sendMessageRed(p, args[0] + " is already set.");
            return;
        }
        boolean hasDesc = args.length > 1;
        if (hasDesc) {
            String strbuilder = "";
            for (int i = 1; i < args.length; i++) {
                strbuilder += args[i];
            }
            strbuilder = strbuilder.trim();
            this.plugin.getApi().setHome(uuid, args[0], strbuilder, p.getLocation());
            ChatUtils.sendMessageAqua(p, args[0] + " home is now set with description: \"" + strbuilder + "\"");
        } else {
            this.plugin.getApi().setHome(uuid, args[0], null, p.getLocation());
            ChatUtils.sendMessageAqua(p, args[0] + " home is now set");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && args[0].equals("")) {
            list.add("<name>");
        }
        if (args.length == 2 && args[1].equals("")) {
            list.add(ChatColor.ITALIC + "<optional.description>");
        }
        return list;
    }
}
