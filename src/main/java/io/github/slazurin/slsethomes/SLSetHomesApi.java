package io.github.slazurin.slsethomes;

import io.github.slazurin.slsethomes.beans.Home;
import io.github.slazurin.slsethomes.ymlstore.HomesStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class SLSetHomesApi {
    private final SLSetHomes plugin;
    private final HomesStore homesStore;
    private final String defaultHomeAccessor;
    private final String homeAccessor;

    public SLSetHomesApi(SLSetHomes plugin) {
        this.plugin = plugin;
        this.homesStore = new HomesStore(this.plugin);
        defaultHomeAccessor = "unknownHomes.";
        homeAccessor = "allNamedHomes.";
    }

    public SLSetHomes getPlugin() {
        return plugin;
    }

    public HomesStore getHomesStore() {
        return homesStore;
    }
    
    public Location getHome(String uuid, String name) {
        if (name == null || name.equals("")) {
            return getDefaultHome(uuid);
        }
        FileConfiguration cache = this.getHomesStore().getCache();
        String playerHomeAccessor = this.homeAccessor + uuid + "." + name;
        if (!cache.isSet(playerHomeAccessor)) {
            return null;
        }
        World world = this.plugin.getServer().getWorld(cache.getString(playerHomeAccessor + ".world"));
        double x = cache.getDouble(playerHomeAccessor + ".x");
        double y = cache.getDouble(playerHomeAccessor + ".y");
        double z = cache.getDouble(playerHomeAccessor + ".z");
        float pitch = Float.parseFloat(cache.getString(playerHomeAccessor + ".pitch"));
        float yaw = Float.parseFloat(cache.getString(playerHomeAccessor + ".yaw"));
        
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    private Location getDefaultHome(String uuid) {
        String playerDefaultHomeAccessor = this.defaultHomeAccessor + uuid;
        FileConfiguration homesCache = getHomesStore().getCache();
        World world = this.plugin.getServer().getWorld(homesCache.getString(playerDefaultHomeAccessor + ".world"));
        double x = homesCache.getDouble(playerDefaultHomeAccessor + ".x");
        double y = homesCache.getDouble(playerDefaultHomeAccessor + ".y");
        double z = homesCache.getDouble(playerDefaultHomeAccessor + ".z");
        float pitch = Float.parseFloat(homesCache.getString(playerDefaultHomeAccessor + ".pitch"));
        float yaw = Float.parseFloat(homesCache.getString(playerDefaultHomeAccessor + ".yaw"));

        Location home = new Location(world, x, y, z, yaw, pitch);

        return home;
    }
    
    public List<Home> getHomes(String uuid) {
        FileConfiguration cache = this.homesStore.getCache();
        List<Home> homes = new ArrayList<>();
        String playerHomeAccessor = this.homeAccessor + uuid;
        if (!cache.isSet(playerHomeAccessor)) {
            return homes;
        }
        
        for (String homeName : cache.getConfigurationSection(playerHomeAccessor).getKeys(false)) {
            Home h = new Home();
            h.setName(homeName);
            h.setDesc(cache.getString(playerHomeAccessor + "." + homeName + "." + "desc", ""));
            h.setWorld(cache.getString(playerHomeAccessor + "." + homeName + "." + "world", ""));
            homes.add(h);
        }
        Collections.sort(homes, (Home h1, Home h2) -> {
            return h1.getName().compareToIgnoreCase(h2.getName());
        });
        return homes;
    }
    
    public boolean hasHomes(String uuid) {
        return this.homesStore.getCache().isSet(this.homeAccessor + uuid);
    }
    
    public boolean hasDefaultHome(String uuid) {
        return this.homesStore.getCache().isSet(this.defaultHomeAccessor + uuid);
    }
    
    public boolean hasHome(String uuid, String name) {
        return this.homesStore.getCache().isSet(this.homeAccessor + uuid + "." + name);
    }
    
    public void setHome(String uuid, String name, String description, Location l) {
        if (name == null || name.equals("")) {
            this.setDefaultHome(uuid,l);
            return;
        }
        String playerHomeAccessor = this.homeAccessor + uuid + "." + name;
        FileConfiguration cache = getHomesStore().getCache();
        cache.set(playerHomeAccessor + ".world", l.getWorld().getName());
        cache.set(playerHomeAccessor + ".x", l.getX());
        cache.set(playerHomeAccessor + ".y", l.getY());
        cache.set(playerHomeAccessor + ".z", l.getZ());
        cache.set(playerHomeAccessor + ".pitch", l.getPitch());
        cache.set(playerHomeAccessor + ".yaw", l.getYaw());
        if (!(description == null || description.equals(""))) {
            cache.set(playerHomeAccessor + ".desc", description);
        }
        this.homesStore.saveStore();
    }
    
    private void setDefaultHome(String uuid, Location l) {
        String playerDefaultHomeAccessor = this.defaultHomeAccessor + uuid;
        FileConfiguration cache = getHomesStore().getCache();
        cache.set(playerDefaultHomeAccessor + ".world", l.getWorld().getName());
        cache.set(playerDefaultHomeAccessor + ".x", l.getX());
        cache.set(playerDefaultHomeAccessor + ".y", l.getY());
        cache.set(playerDefaultHomeAccessor + ".z", l.getZ());
        cache.set(playerDefaultHomeAccessor + ".pitch", l.getPitch());
        cache.set(playerDefaultHomeAccessor + ".yaw", l.getYaw());
        this.homesStore.saveStore();
    }
    
    public void delHome(String uuid, String name) {
        if (name == null || name.equals("")) {
            this.delDefaultHome(uuid);
            return;
        }
        String path = this.homeAccessor + uuid + "." + name;
        this.homesStore.getCache().set(path, null);
        this.homesStore.saveStore();
    }
    
    private void delDefaultHome(String uuid) {
        String playerDefaultHomeAccessor = this.defaultHomeAccessor + uuid;
        this.homesStore.getCache().set(playerDefaultHomeAccessor, null);
        this.homesStore.saveStore();
    }
    
    public List<String> getPlayersWithHomes() {
        List<String> l = new ArrayList<>();
        FileConfiguration cache = this.homesStore.getCache();
        for (String uuid : cache.getConfigurationSection(this.homeAccessor).getKeys(false)) {
            l.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
        }
        for (String uuid : cache.getConfigurationSection(this.defaultHomeAccessor).getKeys(false)) {
            String n = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
            if (!l.contains(n)) {
                l.add(n);
            }
        }
        
        return l;
    }
    
}
