package cloud.lagrange.assassin.Events;

import cloud.lagrange.assassin.Config;
import cloud.lagrange.assassin.Global;
import cloud.lagrange.assassin.Models.Role;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class Events implements Listener {
    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Global.Players.stream().anyMatch(player -> player.isFrozen &&
                player.UUID.equals(p.getUniqueId()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();
        if (Global.Players.stream().anyMatch(p -> p.isFrozen &&
                p.UUID.equals(attacker.getUniqueId()))) {
            e.setCancelled(true);
            return;
        }
        if (Global.Players.stream().anyMatch(p -> p.isFrozen &&
                p.UUID.equals(player.getUniqueId())) &&
        Global.Players.stream().anyMatch(p -> p.role == Role._SPEEDRUNNER_ && p.UUID.equals(attacker.getUniqueId()))) {
            e.setCancelled(true);
            return;
        }
        if (Global.Players.stream().anyMatch(p -> p.role == Role._ASSASSIN_ &&
                p.UUID.equals(attacker.getUniqueId())) &&
                Global.Players.stream().anyMatch(p -> p.role == Role._SPEEDRUNNER_ &&
                p.UUID.equals(player.getUniqueId()))) {
            if (Config.instaKill) player.damage(999);
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (Global.Players.stream().anyMatch(player -> player.isFrozen &&
                player.UUID.equals(p.getUniqueId()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (Global.Players.stream().anyMatch(player -> player.isFrozen &&
                player.UUID.equals(p.getUniqueId()))) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
    	Player player = e.getPlayer();
    	if (Global.Players.stream().anyMatch(p -> p.role == Role._ASSASSIN_ && 
    		p.UUID.equals(player.getUniqueId()))) {
    		
    		PlayerInventory inventory = player.getInventory();
    		inventory.addItem(new ItemStack(Material.COMPASS, 1));
    	}
    }
}
