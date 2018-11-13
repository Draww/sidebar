package me.crune.sidebar.api.scoreboard;

import me.crune.sidebar.scoreboard.cooldown.Cooldown;

import java.util.List;
import java.util.Set;

public interface Sidebar {

    Cooldown getCooldown(String name);

    void removeCooldown(String name);

    void addCooldown(Cooldown cooldown);

    Set<Cooldown> getCooldowns();

    void hide();

    void show();

    boolean isShown();

    void updateTitle(String title);

    void updateList(List<String> entries);
}
