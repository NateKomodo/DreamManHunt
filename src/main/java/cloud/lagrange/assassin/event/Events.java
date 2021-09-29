package cloud.lagrange.assassin.event;

import cloud.lagrange.assassin.Config;
import cloud.lagrange.assassin.PlayerData;
import cloud.lagrange.assassin.model.ManHuntRole;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;


public class Events implements Listener {

    private final PlayerData playerData;
    private final Config config;
    private static Location lastPlayerLocation;

    public Events(PlayerData playerData, Config config) {
        this.playerData = playerData;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (playerData.isFrozen(attacker)) {
            event.setCancelled(true);
            return;
        }

        // prevent speedrunner from attacking assassin
        if (playerData.isFrozen(player) && playerData.getRole(attacker).equals(ManHuntRole.SPEEDRUNNER)) {
            event.setCancelled(true);
            return;
        }

        if (
                config.isInstaKill() &&
                        playerData.getRole(attacker).equals(ManHuntRole.ASSASSIN) &&
                        playerData.getRole(player).equals(ManHuntRole.SPEEDRUNNER)
        ) {
            player.damage(999);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (config.giveCompass() && playerData.getRole(player).equals(ManHuntRole.ASSASSIN))
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (config.isTrackPortals()){
            if (event.getCause().toString().equals("NETHER_PORTAL") || event.getCause().toString().equals("END_PORTAL")){
                if (event.getFrom().getWorld().getEnvironment().equals(World.Environment.NORMAL)
                 && playerData.getRole(event.getPlayer()).equals(ManHuntRole.SPEEDRUNNER)){
                    lastPlayerLocation = event.getFrom();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerData.reset(event.getPlayer());
    }

    public static Location getLastPlayerLocation(){
        return lastPlayerLocation;
    }
}
