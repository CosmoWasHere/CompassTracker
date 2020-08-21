package me.cosmo.compasstracker;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CompassTracker extends JavaPlugin {
    List<String> hunters = new ArrayList<>();

    @Override
    public void onEnable() {
        CommandAddHunter commandAddHunter = new CommandAddHunter(this);

        // Handle compass right click to track set player
        this.getCommand("hunter").setExecutor(commandAddHunter);

        // Handle spawning player with compass if toggled
        EventsCompassInteraction eventsCompassInteraction = new EventsCompassInteraction(this, commandAddHunter);
        getServer().getPluginManager().registerEvents(eventsCompassInteraction, this);
    }
}