package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.beans.Home;
import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class DelHome implements TabExecutor {
    private final SLSetHomes plugin;

    public DelHome(SLSetHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == null) return true;
        if (!(sender instanceof Player)) {
            ChatUtils.sendNotPlayer(sender);
            return true;
        }

        if (!sender.hasPermission("slsethomes.delhome")) {
            ChatUtils.sendNoPerm(sender);
            return true;
        }
        
        if (args.length > 1) {
            ChatUtils.sendMessageRed(sender, "Expected 0 or 1 argument");
            return true;
        }
        
        
        delHome(((Player)sender).getUniqueId().toString(),args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            List<Home> homes = this.plugin.getApi().getHomes(((Player)cs).getUniqueId().toString());
            for (Home home : homes) {
                if (home.getName().toUpperCase().startsWith(args[0].toUpperCase())) {
                    l.add(home.getName());
                }
            }
        }
        return l;
    }

    private void delHome(String uuid, String[] args) {
        if (args.length == 0) {
            if (this.plugin.getApi().hasDefaultHome(uuid)) {
                this.plugin.getApi().delHome(uuid, null);
                ChatUtils.sendMessageAqua(Bukkit.getPlayer(UUID.fromString(uuid)), "Default home deleted");
            } else {
                ChatUtils.sendMessageRed(Bukkit.getPlayer(UUID.fromString(uuid)), "Default home not set");
            }
        } else {
            if (this.plugin.getApi().hasHome(uuid, args[0])) {
                this.plugin.getApi().delHome(uuid, args[0]);
                ChatUtils.sendMessageAqua(Bukkit.getPlayer(UUID.fromString(uuid)), args[0] + " home deleted");
            } else {
                ChatUtils.sendMessageRed(Bukkit.getPlayer(UUID.fromString(uuid)), args[0] + " home not set");
            }
        }
        
    }

}
