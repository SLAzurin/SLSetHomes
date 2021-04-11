package io.github.slazurin.slsethomes;

import io.github.slazurin.slsethomes.commands.SetHome;
import io.github.slazurin.SLUUIDToNamesMapper.SLUUIDToNamesMapperPlugin;
import io.github.slazurin.slsethomes.commands.DelHome;
import io.github.slazurin.slsethomes.commands.Home;
import io.github.slazurin.slsethomes.commands.HomeOf;
import io.github.slazurin.slsethomes.commands.Homes;
import io.github.slazurin.slsethomes.commands.HomesOf;
import org.bukkit.plugin.java.JavaPlugin;

public class SLSetHomes extends JavaPlugin {
    private SLSetHomesApi api;
    private SLUUIDToNamesMapperPlugin uuidMapperPlugin;

    @Override
    public void onEnable() {
        this.api = new SLSetHomesApi(this);
        this.uuidMapperPlugin = (SLUUIDToNamesMapperPlugin) this.getServer().getPluginManager().getPlugin("SLUUIDToNamesMapperAPI");
        registerCommands();
    }

    private void registerCommands() {
        this.getCommand("sethome").setExecutor(new SetHome(this));
        this.getCommand("homes").setExecutor(new Homes(this));
        this.getCommand("delhome").setExecutor(new DelHome(this));
        this.getCommand("home").setExecutor(new Home(this));
        this.getCommand("home-of").setExecutor(new HomeOf(this));
        this.getCommand("homes-of").setExecutor(new HomesOf(this));
    }
    
    public SLSetHomesApi getApi() {
        return api;
    }
    
    public SLUUIDToNamesMapperPlugin getUUIDMapperPlugin() {
        return this.uuidMapperPlugin;
    }
}