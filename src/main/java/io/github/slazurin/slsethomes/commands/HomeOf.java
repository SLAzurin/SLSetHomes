package io.github.slazurin.slsethomes.commands;

import io.github.slazurin.slsethomes.beans.Home;
import io.github.slazurin.slsethomes.SLSetHomes;
import io.github.slazurin.slsethomes.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class HomeOf implements TabExecutor {

    private final SLSetHomes plugin;

    public HomeOf(SLSetHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender == null) return true;
        if (!(sender instanceof Player)) {
            ChatUtils.sendNotPlayer(sender);
            return true;
        }
        
        if (!sender.hasPermission("slsethomes.home-of")) {
            ChatUtils.sendNoPerm(sender);
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            ChatUtils.sendMessageRed(sender, "Expected 1 or 2 arguments.");
            return true;
        }
        UUID p2UUID = this.plugin.getUUIDMapperPlugin().getApi().getUUID(args[0]);
        if (p2UUID == null) {
            ChatUtils.sendMessageRed(sender, "Player not found.");
            return true;
        }
        OfflinePlayer p2 = Bukkit.getOfflinePlayer(p2UUID);
        if (!p2.hasPlayedBefore()) {
            ChatUtils.sendMessageRed(sender, "Player not found.");
            return true;
        }

        String uuid = p2.getUniqueId().toString();
        homeOf((Player) sender, uuid, args);
        return true;
    }

    private void homeOf(Player p, String uuid, String[] args) {
        if (args.length != 2) {
            if (this.plugin.getApi().hasDefaultHome(uuid)) {
                p.teleport(this.plugin.getApi().getHome(uuid, null));
            } else {
                ChatUtils.sendMessageRed(p, Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " has no default home");
                return;
            }
        } else {
            if (this.plugin.getApi().hasHome(uuid,args[1])) {
                p.teleport(this.plugin.getApi().getHome(uuid, args[1]));
            } else {
                ChatUtils.sendMessageRed(p, "Home " + args[1] + " not found for " + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
                return;
            }
        }
        p.playNote(p.getLocation(), Instrument.BELL, Note.sharp(2, Note.Tone.F));
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
            UUID pUUID = this.plugin.getUUIDMapperPlugin().getApi().getUUID(args[0]);
            if (pUUID == null) {
                l.add("Player not found");
                return l;
            }
            OfflinePlayer p = Bukkit.getOfflinePlayer(pUUID);
            if (!p.hasPlayedBefore()) {
                l.add("Player not found");
                return l;
            }
            List<Home> homes = this.plugin.getApi().getHomes(p.getUniqueId().toString());
            for (Home home : homes) {
                if (home.getName().toUpperCase().startsWith(args[1].toUpperCase())) {
                    l.add(home.getName());
                }
            }
        }
        return l;
    }
}