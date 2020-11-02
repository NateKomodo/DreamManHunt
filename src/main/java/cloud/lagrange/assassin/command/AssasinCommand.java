package cloud.lagrange.assassin.command;

import cloud.lagrange.assassin.Config;
import cloud.lagrange.assassin.PlayerData;
import cloud.lagrange.assassin.TeamManager;
import cloud.lagrange.assassin.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AssasinCommand implements CommandExecutor {

    private final Plugin plugin;
    private final TeamManager teamManager;
    private final PlayerData playerData;
    private final Config config;

    public AssasinCommand(Plugin plugin, TeamManager teamManager, PlayerData playerData, Config config) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerData = playerData;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            config.reload();
            sender.sendMessage("Config reloaded");
            return true;
        }

        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("Could not find player " + args[0]);
            return true;
        }

        if (args.length == 1) {
            addAssassin(player);
            sender.sendMessage(ChatColor.GREEN + "Added player " + player.getName() + " to assassins group");
            return true;
        } else if (args.length == 2 && args[1].equals("remove")) {
            removeAssassin(player);
            sender.sendMessage(ChatColor.GREEN + "Removed player " + player.getName() + " from assassins group");
            return true;
        }

        return false;
    }

    private void addAssassin(Player player) {
        playerData.setRole(player, ManHuntRole.ASSASSIN);
        teamManager.addPlayer(ManHuntRole.ASSASSIN, player);
        if (config.giveCompass()) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    private void removeAssassin(Player player) {
        playerData.reset(player);
        teamManager.removePlayer(ManHuntRole.ASSASSIN, player);
    }
}
