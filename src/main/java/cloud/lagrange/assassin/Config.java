package cloud.lagrange.assassin;

public class Config {
    public static boolean instaKill;

    public static boolean freeze;

    public static boolean compass;

    public Config(Assassin parent) {
        parent.getConfig().options().copyDefaults(true);
        parent.saveConfig();
        instaKill = (boolean) parent.getConfig().get("assassins-insta-kill-speedrunner");
        compass = (boolean) parent.getConfig().get("compass-tracking");
        freeze = (boolean) parent.getConfig().get("freeze-assassin-when-seen");
    }
}
