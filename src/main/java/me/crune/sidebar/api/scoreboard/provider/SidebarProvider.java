package me.crune.sidebar.api.scoreboard.provider;

import org.bukkit.entity.Player;
import me.crune.sidebar.api.provider.Provider;

import java.util.List;

public interface SidebarProvider {

    Provider<Player, String> getTitle();

    Provider<Player, List<String>> getLines();

    Provider<Player, Boolean> isShown();

}
