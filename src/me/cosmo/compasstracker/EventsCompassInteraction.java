package me.cosmo.compasstracker;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EventsCompassInteraction implements Listener {
    CompassTracker plugin;
    CommandAddHunter commandAddHunter;
    HashMap<Player, Integer> trackedPlayers = new HashMap<>();

    public EventsCompassInteraction(CompassTracker compassTracker, CommandAddHunter commandAddHunter) {
        this.plugin = compassTracker;
        this.commandAddHunter = commandAddHunter;
    }

    @EventHandler
    public void onCompassLeftClick(PlayerInteractEvent interactEvent) {
        // Check whether this is a valid left click event with a lodestone compass and if so cycle to track another player
        Action action = interactEvent.getAction();
        ItemStack item = interactEvent.getItem();
        Player player = interactEvent.getPlayer();

        boolean rightClickAction = action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;

        if (item != null) {
            boolean itemIsCompass = item.getType() == Material.COMPASS;
            if (rightClickAction && itemIsCompass) {
                ItemMeta itemMeta = item.getItemMeta();
                // Check whether the compass is a tracking compass
                if (itemMeta.getDisplayName().equals(ChatColor.GREEN + "Tracker Compass")) {
                    Player trackedPlayer = getNextTrackedPlayer(player);

                    // Check whether the player is in the same dimension as the tracked player
                    if (player.getWorld().getEnvironment() == trackedPlayer.getWorld().getEnvironment()) {
                        CompassMeta compassMeta = (CompassMeta) itemMeta;
                        compassMeta.setLodestone(trackedPlayer.getLocation());
                        item.setItemMeta(compassMeta);

                        player.sendRawMessage(ChatColor.GREEN + "Tracking " + trackedPlayer.getName());
                    } else {
                        player.sendRawMessage(ChatColor.RED + trackedPlayer.getName() + " is in a different dimension");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCompassRightClick(PlayerInteractEvent interactEvent) {
        // Check whether this a valid right click event with a lodestone compass and if so cycle to track another player
        Action action = interactEvent.getAction();
        ItemStack item = interactEvent.getItem();
        Player player = interactEvent.getPlayer();

        boolean rightClickAction = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;

        if (item != null) {
            boolean itemIsCompass = item.getType() == Material.COMPASS;
            if (rightClickAction && itemIsCompass) {
                ItemMeta itemMeta = item.getItemMeta();
                // Check whether the compass is a tracking compass
                if (itemMeta.getDisplayName().equals(ChatColor.GREEN +  "Tracker Compass")) {
                    Player trackedPlayer = getTrackedPlayer(player);

                    // Check whether the player is in the same dimension as the tracked player
                    if (player.getWorld().getEnvironment() == trackedPlayer.getWorld().getEnvironment()) {
                        CompassMeta compassMeta = (CompassMeta) itemMeta;
                        compassMeta.setLodestone(trackedPlayer.getLocation());
                        item.setItemMeta(compassMeta);

                        player.sendRawMessage(ChatColor.GREEN + "Tracking " + trackedPlayer.getName());
                    } else {
                        player.sendRawMessage(ChatColor.RED + trackedPlayer.getName() + " is in a different dimension");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent joinEvent) {
        // Check whether the player is in the hunter list and if so spawn them a tracking compass
        Player joinedPlayer = joinEvent.getPlayer();

        if (plugin.hunters.contains(joinedPlayer.getDisplayName().toLowerCase())) {
            if (!checkPlayerForExistingCompass(joinedPlayer)) {
                trackedPlayers.put(joinedPlayer, 0);
                Player trackedPlayer = (Player) plugin.getServer().getOnlinePlayers().toArray()[0];

                joinedPlayer.getInventory().addItem(createTrackerCompass(trackedPlayer));

                joinedPlayer.sendRawMessage(ChatColor.GREEN + "Tracking " + trackedPlayer.getDisplayName());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent respawnEvent) {
        // Check whether the player is in the hunter list and if so spawn them a tracking compass
        Player respawnPlayer = respawnEvent.getPlayer();

        if (plugin.hunters.contains(respawnPlayer.getDisplayName().toLowerCase())) {
            if (!trackedPlayers.containsKey(respawnPlayer)) {
                trackedPlayers.put(respawnPlayer, 0);
            }
            Player trackedPlayer = getTrackedPlayer(respawnPlayer);
            respawnPlayer.getInventory().addItem(createTrackerCompass(trackedPlayer));
            respawnPlayer.sendRawMessage(ChatColor.GREEN + "Tracking " + trackedPlayer.getDisplayName());
        }
    }


    static public ItemStack createTrackerCompass(Player player) {
        ItemStack trackerCompass = new ItemStack(Material.COMPASS);
        ItemMeta trackerMeta = trackerCompass.getItemMeta();

        trackerMeta.setDisplayName(ChatColor.GREEN + "Tracker Compass");

        List<String> trackerLore = new ArrayList<String>();
        trackerLore.add(ChatColor.GRAY + "Left-click to change your target");
        trackerLore.add(ChatColor.GRAY + "Right-click to update your target");
        trackerMeta.setLore(trackerLore);

        CompassMeta trackerCompassMeta = (CompassMeta) trackerMeta;
        trackerCompassMeta.setLodestoneTracked(false);
        trackerCompassMeta.setLodestone(player.getLocation());

        trackerCompass.setItemMeta(trackerCompassMeta);

        return trackerCompass;
    }

    public Boolean checkPlayerForExistingCompass(Player player) {
        boolean containsCompass = false;

        for (ItemStack item: player.getInventory().getContents()) {
            if (item != null) {
                if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Tracker Compass")) {
                    containsCompass = true;
                }
            }

        }

        return containsCompass;
    }

    public Player getTrackedPlayer(Player player) {
        Integer trackedPlayerIndex = trackedPlayers.get(player);

        // Check if the trackedPlayerIndex is within the range of online players
        if (trackedPlayerIndex >= plugin.getServer().getOnlinePlayers().size()) {
            trackedPlayerIndex = 0;
            trackedPlayers.put(player, trackedPlayerIndex);
        }

        return (Player) plugin.getServer().getOnlinePlayers().toArray()[trackedPlayerIndex];
    }

    public Player getNextTrackedPlayer(Player player) {
        Integer trackedPlayerIndex = trackedPlayers.get(player);
        trackedPlayerIndex += 1;

        // Check if the trackedPlayerIndex is within the range of online players
        if (trackedPlayerIndex >= plugin.getServer().getOnlinePlayers().size()) {
            trackedPlayerIndex = 0;
        }

        trackedPlayers.put(player, trackedPlayerIndex);
        return (Player) plugin.getServer().getOnlinePlayers().toArray()[trackedPlayerIndex];
    }
}
