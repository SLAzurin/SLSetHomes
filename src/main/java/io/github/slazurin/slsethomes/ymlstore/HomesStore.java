package io.github.slazurin.slsethomes.ymlstore;

import io.github.slazurin.slsethomes.SLSetHomes;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class HomesStore {
    private final SLSetHomes plugin;
    private FileConfiguration cache;
    private final File homesStore;

    public HomesStore(SLSetHomes plugin) {
        this.plugin = plugin;
        this.homesStore = new File(this.plugin.getDataFolder(), "homes.yml");
        this.createStoreIfNotExists();
        this.cache = YamlConfiguration.loadConfiguration(this.homesStore);
        
    }

    public void reloadStore() {
        this.cache = YamlConfiguration.loadConfiguration(this.homesStore);
    }

    public FileConfiguration getCache() {
        return cache;
    }

    public void saveStore() {
        try {
            getCache().save(homesStore);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "{0}Couldn''t open homesStore file when saving!", ChatColor.RED);
        }
    }

    private void createStoreIfNotExists() {
        if (!this.homesStore.exists()) {
            this.homesStore.getParentFile().mkdirs();
            this.plugin.saveResource("homes.yml", false);
        }
    }
}
