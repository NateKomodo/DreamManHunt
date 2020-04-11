package cloud.lagrange.assassin;

import cloud.lagrange.assassin.Models.Role;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamManager {
	public Scoreboard board;
	
	public TeamManager(Assassin parent) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.board = manager.getMainScoreboard();
        Team t = this.board.getTeam(Role._ASSASSIN_.toString());
        if (t == null) {
        	this.board.registerNewTeam(Role._ASSASSIN_.toString());
        }
        
        t = this.board.getTeam(Role._SPEEDRUNNER_.toString());
        if (t == null) {
        	this.board.registerNewTeam(Role._SPEEDRUNNER_.toString());
        	this.setInvisibleNameTag(Role._SPEEDRUNNER_);
        }
	}
	
	public void setInvisibleNameTag(Role teamName) {
		Team team = this.board.getTeam(teamName.toString());
		team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
	}
	
	public void addPlayer(Role teamName, Player player) {
		Team team = this.board.getTeam(teamName.toString());
		team.addPlayer(player);
	}
	
	public void removePlayer(Role teamName, Player player) {
		Team team = this.board.getTeam(teamName.toString());
		team.removePlayer(player);
	}
}

