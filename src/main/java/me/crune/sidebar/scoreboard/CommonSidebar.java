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
    private boolean shown;
    private Set<Cooldown> cooldowns;

    public CommonSidebar(Player player) {
        this.holder = player.getUniqueId();
        this.cooldowns = new HashSet<>();

        setup();
    }

    private synchronized void setup() {
        Player player = getHolder();

        if (player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        if (player.getScoreboard().getObjective("sidebar") == null) {
            player.getScoreboard().registerNewObjective("sidebar", "dummy").setDisplayName("sidebar");
        }

        IntStream.range(0, 16).forEach(i -> {
            if (player.getScoreboard().getTeam("\u0000" + i) == null) {
                player.getScoreboard().registerNewTeam("\u0000" + i);
            }
        });

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
        Objective objective = scoreboard.getObjective("sidebar");
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
        Objective objective = scoreboard.getObjective("sidebar");

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
                removeLine(i + 1);
            } else {
                updateLine(entries.get(i), i + 1);
            }
        });
    }

    private void removeLine(int line) {
        Scoreboard scoreboard = getHolder().getScoreboard();
        scoreboard.resetScores(ChatColor.values()[line].toString());
    }

    private void updateLine(String string, int line) {
        Scoreboard scoreboard = getHolder().getScoreboard();
        Objective objective = scoreboard.getObjective("sidebar");

        String entry = createEntry(string, line);
        Team team = scoreboard.getTeam("\u0000" + line);
        team.addEntry(entry);

        objective.getScore(entry).setScore(line);
    }

    private String createEntry(String string, int line) {
        Scoreboard scoreboard = getHolder().getScoreboard();

        String[] prefixSuffix = getPrefixSuffix(string);
        Team team = scoreboard.getTeam("\u0000" + line);
        team.setPrefix(prefixSuffix[0]);
        team.setSuffix(prefixSuffix[1]);

        return ChatColor.values()[line].toString();
    }

    private String[] getPrefixSuffix(String s) {
        s = color(s);

        String prefix = getPrefix(s);
        String suffix = getPrefix(ChatColor.getLastColors(prefix) + getSuffix(s));
        return new String[]{prefix, suffix};
    }

    private String getPrefix(String s) {
        return s.length() > 16 ? s.substring(0, 16) : s;
    }

    private String getSuffix(String s) {
        if (s.length() > 32) {
            s = s.substring(0, 32);
        }
        return s.length() > 16 ? s.substring(16) : "";
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private Player getHolder() {
        return Bukkit.getPlayer(holder);
    }
}
