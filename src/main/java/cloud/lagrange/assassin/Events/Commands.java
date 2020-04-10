package cloud.lagrange.assassin.Events;

import cloud.lagrange.assassin.Assassin;
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

    public Commands(Assassin parent) {
        this.parent = parent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("assassin")) {
            if (args.length == 3) {
                Proccess(sender, args[0], args[1], args[2]);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /assassin <assassin/speedrunner> <add/remove> <player>. /assassin on its own will display debug info and reload config");
                sender.sendMessage("Current: ");
                Global.Players.stream().forEach(p -> sender.sendMessage(p.UUID + " as " + p.role));
                new Config(parent);
                return true;
            }
        }
        return false;
    }
    private void Proccess(CommandSender sender, String role, String action, String player) {
        Role r = null;
        Action a = null;
        if (role.equals("assassin")) r = Role.ASSASSIN; else if (role.equals("speedrunner")) r = Role.SPEEDRUNNER; else sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /assassin <assassin/speedrunner> <add/remove> <player>");
        if (action.equals("add")) a = Action.ADD; else if (action.equals("remove")) a = Action.REMOVE; else sender.sendMessage(ChatColor.RED + "Invalid arguments. Please use /assassin <assassin/speedrunner> <add/remove> <player>");
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
                    if (r == Role.ASSASSIN) {                    	
                    	PlayerInventory inventory = thePlayer.getInventory();
                    	inventory.addItem(new ItemStack(Material.COMPASS ,1));
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Added player to group " + role);
                break;
            case REMOVE:
                Role finalR = r;
                Global.Players.removeIf(p -> p.role == finalR && p.UUID.equals(UUID));
                sender.sendMessage(ChatColor.GREEN + "Removed player from group " + role);
                break;
        }
    }
}
