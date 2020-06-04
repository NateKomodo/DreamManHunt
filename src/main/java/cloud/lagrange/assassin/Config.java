package cloud.lagrange.assassin;

import org.bukkit.configuration.file.FileConfigurationOptions;

public class Config {
    public static boolean instaKill;
    public static boolean freeze;
    public static boolean compass;
    public static boolean giveCompass;
    public static boolean compassParticle;

    public Config(Assassin parent) {
    	parent.reloadConfig();
    	parent.getConfig().options().copyDefaults(true);
        parent.saveConfig();
        instaKill = (boolean) parent.getConfig().get("assassins-insta-kill-speedrunner");
        compass = (boolean) parent.getConfig().get("compass-tracking");
        giveCompass = (boolean) parent.getConfig().get("compass-give");
        compassParticle = (boolean) parent.getConfig().get("compass-particle");
        freeze = (boolean) parent.getConfig().get("freeze-assassin-when-seen");
    }
}
