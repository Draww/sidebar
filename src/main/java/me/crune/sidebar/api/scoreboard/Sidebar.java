package me.crune.sidebar.api.scoreboard;

import com.google.common.collect.ImmutableSet;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;

import java.util.List;

public interface Sidebar {

    Cooldown getCooldown(String name);

    void removeCooldown(String name);

    void addCooldown(Cooldown cooldown);

    ImmutableSet<Cooldown> getImmutableCooldowns();

    void hide();

    void show();

    boolean isShown();

    void updateTitle(String title);

    void updateList(List<String> entries);
}
