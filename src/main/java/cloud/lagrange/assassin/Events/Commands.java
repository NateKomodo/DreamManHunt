package cloud.lagrange.assassin.Events;

import cloud.lagrange.assassin.Assassin;
import cloud.lagrange.assassin.TeamManager;
import cloud.lagrange.assassin.Config;
import cloud.lagrange.assassin.Global;
import cloud.lagrange.assassin.Models.Action;
import cloud.lagrange.assassin.Models.Player;
import cloud.lagrange.assassin.Models.Role;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class Commands implements CommandExecutor {
    private Assassin parent;
    private TeamManager teamManager;

    public Commands(Assassin parent, TeamManager teamManager) {
        this.parent = parent;
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("assassin")) {
            if (args.length == 1) {
                Proccess(sender, "assassin", "add", args[0]);
                return true;
            } else if (args.length == 2) {
            	if (args[1].equals("remove")) {
	                Proccess(sender, "assassin", "remove", args[0]);
	                return true;
            	}
            	return false;
            } else if (args.length == 0) {
                new Config(parent);
            	sender.sendMessage("Config reloaded");
                return true;
            }
            else {
                sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /assassin <player> [remove]. /assassin on its own will display debug info and reload config");
                sender.sendMessage("Current: ");
                Global.Players.stream().forEach(p -> sender.sendMessage(p.UUID + " as " + p.role));
                //new Config(parent);
                return true;
            }
        } else if (command.getName().equals("speedrunner")) {
            if (args.length == 1) {
                Proccess(sender, "speedrunner", "add", args[0]);
                return true;
            } else if (args.length == 2) {
            	if (args[1].equals("remove")) {
	                Proccess(sender, "speedrunner", "remove", args[0]);
	                return true;
            	}
            	return false;
            }
            else {
                sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /speedrunner <player> [remove]. Use /assassin on its own to display debug info and reload config");
                sender.sendMessage("Current: ");
                Global.Players.stream().forEach(p -> sender.sendMessage(p.UUID + " as " + p.role));
                //new Config(parent);
                return true;
            }
        }
        return false;
    }
    private void Proccess(CommandSender sender, String role, String action, String player) {
        Role r = null;
        Action a = null;
        
        if (role.equals("assassin")) r = Role._ASSASSIN_;
        else if (role.equals("speedrunner")) r = Role._SPEEDRUNNER_;
        // else sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /assassin <assassin/speedrunner> <add/remove> <player>");
        
        if (action.equals("add")) a = Action.ADD;
        else if (action.equals("remove")) a = Action.REMOVE;
        // else sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /assassin <assassin/speedrunner> <add/remove> <player>");
        
        if (r == null || a == null) return;
        org.bukkit.entity.Player thePlayer = Bukkit.getPlayer(player);
        if (thePlayer == null) sender.sendMessage(ChatColor.RED + "Could not find player!");
        UUID UUID = thePlayer.getUniqueId();
        switch (a) {
            case ADD:
                if (Global.Players.stream().anyMatch(p -> p.UUID.equals(UUID))) {
                    Global.Players.stream().filter(p -> p.UUID.equals(UUID)).findFirst().get().role = r;
                    Global.Players.stream().filter(p -> p.UUID.equals(UUID)).findFirst().get().isFrozen = false;
                } else {
                    Player newP = new Player();
                    newP.isFrozen = false;
                    newP.role = r;
                    newP.UUID = UUID;
                    Global.Players.add(newP);

                    if (Config.giveCompass && r == Role._ASSASSIN_) {  
                    	PlayerInventory inventory = thePlayer.getInventory();
                    	inventory.addItem(new ItemStack(Material.COMPASS ,1));
                    }                    
                    this.teamManager.addPlayer(r, thePlayer);
                }
                sender.sendMessage(ChatColor.GREEN + "Added player to group " + role);
                break;
            case REMOVE:
                Role finalR = r;
                Global.Players.removeIf(p -> p.role == finalR && p.UUID.equals(UUID));
                this.teamManager.removePlayer(r, thePlayer);
                
                sender.sendMessage(ChatColor.GREEN + "Removed player from group " + role);
                break;
        }
    }
}
