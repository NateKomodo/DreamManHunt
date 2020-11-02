package cloud.lagrange.assassin.command;


import cloud.lagrange.assassin.PlayerData;
import cloud.lagrange.assassin.TeamManager;
import cloud.lagrange.assassin.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpeedrunnerCommand implements CommandExecutor {

    private final Plugin plugin;
    private final TeamManager teamManager;
    private final PlayerData playerData;

    public SpeedrunnerCommand(Plugin plugin, TeamManager teamManager, PlayerData playerData) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerData = playerData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) return false;

        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("Could not find player " + args[0]);
            return true;
        }

        if (args.length == 1) {
            addSpeedrunner(plugin.getServer().getPlayer(args[0]));
            sender.sendMessage(ChatColor.GREEN + "Added player " + player.getName() + " to speedrunners group");
            return true;
        } else if (args.length == 2 && args[1].equals("remove")) {
            removeSpeedrunner(player);
            sender.sendMessage(ChatColor.GREEN + "Removed player " + player.getName() + " from speedrunners group");
            return true;
        }

        return false;
    }

    private void addSpeedrunner(Player player) {
        playerData.setRole(player, ManHuntRole.SPEEDRUNNER);
        teamManager.addPlayer(ManHuntRole.SPEEDRUNNER, player);
    }

    private void removeSpeedrunner(Player player) {
        playerData.reset(player);
        teamManager.removePlayer(ManHuntRole.SPEEDRUNNER, player);
    }

}
