package me.crune.sidebar.scoreboard.cooldown;

import me.crune.sidebar.util.TimeUtils;
import org.bukkit.ChatColor;

public class Cooldown {

    private String name;
    private String displayName;
    private long end;

    public Cooldown(String name, String displayName, String end) throws Exception {
        this.name = name;
        this.displayName = displayName;
        this.end = TimeUtils.parseDateDiff(end, true);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return String.format("%s%s", color(displayName), TimeUtils.formatDateDiff(end));
    }

    public boolean hasEnded() {
        return TimeUtils.formatDateDiff(end).equalsIgnoreCase("now");
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
