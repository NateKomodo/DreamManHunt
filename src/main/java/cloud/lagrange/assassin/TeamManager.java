package cloud.lagrange.assassin;

import cloud.lagrange.assassin.model.ManHuntRole;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class TeamManager {

    private final Scoreboard board;

    public TeamManager(Plugin plugin) {
        ScoreboardManager manager = plugin.getServer().getScoreboardManager();
        if (manager == null) {
            throw new RuntimeException("Cannot load scoreboard manager: no worlds loaded");
        }

        this.board = manager.getMainScoreboard();
        if (board.getTeam(ManHuntRole.ASSASSIN.toString()) == null)
            this.board.registerNewTeam(ManHuntRole.ASSASSIN.toString());

        if (board.getTeam(ManHuntRole.SPEEDRUNNER.toString()) == null) {
            board.registerNewTeam(ManHuntRole.SPEEDRUNNER.toString());
            setInvisibleNameTag(ManHuntRole.SPEEDRUNNER);
        }
    }

    public void setInvisibleNameTag(ManHuntRole teamName) {
        Team team = this.board.getTeam(teamName.toString());
        if (team == null)
            throw new RuntimeException("No team with name " + teamName + " found");

        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public void addPlayer(ManHuntRole teamName, Player player) {
        Team team = this.board.getTeam(teamName.toString());
        if (team == null)
            throw new RuntimeException("No team with name " + teamName + " found");

        team.addEntry(player.getName());
    }

    public void removePlayer(ManHuntRole teamName, Player player) {
        Team team = this.board.getTeam(teamName.toString());
        if (team == null)
            throw new RuntimeException("No team with name " + teamName + " found");

        team.removeEntry(player.getName());
    }
}

