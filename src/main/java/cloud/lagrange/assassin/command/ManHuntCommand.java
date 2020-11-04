package cloud.lagrange.assassin.command;

import cloud.lagrange.assassin.PlayerData;
import cloud.lagrange.assassin.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ManHuntCommand implements CommandExecutor {

    private final PlayerData playerData;

    public ManHuntCommand(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder result = new StringBuilder(ChatColor.YELLOW + "Current:\n\n");

        String speedrunners = formatPlayerList(playerData.getPlayersByRole(ManHuntRole.SPEEDRUNNER));
        result.append("Speedrunners:\n").append(speedrunners);

        String assassins = formatPlayerList(playerData.getPlayersByRole(ManHuntRole.ASSASSIN));
        result.append("\n\nAssassins:\n").append(assassins);

        sender.sendMessage(result.toString());

        return true;
    }

    private String formatPlayerList(List<Player> players) {
        return players.stream()
                .map(HumanEntity::getName)
                .collect(Collectors.joining("\n"));
    }
}
