package me.cosmo.compasstracker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class CommandAddHunter implements CommandExecutor {
    CompassTracker plugin;


    CommandAddHunter(CompassTracker compassTracker) {
        plugin = compassTracker;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            String playerName = strings[0].toLowerCase();
            Player player = (Player) commandSender;
            if (plugin.hunters.contains(playerName)) {
                player.sendRawMessage(ChatColor.RED + "Hunters already contains " + playerName);
                return false;
            }

            plugin.hunters.add(playerName);
            player.sendRawMessage(ChatColor.GREEN + "Adding " + playerName + " to hunters");
            return true;
        } else {
            return false;
        }
    }
}
