package cloud.lagrange.assassin;

import cloud.lagrange.assassin.Events.Commands;
import cloud.lagrange.assassin.Events.Events;
import cloud.lagrange.assassin.Models.Role;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Assassin extends JavaPlugin {

    @Override
    public void onEnable() {
    	TeamManager manager = new TeamManager(this);
    	getCommand("assassin").setExecutor(new Commands(this, manager));
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        new Worker(this);
        new Config(this);
        
        Bukkit.getLogger().info("Assassin plugin started.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Assassin plugin stopped.");
    }
}
