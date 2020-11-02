package cloud.lagrange.assassin;

import cloud.lagrange.assassin.model.ManHuntRole;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;


public class Worker implements Runnable {

    private final Plugin plugin;
    private final PlayerData playerData;
    private final Config config;

    public Worker(Plugin plugin, PlayerData playerData, Config config) {
        this.plugin = plugin;
        this.playerData = playerData;
        this.config = config;
    }

    @Override
    public void run() {
        Set<Player> frozeThisTick = new HashSet<>();
        if (config.freeze()) {
            for (Player player : playerData.getPlayersByRole(ManHuntRole.SPEEDRUNNER)) {
                Entity target = getTarget(player);
                if (target == null || target.getType() != EntityType.PLAYER) continue;
                Player targetPlayer = (Player) target;
                if (playerData.getRole(targetPlayer) != ManHuntRole.ASSASSIN) continue;
                playerData.setFrozen(targetPlayer, true);
                frozeThisTick.add(targetPlayer);
                drawLine(player.getEyeLocation(), targetPlayer.getEyeLocation(), 1);
            }
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (playerData.isFrozen(player) && !frozeThisTick.contains(player))
                playerData.setFrozen(player, false);
        }

        if (config.isCompassTracking()) {
            for (Player player : playerData.getPlayersByRole(ManHuntRole.ASSASSIN)) {
                updateCompass(player);
            }
        }
    }

    private void updateCompass(Player player) {
        Player nearest = getNearestSpeedrunner(player);
        if (nearest == null) {
            // Randomize compass direction if no player found, e.g. players r in different worlds
            if (config.isCompassRandomizeInDifferentWorlds()) {
                float angle = (float) (Math.random() * Math.PI * 2);
                float dx = (float) (Math.cos(angle) * 5);
                float dz = (float) (Math.sin(angle) * 5);
                player.setCompassTarget(player.getLocation().add(new Vector(dx, 0, dz)));
            }
        } else {
            player.setCompassTarget(nearest.getLocation());
            PlayerInventory inventory = player.getInventory();
            if (inventory.getItemInMainHand().getType() == Material.COMPASS || inventory.getItemInOffHand().getType() == Material.COMPASS) {
                if (config.isCompassParticle() &&
                        (player.getWorld().getEnvironment() != Environment.NETHER || config.isCompassParticleInNether())) {
                    drawDirection(player.getLocation(), nearest.getLocation(), 3);
                }
            }
        }
    }

    public Player getNearestSpeedrunner(Player player) {

        Location playerLocation = player.getLocation();

        return plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> !p.equals(player))
                .filter(p -> playerData.getRole(p) == ManHuntRole.SPEEDRUNNER)
                .filter(p -> p.getWorld().equals(player.getWorld()))
                .min(Comparator.comparing(p -> p.getLocation().distance(playerLocation)))
                .orElse(null);
    }

    private LivingEntity getTarget(Player player) {
        int range = 60;
        List<Entity> nearbyEntities = player.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> entities = new ArrayList<>();

        for (Entity e : nearbyEntities) {
            if (e instanceof LivingEntity) {
                entities.add((LivingEntity) e);
            }
        }

        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            if (block.getType() != Material.AIR && block.getType() != Material.WATER) break;
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : entities) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx - .15 <= ex && ex <= bx + 1.15)
                        && (bz - .15 <= ez && ez <= bz + 1.15)
                        && (by - 1 <= ey && ey <= by + 1)) {
                    // entity is close enough, set target and stop
                    target = e;
                    break;
                }
            }
        }
        return target;
    }

    public void drawLine(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        for (double length = 0; length < distance; length += space) {
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
            if (p1.distance(point1.toVector()) > 1 && p1.distance(p2) > 1)
                world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 0, 0, 0, 0, dust);
            p1.add(vector);
        }
    }

    public void drawDirection(Location point1, Location point2, double space) {
        // Draws a single particle in direction of speedrunner (only X & Z coordinates)
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "You have to be in same worlds!");
        Vector p1 = point1.toVector();
        Vector dir = point2.toVector().clone().subtract(p1).setY(0).normalize().multiply(space);
        Vector p = p1.add(dir);
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1);
        world.spawnParticle(Particle.REDSTONE, p.getX(), p1.getY() + 1.25, p.getZ(), 0, 0, 0, 0, dust);
    }
}
