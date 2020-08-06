package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class Home implements TabExecutor {
    private final SLSetHomes plugin;

    public Home(SLSetHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == null) return true;
        if (!(sender instanceof Player)) {
            ChatUtils.sendNotPlayer(sender);
            return true;
        }
        
        if (!sender.hasPermission("slsethomes.home")) {
            ChatUtils.sendNoPerm(sender);
            return true;
        }

        if (args.length > 1) {
            ChatUtils.sendMessageRed(sender, "Expected 0 or 1 argument.");
            return true;
        }
        
        Player p = (Player) sender;
        String uuid = p.getUniqueId().toString();
        home(p, uuid, args);
        
        return true;
    }
    
    private void home(Player p, String uuid, String[] args) {
        if (args.length == 0) {
            if (!(this.plugin.getApi().hasDefaultHome(uuid))) {
                ChatUtils.sendMessageRed(p, "Default home not set.");
                return;
            }
            p.teleport(this.plugin.getApi().getHome(uuid,null));
        } else {
            if (!(this.plugin.getApi().hasHome(uuid,args[0]))) {
                ChatUtils.sendMessageRed(p, args[0] + " home not set.");
                return;
            }
            p.teleport(this.plugin.getApi().getHome(uuid,args[0]));
        }
        
        p.playNote(p.getLocation(), Instrument.BELL, Note.sharp(2, Note.Tone.F));
        
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            List<io.github.slazurin.slsethomes.beans.Home> homes = this.plugin.getApi().getHomes(((Player)cs).getUniqueId().toString());
            for (io.github.slazurin.slsethomes.beans.Home home : homes) {
                if (home.getName().toUpperCase().startsWith(args[0].toUpperCase())) {
                    l.add(home.getName());
                }
            }
        }
        return l;
    }
}