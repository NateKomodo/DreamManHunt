package cloud.lagrange.assassin;

import cloud.lagrange.assassin.model.ManHuntRole;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerData {

    private final Map<Player, PlayerDetails> players = new HashMap<>();

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

    public ManHuntRole getRole(Player player) {
        return Optional.ofNullable(players.get(player))
                .map(PlayerDetails::getRole)
                .orElse(ManHuntRole.DEFAULT);
    }

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
        private ManHuntRole role = ManHuntRole.DEFAULT;

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
