package cloud.lagrange.assassin;

import cloud.lagrange.assassin.model.ManHuntRole;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Instance of this class contains data per player, such as player's team, and if they are frozen or not
 */
public class PlayerData {

    private final Map<Player, PlayerDetails> players = new HashMap<>();

    /**
     * Get players with given role
     *
     * @param role role of players to be returned
     * @return list of players with given role
     */
    public List<Player> getPlayersByRole(ManHuntRole role) {
        return players.entrySet().stream()
                .filter(e -> e.getValue().role == role)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public boolean isFrozen(Player player) {
        return Optional.ofNullable(players.get(player))
                .map(PlayerDetails::isFrozen)
                .orElse(false);
    }


    /**
     * get role of player (assassin/speedrunner)
     *
     * @param player player
     * @return role of given player, or null player is not assigned to any of the teams
     */
    public ManHuntRole getRole(Player player) {
        return Optional.ofNullable(players.get(player))
                .map(PlayerDetails::getRole)
                .orElse(null);
    }

    /**
     * Remove player's data from memory
     *
     * @param player player to be removed
     */
    public void reset(Player player) {
        players.remove(player);
    }

    public void setFrozen(Player player, boolean frozen) {
        PlayerDetails details = players.getOrDefault(player, new PlayerDetails());
        details.setFrozen(frozen);
        players.putIfAbsent(player, details);
    }

    public void setRole(Player player, ManHuntRole role) {
        PlayerDetails details = players.getOrDefault(player, new PlayerDetails());
        details.setRole(role);
        players.putIfAbsent(player, details);
    }

    private static class PlayerDetails {
        private boolean isFrozen = false;
        private ManHuntRole role;

        public boolean isFrozen() {
            return isFrozen;
        }

        public void setFrozen(boolean frozen) {
            isFrozen = frozen;
        }

        public ManHuntRole getRole() {
            return role;
        }

        public void setRole(ManHuntRole role) {
            this.role = role;
        }
    }

}
