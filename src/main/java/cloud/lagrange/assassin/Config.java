package cloud.lagrange.assassin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {

    private final Plugin plugin;

    private boolean instaKill;
    private boolean freeze;
    private boolean compassTracking;
    private boolean giveCompass;
    private boolean compassParticle;
    private boolean compassParticleInNether;
    private boolean compassRandomizeInDifferentWorlds;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    /**
     * Reload config from config file located in plugins directory
     */
    public void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();

        instaKill = config.getBoolean("assassins-insta-kill-speedrunner");
        compassTracking = config.getBoolean("compass-tracking");
        giveCompass = config.getBoolean("compass-give");
        compassParticle = config.getBoolean("compass-particle");
        compassParticleInNether = config.getBoolean("compass-particle-in-nether");
        compassRandomizeInDifferentWorlds = config.getBoolean("compass-random-different-worlds");
        freeze = config.getBoolean("freeze-assassin-when-seen");
    }

    /**
     * @return true if the assassin can one shot the speedrunner
     */
    public boolean isInstaKill() {
        return instaKill;
    }

    /**
     * @return true if the assassin will be frozen in place if the speedrunner puts their aim over the assassin.
     */
    public boolean freeze() {
        return freeze;
    }

    /**
     * @return true if the assassins compass (if they have one) points to the closest speedrunner
     */
    public boolean isCompassTracking() {
        return compassTracking;
    }

    /**
     * @return if the assassins compass (if they have one) points to the closest speedrunner
     */
    public boolean giveCompass() {
        return giveCompass;
    }

    /**
     * @return true if it's needed to draw a yellow particle near assassin (if he holds a compass in main hand)in the direction of closest speedrunner
     */
    public boolean isCompassParticle() {
        return compassParticle;
    }

    /**
     * @return true if it's needed to draw particles in the nether
     */
    public boolean isCompassParticleInNether() {
        return compassParticleInNether;
    }

    /**
     * @return true, if compass will go crazy if there is no speedrunners in the same world
     */
    public boolean isCompassRandomizeInDifferentWorlds() {
        return compassRandomizeInDifferentWorlds;
    }
}
