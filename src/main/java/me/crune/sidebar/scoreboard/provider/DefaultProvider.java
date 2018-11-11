package me.crune.sidebar.scoreboard.provider;

import me.crune.sidebar.api.provider.Provider;
import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DefaultProvider implements SidebarProvider {

    @Override
    public Provider<Player, String> getTitle() {
        return player -> "&6&lSidebar";
    }

    @Override
    public Provider<Player, List<String>> getLines() {
        return player -> Collections.singletonList("");
    }

    @Override
    public Provider<Player, Boolean> isShown() {
        return player -> false;
    }
}
