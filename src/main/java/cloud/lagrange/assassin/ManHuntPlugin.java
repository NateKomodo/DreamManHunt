package cloud.lagrange.assassin;

import cloud.lagrange.assassin.command.AssassinCommand;
import cloud.lagrange.assassin.command.ManHuntCommand;
import cloud.lagrange.assassin.command.SpeedrunnerCommand;
import cloud.lagrange.assassin.event.Events;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class ManHuntPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        TeamManager manager = new TeamManager(this);
        PlayerData playerData = new PlayerData();
        Config config = new Config(this);

        getServer().getPluginManager().registerEvents(new Events(playerData, config), this);

        Optional.ofNullable(getCommand("assassin"))
                .ifPresent(c -> c.setExecutor(new AssassinCommand(this, manager, playerData, config)));
        Optional.ofNullable(getCommand("speedrunner"))
                .ifPresent(c -> c.setExecutor(new SpeedrunnerCommand(this, manager, playerData)));
        Optional.ofNullable(getCommand("manhunt"))
                .ifPresent(c -> c.setExecutor(new ManHuntCommand(playerData)));

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Worker(this, playerData, config), 1, 1);

        getLogger().info("Assassin plugin started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Assassin plugin stopped.");
    }
}
