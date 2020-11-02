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

    public boolean isInstaKill() {
        return instaKill;
    }

    public boolean freeze() {
        return freeze;
    }

    public boolean isCompassTracking() {
        return compassTracking;
    }

    public boolean giveCompass() {
        return giveCompass;
    }

    public boolean isCompassParticle() {
        return compassParticle;
    }

    public boolean isCompassParticleInNether() {
        return compassParticleInNether;
    }

    public boolean isCompassRandomizeInDifferentWorlds() {
        return compassRandomizeInDifferentWorlds;
    }
}
