package me.crune.sidebar.scoreboard;

import me.crune.sidebar.api.scoreboard.Sidebar;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CommonSidebar implements Sidebar {

    private UUID holder;
    private Map<Integer, String> entryMap;
    private boolean shown;
    private Set<Cooldown> cooldowns;

    public CommonSidebar(Player player) {
        this.holder = player.getUniqueId();
        this.cooldowns = new HashSet<>();

        setup();
    }

    private synchronized void setup() {
        this.entryMap = new HashMap<>();

        Player player = getHolder();

        if (player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        if (player.getScoreboard().getObjective("cleo") == null) {
            player.getScoreboard().registerNewObjective("cleo", "dummy").setDisplayName("cleo");
        }

        show();
    }

    @Override
    public Cooldown getCooldown(String name) {
        if (cooldowns.isEmpty()) {
            return null;
        }

        Stream<Cooldown> stream = getCooldowns().stream();
        return stream.filter(cooldown -> cooldown.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void removeCooldown(String name) {
        Cooldown other = getCooldown(name);
        if (other != null) {
            cooldowns.remove(other);
        }
    }

    @Override
    public void addCooldown(Cooldown cooldown) {
        removeCooldown(cooldown.getName());

        cooldowns.add(cooldown);
    }

    @Override
    public Set<Cooldown> getCooldowns() {
        return new HashSet<>(cooldowns);
    }

    @Override
    public void hide() {
        shown = false;
        Scoreboard scoreboard = getHolder().getScoreboard();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public void show() {
        Scoreboard scoreboard = getHolder().getScoreboard();
        Objective objective = scoreboard.getObjective("cleo");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        shown = true;
    }

    @Override
    public boolean isShown() {
        return shown;
    }

    @Override
    public void updateTitle(String title) {
        title = title.length() > 32 ? title.substring(0, 32) : title;
        Scoreboard scoreboard = getHolder().getScoreboard();
        Objective objective = scoreboard.getObjective("cleo");

        objective.setDisplayName(color(title));
    }

    @Override
    public void updateList(List<String> entries) {
        if (!shown) {
            return;
        }

        Collections.reverse(entries);

        IntStream.range(0, 16).forEach(i -> {
            if (entries.size() < i + 1) {
                removeLine(i + 1, "");
            } else {
                updateLine(color(entries.get(i)), i + 1);
            }
        });
    }

    private void removeLine(int line, String check) {
        Scoreboard scoreboard = getHolder().getScoreboard();
        String oldEntry = entryMap.get(line);

        if (oldEntry != null && !oldEntry.equals(check)) {
            scoreboard.resetScores(oldEntry);
        }
    }

    private void updateLine(String string, int line) {
        Scoreboard scoreboard = getHolder().getScoreboard();
        Objective objective = scoreboard.getObjective("cleo");
        String entry = createEntry(string, line);

        removeLine(line, entry);

        entryMap.put(line, entry);
        objective.getScore(entry).setScore(line);
    }

    private String createEntry(String string, int line) {
        Scoreboard scoreboard = getHolder().getScoreboard();

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        boolean old = version.contains("v1_7");

        String prefix = string;
        String entry = ChatColor.values()[line].toString();
        String suffix = "";

        if (string.length() > 15) {
            prefix = string.substring(0, 16);
            suffix = string.substring(16);

            entry += ChatColor.getLastColors(prefix);

            //thanks https://github.com/Notifyz
            if (prefix.endsWith("\u00a7")) {
                prefix = prefix.substring(0, 15);
                entry += "\u00a7";
            }

            if (string.length() > 32) {
                int colorLength = entry.length();
                entry += string.substring(16);
                suffix += entry.length() > (old ? 16 : 40) - colorLength ? entry.substring((old ? 16 : 40) - colorLength) : "";
                entry = entry.length() > (old ? 16 : 40) - colorLength ? entry.substring(0, (old ? 16 : 40) - colorLength) : entry;

                if (entry.endsWith("\u00a7")) {
                    entry = entry.substring(0, entry.length() - 1);
                    suffix = "\u00a7" + suffix;
                }

                suffix = suffix.length() > 15 ? suffix.substring(0, 16) : suffix;
            }
        }

        Team team = scoreboard.getTeam("\u0000" + line);
        if (team == null) {
            team = scoreboard.registerNewTeam("\u0000" + line);
        }
        team.addPlayer(new FakePlayer(entry));
        team.setPrefix(prefix);
        team.setSuffix(suffix);

        return entry;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private Player getHolder() {
        return Bukkit.getPlayer(holder);
    }
}
